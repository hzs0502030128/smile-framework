package org.smile.report.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
/**
 * 对pdf设置签单信息
 * @author 胡真山
 *
 */
public class PdfSignMark extends PdfReaderSupport{

	/**签单图片*/
	private List<Sign> images = new LinkedList<Sign>();

	/**
	 * 确认签名
	 * @param input
	 * @param putPut
	 * @throws DocumentException 
	 */
	public void commitSign(InputStream input, OutputStream putPut) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(input);
		setAccessAbled(reader);
		PdfStamper stamp = new PdfStamper(reader, putPut);
		try{
			
			int pageSize = reader.getNumberOfPages();// 原pdf文件的总页数
			for (int i = 1; i <= pageSize; i++) {
				doPageSign(stamp, pageSize, i);
			}
		}finally{
			stamp.close();// 关闭
		}
	}
	
	protected void doPageSign(PdfStamper stamp,int pageSize,int page) throws DocumentException{
		PdfContentByte content;
		int  signIdx=0;
		for (Sign sign : images) {
			content = sign.getContent(stamp, page);
			doEachSign(content, page, sign, signIdx);
			signIdx++;
		}
	}
	
	protected void doEachSign(PdfContentByte content,int page,Sign sign,int signIdx) throws DocumentException{
		content.setGState(sign.gstate);// 图片水印 透明度
		content.addImage(sign.image);// 图片水印
	}

	/**
	 * 添加签名图片
	 * @param image 图片
	 * @param lvl
	 * @param x
	 * @param y
	 * @param r
	 * @param rd
	 */
	public void addSignImg(Image image, byte lvl, int x, int y) {
		Sign sign = new Sign(image);
		images.add(sign);
		sign.level=lvl;
		image.setAbsolutePosition(x, y); // 坐标
	}
	
	public void addSignImg(Sign sign){
		images.add(sign);
	}

	public static class Sign {
		/**上层*/
		public static final byte LVL_OVER = 1;
		/**下层*/
		public static final byte LVL_UNDER = 0;
		
		byte level;
		/**签名图片*/
		Image image;
		
		PdfGState gstate = new PdfGState();

		public Sign(Image image) {
			this.image = image;
		}
		
		public void setLvl(byte lvl){
			this.level=lvl;
		}
		/**
		 * 设置透明度 0.5 -> 50%
		 * @param opacity 
		 */
		public void setOpacity(float opacity) {
			gstate.setFillOpacity(opacity);
		}

		public PdfContentByte getContent(PdfStamper stamp, int i) {
			if (level == LVL_OVER) {
				return stamp.getOverContent(i);
			}
			if (level == LVL_UNDER) {
				return stamp.getUnderContent(i);
			}
			return null;
		}
		/**
		 * 旋转 弧度
		 */
		public void setRotation(float f){
			image.setRotation(f);
		}
		/**
		 * 旋转 角度
		 * @param rd
		 */
		public void setRotationDegrees(float rd){
			image.setRotationDegrees(rd);
		}
		
		/**
		 * 自定义大小
		 */
		public void setScaleAbsolute(float w,float h){
			image.scaleAbsolute(w, h);
		}
		
		/**
		 * 依照比例缩放
		 * @param p
		 */
		public void setScalePercent(float p){
			image.scalePercent(p);
		}
		
		public void setAbsolutePosition(float x,float y){
			image.setAbsolutePosition(x, y);
		}

	}
}
