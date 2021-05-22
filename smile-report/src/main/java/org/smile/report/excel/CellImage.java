package org.smile.report.excel;

import java.io.IOException;
import java.io.InputStream;

import org.smile.io.IOUtils;

public class CellImage {
	/**
	 * 图片内容 
	 */
	protected byte[] images;
	/**
	 * 图片类型  如Workbook.PICTURE_TYPE_PNG
	 */
	protected int type;
	/**
	 * 是否需要适应大小
	 */
	private float width;
	
	private float height;
	
	private int left;
	
	private int top;
	/**
	 * @param is 图上片的输入流
	 * @param type
	 * @throws IOException
	 */
	public CellImage(InputStream is,int type) throws IOException{
		this.images=IOUtils.stream2byte(is);
		this.type=type;
	}
	/**
	 * @param bytes 图片的内容
	 * @param type
	 */
	public CellImage(byte[] bytes,int type){
		this.images=bytes;
		this.type=type;
	}

	public byte[] getImages() {
		return images;
	}
	/**
	 * 填满 
	 */
	public void fullCell(){
		this.height=1;
		this.width=1;
	}
	
	public void setImages(byte[] images) {
		this.images = images;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isResize(){
		return width>0&&height>0;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public void setLeft(int left) {
		this.left = left;
	}
	
}
