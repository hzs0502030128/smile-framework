package org.smile.report.excel.pdf;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


public class PdfPageSetting {
	
	private float marginLeft = 20f;
	private float marginRight = 20f;
	private float marginTop = 20f;
	private float marginBottom = 20f;
	private Rectangle rectangle = PageSize.A4;
	private boolean autoPageSize=false;
	
	private boolean heightFixed=false;
	
	private float heightRate;
	private float autoWidth;
	
	private float autoHeight;
	
	public void setAutoPagesize(float w,float h){
		this.autoHeight=h;
		this.autoWidth=w;
		if(autoPageSize){
			useAutoPageSize();
		}
		if(heightFixed){
			heightRate=(rectangle.getHeight()-(20+getMarginTop()+getMarginBottom()))/autoHeight;
		}else{
			heightRate=(rectangle.getWidth()-marginLeft-marginRight)/autoWidth;
		}
	}
	
	public void useAutoPageSize(){
		rectangle=new Rectangle(autoWidth+marginLeft+marginRight,autoHeight+20+getMarginTop()+getMarginBottom());
	}
	
	public float getHeightRate(){
		return heightRate;
	}
	
	public float getWidthRate(){
		return rectangle.getWidth()/autoWidth;
	}
	
	public float getMarginLeft() {
		return marginLeft;
	}
	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}
	public float getMarginRight() {
		return marginRight;
	}
	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}
	public float getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}
	public float getMarginBottom() {
		return marginBottom;
	}
	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	/**
	 * 设置边距
	 * @param margin
	 */
	public void setMargin(float[] margin){
		this.marginLeft=margin[0];
		this.marginTop=margin[1];
		this.marginRight=margin[2];
		this.marginBottom=margin[3];
	}
	public boolean isAutoPageSize() {
		return autoPageSize;
	}
	public void setAutoPageSize(boolean autoPageSize) {
		this.autoPageSize = autoPageSize;
	}

	public boolean isHeightFixed() {
		return heightFixed;
	}

	public void setHeightFixed(boolean heightFixed) {
		this.heightFixed = heightFixed;
	}
	
}
