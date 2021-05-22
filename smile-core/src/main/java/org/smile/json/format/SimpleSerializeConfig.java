package org.smile.json.format;

public class SimpleSerializeConfig extends DefaultSerializeConfig{
	
	/**
	 * 默认构建不进行缩进操作
	 */
	public SimpleSerializeConfig(){
	}
	/**
	 * 设置空值是否需要序列化
	 * @param nullValueView
	 */
	public void setNullValueView(boolean nullValueView){
		this.nullValueView=nullValueView;
	}

}
