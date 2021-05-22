package org.smile.template;

import java.io.File;
import java.io.IOException;

import org.smile.io.BufferedReader;
import org.smile.io.IOUtils;

public class FileTemplateLoader implements TemplateLoader<File>{
	/**文件加载目录*/
	protected File baseDir;
	
	public FileTemplateLoader(File baseDir){
		this.baseDir=baseDir;
	}
	@Override
	public File getTemplateSource(String name) {
		return new File(baseDir, name);
	}

	@Override
	public long getLastModify(final File templateSource) {
	      return templateSource.lastModified();
	}

	@Override
	public String getTemplateContent(File templateSource, String encode) throws IOException {
		BufferedReader reader=new BufferedReader(templateSource, encode);
		return IOUtils.readString(reader);
	}

}
