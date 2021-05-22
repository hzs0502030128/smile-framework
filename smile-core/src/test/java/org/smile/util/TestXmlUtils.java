//package org.smile.util;
//
//import java.io.InputStream;
//import java.util.Map;
//
//import javax.xml.xpath.XPathConstants;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.smile.commons.XmlParser.XPathNode;
//import org.smile.io.ResourceUtils;
//import org.smile.json.JSONObject;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//public class TestXmlUtils {
//	XPathNode node;
//	@Before
//	public void before(){
//		InputStream is =ResourceUtils.loadFromClassPath("test.xml");
//		node=XmlUtils.xpath(is);
//	}
//	@Test
//	public void testXpath(){
//		Node n1=node.findNode("//bean[@id='s2']");
//		Map map=XmlUtils.parseNodeToMap(n1);
//		System.out.println(JSONObject.toJSONString(map));
//		NodeList l1=node.findNodeList("//properties//value");
//		for(int i=0;i<l1.getLength();i++){
//			System.out.println(l1.item(i).getTextContent());
//		}
//	}
//	
//	@Test
//	public void testXpathNodeList(){
//		NodeList n1=node.findNodeList("//bean");
//		for(int i=0;i<n1.getLength();i++){
//			Map map=XmlUtils.parseNodeToMap(n1.item(i));
//			System.out.println(node.find("//beanproperty[@name='address']", XPathConstants.STRING));
//			System.out.println(JSONObject.toJSONString(map));
//		}
//	}
//	@Test
//	public void testAttribute(){
//		Node n1=node.findNode("//bean[@id='s2']");
//		node.setRoot(n1);
//		String s=(String)node.find("attribute::id",XPathConstants.STRING);
//		System.out.println(s);
//	}
//}
