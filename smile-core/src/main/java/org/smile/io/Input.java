package org.smile.io;

import java.io.IOException;

public interface Input {
	/***
	 * 	读取byte数组
	 * @param bytes
	 * @param start
	 * @param len
	 * @throws IOException 
	 */
	public int read(byte[] bytes,int off,int len) throws IOException;
}
