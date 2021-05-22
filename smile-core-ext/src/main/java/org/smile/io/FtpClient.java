package org.smile.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.smile.Smile;
import org.smile.log.LoggerHandler;

/**
 * FTP工具  不论是 window linux 路径分隔符必须使用 '/'
 * @author strive
 *
 */
public class FtpClient implements LoggerHandler {
	
	private static final String CREATE_DIR_ERROR="Create Directory Error .. ";
	/**使用apache的ftp客户端工具实现功能*/
	private FTPClient ftp = new FTPClient();

	private static final String separator = "/";

	/**
	 * tfp服务器ip
	 */
	private String host;

	/**
	 * ftp连接端口
	 */
	private int port = FTP.DEFAULT_PORT;

	/**
	 * 登录远程的用户名
	 */
	private String userName;

	/**登录的密码*/
	private String password;
	/**编码格式*/
	private String encode = Smile.ENCODE;

	public FtpClient() {
		ftp.setControlEncoding(encode);
	}
	/**
	 * 初始化信息
	 * @param host
	 * @param port
	 * @param username
	 * @param passord
	 */
	public void init(String host, int port, String username, String passord) {
		this.host = host;
		this.port = port;
		this.userName = username;
		this.password = passord;
	}
	/**
	 * 下载文件
	 * @param path 文件目录
	 * @param fileName 文件名
	 * @return
	 * @throws Exception
	 */
	public InputStream download(String path,String fileName) throws Exception {
		String filePath = (!path.endsWith(separator) ? path + separator : path) + fileName;
		return download(filePath);
	}

	/**
	 * 下载一个文件
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public InputStream download(String filePath) throws IOException {
		ftp.enterLocalPassiveMode();
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		String path = new String(filePath.getBytes(encode), ftp.getControlEncoding());
		FTPFile files[] = ftp.listFiles(path);
		if (files.length <= 0) {
			logger.info("remote file not exists ," + filePath);
		}
		return ftp.retrieveFileStream(path);
	}
	
	/**
	 * 下载文件到本地文件中
	 * @param filePath
	 * @param localSave
	 * @throws IOException
	 */
	public void download(String filePath,File localSave) throws IOException{
		InputStream is=download(filePath);
		IOUtils.copy(is, new FileOutputStream(localSave));
	}

	/**
	 * 连接服务器
	 * @return
	 * @throws NumberFormatException
	 * @throws SocketException
	 * @throws IOException
	 */
	public boolean connect() throws SocketException, IOException {
		ftp.connect(host, port);
		if (FTPReply.isPositiveCompletion(ftp.getReplyCode()) && ftp.login(userName, password)) {
			return true;
		} else {
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
			return false;
		}
	}

	/**
	 * 上传文件
	 * @param path 目录
	 * @param fileName 文件名称
	 * @param is
	 * @throws IOException
	 */
	public void upload(String path, String fileName, InputStream is) throws IOException {
		path = path.endsWith(separator) ? path : path + separator;
		String remote = path + fileName;
		ftp.enterLocalPassiveMode();
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		if (!createDirecroty(path)) {
			throw new IOException(CREATE_DIR_ERROR+path);
		}
		if(ftp.storeFile(remote, is)){
			logger.info("upload file " + remote + " success ");
		}
	}
	
	/**
	 * 上传文件   完整的远程路径名
	 * @param remotePathName
	 * @param is
	 * @throws IOException
	 */
	public void upload(String remotePathName,InputStream is) throws IOException{
		ftp.enterLocalPassiveMode();
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		String dir=FileNameUtils.getPath(remotePathName);
		if (!createDirecroty(dir)) {
			throw new IOException(CREATE_DIR_ERROR + dir);
		}
		if(ftp.storeFile(remotePathName, is)){
			logger.info("upload file " + remotePathName + " success ");
		}
	}

	/**
	 * 创建下目录在服务器上
	 * @param remote 
	 * @return
	 * @throws IOException
	 */
	public boolean createDirecroty(String remote) throws IOException {
		String directory = remote.substring(0, remote.lastIndexOf(separator) + 1);
		boolean isRoot = directory.equalsIgnoreCase(separator);
		if (!isRoot) {
			//跳转到 根目录
			ftp.changeWorkingDirectory(separator);
			int start = 0;
			int end = 0;
			end = directory.indexOf(separator, start);
			do {
				String subDirectory = new String(remote.substring(start, end));
				if (!ftp.changeWorkingDirectory(subDirectory)) {
					boolean b = ftp.makeDirectory(subDirectory);
					if (b) {
						ftp.changeWorkingDirectory(subDirectory);
					}
				}
				start = end + 1;
				end = directory.indexOf(separator, start);
			} while (end > start);
		}
		return true;
	}

	/** 复制文件. 
	 *  
	 * @param sourceFileName 
	 * @param targetFile 
	 * @throws IOException 
	 */
	public void copyFile(String sourceFileName, String sourceDir, String targetDir) throws IOException {
		File file = File.createTempFile(sourceFileName, null);
		try {
			createDirecroty(targetDir);
			ftp.setBufferSize(1024 * 2);
			// 变更工作路径
			ftp.changeWorkingDirectory(sourceDir);
			// 设置以二进制流的方式传输
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			OutputStream fos = new FileOutputStream(file);
			// 将文件读到内存中
			try {
				ftp.retrieveFile(new String(sourceFileName.getBytes(encode), ftp.getControlEncoding()), fos);
			} finally {
				IOUtils.close(fos);
			}
			ftp.changeWorkingDirectory(targetDir);
			InputStream is = new FileInputStream(file);
			try {
				ftp.storeFile(new String(sourceFileName.getBytes(encode), ftp.getControlEncoding()), is);
			} finally {
				IOUtils.close(is);
			}
		} finally {
			FileUtils.deleteFile(file);
		}
	}

	/**
	 * 关闭ftp客户端
	 */
	public void close() {
		try {
			ftp.disconnect();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public String getEncode() {
		return encode;
	}

	/**
	 * 设置转输入操作编码
	 * @param encode
	 */
	public void setEncode(String encode) {
		this.encode = encode;
		this.ftp.setControlEncoding(encode);
	}
	
	public FTPClient getClient(){
		return ftp;
	}
}
