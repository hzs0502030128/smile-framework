package org.smile.json.format;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.smile.commons.Strings;
import org.smile.expression.DefaultContext;
import org.smile.expression.Engine;

public class IgnoreableConfig extends SimpleSerializeConfig{

	public IgnoreableConfig(){
		super();
	}

	protected Engine engine=new Engine();
	
	@Override
	public boolean ignore(PropertyDescriptor pd,Object value) {
		JsonIgnore ignore=pd.getReadMethod().getAnnotation(JsonIgnore.class);
		if(ignore!=null){
			if(Strings.BLANK.equals(ignore.value())){
				return true;
			}else{
				return (Boolean) engine.evaluate(new DefaultContext(value), ignore.value());
			}
		}
		return false;
	}

	@Override
	public boolean ignore(Field field,Object value) {
		JsonIgnore ignore=field.getAnnotation(JsonIgnore.class);
		if(ignore!=null){
			if(Strings.BLANK.equals(ignore.value())){
				return true;
			}else{
				return (Boolean) engine.evaluate(new DefaultContext(value), ignore.value());
			}
		}
		return false;
	}
}
