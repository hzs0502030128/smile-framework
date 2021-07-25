package org.smile.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.smile.collection.MapUtils;
import org.smile.commons.StringBand;
import org.smile.io.IOUtils;
import org.smile.script.ScriptExecutor;
import org.smile.util.SysUtils;

public class TagTest {
	StringBand string = new StringBand();
	String templateTxt=null;

	@Before
	public void before() throws FileNotFoundException, IOException {
		File file=new File(SysUtils.getUserDir()+"/src/test/java/tagtest.xml");
		templateTxt=IOUtils.readString(new FileInputStream(file));
		string.append("1121444r" + "<c:if test='${r>=0}' age='8' >22223<label>rrr</label>33333333" + "<c:if test='${r==1}'>1111</c:if>" + "<c:elseif test='${r==2}'>2222</c:elseif>" + "</c:if>2333");
		string.append("<c:iterator items='${list}' var='s'>" + "<c:get name='s'/> - " + "<name>${s.name}</name><ag>${s.age*5+2}</age>" + "</c:iterator>");
		string.append("<c:set name='address' value='中国'/>");
		string.append("<address>${address}</address>");
		string.append("<c:when test='${r==1}'><c:then>${address}</c:then><c:other>美国</c:other></c:when>");
		string.append("<c:switch value='${r}'><c:case value='3'>${address}</c:case><c:case value='1'>日本</c:case><c:default>美国</c:default></c:switch>");
		string.append("3333-${ddd} <c:for start='1' end ='10' status='s' step='2'>->${s.count} ${s.index} ${s.last}</c:for>");
	}

	@Test
	public void shouldAnswerWithTrue() throws Exception {
		SimpleContext context = new SimpleContext();
		context.set("r", 2);
		List list = new LinkedList();
		for (int i = 0; i < 10; i++) {
			Map m = new HashMap();
			m.put("name", "name" + i);
			m.put("age", i);
			list.add(m);
		}

		context.set("list", list);
		TagEngine pro = new TagEngine();
		pro.evaluate(context, string);
		System.out.println(context.getWriter().toString());
		System.out.println(context);
	}

	@Test
	public void test() throws Exception {
		StringBand string = new StringBand();
		string.append("<html>$(function(){");
		string.append("		mui.init();");
		string.append("		sessionStorage.clear(); \r\n");
		string.append("		$(\"#inputBoxNoForm\")[0].reset();");
		string.append("		$(\"#locationCode\")[0].focus();");
		string.append("	}); erererer < name <c:if test='${name ==\"胡\" && age ==12;res=3;res==3 }'>iftest <name> 0  </c:if></html>");
		TagEngine pro = new TagEngine();
//		pro.setExpressionEngine(new ScriptExecutor("js"));
		SimpleContext context = new SimpleContext();
		context.set("name", "胡");
		context.set("age", 12);
		pro.evaluate(context, string);
		System.out.println(context.getWriter().toString());
	}
	@Test
	public void testTag() throws Exception {
		
		List list = new LinkedList();
		for (int i = 0; i < 10; i++) {
			Map m = new HashMap();
			m.put("name", "name" + i);
			m.put("age", i);
			list.add(m);
		}
		Map map=MapUtils.hashMap("list", list);
		State s = new State();
		SimpleContext context = new SimpleContext(s,map);
		TagEngine pro = new TagEngine();
		s.registTags("","tag-smile-core");
		pro.setState(s);
		pro.evaluate(context, "1222<if test=\"${list.size()>0}\"> ddd &lt;= d </if>");
		System.out.println(context.getWriter().toString());
	}
}
