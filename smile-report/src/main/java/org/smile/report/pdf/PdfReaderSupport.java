package org.smile.report.pdf;

import java.lang.reflect.Field;

import org.smile.commons.SmileRunException;
import org.smile.reflect.FieldUtils;

import com.lowagie.text.pdf.PdfReader;

public class PdfReaderSupport {
	/**
	 * 设置可访问 密码问题
	 * @param reader
	 */
	public static void setAccessAbled(PdfReader reader){
		Field f = FieldUtils.getField(reader.getClass(),"encrypted");
		try {
			f.set(reader, false);
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
	}
}
