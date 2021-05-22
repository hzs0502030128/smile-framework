package org.smile.io;

import java.io.IOException;
import java.io.Reader;

public class StringReader extends Reader {
	/**读取的字符序列源*/
	private CharSequence sequence;
	private int length;
	private int next = 0;
	private int mark = 0;

	public StringReader(CharSequence paramString) {
		this.sequence = paramString;
		this.length = paramString.length();
	}

	private void ensureOpen() throws IOException {
		if (this.sequence == null) {
			throw new IOException("Stream closed");
		}
	}

	public int read() throws IOException {
		synchronized (this.lock) {
			ensureOpen();
			if (this.next >= this.length) {
				return -1;
			}
			return this.sequence.charAt(this.next++);
		}
	}

	public int read(char[] descChars, int offset, int end) throws IOException {
		synchronized (this.lock) {
			ensureOpen();
			if (end == 0) {
				return 0;
			}
			if (this.next >= this.length) {
				return -1;
			}
			int i = Math.min(this.length - this.next, end);
			if (sequence instanceof String) {
				((String) this.sequence).getChars(this.next, this.next + i, descChars, offset);
			} else {
				String strs = sequence.subSequence(this.next, this.next + i).toString();
				strs.getChars(0,i, descChars, offset);
			}
			this.next += i;
			return i;
		}
	}

	public long skip(long paramLong) throws IOException {
		synchronized (this.lock) {
			ensureOpen();
			if (this.next >= this.length) {
				return 0L;
			}
			long l = Math.min(this.length - this.next, paramLong);
			l = Math.max(-this.next, l);
			this.next = ((int) (this.next + l));
			return l;
		}
	}

	public boolean ready() throws IOException {
		synchronized (this.lock) {
			ensureOpen();
			return true;
		}
	}

	public boolean markSupported() {
		return true;
	}

	public void mark(int paramInt) throws IOException {
		if (paramInt < 0) {
			throw new IllegalArgumentException("Read-ahead limit < 0");
		}
		synchronized (this.lock) {
			ensureOpen();
			this.mark = this.next;
		}
	}

	public void reset() throws IOException {
		synchronized (this.lock) {
			ensureOpen();
			this.next = this.mark;
		}
	}

	public void close() {
		this.sequence = null;
	}
}
