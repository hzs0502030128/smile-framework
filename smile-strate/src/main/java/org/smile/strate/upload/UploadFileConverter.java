package org.smile.strate.upload;

import java.io.File;
import java.io.IOException;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.io.IOUtils;
import org.smile.reflect.Generic;

public class UploadFileConverter extends AbstractTypeConverter<File>{

	@Override
	public Class<File> getType() {
		return File.class;
	}

	@Override
	public File convert(Generic generic, Object value) throws ConvertException {
		if(value==null){
			return null;
		}
		value=getFirst(value);
		if(value instanceof FieldItem){
			FieldItem item=(FieldItem)value;
			if(item.isInMemory()){
				File file=item.getTempFile();
				try {
					IOUtils.write(file, item.getMemoryBytes());
				} catch (IOException e) {
					throw new ConvertException(e);
				}
				return file;
			}else{
				return item.getFile();
			}
		}else if(value instanceof String){
			return new File((String)value);
		}
		return null;
	}
}
