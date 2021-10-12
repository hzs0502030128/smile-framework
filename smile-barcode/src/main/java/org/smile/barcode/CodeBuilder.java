package org.smile.barcode;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.smile.util.ResizeImageUtils;
import org.smile.util.StringUtils;

public class CodeBuilder {
	protected String code;
	protected Character type=' ';
	protected String imgType;
	protected String resize;
	protected int bitPix;
	protected int height;
	protected String color;
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setType(Character type) {
		this.type = type;
	}
	
	public String getImgType() {
		return imgType;
	}
	
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public String getResize() {
		return resize;
	}
	public void setResize(String resize) {
		this.resize = resize;
	}
	
	public void setBitPix(int bitPix) {
		this.bitPix = bitPix;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BufferedImage buildImage(){
		Code128 code128;
		switch(type){
			case 'A':code128=new Code128A(code);break;
			case 'B':code128=new Code128B(code);break;
			case 'C':code128=new Code128C(code);break;
			default:code128=new Code128Auto(code);break;
		}
		if(StringUtils.notEmpty(color)){
			String[] colors=StringUtils.splitc(color, ',');
			code128.setForegroundColor(new Color(Integer.valueOf(colors[0]),Integer.valueOf(colors[1]),Integer.valueOf(colors[2])));
		}
		if(bitPix>0){
			code128.setBitPix(bitPix);
		}
		if(height>0){
			code128.setHeight(height);
		}
		
		BufferedImage image=code128.createImage();
		
		if(StringUtils.notEmpty(resize)){
			String[] resizeArray=StringUtils.splitc(resize, ',');
			int realWidth=code128.barcode.length()*code128.bitPix;
			int width=Integer.valueOf(resizeArray[0]);//长
			width=width>realWidth?realWidth:width;
			int height=Integer.valueOf(resizeArray[1]);
			image=ResizeImageUtils.resizeImage(image,width,height);
		}
		return image;
	}
}
