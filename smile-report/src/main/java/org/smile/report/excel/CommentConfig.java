package org.smile.report.excel;
/**
 * 批注配置信息
 * @author 胡真山
 *
 */
public class CommentConfig {
	/**
	 * 数据填充名称行
	 */
	protected Integer dataNameRowIndex;
	/**
	 * 底部行的索引
	 */
	protected Integer bottomRowIndex;
	/**
	 * 底部行的结束索引
	 */
	protected Integer bottomRowEnd;
	
	
	protected void setXlsExportTemplate(XlsExportTemplate template){
		if(dataNameRowIndex!=null){
			template.setDataNameRowIndex(dataNameRowIndex);
		}
		if(bottomRowIndex!=null){
			template.bottomRowIndex=bottomRowIndex;
		}
		if(bottomRowEnd!=null){
			template.bottomRowEnd=bottomRowEnd;
			template.bottomRowCount=bottomRowEnd-bottomRowIndex;
		}
	}
	
	public Integer getDataNameRowIndex() {
		return dataNameRowIndex;
	}
	public void setDataNameRowIndex(Integer dataNameRowIndex) {
		this.dataNameRowIndex = dataNameRowIndex;
	}
	public Integer getBottomRowIndex() {
		return bottomRowIndex;
	}
	public void setBottomRowIndex(Integer bottomRowIndex) {
		this.bottomRowIndex = bottomRowIndex;
	}
	public Integer getBottomRowEnd() {
		return bottomRowEnd;
	}
	public void setBottomRowEnd(Integer bottomRowEnd) {
		this.bottomRowEnd = bottomRowEnd;
	}
	
}
