package org.smile.util;

import java.net.InetAddress;

import org.smile.commons.IdGenerator;
import org.smile.commons.StringRandom;
import org.smile.math.NumberUtils;
import org.smile.math.Probability;

/**
 * UUid生成器
 */
public class UUIDGenerator implements IdGenerator{
	/** 计数器最大  最大三位表示*/
	private static final int COUNT_MAX = 36 * 36 * 36;
	/** 计数器 */
	private static int counter = 0;
	/** 36进制 */
	private static final short _36 = 36;
	/** 36进制的第五位 */
	private static final int _36_5 = 36 * 36 * 36 * 36;
	/** 36进制第四位 */
	private static final int _36_4 = 36 * 36 * 36;
	/** 本机的IP地址 */
	private static final int IP;
	/** 分隔符 */
	protected static String sep = "";
	/**标识启动时间*/
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);
	/**启动标记*/
	private static final int flag;

	static {
		int ipadd;
		try {
			ipadd = IptoInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = Math.abs(ipadd);
		flag = JVM%COUNT_MAX;
	}

	/** 把ip转成一个int类型的数字 */
	private static int IptoInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	protected static int getJVM() {
		return JVM;
	}

	/**
	 * 计数
	 * 
	 * @return
	 */
	protected static int getCount() {
		synchronized (UUIDGenerator.class) {
			counter++;
			if (COUNT_MAX == counter) {
				counter = 1;
			}
			return counter;
		}
	}
	
	protected static int getIP() {
		return IP;
	}

	protected static short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	protected static int getLoTime() {
		return Math.abs((int) System.currentTimeMillis());
	}

	protected static String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuilder buf = new StringBuilder("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}
	/**
	 * 转换成8位字符串
	 * 
	 * @param longval
	 * @return
	 */
	protected static String format8(long longval, int radix) {
		return format(longval, radix, "00000000");
	}

	protected static String format4(int longval) {
		String formatted = Integer.toHexString(longval);
		StringBuilder buf = new StringBuilder("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	/**
	 * 以36进制编码当前毫秒数为基础的id 其中包含了随机数和校验码 1-6 位 ip标识 7-14 位当前时间标识
	 * 
	 * @return
	 */
	public static String cdkey() {
		long times = System.currentTimeMillis();
		int count = getCount();
		int random = Probability.randomGetInt(0, 35);
		count += _36_5 * random;
		int random2 = Probability.randomGetInt(0, 35);
		count += _36_4 * random2;
		// 验证码位
		char check = NumberUtils.getChar((int) (((times + IP) / count) % _36));
		StringBuilder string = new StringBuilder(32);
		string.append(format(IP, _36, "000000"));
		string.append(format(times, _36,"000000000"));
		string.append(format(count, _36, "00000"));
		string.append(check);
		string.append(format(flag, _36,"000"));
		return string.toString();
	}

	/**
	 * 较验以36进制编码当前毫秒数为基础的id
	 * 
	 * @return 是否是通过uuid方法生成的
	 */
	public static boolean checkCdkey(String id) {
		long one = NumberUtils.parseLong(id.substring(0, 6), _36);
		long two = NumberUtils.parseLong(id.substring(6, 15), _36);
		long three = NumberUtils.parseLong(id.substring(15, 20), _36);
		long four = NumberUtils.parseLong(id.substring(20, 21), _36);
		return ((one + two) / three) % _36 == four;
	}

	/**
	 * 转换成一个字符串
	 * 
	 * @param longval 数字
	 * @param bit 字符串的长度
	 * @param radix 转成字符串的进制数
	 * @return
	 */
	protected static String format(long longval, int radix, int bit) {
		StringBuilder buf = new StringBuilder(bit);
		for (int i = 0; i < bit; i++) {
			buf.append('0');
		}
		return format(longval, radix, buf);
	}
	/**
	 * integer 转为10位16进制
	 * 
	 * @param intval
	 * @return
	 */
	protected static String format(long intval, int radix, StringBuilder format) {
		int formatSize = format.length();
		String formatted = NumberUtils.toString(intval, radix);
		int len = formatted.length();
		// 如果大小位数
		if (len > formatSize) {
			formatted = formatted.substring(len - formatSize, len);
		}
		format.replace(formatSize - formatted.length(), formatSize, formatted);
		return format.toString();
	}

	/**
	 * integer 转为10位16进制
	 * 
	 * @param intval
	 * @return
	 */
	protected static String format(long intval, int radix, String format) {
		return format(intval, radix, new StringBuilder(format));
	}

	/**
	 * 一个新的32位的UUID
	 * @return
	 */
	public static String uuid32() {
		StringBuilder uuid=new StringBuilder(36);
		uuid.append(format(getIP())).append(sep);
		uuid.append(format(getJVM())).append(sep);
		uuid.append(format4(getHiTime())).append(sep);
		uuid.append(format4(getCount())).append(sep);
		uuid.append(format(getLoTime()));
		return uuid.toString();
	}
	/**
	 * 此私策略与hibernate差不多
	 * 只是采购32进制方式缩短在长度
	 * 新的22位id
	 * @return
	 */
	public static String uuid22() {
		StringBuilder uuid=new StringBuilder(32);
		uuid.append(format(getIP(), _36,"000000")).append(sep);
		uuid.append(format(getJVM(), _36,"0000000")).append(sep);
		uuid.append(format(getHiTime(), _36,"000")).append(sep);
		uuid.append(format(getLoTime(), _36,"000")).append(sep);
		uuid.append(format(getCount(), _36, "000"));
		return uuid.toString();
	}
	/**
	 * smile 定义策略生成 21位
	 * @return
	 */
	public static String uuid() {
		return createBuilder(sep).toString();
	}
	/**
	 * 21位id
	 * 6位ip + 9位时间 +6位count
	 * @return
	 */
	public static String create(String sep) {
		return createBuilder(sep).toString();
	}
	/**
	 * 构建uuid返回builder
	 * @param sep
	 * @return
	 */
	protected static StringBuilder createBuilder(String sep){
		StringBuilder sb = new StringBuilder(32);
		sb.append(format(getIP(), _36, "000000")).append(sep);
		sb.append(format(System.currentTimeMillis(), _36,"000000000")).append(sep);
		sb.append(format(flag * COUNT_MAX + getCount(), _36,"000000")).append(sep);
		return sb;
	}
	/**
	 * /**
	 * 21位id
	 * 6位ip + 9位时间 +6位count  
	 * 如果指定长度大于21 会增加随机字符到指定长度
	 * @return
	 */
	public static String create(int length){
		StringBuilder uuid=createBuilder(sep);
		int count=length-uuid.length();
		if(count>0){
			uuid.append(StringRandom.getInstance().randomLowAlphaNumeric(count));
		}
		return uuid.toString();
	}
	/**
	 * 13位短id  不能一次性大量生成id 一秒内不能超过 46656
	 * 启动标记+ 秒+记数器 标识 
	 * @return
	 */
	public static String shortId(){
		StringBuilder sb = new StringBuilder(20);
		sb.append(format(System.currentTimeMillis()/1000, _36,"0000000"));
		sb.append(format(flag * COUNT_MAX + getCount(), _36,"000000"));
		return sb.toString();
	}

	@Override
	public Object generate() {
		return uuid();
	}
}
