package org.smile.beans.converter.type;

import java.io.File;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.StringUtils;

public class FileConverter extends AbstractTypeConverter<File>{

	@Override
	public File convert(Generic generic, Object value)
			throws ConvertException {
		if(StringUtils.isNull(value)){
			return null;
		}
		value=getFirst(value);
		try {
			return new File(value.toString());
		} catch (Exception e) {
			throw new ConvertException(e);
		}
	}

	@Override
	public Class<File> getType() {
		return File.class;
	}
}
