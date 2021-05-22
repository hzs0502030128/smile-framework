package org.smile.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.smile.commons.Strings;
/**
 * MD5加密类
 * @author strive
 *
 */
public class Md5
{
	 private static final int BUFFER_SIZE = 1024;
	 private static final int S11 = 7;
	 private static final int S12 = 12;
	 private static final int S13 = 17;
	 private static final int S14 = 22;
	 private static final int S21 = 5;
	 private static final int S22 = 9;
	 private static final int S23 = 14;
	 private static final int S24 = 20;
	 private static final int S31 = 4;
	 private static final int S32 = 11;
	 private static final int S33 = 16;
	 private static final int S34 = 23;
	 private static final int S41 = 6;
	 private static final int S42 = 10;
	 private static final int S43 = 15;
	 private static final int S44 = 21;
	 private static byte padding[] = {
	     -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	     0, 0, 0, 0
	 };
	 private InputStream in;
	 private boolean stringp;
	 private int state[];
	 private long count;
	 private byte buffer[];
	 private byte digest[];

	 private static String stringify(byte bytes[])
	 {
	     StringBuffer stringbuffer = new StringBuffer(2 * bytes.length);
	     for(int i = 0; i < bytes.length; i++)
	     {
	         int j = (bytes[i] & 240) >> 4;
	         int k = bytes[i] & 15;
	         stringbuffer.append(new Character((char)(j <= 9 ? 48 + j : (97 + j) - 10)));
	         stringbuffer.append(new Character((char)(k <= 9 ? 48 + k : (97 + k) - 10)));
	     }
	     return stringbuffer.toString();
	 }
	
	 private final int F(int i, int j, int k)
	 {
	     return i & j | ~i & k;
	 }
	
	 private final int G(int i, int j, int k)
	 {
	     return i & k | j & ~k;
	 }
	
	 private final int H(int i, int j, int k)
	 {
	     return i ^ j ^ k;
	 }
	
	 private final int I(int i, int j, int k)
	 {
	     return j ^ (i | ~k);
	 }
	
	 private final int rotate_left(int i, int j)
	 {
	     return i << j | i >>> 32 - j;
	 }
	
	 private final int FF(int i, int j, int k, int l, int i1, int j1, int k1)
	 {
	     i += F(j, k, l) + i1 + k1;
	     i = rotate_left(i, j1);
	     i += j;
	     return i;
	 }
	
	 private final int GG(int i, int j, int k, int l, int i1, int j1, int k1)
	 {
	     i += G(j, k, l) + i1 + k1;
	     i = rotate_left(i, j1);
	     i += j;
	     return i;
	 }
	
	 private final int HH(int i, int j, int k, int l, int i1, int j1, int k1)
	 {
	     i += H(j, k, l) + i1 + k1;
	     i = rotate_left(i, j1);
	     i += j;
	     return i;
	 }
	
	 private final int II(int i, int j, int k, int l, int i1, int j1, int k1)
	 {
	     i += I(j, k, l) + i1 + k1;
	     i = rotate_left(i, j1);
	     i += j;
	     return i;
	 }
	
	 private final void decode(int ai[], byte abyte0[], int i, int j)
	 {
	     int k = 0;
	     for(int l = 0; l < j; l += 4)
	     {
	         ai[k] = abyte0[i + l] & 255 | (abyte0[i + l + 1] & 255) << 8 | (abyte0[i + l + 2] & 255) << 16 | (abyte0[i + l + 3] & 255) << 24;
	         k++;
	     }
	
	 }
	
	 private final void transform(byte abyte0[], int i)
	 {
	     int j = state[0];
	     int k = state[1];
	     int l = state[2];
	     int i1 = state[3];
	     int ai[] = new int[16];
	     decode(ai, abyte0, i, 64);
	     j = FF(j, k, l, i1, ai[0], 7, -680876936);
	     i1 = FF(i1, j, k, l, ai[1], 12, -389564586);
	     l = FF(l, i1, j, k, ai[2], 17, 606105819);
	     k = FF(k, l, i1, j, ai[3], 22, -1044525330);
	     j = FF(j, k, l, i1, ai[4], 7, -176418897);
	     i1 = FF(i1, j, k, l, ai[5], 12, 1200080426);
	     l = FF(l, i1, j, k, ai[6], 17, -1473231341);
	     k = FF(k, l, i1, j, ai[7], 22, -45705983);
	     j = FF(j, k, l, i1, ai[8], 7, 1770035416);
	     i1 = FF(i1, j, k, l, ai[9], 12, -1958414417);
	     l = FF(l, i1, j, k, ai[10], 17, -42063);
	     k = FF(k, l, i1, j, ai[11], 22, -1990404162);
	     j = FF(j, k, l, i1, ai[12], 7, 1804603682);
	     i1 = FF(i1, j, k, l, ai[13], 12, -40341101);
	     l = FF(l, i1, j, k, ai[14], 17, -1502002290);
	     k = FF(k, l, i1, j, ai[15], 22, 1236535329);
	     j = GG(j, k, l, i1, ai[1], 5, -165796510);
	     i1 = GG(i1, j, k, l, ai[6], 9, -1069501632);
	     l = GG(l, i1, j, k, ai[11], 14, 643717713);
	     k = GG(k, l, i1, j, ai[0], 20, -373897302);
	     j = GG(j, k, l, i1, ai[5], 5, -701558691);
	     i1 = GG(i1, j, k, l, ai[10], 9, 38016083);
	     l = GG(l, i1, j, k, ai[15], 14, -660478335);
	     k = GG(k, l, i1, j, ai[4], 20, -405537848);
	     j = GG(j, k, l, i1, ai[9], 5, 568446438);
	     i1 = GG(i1, j, k, l, ai[14], 9, -1019803690);
	     l = GG(l, i1, j, k, ai[3], 14, -187363961);
	     k = GG(k, l, i1, j, ai[8], 20, 1163531501);
	     j = GG(j, k, l, i1, ai[13], 5, -1444681467);
	     i1 = GG(i1, j, k, l, ai[2], 9, -51403784);
	     l = GG(l, i1, j, k, ai[7], 14, 1735328473);
	     k = GG(k, l, i1, j, ai[12], 20, -1926607734);
	     j = HH(j, k, l, i1, ai[5], 4, -378558);
	     i1 = HH(i1, j, k, l, ai[8], 11, -2022574463);
	     l = HH(l, i1, j, k, ai[11], 16, 1839030562);
	     k = HH(k, l, i1, j, ai[14], 23, -35309556);
	     j = HH(j, k, l, i1, ai[1], 4, -1530992060);
	     i1 = HH(i1, j, k, l, ai[4], 11, 1272893353);
	     l = HH(l, i1, j, k, ai[7], 16, -155497632);
	     k = HH(k, l, i1, j, ai[10], 23, -1094730640);
	     j = HH(j, k, l, i1, ai[13], 4, 681279174);
	     i1 = HH(i1, j, k, l, ai[0], 11, -358537222);
	     l = HH(l, i1, j, k, ai[3], 16, -722521979);
	     k = HH(k, l, i1, j, ai[6], 23, 76029189);
	     j = HH(j, k, l, i1, ai[9], 4, -640364487);
	     i1 = HH(i1, j, k, l, ai[12], 11, -421815835);
	     l = HH(l, i1, j, k, ai[15], 16, 530742520);
	     k = HH(k, l, i1, j, ai[2], 23, -995338651);
	     j = II(j, k, l, i1, ai[0], 6, -198630844);
	     i1 = II(i1, j, k, l, ai[7], 10, 1126891415);
	     l = II(l, i1, j, k, ai[14], 15, -1416354905);
	     k = II(k, l, i1, j, ai[5], 21, -57434055);
	     j = II(j, k, l, i1, ai[12], 6, 1700485571);
	     i1 = II(i1, j, k, l, ai[3], 10, -1894986606);
	     l = II(l, i1, j, k, ai[10], 15, -1051523);
	     k = II(k, l, i1, j, ai[1], 21, -2054922799);
	     j = II(j, k, l, i1, ai[8], 6, 1873313359);
	     i1 = II(i1, j, k, l, ai[15], 10, -30611744);
	     l = II(l, i1, j, k, ai[6], 15, -1560198380);
	     k = II(k, l, i1, j, ai[13], 21, 1309151649);
	     j = II(j, k, l, i1, ai[4], 6, -145523070);
	     i1 = II(i1, j, k, l, ai[11], 10, -1120210379);
	     l = II(l, i1, j, k, ai[2], 15, 718787259);
	     k = II(k, l, i1, j, ai[9], 21, -343485551);
	     state[0] += j;
	     state[1] += k;
	     state[2] += l;
	     state[3] += i1;
	 }
	
	 private final void update(byte abyte0[], int i)
	 {
	     int j = (int)(count >> 3) & 63;
	     count += i << 3;
	     int k = 64 - j;
	     int l = 0;
	     if(i >= k)
	     {
	         System.arraycopy(abyte0, 0, buffer, j, k);
	         transform(buffer, 0);
	         for(l = k; l + 63 < i; l += 64)
	             transform(abyte0, l);
	
	         j = 0;
	     } else
	     {
	         l = 0;
	     }
	     System.arraycopy(abyte0, l, buffer, j, i - l);
	 }
	
	 private byte[] end()
	 {
	     byte abyte0[] = new byte[8];
	     for(int i = 0; i < 8; i++)
	         abyte0[i] = (byte)(int)(count >>> i * 8 & 255L);
	
	     int j = (int)(count >> 3) & 63;
	     int k = j >= 56 ? 120 - j : 56 - j;
	     update(padding, k);
	     update(abyte0, 8);
	     return encode(state, 16);
	 }
	
	 private byte[] encode(int ai[], int i)
	 {
	     byte abyte0[] = new byte[i];
	     int j = 0;
	     for(int k = 0; k < i; k += 4)
	     {
	         abyte0[k] = (byte)(ai[j] & 255);
	         abyte0[k + 1] = (byte)(ai[j] >> 8 & 255);
	         abyte0[k + 2] = (byte)(ai[j] >> 16 & 255);
	         abyte0[k + 3] = (byte)(ai[j] >> 24 & 255);
	         j++;
	     }
	
	     return abyte0;
	 }
	
	 public byte[] getDigest()
	     throws IOException
	 {
	     byte abyte0[] = new byte[1024];
	     int i = -1;
	     if(digest != null)
	         return digest;
	     while((i = in.read(abyte0)) > 0) 
	         update(abyte0, i);
	     digest = end();
	     return digest;
	 }
	
	 public byte[] processString()
	 {
	     if(!stringp)
	         throw new RuntimeException(getClass().getName() + "[processString]" + " not a string.");
	     try{
	    	 return getDigest();
	     }catch(IOException e){
	    	 throw new RuntimeException(getClass().getName() + "[processString]" + ": implementation error.");
	     }
	 }
	
	 public String getStringDigest()
	 {
	     if(digest == null)
	         throw new RuntimeException(getClass().getName() + "[getStringDigest]" + ": called before processing.");
	     else
	         return stringify(digest);
	 }
	 /**
	  * @param s  原字符串
	  * @param encoding 编码
	  */
	 public Md5(String s, String encoding)
	 {
	     in = null;
	     stringp = false;
	     state = null;
	     count = 0L;
	     buffer = null;
	     digest = null;
	     byte abyte0[] = null;
	     try
	     {
	         abyte0 = s.getBytes(encoding);
	     }
	     catch(UnsupportedEncodingException unsupportedencodingexception)
	     {
	         throw new RuntimeException("no " + encoding + " encoding!!!");
	     }
	     stringp = true;
	     in = new ByteArrayInputStream(abyte0);
	     state = new int[4];
	     buffer = new byte[64];
	     count = 0L;
	     state[0] = 1732584193;
	     state[1] = -271733879;
	     state[2] = -1732584194;
	     state[3] = 271733878;
	 }
	 /**
	  * 
	  * @param s 原字符串
	  * 默认UTF-8编码
	  */
	 public Md5(String s)
	 {
	     this(s, Strings.UTF_8);
	 }
	 /**
	  * 从一个流中得到原数据
	  * @param inputstream
	  */
	 public Md5(InputStream inputstream)
	 {
	     in = null;
	     stringp = false;
	     state = null;
	     count = 0L;
	     buffer = null;
	     digest = null;
	     stringp = false;
	     in = inputstream;
	     state = new int[4];
	     buffer = new byte[64];
	     count = 0L;
	     state[0] = 1732584193;
	     state[1] = -271733879;
	     state[2] = -1732584194;
	     state[3] = 271733878;
	 }
	 
	 /**
	  * 加密码一个字符串
	  * @param str
	  * @return
	  */
	 public static String encrypt(String str){
		 Md5 md5=new Md5(str);
		 try {
			return stringify(md5.getDigest());
		} catch (IOException e) {
			throw new RuntimeException("MD5加密码失败",e);
		}
	 }
	 /**
	  * 加密一个输入流
	  * @param is
	  * @return
	  */
	 public static String encrypt(InputStream is){
		 Md5 md5=new Md5(is);
		 try {
			return stringify(md5.getDigest());
		} catch (IOException e) {
			throw new RuntimeException("MD5加密码失败",e);
		}
	 } 
	 /**
	  * 加密码一个字符串
	  * @param str
	  * @return
	  */
	 public static boolean validateEncrypt(String cryptograph,String source){
		 return encrypt(source).equals(cryptograph);
	 }
}


