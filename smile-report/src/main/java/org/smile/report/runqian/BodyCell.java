package org.smile.report.runqian;

import com.runqian.report4.model.NormalCell;
import com.runqian.report4.usermodel.INormalCell;

/**
 * 表休单元格
 * @author strive
 *
 */
public class BodyCell extends NormalCell {
	
	public BodyCell(){
		this.setBBColor(-16763905); //设定下边框线色
		this.setBBStyle(INormalCell.LINE_SOLID); //设定下边框类型
		this.setBBWidth((float)0.75); //设定下边框线粗
	
		//左边框
		this.setLBColor(-16763905);
		this.setLBStyle(INormalCell.LINE_SOLID);
		this.setLBWidth((float)0.75);
	
		//右边框
		this.setRBColor(-16763905);
		this.setRBStyle(INormalCell.LINE_SOLID);
		this.setRBWidth((float) 0.75);
	
		//上边框
		this.setTBColor(-16763905);
		this.setTBStyle(INormalCell.LINE_SOLID);
		this.setTBWidth((float)0.75);
		
		this.setVAlign(INormalCell.VALIGN_MIDDLE);
		this.setHAlign(INormalCell.HALIGN_CENTER);
		this.setAdjustSizeMode(INormalCell.ADJUST_FIXED_WRAP);
		this.setTextWrap(true);
		this.setFontName("宋体");
	}
}
