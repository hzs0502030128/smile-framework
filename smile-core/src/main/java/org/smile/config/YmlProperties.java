package org.smile.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.smile.io.IOUtils;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.Template;
import org.smile.util.Properties;
import org.yaml.snakeyaml.Yaml;

public class YmlProperties extends Properties{

	@Override
	public void load(InputStream is, Object initParam) throws IOException {
		String xml=IOUtils.readString(is);
		if(initParam!=null){
			Template template=new SimpleStringTemplate(xml);
			xml=template.processToString(initParam);
		}
		Yaml yaml = new Yaml();
		Map map=yaml.loadAs(xml, Map.class);
		this.putAll(map);
	}
}

