package org.smile.strate.upload.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.SmileRunException;
import org.smile.io.ByteArrayOutputStream;
import org.smile.io.FileNameUtils;
import org.smile.io.FileUtils;
import org.smile.io.IOUtils;
import org.smile.util.DateUtils;
import org.smile.util.StringUtils;

public class DiskFileItem extends FileItem {

	protected static final String TMP_FILE_SUFFIX = ".upload.tmp";

	protected static final String DEFAULT_CHARSET = "ISO8859-1";

	protected final int sizeThreshold;
	protected final File repository;
	protected final boolean breakOnError;
	protected final String[] fileExtensions;
	protected final boolean allowFileExtensions;

	DiskFileItem(MultipartRequestInputStream input, int memoryThreshold, File uploadPath, boolean breakOnError, String[] extensions, boolean allowed) {
		super(input);
		this.sizeThreshold = memoryThreshold;
		this.repository = uploadPath;
		this.breakOnError = breakOnError;
		this.fileExtensions = extensions;
		this.allowFileExtensions = allowed;
	}


	public boolean isBreakOnError() {
		return breakOnError;
	}

	public String[] getFileExtensions() {
		return fileExtensions;
	}

	public boolean isAllowFileExtensions() {
		return allowFileExtensions;
	}

	/**临时文件*/
	protected File tempFile;
	/** 字节内容 */
	protected byte[] byteContents;

	/**
	 * Returns <code>true</code> if file upload resides in memory.
	 */
	@Override
	public boolean isInMemory() {
		return byteContents != null;
	}

	// ---------------------------------------------------------------- process

	protected boolean matchFileExtension() throws IOException {
		String fileNameExtension = FileNameUtils.getExtension(getHeader().getFileName());
		for (String fileExtension : fileExtensions) {
			if (fileNameExtension.equalsIgnoreCase(fileExtension) == true) {
				if (allowFileExtensions == false) { // extension matched and it
													// is not allowed
					if (breakOnError == true) {
						throw new IOException("Upload filename extension not allowed: " + fileNameExtension);
					}
					size = input.skipToBoundary();
					return false;
				}
				return true; // extension matched and it is allowed.
			}
		}
		if (allowFileExtensions == true) { // extension is not one of the
											// allowed ones.
			if (breakOnError == true) {
				throw new IOException("Upload filename extension not allowed: " + fileNameExtension);
			}
			size = input.skipToBoundary();
			return false;
		}
		return true;
	}

	/**
	 * Determines if upload is allowed.
	 */
	protected boolean checkUpload() throws IOException {
		if (fileExtensions != null) {
			if (matchFileExtension() == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void processStream(long maxFileSize) throws IOException {
		if (checkUpload() == false) {
			return;
		}
		size = 0;
		if (sizeThreshold > 0) {
			//先读入最大内存冲
			ByteArrayOutputStream fbaos = new ByteArrayOutputStream(sizeThreshold + 1);
			int written = input.copyMax(fbaos, sizeThreshold + 1);
			byteContents = fbaos.toByteArray();
			if (written <= sizeThreshold) { //如果读完了
				size = byteContents.length;
				valid = true;
				return;
			}
		}
		
		tempFile = getTempFile();
		//写入文件中
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
		if (byteContents != null) { //把内存中的写入文件
			size = byteContents.length;
			out.write(byteContents);
			byteContents = null; // not needed anymore
		}
		boolean deleteTempFile = false;
		try {
			if (maxFileSize == -1) {
				size += input.copyAll(out);
			} else {
				size += input.copyMax(out, (int) (maxFileSize - size + 1)); // one
																			// more
																			// byte
																			// to
																			// detect
																			// larger
																			// files
				if (size > maxFileSize) {
					deleteTempFile = true;
					fileTooBig = true;
					valid = false;
					if (breakOnError == true) {
						throw new IOException("File upload (" + header.getFileName() + ") too big, > " + maxFileSize);
					}
					input.skipToBoundary();
					return;
				}
			}
			valid = true;
		} finally {
			IOUtils.close(out);
			if (deleteTempFile) {
				tempFile.delete();
				tempFile = null;
			}
		}
	}

	// ----------------------------------------------------------------
	// operations

	/**
	 * Deletes file uploaded item from disk or memory.
	 */
	@Override
	public void delete() {
		if (tempFile != null) {
			tempFile.delete();
		}
		if (byteContents != null) {
			byteContents = null;
		}
	}

	/**
	 * Writes file uploaded item.
	 */
	public File write(String destination) throws IOException {
		return write(new File(destination));
	}

	/**
	 * Writes file upload item to destination folder or to destination file.
	 * Returns the destination file.
	 */
	public File write(File destination) throws IOException {
		if (destination.isDirectory() == true) {
			destination = new File(destination, this.header.getFileName());
		}
		if (byteContents != null) {
			FileUtils.writeBytes(destination, byteContents);
		} else {
			if (tempFile != null) {
				IOUtils.copy(tempFile, destination);
			}
		}
		return destination;
	}

	/**
	 * Returns the content of file upload item.
	 */
	@Override
	public byte[] getMemoryBytes() {
		if (byteContents != null) {
			return byteContents;
		}
		if (tempFile != null) {
			try {
				return FileUtils.readBytes(tempFile);
			} catch (IOException e) {
				throw new SmileRunException(e);
			}
		}
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (byteContents != null) {
			return new BufferedInputStream(new ByteArrayInputStream(byteContents));
		}
		if (tempFile != null) {
			return new BufferedInputStream(new FileInputStream(tempFile));
		}
		return null;
	}

	@Override
	public boolean isFormField() {
		return !header.isFile;
	}

	@Override
	public String getString() {
		String charset=getCharset();
		if(charset==null){
			charset=DEFAULT_CHARSET;
		}
		return getString(charset);
	}

	@Override
	public String getString(String encoding) {
		if(this.byteContents==null){
			return null;
		}
		try {
			return new String(this.byteContents,encoding);
		} catch (UnsupportedEncodingException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public String getFieldName() {
		return header.getFormFieldName();
	}

	@Override
	public File getFile() {
		return tempFile;
	}

	@Override
	public String getContentType() {
		return header.getContentType();
	}

	@Override
	public String getFileName() {
		return header.getFileName();
	}

	@Override
	public File getTempFile() {
		if (this.tempFile == null) {
			try {
				String prefix=DateUtils.formatDate(new Date(), "yyyyMMddHHmmss_");
				this.tempFile = FileUtils.createTempFile(prefix, TMP_FILE_SUFFIX, this.repository);
			} catch (IOException e) {
				throw new SmileRunException(e);
			}
		}
		return this.tempFile;
	}

	protected String getCharset() {
		String contentType = this.header.getContentType();
		if (StringUtils.notEmpty(contentType)) {
			Map<String, String> params = new KeyNoCaseHashMap<String>();
			String[] ss = contentType.split(";");
			for (String s : ss) {
				int i = s.indexOf('=');
				if (i > 0) {
					params.put(StringUtils.trim(s.substring(0, i)), StringUtils.trim(s.substring(i + 1, s.length())));
				}
			}
			return params.get("charset");
		}
		return null;
	}

	@Override
	protected void processFormField() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.input.copyAll(os);
		this.byteContents = os.toArray();
	}

}