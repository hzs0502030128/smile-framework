package org.smile.template;

import java.io.IOException;
import java.io.InputStream;

import org.smile.io.BufferedReader;
import org.smile.io.IOUtils;
import org.smile.io.ResourceUtils;

public class ClsPathTemplateLoader implements TemplateLoader<InputStream>{
	/**文件加载目录*/
	private String root;
	
	public ClsPathTemplateLoader(String rootPath){
		this.root=rootPath;
	}
	
	@Override
	public InputStream getTemplateSource(String name) {
		return ResourceUtils.getResourceAsStream(root+name);
	}

	@Override
	public long getLastModify(final InputStream templateSource) {
	      return Long.MAX_VALUE;
	}

	@Override
	public String getTemplateContent(InputStream templateSource, String encode) throws IOException {
		BufferedReader reader=new BufferedReader(templateSource, encode);
		return IOUtils.readString(reader);
	}

}
