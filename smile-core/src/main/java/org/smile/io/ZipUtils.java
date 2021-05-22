package org.smile.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.smile.collection.ArrayUtils;
import org.smile.commons.Strings;
import org.smile.util.StringUtils;
import org.smile.util.Wildcard;

/**
 * Performs zip/gzip/zlib operations on files and directories.
 * These are just tools over existing <code>java.util.zip</code> classes,
 * meaning that existing behavior and bugs are persisted.
 * Most common issue is not being able to use UTF8 in file names,
 * because implementation uses old ZIP format that supports only
 * IBM Code Page 437. This bug was resolved in JDK7:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4244499
 */
public class ZipUtils {

	public static final String ZIP_EXT = ".zip";
	public static final String GZIP_EXT = ".gz";
	public static final String ZLIB_EXT = ".zlib";

	// ---------------------------------------------------------------- deflate

	/**
	 * Compresses a file into zlib archive.
	 */
	public static File zlib(String file) throws IOException {
		return zlib(new File(file));
	}

	/**
	 * Compresses a file into zlib archive.
	 */
	public static File zlib(File file) throws IOException {
		if (file.isDirectory() == true) {
			throw new IOException("Can't zlib folder");
		}
		FileInputStream fis = new FileInputStream(file);
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);

		String zlibFileName = file.getAbsolutePath() + ZLIB_EXT;

		DeflaterOutputStream dos = new DeflaterOutputStream(new FileOutputStream(zlibFileName), deflater);

		IOUtils.copy(fis, dos);

		return new File(zlibFileName);
	}

	// ---------------------------------------------------------------- gzip

	/**
	 * Compresses a file into gzip archive.
	 */
	public static File gzip(String fileName) throws IOException {
		return gzip(new File(fileName));
	}

	/**
	 * Compresses a file into gzip archive.
	 */
	public static File gzip(File file) throws IOException {
		if (file.isDirectory() == true) {
			throw new IOException("Can't gzip folder");
		}
		FileInputStream fis = new FileInputStream(file);

		String gzipName = file.getAbsolutePath() + GZIP_EXT;

		GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(gzipName));
		IOUtils.copy(fis, gzos);
		return new File(gzipName);
	}

	/**
	 * Decompress gzip archive.
	 */
	public static File ungzip(String file) throws IOException {
		return ungzip(new File(file));
	}

	/**
	 * Decompress gzip archive.
	 */
	public static File ungzip(File file) throws IOException {
		String outFileName = FileNameUtils.removeExtension(file.getAbsolutePath());
		File out = new File(outFileName);
		out.createNewFile();

		FileOutputStream fos = new FileOutputStream(out);
		GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(file));
		IOUtils.copy(gzis, fos);
		return out;
	}

	// ---------------------------------------------------------------- zip

	/**
	 * Zips a file or a folder.
	 * @see #zip(java.io.File)
	 */
	public static File zip(String file) throws IOException {
		return zip(new File(file), null,true);
	}

	/**
	 * 修改zip文件名称 把包名添加到zip文件内部
	 * @param file
	 * @param zipFileName
	 * @return
	 * @throws IOException
	 */
	public static File zip(String file, String zipFileName) throws IOException {
		return zip(new File(file), zipFileName,true);
	}

	/**
	 *  Zips a file or a folder. If adding a folder, all its content will be added.
	 * @param file
	 * @param zipFileName
	 * @param addDirTozip
	 * @return
	 * @throws IOException
	 */
	public static File zip(File file, String zipFileName,boolean addDirTozip) throws IOException {
		String zipFile=file.getAbsolutePath();
		if(zipFileName!=null){
			zipFile=file.getParentFile().getAbsolutePath()+File.separator+zipFileName;
		}
		zipFile+= ZIP_EXT;
		ZipOutputStream zos = null;
		try {
			zos = createZip(zipFile);
			String path=null;
			if(!addDirTozip){
				path="/";
			}
			addToZip(zos).file(file).path(path).recursive().add();
		} finally {
			IOUtils.close(zos);
		}

		return new File(zipFile);
	}

	// ---------------------------------------------------------------- unzip

	/**
	 * Extracts zip file content to the target directory.
	 * @see #unzip(java.io.File, java.io.File, String...)
	 */
	public static void unzip(String zipFile, String destDir, String... patterns) throws IOException {
		File dest=null;
		if(StringUtils.notEmpty(destDir)){
			dest=new File(destDir);
		}
		unzip(Charset.forName(IOUtils.DEFAULT_ENCODE), new File(zipFile),dest , patterns);
	}
	/**
	 * 解压到当前文件夹
	 * @param zipFile
	 * @throws IOException
	 */
	public static void unzip(String zipFile) throws IOException{
		unzip(zipFile,null);
	}

	/**
	 * Extracts zip file to the target directory. If patterns are provided
	 * only matched paths are extracted.
	 *
	 * @param zipFile zip file
	 * @param destDir destination directory
	 * @param patterns optional wildcard patterns of files to extract, may be <code>null</code>
	 */
	public static void unzip(Charset charset, File zipFile, File destDir, String... patterns) throws IOException {
		ZipFile zip = new ZipFile(zipFile, charset);
		try {
			Enumeration<?> zipEntries = zip.entries();
			while (zipEntries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipEntries.nextElement();
				String entryName = entry.getName();
				if (ArrayUtils.notEmpty(patterns)) {
					if (Wildcard.matchPathOne(entryName, patterns) == -1) {
						continue;
					}
				}

				File file = (destDir != null) ? new File(destDir, entryName) : new File(zipFile.getParent(),entryName);
				if (entry.isDirectory()) {
					if (!file.mkdirs()) {
						if (file.isDirectory() == false) {
							throw new IOException("Failed to create directory: " + file);
						}
					}
				} else {
					File parent = file.getParentFile();
					if (parent != null && !parent.exists()) {
						if (!parent.mkdirs()) {
							if (file.isDirectory() == false) {
								throw new IOException("Failed to create directory: " + parent);
							}
						}
					}

					InputStream in = zip.getInputStream(entry);
					OutputStream out = new FileOutputStream(file);
					IOUtils.copy(in, out);
				}
			}
		} finally {
			IOUtils.close(zip);
		}
	}

	// ---------------------------------------------------------------- zip
	// stream

	/**
	 * Creates and opens zip output stream of a zip file.
	 * @see #createZip(java.io.File)
	 */
	public static ZipOutputStream createZip(String zipFile) throws FileNotFoundException {
		return createZip(new File(zipFile));
	}

	/**
	 * Creates and opens zip output stream of a zip file. If zip file exist it will be recreated.
	 */
	public static ZipOutputStream createZip(File zip) throws FileNotFoundException {
		return new ZipOutputStream(new FileOutputStream(zip));
	}

	/**
	 * Starts a command for adding file entries to the zip.
	 * @see #addToZip(java.util.zip.ZipOutputStream, java.io.File, String, String, boolean)
	 */
	public static AddToZip addToZip(ZipOutputStream zos) {
		return new AddToZip(zos);
	}

	/**
	 * Command: "add to zip".
	 */
	public static class AddToZip {
		private final ZipOutputStream zos;
		private File file;
		private String path;
		private String comment;
		private boolean recursive = true;

		private AddToZip(ZipOutputStream zos) {
			this.zos = zos;
		}

		/**
		 * Defines file or folder to be added to zip.
		 */
		public AddToZip file(File file) {
			this.file = file;
			return this;
		}

		/**
		 * Defines file or folder to be added to zip.
		 */
		public AddToZip file(String fileName) {
			this.file = new File(fileName);
			return this;
		}

		/**
		 * Defines file or folder to be added to zip.
		 */
		public AddToZip file(String parent, String child) {
			this.file = new File(parent, child);
			return this;
		}

		/**
		 * Defines optional entry path.
		 */
		public AddToZip path(String path) {
			this.path = path;
			return this;
		}

		/**
		 * Defines optional comment.
		 */
		public AddToZip comment(String comment) {
			this.comment = comment;
			return this;
		}

		/**
		 * Defines if folders content should be added.
		 * Ignored for files.
		 */
		public AddToZip recursive() {
			this.recursive = true;
			return this;
		}

		/**
		 * Invokes the adding command.
		 */
		public void add() throws IOException {
			addToZip(zos, file, path, comment, recursive);
		}
	}

	/**
	 * Adds single entry to ZIP output stream. For user-friendly way of adding entries to zip
	 * see {@link #addToZip(java.util.zip.ZipOutputStream)}.
	 *
	 * @param zos zip output stream
	 * @param file file or folder to add
	 * @param path relative path of file entry; if <code>null</code> files name will be used instead
	 * @param comment optional comment
	 * @param recursive when set to <code>true</code> content of added folders will be added, too
	 */
	public static void addToZip(ZipOutputStream zos, File file, String path, String comment, boolean recursive) throws IOException {
		if (file.exists() == false) {
			throw new FileNotFoundException(file.toString());
		}

		if (path == null) {
			path = file.getName();
		}

		while (path.length() != 0 && path.charAt(0) == '/') {
			path = path.substring(1);
		}

		boolean isDir = file.isDirectory();

		if (isDir) {
			// add folder record
			if (!StringUtils.endsWith(path, '/')) {
				path += '/';
			}
		}

		ZipEntry zipEntry = new ZipEntry(path);
		zipEntry.setTime(file.lastModified());

		if (comment != null) {
			zipEntry.setComment(comment);
		}

		if (isDir) {
			zipEntry.setSize(0);
			zipEntry.setCrc(0);
		}

		zos.putNextEntry(zipEntry);

		if (!isDir) {
			InputStream is = new FileInputStream(file);
			try {
				IOUtils.copy(is, zos, false);
			} finally {
				IOUtils.close(is);
			}
		}

		zos.closeEntry();

		// continue adding

		if (recursive && file.isDirectory()) {
			boolean noRelativePath = StringUtils.isEmpty(path);

			final File[] children = file.listFiles();

			if (children != null && children.length != 0) {
				for (File child : children) {
					String childRelativePath = (noRelativePath ? Strings.EMPTY : path) + child.getName();
					addToZip(zos, child, childRelativePath, comment, recursive);
				}
			}
		}

	}
}
