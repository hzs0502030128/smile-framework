package org.smile.commons;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.smile.math.MathUtils;
import org.smile.math.Probability;

public class LongIdGenerator implements IdGenerator{
	
	/**计数器*/
	private static final AtomicInteger counter=new AtomicInteger();
	/**
	 * 序列计数器最大值
	 */
	private static final int MAX_COUNT=MathUtils.ceilPowerOf2(8192*2)-1;
	/**启动时间标识*/
	private static final int JVM_COUNT=MathUtils.ceilPowerOf2(256)-1;

	private static final int STATE =0;

	/** 本机的IP地址 */
	private static final int IP;
	/**标识启动时间*/
	private static final int JVM = (int) ((System.currentTimeMillis()/1000)&JVM_COUNT);
	
	static {
		int ipadd;
		try {
			ipadd = IptoInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = Probability.randomGetInt(1, 256);
		}
		IP = Math.abs(ipadd);
	}

	/** 把ip转成一个int类型的数字 */
	private static int IptoInt(byte[] bytes) {
		return (bytes[2]^bytes[3])&256;
	}

	@Override
	public Object generate() {
		return generateId();
	}
	
	/**
	 *	 获取计数器
	 * @return
	 */
	private static int getCount() {
		int count=counter.incrementAndGet();
		if(count>MAX_COUNT) {
			synchronized (counter) {
				count=counter.incrementAndGet();
				if(count>MAX_COUNT) {//
					counter.set(0);
					count= counter.incrementAndGet();
				}
			}
		}
		return count;
	}
	/**
	 * 创建一个长整形的id
	 * @return
	 */
	public static long generateId() {
		//时间去毫秒
		long times=(System.currentTimeMillis()-STATE)>>10;
		//IP
		times=(times<<8)+IP;
		//启动时间
		times=(times<<8)+JVM;
		//取8192以内数器
		times=(times<<14)+getCount(); 
		return times;
	}

}
