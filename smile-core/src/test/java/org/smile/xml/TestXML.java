package org.smile.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.smile.bean.Student;
import org.smile.beans.BeanUtils;
import org.smile.commons.XmlParser;
import org.smile.config.BeansConfig;
import org.smile.json.JSON;
import org.smile.log.jvm.JVMLogger;
import org.smile.util.XmlUtils;

public class TestXML {
	@Test
	public void testXml() {
		String xml="<beans>\r\n" + 
				"	<bean id=\"s1\" class=\"xml.TestVo\">\r\n" + 
				"		<property name=\"name\">胡真山-- 123--</property>" +
				"    </bean> " +
				"  </beans>";
		BeansConfig vo2=XmlUtils.parserXml(BeansConfig.class, xml);
		System.out.println(JSON.toJSONString(vo2));
	}
	@Test
	public void test(){
		TestXmlVo vo=new TestXmlVo();
		vo.init();
		try {
			String xml=XmlUtils.encodeXml(vo);
			System.out.println(xml);
			Map map=XmlUtils.parseXmlToMap(xml);
			TestXmlVo vo2=XmlUtils.parserXml(TestXmlVo.class, xml);
			System.out.println(JSON.toJSONString(vo2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test1(){
		 InputStream is=XmlUtils.class.getClassLoader().getResourceAsStream("org/smile/xml/test.xml");
		 try {
			XmlParser parser=new XmlParser();
			Map map=XmlUtils.parseXmlToMap(is);
			Student s=new Student();
			s.setName("122");
			map.put("stu",s);
			System.out.println(BeanUtils.getExpValue(map, "beans.bean.0.list.bean.class"));
			System.out.println(BeanUtils.getExpValue(map, "stu.name"));
			
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testvo(){
		 InputStream is=XmlUtils.class.getClassLoader().getResourceAsStream("org/smile/xml/vo.xml");
		 try {
			MVo vo=XmlUtils.parserXml(MVo.class, is);
			System.out.println(vo);
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test2() throws Exception{
//		BeansConfig vo=XmlUtils.parserXml(BeansConfig.class, XmlUtils.class.getClassLoader().getResourceAsStream("org/smile/xml/test.xml"));
//		
//		SysUtils.println(XmlUtils.encodeXml(vo));
//		
//		List<BeanConfig> list=vo.getBean();
//		for(BeanConfig b:list){
//			BeanCreator c=new BeanCreator(b);
//			Object obj=c.getBean();
//			SysUtils.println(b.getId()+" "+JSON.toJSONString(obj));
//		}
	}
	@Test
	public void test3() throws IOException{
//		Config prop=new BeansConfigProperties();
//		prop.load(XmlUtils.class.getClassLoader().getResourceAsStream("xml/test.xml"));
//		Collection cs=prop.getValues();
//		SysUtils.log(JSONFormatter.toJSONString(cs));
//		SysUtils.log(JSON.toJSONString(prop.getValue("s1")));
//		try {
//			SysUtils.log(JSON.toJSONString(((BeanFactorySupport)prop).getBean(org.smile.xml.TestVo.class)));
//		} catch (BeanException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	@Test
	public void test4() throws Exception{
//		Properties p= new SimpleXmlProperties();
//		p.load(XmlUtils.class.getClassLoader().getResourceAsStream("logger.xml"),System.getProperties());
//		SysUtils.log(JSONFormatter.toJSONString(p));
//		SimpleSmileLogConfig ss=p.convertTo(SimpleSmileLogConfig.class);
//		SysUtils.println(JSONFormatter.toJSONString(ss));
//		String s=p.getValue("logger.file.level");
//		System.out.println(s);
//		System.out.println(p.getValue("org.jj"));
	}
	@Test
	public void test5(){
		System.out.println(JVMLogger.dumpInfo());
		System.out.println(JVMLogger.getThreadInfo());
		System.out.println(JVMLogger.jmapInfo());
		System.out.println(JVMLogger.jvmInfo());
	}
}
