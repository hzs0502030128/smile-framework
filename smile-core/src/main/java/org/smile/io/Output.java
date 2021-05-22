package org.smile.io;

import java.io.IOException;

public interface Output {
	/**
	 * 写入数组
	 * @param bytes
	 * @param off
	 * @param len
	 */
	public void write(byte[] bytes,int off,int len) throws IOException;
	/**转成数组*/
	public byte[] toArray();
	/**
	 * 转成数组
	 * @param off 
	 * @param len
	 * @return
	 */
	public byte[] toArray(int off,int len);
}
