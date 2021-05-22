package org.smile.strate.upload;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;

public class UploadInputSteamConverter extends AbstractTypeConverter<InputStream>{

	@Override
	public Class<InputStream> getType() {
		return InputStream.class;
	}

	@Override
	public InputStream convert(Generic generic, Object value) throws ConvertException {
		if(value==null){
			return null;
		}
		value=getFirst(value);
		try{
			if(value instanceof FieldItem){
				FieldItem item=(FieldItem)value;
				if(item.isInMemory()){
					return item.getInputStream();
				}else{
					return new BufferedInputStream(new FileInputStream(item.getFile()));
				}
			}else if(value instanceof String){
				return new FileInputStream((String)value);
			}
		}catch(IOException e){
			throw new ConvertException(e);
		}
		return null;
	}
}
