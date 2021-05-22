package org.smile.util;



public class Base64 {
	/**base64字符集*/
	private static final String EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	/**base64字符*/
	public static final char[] CHARS=EncodeChars.toCharArray();
	
	private static final int[] DecodeChars = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1,
			-1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
	
	/**
	 * 编码
	 * @param text
	 * @return
	 */
	protected static StringBuilder base64Encode(CharSequence text) {
		int i = 0;
		int len = text.length();
		int c1;
		int c2;
		int c3;

		char[] base64EncodeChars = CHARS;

		StringBuilder sb = new StringBuilder(len*3);

		while (i < len) {
			c1 = ((int) text.charAt(i++)) & 0xff;

			if (i == len) {
				sb.append(base64EncodeChars[c1 >> 2]);
				sb.append(base64EncodeChars[(c1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			c2 = ((int) text.charAt(i++));
			if (i == len) {
				sb.append(base64EncodeChars[c1 >> 2]);
				sb.append(base64EncodeChars[((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4)]);
				sb.append(base64EncodeChars[(c2 & 0xF) << 2]);
				sb.append("=");
				break;
			}
			c3 = ((int) text.charAt(i++));
			sb.append(base64EncodeChars[c1 >> 2]);
			sb.append(base64EncodeChars[((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4)]);

			sb.append(base64EncodeChars[((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6)]);
			sb.append(base64EncodeChars[c3 & 0x3F]);
		}
		return sb;
	}

	/**
	 * 解码
	 * 
	 * @param text
	 * @return
	 */
	protected static StringBuilder base64Decode(String text) {
		int c1;
		int c2;
		int c3;
		int c4;
		int i = 0;
		int len = text.length();

		StringBuilder sb = new StringBuilder(len);
		char[] c = text.toCharArray();

		while (i < len) {
			do {
				c1 = DecodeChars[((int) c[i++]) & 0xff];
			} while (i < len && c1 == -1);
			if (c1 == -1)
				break;
			/* c2 */
			do {
				c2 = DecodeChars[((int) c[i++]) & 0xff];
			} while (i < len && c2 == -1);
			if (c2 == -1)
				break;
			sb.append((char) ((c1 << 2) | ((c2 & 0x30) >> 4)));
			/* c3 */
			do {
				c3 = ((int) c[i++]) & 0xff;
				if (c3 == 61)
					return sb;

				c3 = DecodeChars[c3];
			} while (i < len && c3 == -1);
			if (c3 == -1)
				break;
			sb.append((char) (((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2)));
			/* c4 */
			do {
				c4 = ((int) c[i++]) & 0xff;
				if (c4 == 61)
					return sb;
				c4 = DecodeChars[c4];
			} while (i < len && c4 == -1);
			if (c4 == -1)
				break;
			sb.append((char) (((c3 & 0x03) << 6) | c4));

		}
		return sb;
	}

	/**
	 * 中文转成字符
	 * 
	 * @param text
	 * @return
	 */
	protected static StringBuilder utf16to8(CharSequence text) {
		
		StringBuilder out = new StringBuilder(text.length()*3);

		int len = text.length();
		int c;

		for (int i = 0; i < len; i++) {
			c = (int) text.charAt(i); // str.charCodeAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				out.append((char)c);
			} else if (c > 0x07FF) {
				out.append((char) (0xE0 | ((c >> 12) & 0x0F)));
				out.append((char) (0x80 | ((c >> 6) & 0x3F)));
				out.append((char) (0x80 | ((c >> 0) & 0x3F)));
			} else {
				out.append((char) (0xC0 | ((c >> 6) & 0x1F)));
				out.append( (char) (0x80 | ((c >> 0) & 0x3F)));
			}
		}
		return out;
	}

	/**
	 * 字符转成中文
	 * 
	 * @param text
	 * @return
	 */
	protected static StringBuilder utf8to16(CharSequence text) {
		StringBuilder out = new StringBuilder(text.length());
		int i = 0;
		int len = text.length();
		int c;

		int char2;
		int char3;
		// var char2, char3;

		while (i < len) {
			c = (int) text.charAt(i++);
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				// 0xxxxxxx
				out.append(text.charAt(i - 1));
				break;
			case 12:
			case 13:
				// 110x xxxx 10xx xxxx
				char2 = (int) text.charAt(i++);
				out.append((char) (((c & 0x1F) << 6) | (char2 & 0x3F)));
				break;
			case 14:
				// 1110 xxxx 10xx xxxx 10xx xxxx
				char2 = (int) text.charAt(i++);
				char3 = (int) text.charAt(i++);
				out.append((char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0)));
				break;
			}
		}
		return out;
	}

	/**
	 * 编码 此方法是与前端的一个js匹配
	 * 与常用的encode不通用
	 * @param text
	 * @return
	 */
	public static String encodeUTF(String text) {
		return base64Encode(utf16to8(text)).toString();
	}
	/**
	 * 编码 此方法是与前端的一个js匹配
	 * 与常用的decode不通用
	 * @param text
	 * @return
	 */
	public static String decodeUTF(String text) {
		return utf8to16(base64Decode(text)).toString();
	}
	/**
	 * 把字节数组编码成字符串
	 * @param bytes 要编码的字节数组
	 * @return
	 */
	public static String encode(byte[] bytes){
		return new String(encodeToChar(bytes,false));
	}
	
	/**
	 * 编码成字符串数组
	 * @param arr
	 * @return
	 */
	public static char[] encodeToChar(byte[] arr) {
		return encodeToChar(arr, false);
	}
	
	/**
	 * Encodes a raw byte array into a BASE64 <code>char[]</code>.
	 * @param lineSeparator optional CRLF after 76 chars, unless EOF.
	 */
	public static char[] encodeToChar(byte[] arr, boolean lineSeparator) {
		int len = arr != null ? arr.length : 0;
		if (len == 0) {
			return new char[0];
		}

		int evenlen = (len / 3) * 3;
		int cnt = ((len - 1) / 3 + 1) << 2;
		int destLen = cnt + (lineSeparator ? (cnt - 1) / 76 << 1 : 0);
		char[] dest = new char[destLen];

		for (int s = 0, d = 0, cc = 0; s < evenlen;) {
			int i = (arr[s++] & 0xff) << 16 | (arr[s++] & 0xff) << 8 | (arr[s++] & 0xff);

			dest[d++] = CHARS[(i >>> 18) & 0x3f];
			dest[d++] = CHARS[(i >>> 12) & 0x3f];
			dest[d++] = CHARS[(i >>> 6) & 0x3f];
			dest[d++] = CHARS[i & 0x3f];

			if (lineSeparator && (++cc == 19) && (d < (destLen - 2))) {
				dest[d++] = '\r';
				dest[d++] = '\n';
				cc = 0;
			}
		}

		int left = len - evenlen; // 0 - 2.
		if (left > 0) {
			int i = ((arr[evenlen] & 0xff) << 10) | (left == 2 ? ((arr[len - 1] & 0xff) << 2) : 0);

			dest[destLen - 4] = CHARS[i >> 12];
			dest[destLen - 3] = CHARS[(i >>> 6) & 0x3f];
			dest[destLen - 2] = left == 2 ? CHARS[i & 0x3f] : '=';
			dest[destLen - 1] = '=';
		}
		return dest;
	}

	/**
	 * 把base64的字符数组解码成byte数组
	 * @param base64chars 
	 * @return
	 */
	public static byte[] decode(char[] base64chars) {
		int length = base64chars.length;
		if (length == 0) {
			return new byte[0];
		}

		int sndx = 0, endx = length - 1;
		int pad = base64chars[endx] == '=' ? (base64chars[endx - 1] == '=' ? 2 : 1) : 0;
		int cnt = endx - sndx + 1;
		int sepCnt = length > 76 ? (base64chars[76] == '\r' ? cnt / 78 : 0) << 1 : 0;
		int len = ((cnt - sepCnt) * 6 >> 3) - pad;
		byte[] dest = new byte[len];

		int d = 0;
		for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
			int i = DecodeChars[base64chars[sndx++]] << 18 | DecodeChars[base64chars[sndx++]] << 12 | DecodeChars[base64chars[sndx++]] << 6 | DecodeChars[base64chars[sndx++]];

			dest[d++] = (byte) (i >> 16);
			dest[d++] = (byte) (i >> 8);
			dest[d++] = (byte) i;

			if (sepCnt > 0 && ++cc == 19) {
				sndx += 2;
				cc = 0;
			}
		}

		if (d < len) {
			int i = 0;
			for (int j = 0; sndx <= endx - pad; j++) {
				i |= DecodeChars[base64chars[sndx++]] << (18 - j * 6);
			}
			for (int r = 16; d < len; r -= 8) {
				dest[d++] = (byte) (i >> r);
			}
		}

		return dest;
	}

	public static byte[] encodeToByte(byte[] arr) {
		return encodeToByte(arr, false);
	}

	/**
	 * Encodes a raw byte array into a BASE64 <code>char[]</code>.
	 * @param lineSep optional CRLF after 76 chars, unless EOF.
	 */
	public static byte[] encodeToByte(byte[] arr, boolean lineSep) {
		int len = arr != null ? arr.length : 0;
		if (len == 0) {
			return new byte[0];
		}

		int evenlen = (len / 3) * 3;
		int cnt = ((len - 1) / 3 + 1) << 2;
		int destlen = cnt + (lineSep ? (cnt - 1) / 76 << 1 : 0);
		byte[] dest = new byte[destlen];

		for (int s = 0, d = 0, cc = 0; s < evenlen;) {
			int i = (arr[s++] & 0xff) << 16 | (arr[s++] & 0xff) << 8 | (arr[s++] & 0xff);

			dest[d++] = (byte) CHARS[(i >>> 18) & 0x3f];
			dest[d++] = (byte) CHARS[(i >>> 12) & 0x3f];
			dest[d++] = (byte) CHARS[(i >>> 6) & 0x3f];
			dest[d++] = (byte) CHARS[i & 0x3f];

			if (lineSep && ++cc == 19 && d < destlen - 2) {
				dest[d++] = '\r';
				dest[d++] = '\n';
				cc = 0;
			}
		}

		int left = len - evenlen;
		if (left > 0) {
			int i = ((arr[evenlen] & 0xff) << 10) | (left == 2 ? ((arr[len - 1] & 0xff) << 2) : 0);

			dest[destlen - 4] = (byte) CHARS[i >> 12];
			dest[destlen - 3] = (byte) CHARS[(i >>> 6) & 0x3f];
			dest[destlen - 2] = left == 2 ? (byte) CHARS[i & 0x3f] : (byte) '=';
			dest[destlen - 1] = '=';
		}
		return dest;
	}

	/**
	 * Decodes BASE64 encoded byte array.
	 */
	public static byte[] decode(byte[] arr) {
		int length = arr.length;
		if (length == 0) {
			return new byte[0];
		}

		int sndx = 0, endx = length - 1;
		int pad = arr[endx] == '=' ? (arr[endx - 1] == '=' ? 2 : 1) : 0;
		int cnt = endx - sndx + 1;
		int sepCnt = length > 76 ? (arr[76] == '\r' ? cnt / 78 : 0) << 1 : 0;
		int len = ((cnt - sepCnt) * 6 >> 3) - pad;
		byte[] dest = new byte[len];

		int d = 0;
		for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
			int i = DecodeChars[arr[sndx++]] << 18 | DecodeChars[arr[sndx++]] << 12 | DecodeChars[arr[sndx++]] << 6 | DecodeChars[arr[sndx++]];

			dest[d++] = (byte) (i >> 16);
			dest[d++] = (byte) (i >> 8);
			dest[d++] = (byte) i;

			if (sepCnt > 0 && ++cc == 19) {
				sndx += 2;
				cc = 0;
			}
		}

		if (d < len) {
			int i = 0;
			for (int j = 0; sndx <= endx - pad; j++) {
				i |= DecodeChars[arr[sndx++]] << (18 - j * 6);
			}
			for (int r = 16; d < len; r -= 8) {
				dest[d++] = (byte) (i >> r);
			}
		}

		return dest;
	}
	
	/**
	 * 把BASE64 码的字符串解码成byte数组
	 */
	public static byte[] decode(String s) {
		int length = s.length();
		if (length == 0) {
			return new byte[0];
		}

		int sndx = 0, endx = length - 1;
		int pad = s.charAt(endx) == '=' ? (s.charAt(endx - 1) == '=' ? 2 : 1) : 0;
		int cnt = endx - sndx + 1;
		int sepCnt = length > 76 ? (s.charAt(76) == '\r' ? cnt / 78 : 0) << 1 : 0;
		int len = ((cnt - sepCnt) * 6 >> 3) - pad;
		byte[] dest = new byte[len];

		int d = 0;
		for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
			int i = DecodeChars[s.charAt(sndx++)] << 18 | DecodeChars[s.charAt(sndx++)] << 12 | DecodeChars[s.charAt(sndx++)] << 6 | DecodeChars[s.charAt(sndx++)];

			dest[d++] = (byte) (i >> 16);
			dest[d++] = (byte) (i >> 8);
			dest[d++] = (byte) i;

			if (sepCnt > 0 && ++cc == 19) {
				sndx += 2;
				cc = 0;
			}
		}

		if (d < len) {
			int i = 0;
			for (int j = 0; sndx <= endx - pad; j++) {
				i |= DecodeChars[s.charAt(sndx++)] << (18 - j * 6);
			}
			for (int r = 16; d < len; r -= 8) {
				dest[d++] = (byte) (i >> r);
			}
		}

		return dest;
	}

}
