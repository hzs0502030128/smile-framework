package org.smile.io.buff;

import java.io.IOException;

import org.smile.commons.SmileRunException;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;
import org.smile.math.MathUtils;

/**
 * 写入数据的时候是没有参是否可写入多少数据进行验证，
 * 所以在写入数据前需自行判断 读取数据时没有对可读取的长度进行判断，
 * 所以在读取数据的时候需行判断
 * 
 * @author 胡真山
 * @see java.io.DataInputStream
 * @see java.io.DataOutputStream
 */
public class ByteBuffer extends AbstractByteBuff {

	protected static Logger logger = LoggerFactory.getLogger(ByteBuffer.class);
	/** 保留最小空闲长度 */
	protected static final short MIN_EMPTY_SIZE = 16;

	protected static final int MAX_COUNT = Integer.MAX_VALUE / 2;
	
	/** 缓存数组 */
	private byte[] buff;
	/**
	 * 用于回复的读标记的记录
	 */
	protected int mark = -1;
	/**
	 * 读标记索引
	 */
	protected int readIndex = 0;
	/**
	 * 写标记索引
	 */
	protected int writeIndex = 0;
	/**
	 * 读取的总索引
	 */
	protected int readCount = 0;
	/**
	 * 当前缓存开始位置
	 */
	protected int writeCount = 0;

	/** 需要保持的最小空闲长度 */
	protected short minEmptySize = MIN_EMPTY_SIZE;

	/** 读取到多少个的时候需要加锁需要加锁 */
	protected int needLockReadCount = MAX_COUNT;

	/** 用于记数器处理溢出回转 */
	protected int maxCount = MAX_COUNT;
	
	/**用于取余快速定位索引*/
	private int indexMask;

	/**
	 * 构建一个指定长度的缓冲流
	 * 
	 * @param size
	 */
	public ByteBuffer(int size) {
		if(size<MIN_EMPTY_SIZE*2){
			throw new SmileRunException("size must large than "+MIN_EMPTY_SIZE*2);
		}
		//设置成长度为2 n次幂方便快速取模
		size=MathUtils.ceilPowerOf2(size);
		this.indexMask=size-1;
		this.buff = new byte[size];
		needLockReadCount = maxCount - buff.length;
	}
	@Override
	public void mark() {
		mark = readCount;
	}
	@Override
	public void reset() {
		readCount = mark;
		readIndex = readCount&indexMask;
	}
	
	
	@Override
	public int read() throws IOException {
		int i = buff[readIndex] & 0xff;
		incReadCount(1);
		return i;
	}

	/**
	 * 跳过
	 * 
	 * @param n
	 * @return
	 * @throws IOException 
	 */
	@Override
	public long skip(long n) throws IOException {
		incReadCount(n);
		return n;
	}

	@Override
	public int readabled() {
		synchronized (buff){
			int count= writeCount - readCount;
			return count;
		}
	}
	@Override
	public int writeabled() {
		return buff.length - writeCount + readCount - minEmptySize;
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		if (off < 0 || len < 0) {
			throw new IndexOutOfBoundsException("off " + off + ",len " + len);
		}
		int readable=readabled();
		if(readable==0){
			return -1;
		}
		//第一段长度
		int oneSize=buff.length - readIndex;
		len=Math.min(readable, len);
		if ( oneSize>= readable||oneSize>len) {
			System.arraycopy(buff, readIndex, b, off, len);
		} else {
			// 第一段
			System.arraycopy(buff, readIndex, b, off, oneSize);
			int len2=len-oneSize;
			// 第二段
			System.arraycopy(buff, 0, b, off + oneSize, len2);
		}
		incReadCount(len);
		return len;
	}

	/**
	 * 增加读取索引
	 * @param count
	 */
	private void incReadCount(long count) throws IOException {
		if (readCount > needLockReadCount) {
			synchronized (buff) {
				readCount += count;
				readIndex = readCount & indexMask;
			}
		} else {
			readCount += count;
			readIndex = readCount & indexMask;
		}
	}
	@Override
	public void write(int b) throws IOException {
		buff[writeIndex] = (byte) b;
		incWriteCount(1);
	}

	/**
	 * 增加写索引
	 * @param count
	 */
	private void incWriteCount(int count) {
		if (writeCount > needLockReadCount) {
			synchronized (buff) {
				int temp = writeCount & indexMask + buff.length;
				// 同时把读取数据减少
				readCount = readCount - (writeCount - temp);
				writeCount = temp + count;
				// 设置写索引
				writeIndex = writeCount & indexMask;
			}
		} else {
			writeCount += count;
			// 设置写索引
			writeIndex = writeCount & indexMask;
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if(off+len>b.length){
			throw new IOException("byte[] len "+b.length+" can not offset "+off+" len "+len);
		}
		int tempIndex = writeIndex + len;
		if (tempIndex > buff.length) {
			int len1 = buff.length - writeIndex;
			int len2 = tempIndex - buff.length;
			System.arraycopy(b, off, buff, writeIndex, len1);
			System.arraycopy(b, off + len1, buff, 0, len2);
			// 复盖后的索引为第二段的长度
			tempIndex = len2;
		} else {
			System.arraycopy(b, off, buff, writeIndex, len);
		}
		incWriteCount(len);
	}

	@Override
	public String toString() {
		return "writeCount:" + writeCount + ",readCount:" + readCount + ",readIndex:" + readIndex + ",mark:" + mark + ",writeIndex:" + writeIndex;
	}

	/**
	 * 设置最小空闲空间
	 * 
	 * @param minEmptySize
	 */
	public void setMinEmptySize(short minEmptySize) {
		this.minEmptySize = (short) Math.max(MIN_EMPTY_SIZE, minEmptySize);
	}

	/**
	 * 设置最大记数限制
	 * 
	 * @param maxCount
	 */
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
		this.needLockReadCount = maxCount - buff.length;
	}

	/**
	 * 转成bytearray
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public byte[] toByteArray() throws IOException {
		byte[] bytes = new byte[writeCount - readCount];
		read(bytes);
		return bytes;
	}
	@Override
	public void clear() {
		synchronized (buff) {
			this.writeCount = 0;
			this.writeIndex = 0;
			this.mark = -1;
			this.readIndex = 0;
			this.readCount = 0;
		}
	}

	@Override
	public byte[] toArray() {
		try {
			return toByteArray();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public byte[] toArray(int off, int len) {
		byte[] bytes = new byte[len];
		try {
			read(bytes, off, len);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
		return bytes;
	}
}
