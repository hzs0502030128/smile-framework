package org.smile.commons;

import java.io.IOException;
import java.io.Writer;

/***
 * 
 * @author 胡真山
 *
 */
public class StringBandBuffer extends Writer{

	protected StringBand[] buffers = new StringBand[16];

	protected int size=0;

	public StringBandBuffer(StringBand band) {
		buffers[0] = band;
		size = 1;
	}
	
	public StringBandBuffer(){
		//
	}

	public void pushBuffer(StringBand customBuffer) {
		if (size == buffers.length) {
			StringBand[] newBuffers = new StringBand[buffers.length * 2];
			System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
			buffers = newBuffers;
		}
		buffers[size++] = customBuffer;
	}

	/**
	 * 添加一个空的StringBand
	 * @return
	 */
	public StringBand pushBuffer() {
		StringBand sb=new StringBand();
		pushBuffer(sb);
		return sb;
	}

	/**
	 * 当前的最后一个StringBand
	 * @return
	 */
	public StringBand currentBuffer() {
		if(size==0){
			return pushBuffer();
		}
		return buffers[size - 1];
	}
	
	/**
	 * 最后一个buffer退出
	 */
	public void popBuffer() {
		buffers[--size] = null;
	}

	public int getSize() {
		return size;
	}
	
	public StringBand indexOf(int index){
		if(index>=size){
			throw new IndexOutOfSizeException(size, index);
		}
		return buffers[index];
	}
	/**
	 * 合并成一个字符串带
	 * @return
	 */
	public StringBand merge(int start,int end){
		StringBand band=new StringBand();
		for(int i=start;i<=end;i++){
			try {
				band.append(buffers[i]);
			} catch (IOException e) {
			}
		}
		return band;
	}

	
	public StringBand merge(){
		return merge(0, size-1);
	}
	

	@Override
	public void write(String str) throws IOException {
		pushBuffer().append(str);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		pushBuffer().append(str, off, len);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		currentBuffer().append(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		
	}

	@Override
	public void close() throws IOException {
		
	}
	
	

}
