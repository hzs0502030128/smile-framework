package org.smile.io.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.io.download.DownloadHandler.DownloadRange;

public class Downloader{
	/**
	 * 下载文件时读取的缓冲字节长度
	 */
	protected int BUFFER_SIZE=4096;
	
	protected DownloadHandler  downloadHandler=new SimpleDownloadHandler();
	/**
	 * 下载文件 
	 * @param download 需要下载的文件
	 * @param fileName 下载文件显示名称
	 * @param request 
	 * @param response
	 * @throws IOException
	 */
	public void download(File download,String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.reset();//必需
		//告诉客户端允许断点续传多线程连接下载
		//响应的格式是:      Accept-Ranges: bytes
		response.setHeader("Accept-Ranges", "bytes");
		long len=download.length();
		DownloadRange range;
		if(downloadHandler.isUseRange(request)){
			range=downloadHandler.getDownloadArea(request);
			if(range.end==0){
				range.end=len-1;
			}
			response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
		}else{
			range=new DownloadRange(0, len-1);
		}
		response.setHeader("Content-Length",String.valueOf(range.getLength()));
		if(range.start>0||range.end<len-1){
			response.setHeader("Content-Range", range.getRangeString());
		}
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename=\""+ encodeFileName(fileName)+ "\"");
		FileInputStream fis=new FileInputStream(download);
		fis.skip(range.start);
		byte[] b = new byte[BUFFER_SIZE];
		int i;
		long cnt = 0;
		//response转出流
		ServletOutputStream os = response.getOutputStream();
		//开始下载
		while ((i = fis.read(b)) != -1 && cnt <= range.end) {
			cnt = cnt + b.length;
			if(cnt>range.end){
				os.write(b, 0, (int)(b.length-cnt+range.end+1));
			}else{
				os.write(b, 0, i);
			}
			os.flush();
			response.flushBuffer();
		}
		fis.close();
	}
	/**
	 * 对下载文件名称进行转码
	 * @param fileName 下载显示的文件名
	 * @return 转码后的字符串
	 */
	protected String encodeFileName(String fileName){
		try {
			return  new String(fileName.getBytes("GBK"), "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			return fileName;
		}
	}
}
