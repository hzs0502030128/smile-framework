package org.smile.decompiler;

import java.util.List;

import org.smile.Smile;
/**
 * 对反编译后的文件进行处理
 * @author 胡真山
 * 2015年11月3日
 */
public class JdGuiExport extends AbstractExporter{
	/**去行号注释正则*/
	private static String regex = "\\/\\*[\\p{ASCII}]*?\\*\\/\\s?";
    /**反编译后的java文件目录*/
    private  String directory;
    /**是否不回空行*/
    private boolean needAddSpaceLine=false;
    
    public JdGuiExport(String directory){
    	this.directory=directory;
    }
    
    public JdGuiExport(String directory,boolean needAddSpaceLine){
    	this.directory=directory;
    	this.needAddSpaceLine=needAddSpaceLine;
    }
     
    /***
     * 添加空行 使用代码行号与原有行号一致
     * @param lines
     * @return
     */
    protected  String relocationLineNumber(List<String> lines){
        int currentLine = 0;
        StringBuilder content = new StringBuilder();
        for(String line : lines){
            currentLine++;
            int num = readLineNumber(line);
            if(num != -1&&needAddSpaceLine){
                while(currentLine < num){
                    currentLine++;
                    content.append(Smile.LINE_SEPARATOR);
                }
            }
            content.append(line).append(Smile.LINE_SEPARATOR);
        }
        return content.toString();
    }
    /**
     * 读取行号
     * @param line
     * @return
     */
    private  int readLineNumber(String line){
        int start = line.indexOf("/*");
        int end = line.indexOf("*/");
        if(start > -1 && end > start){
            String left = line.substring(end + 2).trim();
            if(left.startsWith("@")){
                return -1;
            }
             
            String linNum = line.substring(start + 2, end).trim();
            try{
                return Integer.parseInt(linNum);
            }catch(NumberFormatException e){
                return -1;
            }
        }
        return -1;
    }


	@Override
	protected String getDirectory() {
		return this.directory;
	}

	@Override
	protected String doConvertLine(String line) {
		return line.replaceAll(regex, "");
	}
}
