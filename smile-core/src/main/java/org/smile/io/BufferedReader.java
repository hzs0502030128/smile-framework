package org.smile.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.smile.Smile;
import org.smile.io.buff.CharBand;
import org.smile.log.LoggerHandler;
/**
 * 可以缓冲的字符流
 * @author strive
 *
 */
public class BufferedReader extends Reader implements Readable,LoggerHandler{
	
	private InputStream is;
	
	private InputStreamReader isr;
	
	private java.io.BufferedReader br;
	
	public BufferedReader(Reader reader) {
		this.br=new java.io.BufferedReader(reader);
	}
	
	public BufferedReader(java.io.BufferedReader reader) {
		this.br=reader;
	}
	/**
	 * 从一个文件创建一个流
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public BufferedReader(File file) throws IOException{
		try {
			this.is=new FileInputStream(file);
			this.isr=new InputStreamReader(is);
			this.br=new java.io.BufferedReader(isr);
		} catch (Exception e) {
			throw new IOException("从文件"+file.getName()+"实现化流出错"+e);
		}
	}
	/**
	 * 从一个文件创建一个流
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public BufferedReader(File file,String encode) throws IOException{
		try {
			this.is=new FileInputStream(file);
			this.isr=new InputStreamReader(is,encode);
			this.br=new java.io.BufferedReader(isr);
		} catch (Exception e) {
			throw new IOException("从文件"+file.getName()+"实现化流出错"+e);
		}
	}
	/**
	 * 从一个文件创建一个流
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public BufferedReader(String file) throws FileNotFoundException{
		try {
			this.is=new FileInputStream(file);
			this.isr=new InputStreamReader(is);
			this.br=new java.io.BufferedReader(isr);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("从文件"+file+"实现化流出错"+e);
		}
	}
	/**
	 * 从一个输入流创建
	 * @param is
	 */
	public BufferedReader(InputStream is){
		this(is,Smile.ENCODE);
	}
	
	/**
	 * 从一个输入流创建
	 * @param is
	 */
	public BufferedReader(InputStream is,String charset){
		this.is=is;
		try {
			this.isr=new InputStreamReader(is,charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		this.br=new java.io.BufferedReader(isr);
	}
	/**
	 * 读取数据内容为一个字符串
	 * @return
	 * @throws IOException
	 */
	public String readToString() throws IOException{
		CharBand chars=new CharBand();
		String index;
		//是否是第一行
		boolean isFirst=true;
		while((index=readLine())!=null){
			if(isFirst){
				isFirst=false;
			}else{
				//不是第一行时添加换行符
				chars.append(Smile.LINE_SEPARATOR);
			}
			chars.append(index);
		}
		return chars.toString();
	}
	/**
	 * 请取到字符串中
	 * @param filter 对一行进行过滤 是否需要添加到结果中
	 * @return
	 * @throws IOException
	 */
	public String readToString(ReadLineFilter filter) throws IOException{
		CharBand chars=new CharBand();
		String line;
		//是否是第一行
		boolean isFirst=true;
		while((line=readLine())!=null){
			if(!filter.needLine(line)){
				continue;
			}
			if(isFirst){
				isFirst=false;
			}else{
				//不是第一行时添加换行符
				chars.append(Smile.LINE_SEPARATOR);
			}
			chars.append(line);
		}
		return chars.toString();
	}
	
	/**过滤行*/
	public interface ReadLineFilter{
		boolean needLine(String line);
	}
	
	/**
	 * 读取一行
	 * @return
	 * @throws IOException
	 */
	public String readLine() throws IOException{
		return this.br.readLine();
	}
	
	/**
	 * 读取数据到一个字符数组中
	 * @param b
	 * @return
	 * @throws IOException
	 */
	public int read(char[] b) throws IOException{
		return this.br.read(b);
	}
	/**
	 * 读取一个字符
	 * @return
	 * @throws IOException
	 */
	public int read() throws IOException{
		return this.br.read();
	}
	
	/**
	 * 关闭连接
	 */
	public void close(){
		try{
			if(this.br!=null){
				this.br.close();
			}else if(isr!=null){
				this.isr.close();
			}else if(is!=null){
				this.is.close();
			}
		}catch(IOException e){
			logger.error("关闭流了出错",e);
		}
	}
	
	@Override
	public int read(java.nio.CharBuffer cb) throws IOException {
		return br.read(cb);
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return br.read(cbuf, off, len);
	}
	@Override
	public long skip(long n) throws IOException {
		return br.skip(n);
	}
	@Override
	public boolean ready() throws IOException {
		return br.ready();
	}
	@Override
	public boolean markSupported() {
		return br.markSupported();
	}
	@Override
	public void mark(int readAheadLimit) throws IOException {
		br.mark(readAheadLimit);
	}
	@Override
	public void reset() throws IOException {
		br.reset();
	}
}
