package org.smile.strate.upload.impl;

import java.io.IOException;

import org.smile.strate.upload.FieldItem;

/**
 * Encapsulates base for uploaded file. Its instance may be
 * either valid, when it represent an uploaded file, or invalid
 * when uploaded file doesn't exist or there was a problem with it.
 */
public abstract class FileItem implements FieldItem{

	protected final MultipartRequestInputStream input;
	protected final FileItemHeader header;

	protected FileItem(MultipartRequestInputStream input) {
		this.input = input;
		this.header = input.lastHeader;
	}

	// ----------------------------------------------------------------  header

	/**
	 * Returns {@link FileItemHeader} of uploaded file.
	 */
	public FileItemHeader getHeader() {
		return header;
	}

	// ---------------------------------------------------------------- size and validity

	protected boolean valid;

	protected int size = -1;

	protected boolean fileTooBig;

	/**
	 * Returns the file upload size or <code>-1</code> if file was not uploaded.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns <code>true</code> if file was uploaded.
	 */
	public boolean isUploaded() {
		return size != -1;
	}

	/**
	 * Returns <code>true</code> if upload process went correctly.
	 * This still does not mean that file is uploaded, e.g. when file
	 * was not specified on the form.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Returns <code>true</code> if file is too big. File will be marked as invalid.
	 */
	public boolean isFileTooBig() {
		return fileTooBig;
	}
	
	

	// ---------------------------------------------------------------- status


	// ---------------------------------------------------------------- process

	/**
	 * Process request input stream. Note that file size is unknown at this point.
	 * Therefore, the implementation <b>should</b> set the {@link #getSize() size}
	 * attribute after successful processing. This method also must set the
	 * {@link #isValid() valid} attribute.
	 * 
	 * @see MultipartRequestInputStream
	 */
	protected abstract void processStream(long maxFileSize) throws IOException;
	/***
	 * 处理表单字段
	 * @throws IOException 
	 */
	protected abstract void processFormField() throws IOException;

	// ---------------------------------------------------------------- toString

	/**
	 * Returns basic information about the uploaded file.
	 */
	@Override
	public String toString() {
		return "FileUpload: uploaded=[" + isUploaded() + "] valid=[" + valid + "] field=[" +
				header.getFormFieldName() + "] name=[" + header.getFileName() + "] size=[" + size + ']';
	}
}