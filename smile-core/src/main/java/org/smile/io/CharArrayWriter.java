
package org.smile.io;

import java.io.IOException;
import java.io.Writer;

import org.smile.io.buff.CharBand;

public class CharArrayWriter extends Writer {
	//缓存写入的字符
	protected final CharBand buffer;

	/**
	 * Creates a new writer. The buffer capacity is
	 * initially 1024 bytes, though its size increases if necessary.
	 */
	public CharArrayWriter() {
		buffer=new CharBand();
	}

	/**
	 * Creates a new char array writer, with a buffer capacity of
	 * the specified size, in bytes.
	 *
	 * @param size the initial size.
	 * @throws IllegalArgumentException if size is negative.
	 */
	public CharArrayWriter(int size) {
		buffer = new CharBand(size);
	}

	/**
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(char[] b, int off, int len) {
		buffer.append(b, off, len);
	}

	/**
	 * Writes single byte.
	 */
	@Override
	public void write(int b) {
		buffer.append((char) b);
	}

	@Override
	public void write(String s, int off, int len) {
		write(s.toCharArray(), off, len);
	}

	/**
	 * @see java.io.CharArrayWriter#size()
	 */
	public int size() {
		return buffer.size();
	}

	/**
	 * Closing a <code>FastCharArrayWriter</code> has no effect. The methods in
	 * this class can be called after the stream has been closed without
	 * generating an <code>IOException</code>.
	 */
	@Override
	public void close() {
		//nop
	}

	/**
	 * Flushing a <code>FastCharArrayWriter</code> has no effects.
	 */
	@Override
	public void flush() {
		//nop
	}

	/**
	 * @see java.io.CharArrayWriter#reset()
	 */
	public void reset() {
		buffer.clear();
	}

	/**
	 * @see java.io.CharArrayWriter#writeTo(java.io.Writer)
	 */
	public void writeTo(Writer out) throws IOException {
		int index = buffer.index();
		for (int i = 0; i < index; i++) {
			char[] buf = buffer.array(i);
			out.write(buf);
		}
		out.write(buffer.array(index), 0, buffer.offset());
	}

	/**
	 * @see java.io.CharArrayWriter#toCharArray()
	 */
	public char[] toCharArray() {
		return buffer.toArray();
	}

	/**
	 * @see java.io.CharArrayWriter#toString()
	 */
	@Override
	public String toString() {
		return new String(toCharArray());
	}
}