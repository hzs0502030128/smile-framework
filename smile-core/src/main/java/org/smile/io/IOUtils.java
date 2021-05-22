package org.smile.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.smile.Smile;
import org.smile.io.buff.ByteBand;

/**
 * 这是一个io工具类
 * 
 * @author strive
 *
 */
public class IOUtils {
	/** 默认的字符编码 */
	public static final String DEFAULT_ENCODE = Smile.ENCODE;
	/** 默认的缓冲长度 */
	public static final int DEFAULT_BUFF_SIZE = Smile.IO_READ_BUFF_SIZE;
	/**KB*/
    public static final long ONE_KB = 1024;
    /**MB*/
    public static final long ONE_MB = ONE_KB * ONE_KB;
    /**chanel复制时最大缓存 30MB*/
    private static final long CHANEL_COPY_BUFFER_SIZE = ONE_MB * 30;

	/**
	 * 把输入流转化一个byte数组
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] stream2byte(InputStream is, int initSize) throws IOException {
		try {
			byte[] b = new byte[initSize];
			ByteBand bf = new ByteBand(initSize);
			int i = 0;
			while ((i = is.read(b)) > 0) {
				bf.append(b, 0, i);
			}
			return bf.toArray();
		} finally {
			close(is);
		}
	}

	/**
	 * 复制一个流中的内容到一个输出流中 完成后会把输入 输出流关闭
	 * 
	 * @param is
	 *            源流
	 * @param os
	 *            目标输出流
	 * @param initSize
	 *            缓存长度
	 * @throws IOException
	 */
	public static void copy(InputStream is, OutputStream os, int initSize) throws IOException {
		byte[] b = new byte[initSize];
		int i = 0;
		try {
			while ((i = is.read(b)) > 0) {
				os.write(b, 0, i);
			}
			os.flush();
		} finally {
			close(is);
			close(os);
		}
	}

	/**
	 * 复制流中的数据
	 * @param is 输入流
	 * @param os 输出流
	 * @param close 在复制完成后是否关闭输入流和输出流
	 * @throws IOException
	 */
	public static void copy(InputStream is, OutputStream os, boolean close) throws IOException {
		byte[] b = new byte[DEFAULT_BUFF_SIZE];
		int i = 0;
		try {
			while ((i = is.read(b)) > 0) {
				os.write(b, 0, i);
			}
			os.flush();
		} finally {
			if (close) {
				close(is);
				close(os);
			}
		}
	}

	/**
	 * 复制一个流中的内容到一个输出流中 默认缓存长度 完成后会把输入 输出流关闭
	 * 
	 * @param is
	 *            源流
	 * @param os
	 *            目标输出流
	 * @throws IOException
	 */
	public static void copy(InputStream is, OutputStream os) throws IOException {
		copy(is, os, DEFAULT_BUFF_SIZE);
	}

	/**
	 * 把输入流转化一个byte数组
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] stream2byte(InputStream is) throws IOException {
		return stream2byte(is, DEFAULT_BUFF_SIZE);
	}

	/**
	 * 读取一个文件到一个byte数组中
	 * 
	 * @param file
	 *            要读取的文件
	 * @return
	 * @throws IOException
	 *             当文件不存在或其它io异常
	 */
	public static byte[] file2byte(File file) throws IOException {
		int size = (int) file.length();
		byte[] data = new byte[size];
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			is.read(data);
		} finally {
			close(is);
		}
		return data;
	}

	/**
	 * 写一个byte数组到一个文件中
	 * 
	 * @param file
	 * @param bytes
	 * @throws IOException
	 */
	public static void write(File file, byte[] bytes) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		try {
			os.write(bytes);
			os.flush();
		} finally {
			close(os);
		}
	}

	/**
	 * 打开一个文件
	 * 
	 * @param file
	 * @param append
	 *            是否是对文件增加添加 false 时写入文件时会覆盖原内容
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canWrite() == false) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent + "' could not be created");
				}
			}
		}
		return new FileOutputStream(file, append);
	}

	/**
	 * 打开一个输出流
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream openOutputStream(String path) throws IOException {
		return openOutputStream(new File(path), false);
	}

	/**
	 * 打开一个输出流
	 * 
	 * @param 输出文件
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream openOutputStream(File file) throws IOException {
		return openOutputStream(file, false);
	}

	/**
	 * 一行一行读取文件
	 * 
	 * @param file
	 * @param encode 读取的编码
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(File file, String encode) throws IOException {
		BufferedReader reader = new BufferedReader(file, encode);
		return readLines(reader);
	}

	/**
	 * 写入多行数据到文件中
	 * 
	 * @param file
	 * @param lines
	 * @param encode
	 * @throws IOException
	 */
	public static void writeLines(FileOutputStream os, List<String> lines, String encode) throws IOException {
		try {
			Writer write = new OutputStreamWriter(os, encode);
			int index = 0;
			for (String str : lines) {
				write.write(str);
				if (index < lines.size() - 1) {
					write.write(Smile.LINE_SEPARATOR);
				}
			}
			write.flush();
		} finally {
			close(os);
		}
	}

	/**
	 * 写入多个字符串到文件中
	 * 
	 * @param file
	 * @param lines
	 * @param encode
	 * @throws IOException
	 */
	public static void writeLines(File file, List<String> lines, String encode) throws IOException {
		FileOutputStream os = openOutputStream(file);
		writeLines(os, lines, encode);
	}

	/**
	 * 一行一行的读取文件 返回所有的行的一个集合
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(BufferedReader reader) throws IOException {
		List<String> list = new LinkedList<String>();
		String temp;
		while ((temp = reader.readLine()) != null) {
			list.add(temp);
		}
		return list;
	}

	/**
	 * 一行一行读取文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(File file) throws IOException {
		BufferedReader reader = new BufferedReader(file);
		return readLines(reader);
	}

	/**
	 * 写一个字符串到文件中
	 * 
	 * @param file
	 * @param string
	 * @throws IOException
	 */
	public static void write(File file, String string) throws IOException {
		FileOutputStream os = openOutputStream(file, false);
		try {
			os.write(string.getBytes());
			os.flush();
		} finally {
			close(os);
		}
	}

	/**
	 * 往文件中添加 一个字符串到文件中内容最后
	 * 
	 * @param file
	 * @param string
	 * @throws IOException
	 */
	public static void append(File file, String string) throws IOException {
		FileOutputStream os = openOutputStream(file, true);
		try {
			os.write(string.getBytes());
			os.flush();
		} finally {
			close(os);
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param file
	 * @param to
	 * @throws IOException
	 */
	public static void copy(File file ,File to) throws IOException{
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		FileChannel fileChannelInput = null;
		FileChannel fileChannelOutput = null;
		try {
    		fileInputStream = new FileInputStream(file);
    		File toParent=to.getParentFile();
    		if(toParent!=null&&!toParent.exists()){//创建目录
    			if(!to.getParentFile().mkdirs()){
    				throw new IOException(toParent +" can not created ");
    			}
    		}
    		fileOutputStream = new FileOutputStream(to);
    		//得到fileInputStream的文件通道
    		fileChannelInput = fileInputStream.getChannel();
    		//得到fileOutputStream的文件通道
    		fileChannelOutput = fileOutputStream.getChannel();
    		//将fileChannelInput通道的数据，写入到fileChannelOutput通道
    		long size = fileChannelInput.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = size - pos > CHANEL_COPY_BUFFER_SIZE ? CHANEL_COPY_BUFFER_SIZE : size - pos;
                pos += fileChannelOutput.transferFrom(fileChannelInput, pos, count);
            }
		} finally {
    		close(fileInputStream);
    		close(fileChannelInput);
    		close(fileOutputStream);
    		close(fileChannelOutput);
		}
    }

	/**
	 * 读取字符串
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readString(InputStream is) throws IOException {
		BufferedReader dis = new BufferedReader(is);
		try {
			return dis.readToString();
		} finally {
			close(dis);
		}
	}

	/**
	 * 读取read中的数据成string
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String readString(Reader reader) throws IOException {
		BufferedReader dis = new BufferedReader(reader);
		try {
			return dis.readToString();
		} finally {
			close(dis);
		}
	}

	/**
	 * 关闭输入流
	 * 
	 * @param is
	 */
	public static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭输入流
	 * 
	 * @param is
	 */
	public static void close(Writer is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 有为空判断的关闭
	 * 
	 * @param reader
	 */
	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 有为空判断关闭输出流
	 * 
	 * @param os
	 */
	public static void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 打开一个文件做为writer
	 * 
	 * @param file
	 *            要写入的文件
	 * @param encode
	 * @return
	 * @throws IOException
	 */
	public static Writer openOutputStreamWriter(File file, String encode) throws IOException {
		return new OutputStreamWriter(openOutputStream(file), Charset.forName(encode));
	}

	/**
	 * 打开一个文件做为写入对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Writer openOutputStreamWriter(File file) throws IOException {
		return openOutputStreamWriter(file, DEFAULT_ENCODE);
	}

	/**
	 * 关闭一个可关闭的对象
	 * 
	 * @param close
	 */
	public static void close(Closeable close) {
		if (close != null) {
			try {
				close.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
