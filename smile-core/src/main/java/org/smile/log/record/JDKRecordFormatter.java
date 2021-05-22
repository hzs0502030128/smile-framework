package org.smile.log.record;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;

import org.smile.commons.Strings;
import org.smile.log.impl.JDKLogger;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.StringTemplate;
import org.smile.template.StringTemplateParser;

public class JDKRecordFormatter extends Formatter{
	/**输入格式化字符串*/
	protected String pattern="[?{date}] [?{level}] (?{className}.java:?{lineNumber}).?{methodName} - ?{message}"+Strings.NEWLINE;
	/**输出模板*/
	protected StringTemplate template=new SimpleStringTemplate(new StringTemplateParser("?{","}"),pattern);
	@Override
	public String format(java.util.logging.LogRecord record) {
        LogRecord red=new LogRecord(JDKLogger.class, record.getLoggerName(), JDKLogger.parseJdkLvl(record.getLevel()), record.getMessage());
        String format=template.processToString(red);
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.print(format);
            record.getThrown().printStackTrace(pw);
            pw.close();
            return sw.toString();
        }
        return format;
	}
}
