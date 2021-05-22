package org.smile.commons;

import java.util.Random;

import org.smile.util.Base64;

/**
 *   随机生成字符串类
 */
public class StringRandom {
	/**希腊字母范围*/
	protected static final char[] ALPHA_RANGE = new char[] {'A', 'Z', 'a', 'z'};
	/**大小写字母与数字范围*/
	protected static final char[] ALPHA_NUMERIC_RANGE = new char[] {'0', '9', 'A', 'Z', 'a', 'z'};
	/**小写希腊字母与数字*/
	protected static final char[] LOW_ALPHA_NUMERIC_CHARS="abcdefghigklmnopqrstuvwxyz0123456789".toCharArray();
	/**大写希腊字母与数字*/
	protected static final char[] UPPER_ALPHA_NUMERIC_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	
	protected final static StringRandom INSTANCE = new StringRandom();

	/**
	 * Returns default instance of <code>RandomString</code>.
	 */
	public static StringRandom getInstance() {
		return INSTANCE;
	}

	protected final Random rnd;

	/**
	 * Creates new random string.
	 */
	public StringRandom() {
		this(new Random());
	}

	/**
	 * Creates new random string with given random object,
	 * so random strings can be repeated.
	 */
	public StringRandom(Random rnd) {
		this.rnd = rnd;
	}

	/**
	 * Creates new random string with given seed.
	 */
	public StringRandom(long seed) {
		this.rnd = new Random(seed);
	}


	/**
	 * 从给定的char数组中随机长一个长度为count 的字符串
	 * @param count 结果字符串的长度
	 * @param chars  源字符数组
	 * @return 一个字符串
	 */
	public String random(int count, char[] chars) {
		if (count == 0) {
			return Strings.EMPTY;
		}
		char[] result = new char[count];
		while (count-- > 0) {
			result[count] = chars[rnd.nextInt(chars.length)];
		}
		return new String(result);
	}

	/**
	 * 把给定的字符串当成一个char数组 来随机出一个长度为count的字符串
	 * @param count 长度
	 * @param chars 源字符集
	 * @return
	 */
	public String random(int count, String chars) {
		return random(count, chars.toCharArray());
	}


	/**
	 * 随机出一个字符串
	 * @param count 长度
	 * @param start 起始字符
	 * @param end 结束字符
	 * @return 
	 */
	public String random(int count, char start, char end) {
		if (count == 0) {
			return Strings.EMPTY;
		}
		char[] result = new char[count];
		int len = end - start + 1;
		while (count-- > 0) {
			result[count] = (char) (rnd.nextInt(len) + start);
		}
		return new String(result);
	}

	/**
	 * ASCII value is between <code>32</code> and <code>126</code> (inclusive).
	 */
	public String randomAscii(int count) {
		return random(count, (char) 32, (char) 126);
	}

	/**
	 * 数字字符串
	 * @param count 长度
	 * @return
	 */
	public String randomNumeric(int count) {
		return random(count, '0', '9');
	}

	/**
	 * Creates random string whose length is the number of characters specified.
	 * Characters are chosen from the multiple sets defined by range pairs.
	 * All ranges must be in acceding order.
	 */
	public String randomRanges(int count, char... ranges) {
		if (count == 0) {
			return Strings.EMPTY;
		}
		int i = 0;
		int len = 0;
		int lens[] = new int[ranges.length];
		while (i < ranges.length) {
			int gap = ranges[i + 1] - ranges[i] + 1;
			len += gap;
			lens[i] = len;
			i += 2;
		}

		char[] result = new char[count];
		while (count-- > 0) {
			char c = 0;
			int r = rnd.nextInt(len);
			for (i = 0; i < ranges.length; i += 2) {
				if (r < lens[i]) {
					r += ranges[i];
					if (i != 0) {
						r -= lens[i - 2];
					}
					c = (char) r;
					break;
				}
			}
			result[count] = c;
		}
		return new String(result);
	}
	/**
	 * 字母中随机
	 * @param count
	 * @return
	 */
	public String randomAlpha(int count) {
		return randomRanges(count, ALPHA_RANGE);
	}

	/***
	 * 字母和数字集合中随机
	 * @param count
	 * @return
	 */
	public String randomAlphaNumeric(int count) {
		return randomRanges(count, ALPHA_NUMERIC_RANGE);
	}
	
	/**小写字母与数据的中随机*/
	public String randomLowAlphaNumeric(int count){
		return random(count, LOW_ALPHA_NUMERIC_CHARS);
	}
	
	/**大写字母与数据的中随机*/
	public String randomUpperAlphaNumeric(int count){
		return random(count, UPPER_ALPHA_NUMERIC_CHARS);
	}
	/**
	 * 从base64编码字符集中随机
	 * @param count
	 * @return
	 */
	public String randomBase64(int count) {
		return random(count, Base64.CHARS);
	}

}