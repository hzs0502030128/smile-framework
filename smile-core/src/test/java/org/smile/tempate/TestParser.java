package org.smile.tempate;

import java.util.HashMap;
import java.util.Map;

import org.smile.log.LoggerHandler;
import org.smile.template.StringTemplateParser;
import org.smile.template.StringTemplateParser.BaseMacroResolver;

public class TestParser implements LoggerHandler{
	
	public  static void main(String[] s){
		StringTemplateParser parser=new StringTemplateParser();
		String sql="我是胡真山啊${ ${dd${name}dd}} sdfdf ${name} --- ${dd2dd}";
		Map m=new HashMap();
		m.put("name", "2");
		m.put("dd2dd", "age");
		m.put("age", "29");
		String ss=parser.parse(sql, new BaseMacroResolver(m));
		System.out.println(ss);
		logger.error(ss);
		logger.error(ss);
		logger.error(ss,new Exception("22"));
	}
}	
