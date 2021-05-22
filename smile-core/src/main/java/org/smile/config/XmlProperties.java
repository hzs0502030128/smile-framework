package org.smile.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.smile.io.IOUtils;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.Template;
import org.smile.util.Properties;
import org.smile.util.XmlUtils;

public class XmlProperties extends Properties{
	/**根结点标签 */
	protected String root="config";
	
	@Override
	public void load(File file) throws IOException {
		load(new FileInputStream(file));
	}

	@Override
	public void load(InputStream is) throws IOException {
		Map<String,Object> result=XmlUtils.parseXmlToMap(is);
		Map config=(Map) result.get(root);
		if(config!=null){
			putAll(config);
		}else{
			throw new NullPointerException("不存在的根结点:"+root);
		}
	}

	@Override
	public void load(File file, Object initParam) throws IOException {
		load(new FileInputStream(file));
	}

	@Override
	public void load(InputStream is, Object initParam) throws IOException {
		String xml=IOUtils.readString(is);
		if(initParam!=null){
			Template template=new SimpleStringTemplate(xml);
			xml=template.processToString(initParam);
		}
		Map<String,Object> result=XmlUtils.parseXmlToMap(xml);
		Map config=(Map) result.get(root);
		if(config!=null){
			putAll(config);
		}else{
			throw new NullPointerException("不存在的根结点:"+root);
		}
	}

	public void setRoot(String root) {
		this.root = root;
	}

}
