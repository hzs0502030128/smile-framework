package org.smile.db.meta;
/**
 * 列信息
 * @author 胡真山
 * 2015年10月14日
 */
public class ColumnInfo {
	/**
	 * 字段名称
	 */
	protected String name;
	/**
	 * 字段类型名称
	 */
	protected String type;
	/**
	 * 字段类型java类型
	 */
	protected String className;
	/**
	 * 字段的备注
	 */
	protected String remark;
	/**
	 * 字段长度
	 */
	protected int length;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
