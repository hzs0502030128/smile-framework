package org.smile.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.io.ByteArrayInputStream;
import org.smile.io.IOUtils;

public class MessageDigests {
	
	public static final String MD5="MD5";
	
	public static final String SHA_1="SHA-1";
	
	public static final String SHA_256="SHA-256";
	
	/***
	 * 
	 * @param fis
	 * @param algorithm
	 * @return
	 * @throws IOException
	 */
	public static byte[] digest(final InputStream fis, MessageDigest algorithm) throws IOException {
		algorithm.reset();
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
	 * 对一个流进行md5加密
	 * @param fis 要加密的输入流
	 * @return
	 * @throws IOException
	 */
	public static String md5(final InputStream fis) throws IOException {
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance(MD5);
		} catch (NoSuchAlgorithmException ignore) {
		}

		byte[] digest = digest(fis, md5Digest);

		return StringUtils.toHexLowerString(digest);
	}
	/**
	 * SHA-1 加密
	 * @param fis
	 * @return
	 * @throws IOException
	 */
	public static String sha(final InputStream fis) throws IOException {
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance(SHA_1);
		} catch (NoSuchAlgorithmException ignore) {
		}

		byte[] digest = digest(fis, md5Digest);

		return StringUtils.toHexString(digest);
	}
	
	public static String sha256(String str){
		return sha256(str, Strings.UTF_8);
	}
	
	/**
	 * SHA_256 加密字符串
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String sha256(String str,String charset){
		InputStream is;
		try {
			is = new ByteArrayInputStream(str.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new SmileRunException(e);
		}
		try {
			return sha256(is);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
	/**
	 * SHA_256 加密
	 * @param fis
	 * @return
	 * @throws IOException
	 */
	public static String sha256(final InputStream fis) throws IOException {
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance(SHA_256);
		} catch (NoSuchAlgorithmException ignore) {
		}

		byte[] digest = digest(fis, md5Digest);

		return StringUtils.toHexString(digest);
	}
	
	/**
	 * 对字符串进行MD5使用UTF-8格式编码
	 * @param str
	 * @return
	 */
	public static String md5(String str){
		return md5(str, Strings.UTF_8);
	}
	
	/**
	 * 对一个字符串进行MD5
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String md5(String str,String charset){
		InputStream is;
		try {
			is = new ByteArrayInputStream(str.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new SmileRunException(e);
		}
		try {
			return md5(is);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
}
