
package org.smile.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.smile.io.buff.ByteBand;
/**
 * 使用byte带实现的 类似于java.io.ByteArrayOutputStream 
 * 但此类不是线程安全的
 * @author 胡真山
 *
 */
public class ByteArrayOutputStream extends OutputStream implements Output{
	/**
	 * 用于缓存数据
	 */
	private  ByteBand buffer;

	/**
	 * Creates a new byte array output stream. The buffer capacity is
	 * initially 1024 bytes, though its size increases if necessary.
	 */
	public ByteArrayOutputStream() {
		buffer=new ByteBand();
	}

	/**
	 * Creates a new byte array output stream, with a buffer capacity of
	 * the specified size, in bytes.
	 *
	 * @param size the initial size.
	 * @throws IllegalArgumentException if size is negative.
	 */
	public ByteArrayOutputStream(int size) {
		buffer = new ByteBand(size);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		buffer.append(b, off, len);
	}

	@Override
	public void write(int b) {
		buffer.append((byte) b);
	}

	public int size() {
		return buffer.size();
	}

	/**
	 * Closing a <code>FastByteArrayOutputStream</code> has no effect. The methods in
	 * this class can be called after the stream has been closed without
	 * generating an <code>IOException</code>.
	 */
	@Override
	public void close() {
		
	}

	public void reset() {
		buffer.clear();
	}

	/**
	 * @see java.io.ByteArrayOutputStream#writeTo(OutputStream)
	 */
	public void writeTo(OutputStream out) throws IOException {
		int index = buffer.index();
		for (int i = 0; i < index; i++) {
			byte[] buf = buffer.array(i);
			out.write(buf);
		}
		out.write(buffer.array(index), 0, buffer.offset());
	}

	public byte[] toByteArray() {
		return buffer.toArray();
	}

	@Override
	public String toString() {
		return new String(toByteArray());
	}

	public String toString(String enc) throws UnsupportedEncodingException {
		return new String(toByteArray(), enc);
	}

	@Override
	public byte[] toArray() {
		return toByteArray();
	}

	@Override
	public byte[] toArray(int off, int len) {
		return buffer.toArray(off, len);
	}

}