package org.smile.decompiler;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.smile.Smile;
import org.smile.util.RegExp;

/**
 * 导出代码
 * @author 胡真山
 * 
 */
public class CodeExporter extends AbstractExporter{
	
	private static RegExp javaexp=new RegExp("\\/\\*{1,2}[\\s\\S]*?\\*\\/");
	
	private static RegExp javaexp2=new RegExp("\\/\\/[\\s\\S]*?\n");
	
	private static RegExp blankExp=new RegExp("^\\s*\n");
	
	private static final String author="\n/** 不怕神一样的对手  就怕猪一样的队友   代码也是一种艺术   \n   290146360@qq.com **/";
	
	private static final String copyRight="/**@CopyRight hu zhenshan  All Rights Reserved**/\n";
	
	private String dir;
	
	public CodeExporter(String dir){
		this.dir=dir;
	}

	@Override
	protected String getDirectory() {
		return dir;
	}
	
	@Override
	protected FileFilter createFileFilter() {
		return new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()){
					return true;
				}
				return pathname.getName().endsWith(".java")||pathname.getName().endsWith(".properties");
			}};
	}

	@Override
	protected String doConvertLine(String line) {
		line=javaexp.replaceAll(line, "");
		line=javaexp2.replaceAll(line, "\n");
		line=blankExp.replaceAll(line, "");
		return copyRight+line+author;
	}

	@Override
	protected String relocationLineNumber(List<String> lines) {
        StringBuilder content = new StringBuilder();
        for(String line : lines){
            content.append(line).append(Smile.LINE_SEPARATOR);
        }
        return content.toString();
	}

	
}
