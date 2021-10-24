package org.smile.orm.mapping.flag;

import org.smile.commons.NotImplementedException;

import java.lang.reflect.Field;

public class SmileMapperFlagHandler implements MapperFlagHandler {
	
	@Override
	public TableFlag getTableFlag(Class clazz) {
		SmileTableFlag flag=new SmileTableFlag();
		if(flag.checkFlag(clazz)){
			return flag;
		}
		return null;
	}

	@Override
	public PropertyFlag getPropertyFlag(TableFlag tableFlag,Field field) {
		SmilePropertyFlag flag=new SmilePropertyFlag();
		if(flag.checkFlag(tableFlag,field)){
			return flag;
		}
		return null;
	}
}
