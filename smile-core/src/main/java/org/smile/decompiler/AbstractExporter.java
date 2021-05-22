package org.smile.decompiler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.smile.io.IOUtils;
/**
 * 导出代码
 * @author 胡真山
 *
 */
public abstract class AbstractExporter implements Exporter{

	/**
	 * 导出文件过滤
	 */
	protected FileFilter filter=createFileFilter();
	/**
	 * 导出的原目录
	 * @return
	 */
	protected abstract  String getDirectory();
	/**
	 * 过滤导出的文件
	 * @return
	 */
	protected FileFilter createFileFilter(){
		return null;
	}
	
	/**
	 * 循环处理文件
	 * @param files
	 * @param newDirectory
	 * @throws IOException
	 */
    protected  void loopFile(File[] files,String newDirectory) throws IOException{
        if(files == null){
            return;
        }
        for(File f : files){
            if(f.isFile()){
                if(f.getName().endsWith(".java")){
                    removeContent(getDirectory(),newDirectory, f, true);
                }else{
                    removeContent(getDirectory(),newDirectory, f, false);
                }
            }else{
                loopFile(f.listFiles(filter),newDirectory);
            }
        }
    }
    /**
     * 对一个文件的内容进行处理
     * @param line
     * @return
     */
    protected abstract String doConvertLine(String line);
     
    /**
     * 处理一个文件的内容
     * @param oldDir 原目录
     * @param newDir 新目录
     * @param file 当前导出的文件
     * @param needConvert  是否需要处理
     * @throws IOException
     */
    protected void removeContent(String oldDir, String newDir, File file, boolean needConvert) throws IOException{
        List<String> lines = IOUtils.readLines(file);
        String relocationString = relocationLineNumber(lines);
        String newContent = needConvert ? doConvertLine(relocationString) : relocationString;
        File newFile = new File(file.getCanonicalPath().replace(oldDir, newDir));
        IOUtils.write(newFile, newContent);
    }
    /***
     * 把文件中的多行转成一个字符串
     * @param lines
     * @return
     */
    protected abstract  String relocationLineNumber(List<String> lines);

	@Override
	public void export(String dir) throws IOException{
		File folder = new File(getDirectory());
        File[] files = folder.listFiles(filter);
        loopFile(files,dir);
	}

}
