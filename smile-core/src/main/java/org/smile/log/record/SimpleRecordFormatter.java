package org.smile.log.record;

import org.smile.template.SimpleStringTemplate;
import org.smile.template.StringTemplate;
import org.smile.template.StringTemplateParser;

public class SimpleRecordFormatter implements RecordFormatter{
	
	protected String pattern="[?{date}] [?{level}] (?{className}.java:?{lineNumber}).?{methodName} - ?{message}";
	/**输出模板*/
	protected StringTemplate template=new SimpleStringTemplate(new StringTemplateParser("?{","}"),pattern);
	
	@Override
	public String format(LogRecord record) {
		return template.processToString(record);
	}

	public String getPattern() {
		return pattern;
	}
	
	@Override
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
