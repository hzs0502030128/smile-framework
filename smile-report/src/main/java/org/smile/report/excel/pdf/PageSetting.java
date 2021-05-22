package org.smile.report.excel.pdf;

import com.lowagie.text.Rectangle;
/**
 * 纸张设置
 * @author 胡真山
 *
 */
public class PageSetting {
	
	protected int[] margin=new int[]{10,10,10,10};
	
	protected Rectangle rectangle;
	
	public PageSetting(Rectangle rectangle){
		this.rectangle=rectangle;
	}

	public void setMargin(int[] margin) {
		this.margin = margin;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	
}
