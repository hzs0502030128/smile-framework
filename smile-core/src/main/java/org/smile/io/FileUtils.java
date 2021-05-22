package org.smile.io;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.smile.Smile;
import org.smile.util.StringUtils;

/**
 * File utilities.
 */
public class FileUtils {

	private static final String MSG_NOT_A_DIRECTORY = "Not a directory: ";
	private static final String MSG_CANT_CREATE = "Can't create: ";
	private static final String MSG_NOT_FOUND = "Not found: ";
	private static final String MSG_NOT_A_FILE = "Not a file: ";
	private static final String MSG_ALREADY_EXISTS = "Already exists: ";
	private static final String MSG_UNABLE_TO_DELETE = "Unable to delete: ";

	/**
	 * Simple factory for <code>File</code> objects.
	 */
	private static File file(String fileName) {
		return new File(fileName);
	}

	// ---------------------------------------------------------------- misc shortcuts

	/**
	 * Checks if two files points to the same file.
	 */
	public static boolean equals(String file1, String file2) {
		return equals(file(file1), file(file2));
	}

	/**
	 * Checks if two files points to the same file.
	 */
	public static boolean equals(File file1, File file2) {
		try {
			file1 = file1.getCanonicalFile();
			file2 = file2.getCanonicalFile();
		} catch (IOException ignore) {
			return false;
		}
		return file1.equals(file2);
	}

	/**
	 * Converts file URLs to file. Ignores other schemes and returns <code>null</code>.
	 * @throws UnsupportedEncodingException 
	 */
	public static File toFile(URL url) throws UnsupportedEncodingException {
		String fileName = toFileName(url);
		if (fileName == null) {
			return null;
		}
		return file(fileName);
	}

	/**
	 * Converts file to URL in a correct way.
	 * Returns <code>null</code> in case of error.
	 */
	public static URL toURL(File file) throws MalformedURLException {
		return file.toURI().toURL();
	}

	/**
	 * Converts file URLs to file name. Accepts only URLs with 'file' protocol.
	 * Otherwise, for other schemes returns <code>null</code>.
	 * @throws UnsupportedEncodingException 
	 */
	public static String toFileName(URL url) throws UnsupportedEncodingException {
		if ((url == null) || (url.getProtocol().equals("file") == false)) {
			return null;
		}
		String filename = url.getFile().replace('/', File.separatorChar);

		return URLDecoder.decode(filename, Smile.ENCODE);
	}

	/**
	 * Returns a file of either a folder or a containing archive.
	 * @throws UnsupportedEncodingException 
	 */
	public static File toContainerFile(URL url) throws UnsupportedEncodingException {
		String protocol = url.getProtocol();
		if (protocol.equals("file")) {
			return toFile(url);
		}
		String path = url.getPath();
		return new File(URI.create(path.substring(0, path.lastIndexOf("!/"))));
	}

	/**
	 * 是否是一个存在的文件
	 */
	public static boolean isExistingFile(File file) {
		return file.exists() && file.isFile();
	}

	/**
	 * 是否是一个存在的目录 
	 */
	public static boolean isExistingFolder(File folder) {
		return folder.exists() && folder.isDirectory();
	}


	/**
	 * Creates all folders at once.
	 * @see #mkdirs(java.io.File)
	 */
	public static void mkdirs(String dirs) throws IOException {
		mkdirs(file(dirs));
	}
	/**
	 * Creates all folders at once.
	 */
	public static void mkdirs(File dirs) throws IOException {
		if (dirs.exists()) {
			if (dirs.isDirectory() == false) {
				throw new IOException(MSG_NOT_A_DIRECTORY + dirs);
			}
			return;
		}
		if (dirs.mkdirs() == false) {
			throw new IOException(MSG_CANT_CREATE + dirs);
		}
	}

	/**
	 * Creates single folder.
	 * @see #mkdir(java.io.File)
	 */
	public static void mkdir(String dir) throws IOException {
		mkdir(file(dir));
	}
	/**
	 * Creates single folders.
	 */
	public static void mkdir(File dir) throws IOException {
		if (dir.exists()) {
			if (dir.isDirectory() == false) {
				throw new IOException(MSG_NOT_A_DIRECTORY + dir);
			}
			return;
		}
		if (dir.mkdir() == false) {
			throw new IOException(MSG_CANT_CREATE + dir);
		}
	}

	// ---------------------------------------------------------------- touch

	/**
	 * @see #touch(java.io.File)
	 */
	public static void touch(String file) throws IOException {
		touch(file(file));
	}
	/**
	 * Implements the Unix "touch" utility. It creates a new file
	 * with size 0 or, if the file exists already, it is opened and
	 * closed without modifying it, but updating the file date and time.
	 */
	public static void touch(File file) throws IOException {
		if (file.exists() == false) {
			IOUtils.close(new FileOutputStream(file));
		}
		file.setLastModified(System.currentTimeMillis());
	}

	public static void deleteFile(String dest) throws IOException {
		deleteFile(file(dest));
	}

	public static void deleteFile(File dest) throws IOException {
		if (dest.exists() == false) {
			throw new FileNotFoundException(MSG_NOT_FOUND + dest);
		}
		if (dest.isFile() == false) {
			throw new IOException(MSG_NOT_A_FILE + dest);
		}
		if (dest.delete() == false) {
			throw new IOException(MSG_UNABLE_TO_DELETE + dest);
		}
	}
	/**
	 * 删除一个目录
	 * @param file
	 * @throws IOException
	 */
	public static void deleteDirectory(String file) throws IOException{
		File dir=file(file);
		if(!dir.isDirectory()){
			throw new IOException(MSG_NOT_A_DIRECTORY+file);
		}
		recursiveDelete(dir, null);
	}
	/**
	 * 递归删除文件   如果是文件夹会删除子文件夹
	 * @param file
	 * @param filter
	 */
	public static void recursiveDelete(File file,FileFilter filter) {
		if(file.isDirectory()){
			File[] subFiles=file.listFiles();
			for(File f:subFiles){
				if(isAccept(filter, file)){
					recursiveDelete(f, filter);
				}
			}
		}
		if(isAccept(filter, file)){
			file.delete();
		}
	}
	/**
	 * 过滤器是否接收文件
	 * @param filter
	 * @param file
	 * @return
	 */
	public static boolean isAccept(FileFilter filter,File file){
		return filter==null||filter.accept(file);
	}

	public static void writeChars(File dest, char[] data, String encoding) throws IOException {
		outChars(dest, data, encoding, false);
	}
	public static void writeChars(String dest, char[] data, String encoding) throws IOException {
		outChars(file(dest), data, encoding, false);
	}
	
	protected static void outChars(File dest, char[] data, String encoding, boolean append) throws IOException {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new IOException(MSG_NOT_A_FILE + dest);
			}
		}
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest, append), encoding));
		try {
			out.write(data);
		} finally {
			IOUtils.close(out);
		}
	}



	public static void appendString(File dest, String data, String encoding) throws IOException {
		outString(dest, data, encoding, true);
	}

	protected static void outString(File dest, String data, String encoding, boolean append) throws IOException {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new IOException(MSG_NOT_A_FILE + dest);
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dest, append);
			out.write(data.getBytes(encoding));
		} finally {
			IOUtils.close(out);
		}
	}

	public static byte[] readBytes(String file) throws IOException {
		return readBytes(file(file));
	}

	public static byte[] readBytes(File file) throws IOException {
		if (file.exists() == false) {
			throw new FileNotFoundException(MSG_NOT_FOUND + file);
		}
		if (file.isFile() == false) {
			throw new IOException(MSG_NOT_A_FILE + file);
		}
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new IOException("File is larger then max array size");
		}

		byte[] bytes = new byte[(int) len];
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		randomAccessFile.readFully(bytes);
		randomAccessFile.close();

		return bytes;
	}



	public static void writeBytes(String dest, byte[] data) throws IOException {
		outBytes(file(dest), data, 0, data.length, false);
	}

	public static void writeBytes(String dest, byte[] data, int off, int len) throws IOException {
		outBytes(file(dest), data, off, len, false);
	}

	public static void writeBytes(File dest, byte[] data) throws IOException {
		outBytes(dest, data, 0, data.length, false);
	}

	public static void writeBytes(File dest, byte[] data, int off, int len) throws IOException {
		outBytes(dest, data, off, len, false);
	}


	public static void appendBytes(String dest, byte[] data) throws IOException {
		outBytes(file(dest), data, 0, data.length, true);
	}

	public static void appendBytes(String dest, byte[] data, int off, int len) throws IOException {
		outBytes(file(dest), data, off, len, true);
	}

	public static void appendBytes(File dest, byte[] data) throws IOException {
		outBytes(dest, data, 0, data.length, true);
	}

	public static void appendBytes(File dest, byte[] data, int off, int len) throws IOException {
		outBytes(dest, data, off, len, true);
	}

	protected static void outBytes(File dest, byte[] data, int off, int len, boolean append) throws IOException {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new IOException(MSG_NOT_A_FILE + dest);
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dest, append);
			out.write(data, off, len);
		} finally {
			IOUtils.close(out);
		}
	}


	public static boolean isNewer(String file, String reference) {
		return isNewer(file(file), file(reference));
	}

	/**
	 * Test if specified <code>File</code> is newer than the reference <code>File</code>.
	 *
	 * @param file		the <code>File</code> of which the modification date must be compared
	 * @param reference	the <code>File</code> of which the modification date is used
	 * @return <code>true</code> if the <code>File</code> exists and has been modified more
	 * 			recently than the reference <code>File</code>.
	 */
	public static boolean isNewer(File file, File reference) {
		if (reference.exists() == false) {
			throw new IllegalArgumentException("Reference file not found: " + reference);
		}
		return isNewer(file, reference.lastModified());
	}


	public static boolean isOlder(String file, String reference) {
		return isOlder(file(file), file(reference));
	}

	public static boolean isOlder(File file, File reference) {
		if (reference.exists() == false) {
			throw new IllegalArgumentException("Reference file not found: " + reference);
		}
		return isOlder(file, reference.lastModified());
	}

	/**
	 * Tests if the specified <code>File</code> is newer than the specified time reference.
	 *
	 * @param file			the <code>File</code> of which the modification date must be compared.
	 * @param timeMillis	the time reference measured in milliseconds since the
	 * 						epoch (00:00:00 GMT, January 1, 1970)
	 * @return <code>true</code> if the <code>File</code> exists and has been modified after
	 *         the given time reference.
	 */
	public static boolean isNewer(File file, long timeMillis) {
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() > timeMillis;
	}

	public static boolean isNewer(String file, long timeMillis) {
		return isNewer(file(file), timeMillis);
	}


	public static boolean isOlder(File file, long timeMillis) {
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() < timeMillis;
	}

	public static boolean isOlder(String file, long timeMillis) {
		return isOlder(file(file), timeMillis);
	}




	


	public static boolean isFilePathAcceptable(File file, FileFilter fileFilter) {
		do {
			if (fileFilter != null && !fileFilter.accept(file)) {
				return false;
			}
			file = file.getParentFile();
		} while (file != null);
		return true;
	}

	

	/**
	 * Creates temporary file.
	 * If <code>create</code> is set to <code>true</code> file will be
	 * physically created on the file system. Otherwise, it will be created and then
	 * deleted - trick that will make temp file exist only if they are used.
	 */
	public static File createTempFile(String prefix, String suffix, File tempDir, boolean create) throws IOException {
		File file = createTempFile(prefix, suffix, tempDir);
		file.delete();
		if (create) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * Creates temporary file. Wraps java method and repeat creation several time
	 * if something fail.
	 */
	public static File createTempFile(String prefix, String suffix, File dir) throws IOException {
		int exceptionsCount = 0;
		while (true) {
			try {
				return File.createTempFile(prefix, suffix, dir).getCanonicalFile();
			} catch (IOException ioex) {	// fixes java.io.WinNTFileSystem.createFileExclusively access denied
				if (++exceptionsCount >= 50) {
					throw ioex;
				}
			}
		}
	}

	/**
	 * Calculates digest for a file using provided algorithm.
	 */
	public static byte[] digest(final File file, MessageDigest algorithm) throws IOException {
		algorithm.reset();
		InputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DigestInputStream dis = new DigestInputStream(bis, algorithm);
		try{
			int bytesRead;
			while ((bytesRead = dis.read()) != -1) {
			}
		}finally{
			IOUtils.close(dis);
		}
		return algorithm.digest();
	}

	/**
	 * Creates MD5 digest of a file.
	 */
	public static String md5(final File file) throws IOException {
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ignore) {
		}

		byte[] digest = digest(file, md5Digest);

		return StringUtils.toHexString(digest);
	}

	/**
	 * Creates SHA-1 digest of a file.
	 */
	public static String sha(final File file) throws IOException {
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ignore) {
		}

		byte[] digest = digest(file, md5Digest);

		return StringUtils.toHexString(digest);
	}

	/**
	 * Creates SHA-256 digest of a file.
	 */
	public static String sha256(final File file) throws IOException {
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException ignore) {
		}

		byte[] digest = digest(file, md5Digest);

		return StringUtils.toHexString(digest);
	}
	/**
	 * 返回所有的文件 不包括目录
	 * @param folder 目录
	 * @param recursive 是否递归子目录
	 * @return 所有的文件列表
	 * @throws IOException
	 */
	public static Set<File> listFiles(File folder,boolean recursive) throws IOException{
		if(!isExistingFolder(folder)){
			throw new FileNotFoundException("can not find a folder "+folder);
		}
		Set<File> result=new LinkedHashSet<File>();
		File[] files=folder.listFiles();
		for(File f:files){
			if(f.isDirectory()){
				if(recursive){//递归的时候查询子包
					recursiveFiles(folder,null, result);
				}
			}else{
				result.add(f);
			}
		}
		return result;
	}
	/**
	 * 列出文件夹中的文件
	 * @param folder
	 * @param exts
	 * @param recursive
	 * @return
	 */
	public static Set<File> listFiles(File folder,final Set<String> extensions,boolean recursive)  throws IOException{
		if(!isExistingFolder(folder)){
			throw new FileNotFoundException("can not find a folder "+folder);
		}
		Set<File> result=new LinkedHashSet<File>();
		File[] files=folder.listFiles();
		if(files.length>0){
			FileFilter filter=new FileFilter(){
				@Override
				public boolean accept(File pathname) {
					if(pathname.isDirectory()){
						return true;
					}
					String ext=FileNameUtils.getExtension(pathname.getName());
					if(extensions.contains(ext)){
						return true;
					}
					return false;
				}
			};
			for(File f:files){
				if(f.isDirectory()){
					if(recursive){//递归的时候查询子包
						recursiveFiles(folder,filter, result);
					}
				}else{
					result.add(f);
				}
			}
		}
		
		return result;
	}
	/**
	 * 循环一个目录中的所有文件
	 * @param folder
	 * @param result 查找到的文件放入此集合中
	 * @throws IOException
	 */
	public static void recursiveFiles(File folder,FileFilter filter,Set<File> result) throws IOException{
		String ss[] = folder.list();
        if (ss == null) return;
        for (String s : ss) {
            File f = new File(folder,s);
            if ((filter == null) || filter.accept(f)){
            	if(f.isDirectory()){
            		recursiveFiles(f,filter, result);
    			}else{
    				result.add(f);
    			}
            }
        }
	}
}