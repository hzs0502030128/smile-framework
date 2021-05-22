package org.smile.json;

import java.util.Date;

import org.smile.json.format.SerializeConfig;

public abstract class JSONWriter{
	
	protected SerializeConfig config;
	
	public JSONWriter(){
		this.config=SerializeConfig.NULL_NOT_VIEW;
	}
	
	public JSONWriter(SerializeConfig config){
		this.config=config;
	}
	
	public JSONWriter write(Date date){
		write(this.config.formatDate(date));
		return this;
	}
	
	public JSONWriter writeSqlDate(java.sql.Date date){
		write(this.config.formatSqlDate(date));
		return this;
	}

	public void setConfig(SerializeConfig config) {
		this.config = config;
	}
	
	public abstract JSONWriter writeDouble(Double d);
	
	public abstract JSONWriter writeFloat(Float f);
	
	public abstract JSONWriter write(String str);
	
	public abstract JSONWriter write(char chr);
	
	public abstract JSONWriter write(CharSequence cs);
	
	public abstract String toJSONString();
	
	public  void flush(){}
	
}
