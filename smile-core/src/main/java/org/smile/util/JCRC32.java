package org.smile.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * 
 * @author strive
 * 
 */
public class JCRC32 {
	/**
	 * @param file 文件
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String getFileCRCCode(File file) throws IOException {

		FileInputStream fileInputStream = new FileInputStream(file);
		return getStreamCRCCode(fileInputStream);
	}
	/**
	 * 得到一个流的CRC-32值
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String getStreamCRCCode(InputStream input) throws IOException {
		CRC32 crc32 = new CRC32();
		for (CheckedInputStream checkedinputstream = new CheckedInputStream(
				input, crc32); checkedinputstream.read() != -1;)
			;
		return Long.toHexString(crc32.getValue());
	}
	/**
	 * 得到一个流的CRC-32值
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String getStringCRCCode(String inputStr) throws IOException {
		CRC32 crc32 = new CRC32();
		InputStream input=new ByteArrayInputStream(inputStr.getBytes());
		for (CheckedInputStream checkedinputstream = new CheckedInputStream(
				input, crc32); checkedinputstream.read() != -1;)
			;
		return Long.toHexString(crc32.getValue());
	}
	/**
	 * 得到一个流的CRC-32值
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String getBytesCRCCode(byte[] inputBytes) throws IOException {
		CRC32 crc32 = new CRC32();
		InputStream input=new ByteArrayInputStream(inputBytes);
		for (CheckedInputStream checkedinputstream = new CheckedInputStream(
				input, crc32); checkedinputstream.read() != -1;)
			;
		return Long.toHexString(crc32.getValue());
	}
	/**
	 * 得到一个文件的CRC-32值
	 * @param file 文件路径
	 * @return 
	 * @throws IOException
	 */
	public static String getFileCRCCode(String file) throws IOException {
		return getFileCRCCode(new File(file));
	}
	/**
	 * 验证一个流的CRC-32的值
	 * @param input
	 * @param value
	 * @return
	 * @throws IOException 
	 */
	public static boolean checkCRCCode(InputStream input,String value) throws IOException{
		String code=getStreamCRCCode(input);
		return code.equals(value);
	}
}
