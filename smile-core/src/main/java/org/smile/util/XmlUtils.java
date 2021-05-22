package org.smile.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.smile.Smile;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.commons.XmlParser;
import org.smile.commons.XmlParser.XPathNode;
import org.w3c.dom.Node;
/**
 * xml操作工具类
 * @author 胡真山
 *
 */
public class XmlUtils {
	
	private static final XmlParser parser=new XmlParser();
	/**
	 * 从一个流中解析xml为一个对象
	 * @param rootClazz
	 * @param is
	 * @return
	 */
	public static  <T> T parserXml(Class<T> rootClazz,InputStream is){
		try {
			return parser.parserXml(rootClazz,is);
		} catch (IOException e) {
			throw new SmileRunException("parser xml to obj error "+rootClazz,e);
		}
	}
	/**
	 * 从字符串解析xml为一个对象
	 * @param rootClazz
	 * @param xml
	 * @return
	 */
	public static  <T> T parserXml(Class<T> rootClazz,String xml){
		try {
			return parser.parserXml(rootClazz,xml);
		} catch (IOException e) {
			throw new SmileRunException("parser xml to obj error"+rootClazz+"--"+xml,e);
		}
	}
	/**
	 * 解析xml为对象
	 * @param rootClazz
	 * @param xml
	 * @return
	 */
	public static  <T> T parserXml(Class<T> rootClazz,Reader xml){
		try {
			return parser.parserXml(rootClazz,xml);
		} catch (IOException e) {
			throw new SmileRunException("parser xml to obj error"+rootClazz+"--"+xml,e);
		}
	}
	/**
	 * 把一个对象编码为 xml字符串
	 * @param rootObj 根结点对象
	 * @return
	 * @throws JAXBException
	 */
	public static String encodeXml(Object rootObj) throws JAXBException{
		return parser.storeToString(rootObj);
	}
	
	/**
	 * 把一个文件流解析也一个map
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> parseXmlToMap(InputStream is) throws IOException {
		return parser.parseXml2Map(is,Smile.XML_PARSER_TEXT_NODE_KEY);
	}
	
	/**
	 * 把一个xml字符串解析成一个map
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> parseXmlToMap(String xml) throws IOException {
		return parser.parseXml2Map(xml,Smile.XML_PARSER_TEXT_NODE_KEY);
	}
	
	public static Map<String,Object> parseNodeToMap(Node node){
		return (Map<String,Object>)parser.parseNode(node, Smile.XML_PARSER_TEXT_NODE_KEY);
	}
	
	public static Object parseNode(Node node){
		return parser.parseNode(node, Smile.XML_PARSER_TEXT_NODE_KEY);
	}
	/**
	 * 可使用 xpth 查找的结点
	 * @param is
	 * @return
	 */
	public static XPathNode xpath(InputStream is){
		return parser.xpath(is);
	}
	
	/**
	 * 可使用 xpth 查找的结点
	 * @param text
	 * @return
	 */
	public static XPathNode xpath(String text){
		try {
			return parser.xpath(new ByteArrayInputStream(text.getBytes(Strings.UTF_8)));
		} catch (UnsupportedEncodingException e) {
			throw new SmileRunException(e);
		}
	}
}
