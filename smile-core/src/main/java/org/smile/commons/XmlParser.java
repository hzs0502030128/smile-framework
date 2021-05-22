package org.smile.commons;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.smile.collection.LinkedHashMap;
import org.smile.io.BufferedReader;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 解析xml文件类
 * @author strive
 */
public class XmlParser implements LoggerHandler {
	/**
	 * 解析一个结点的所有属性成一个map 
	 * 只包括当前结点 不包括其子结点
	 * @param node
	 * @return
	 */
	public Map<String, Object> parseNodeAttribute(Node node) {
		Map<String, Object> map = new HashMap<String, Object>();
		NamedNodeMap childs = node.getAttributes();
		if (childs != null) {
			for (int i = 0; i < childs.getLength(); i++) {
				Node temp = childs.item(i);
				map.put(temp.getNodeName(), temp.getNodeValue());
			}
		}
		return map;
	}
	/**
	 * 创建xml查找结节
	 * @param is
	 * @return
	 */
	public XPathNode xpath(InputStream is){
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc=builder.parse(is);
			XPathNode node=new XPathNode(doc);
			return node;
		} catch (Exception e) {
			throw new SmileRunException(e);
		}finally{
			IOUtils.close(is);
		}
	}
	/**
	 * 把一个文件流解析成一个map
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> parseXml2Map(InputStream is,String textContentKey) throws IOException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc=builder.parse(is);
			return (Map<String, Object>)this.parseNode(doc,textContentKey);
		} catch (SAXException e) {
			throw new IOException("解析xml成一个map出错", e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		}finally{
			IOUtils.close(is);
		}
	}
	/**
	 * 把一个xml字符串解析成一个map
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> parseXml2Map(String xml,String textContentKey) throws IOException {
		InputStream is = new ByteArrayInputStream(xml.getBytes(Strings.UTF_8));
		return parseXml2Map(is,textContentKey);
	}

	/**
	 * 解析一个xml节点成一个Map
	 * 不能简单是文本结节
	 * @param node
	 * @return
	 */
	public Object parseNode(Node node,String textContentKey) {
		return  parseXmlNode(node,textContentKey);
	}
	

	/**
	 * 解析一个xml的节点 包括其所有的子结节
	 * @param node
	 * @return 
	 */
	protected Object parseXmlNode(Node node,String textContentKey) {
		short type = node.getNodeType();
		if (type == Node.TEXT_NODE) {
			// 为文本的时候
			return StringUtils.trim(node.getNodeValue());
		} else {
			// 不为文本是一个结点
			Map<String, Object> map = parseNodeAttribute(node);
			NodeList nodes = node.getChildNodes();
			if (map.isEmpty() && !hasNodeChild(nodes)) {
				return getNodeText(nodes);
			} else if (!map.isEmpty() && !hasNodeChild(nodes)) {
				String value = getNodeText(nodes);
				if (!StringUtils.isNull(value)) {
					putMap(map, textContentKey, value);
				}
				return map;
			} else {
				for (int i = 0; i < nodes.getLength(); i++) {
					Node n = nodes.item(i);
					Object value = parseXmlNode(n,textContentKey);
					short _type = n.getNodeType();
					if (_type != Node.TEXT_NODE) {
						putMap(map, n.getNodeName(), value);
					}
				}
				return map;
			}
		}
	}

	/**
	 * 往map里填数据 如当前map中的已经存在key 
	 * 则把key对应的对象转成List 
	 * 并把样同key的对象放到list中
	 * @param map
	 * @param key
	 * @param v
	 */
	private void putMap(Map<String,Object> map, String key, Object v) {
		if (map.containsKey(key)) {
			Object value = map.get(key);
			if (value instanceof List) {
				List<Object> list = (List<Object>) map.get(key);
				list.add(v);
			} else {
				List<Object> list = new ArrayList<Object>();
				list.add(map.get(key));
				list.add(v);
				map.put(key, list);
			}
		} else {
			map.put(key, v);
		}
	}
	/**
	 * 是否有非文本子节点
	 * @param nodes
	 * @return
	 */
	private boolean hasNodeChild(NodeList nodes) {
		if (nodes.getLength() == 0) {
			return false;
		} else if (nodes.getLength() == 1) {
			if (nodes.item(0).getNodeType() == Node.TEXT_NODE) {
				return false;
			} else {
				return true;
			}
		}else {
			return true;
		}
	}

	/**
	 * 得到文本结点的文本 
	 * 已经确定只有文本结点
	 * @param nodes
	 * @return
	 */
	private String getNodeText(NodeList nodes) {
		if (nodes.getLength() == 1) {
			return nodes.item(0).getNodeValue().trim();
		}
		return Strings.BLANK;
	}

	/**
	 * 保存xml内容到一个流中
	 * 使用JAXB方式
	 * @param rootObj
	 * @param os
	 */
	public void store(Object rootObj, OutputStream os) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(rootObj.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(rootObj, os);
	}

	/**
	 * 保存到一个字符串中
	 * 使用JAXB方式
	 * @param rootObj
	 * @param os
	 */
	public String storeToString(Object rootObj) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(rootObj.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		Writer w = new StringWriter();
		jaxbMarshaller.marshal(rootObj, w);
		return w.toString();
	}

	/**
	 * 保存一个xml对象到文件中
	 * 使用JAXB方式
	 * @param rootObj
	 * @param file
	 * @throws JAXBException
	 */
	public void store(Object rootObj, File file) throws JAXBException {
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			store(rootObj, os);
		} catch (FileNotFoundException e) {
			throw new JAXBException(e);
		} finally {
			IOUtils.close(os);
		}
	}
	/**
	 * 解析xml为对象 
	 * 使用JAXB方式
	 * @param rootClass 根结点对象的类
	 * @param input xml文件的输入流
	 * @return
	 * @throws IOException
	 */
	public <E> E parserXml(Class<E> rootClass,InputStream input) throws IOException {
		JAXBContext jaxbContext;
		BufferedReader in=null;
		try {
            //读入xml文件流
			in= new BufferedReader(input);
			//加载映射bean类
			jaxbContext = JAXBContext.newInstance(rootClass);
            //创建解析
			Unmarshaller um = jaxbContext.createUnmarshaller();
			StreamSource streamSource = new StreamSource(in);
			E root = (E)um.unmarshal(streamSource); 
			return root;
		} catch (Exception e) {
			throw new IOException("parser inputstream to object error "+rootClass,e);
		}finally{
			IOUtils.close(in);
		}
	}

	public <E> E parserXml(Class<E> rootClass, Reader reader) throws IOException {
		try {
			// 加载映射bean类
			JAXBContext jaxbContext = JAXBContext.newInstance(rootClass);
	        XMLInputFactory xif = XMLInputFactory.newFactory();  
	        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);  
	        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);  
	        XMLStreamReader xsr = xif.createXMLStreamReader(reader); 
			// 创建解析
			Unmarshaller um = jaxbContext.createUnmarshaller();
			StreamSource streamSource = new StreamSource(reader);
			E root = (E) um.unmarshal(xsr);
			return root;
		} catch (Exception e) {
			throw new IOException("parser inputstream to object error " + rootClass, e);
		} finally {
			IOUtils.close(reader);
		}
	}

	/**
	 * 解析一个字符串成对象
	 * @param rootClass
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	public <E> E parserXml(Class<E> rootClass, String xml) throws IOException {
		return parserXml(rootClass, new StringReader(xml));
	}
	
	/**
	 * 用于xml结点的路径查找
	 * @author 胡真山
	 *
	 */
	public final static class XPathNode {
		
		private static XPathFactory factory=XPathFactory.newInstance();
		
		private Node root;
		
		private XPath xpath;
		
		protected XPathNode(Node root){
			this.root=root;
			this.xpath=factory.newXPath();
		}
		
		public void setRoot(Node node){
			this.root=node;
		}
		
		/***
		 * 获取属性
		 * @param name
		 * @return
		 */
		public String getAttribute(String name){
			return getAttribute(this.root, name);
		}
		/**
		 * 查找一个结点的一个属性
		 * @param path
		 * @param name
		 * @return
		 */
		public String findNodeAttribute(String path,String name){
			Node node=findNode(path);
			return getAttribute(node, name);
		}
		/**
		 * 获取一个结点的所有属性
		 * @param node
		 * @return
		 */
		public static Map<String,String> getAttributes(Node node){
			Map<String,String> attributes=new LinkedHashMap<String,String>();
			NamedNodeMap attribteNode=node.getAttributes();
			if(attribteNode!=null){
				int len=attribteNode.getLength();
				for(int i=0;i<len;i++){
					Node n=attribteNode.item(i);
					attributes.put(n.getNodeName(), n.getNodeValue());
				}
			}
			return attributes;
		}
		/**
		 * 获取一个结点的一个属性
		 * @param node 结点
		 * @param attribute 属性名称
		 * @return
		 */
		public static String getAttribute(Node node,String attribute){
			Node attribteNode=node.getAttributes().getNamedItem(attribute);
			if(attribteNode!=null){
				return attribteNode.getNodeValue();
			}
			return null;
		}
		
		/**
		 * 查找结点
		 * @param node
		 * @param path
		 * @param qname
		 * @return
		 */
		public Object find(Node node,String path,QName qname){
			try {
				return xpath.evaluate(path,node,qname);
			} catch (XPathExpressionException e) {
				throw new SmileRunException(e);
			}
		}
		/**
		 * 查找结点
		 * @param path
		 * @param qname
		 * @return
		 */
		public Object find(String path,QName qname){
			return find(root, path, qname);
		}
		/**
		 * 查找多个结点
		 * @param path
		 * @return
		 */
		public NodeList findNodeList(String path){
			return (NodeList) find(path, XPathConstants.NODESET);
		}
		/**
		 * 查找一个结点
		 * @param path
		 * @return
		 */
		public Node findNode(String path){
			return (Node)find(path, XPathConstants.NODE);
		}
		/**
		 * 查找文本结点
		 * @param path
		 * @return
		 */
		public String findString(String path){
			return (String)find(path, XPathConstants.STRING);
		}
	}
}
