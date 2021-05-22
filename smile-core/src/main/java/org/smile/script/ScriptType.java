package org.smile.script;

public enum ScriptType {
	/***
	 * js
	 */
	JS("javascript"),
	/***
	 * groovy
	 */
	GROOVY("groovy"),
	/**smile expression language*/
	SMIMLE_ELXL("smile_elxl"),
	/**
	 * 基本
	 * */
	BASE("base");
	
	private String value;
	
	private ScriptType(String name){
		this.value=name;
	}
	
	public String value(){
		return this.value;
	}
	/**
	 * 创建一个脚本执行器
	 * @return
	 */
	public ScriptExecutor createExecutor(){
		return new ScriptExecutor(this.value);
	}
}
