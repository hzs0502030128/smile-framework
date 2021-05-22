package org.smile.json.format;

import org.smile.json.JSONWriter;

public class SimpleIndent{
	private int count;
	private static String SPACE="	";
	
	public void indent(JSONWriter sb,int add) {
		count+=add;
		sb.write('\n');
        for (int i = 0; i < count; i++) {
        	sb.write(SPACE);
        }
    }
	
	public void indent(JSONWriter sb) {
		indent(sb,0);
    }
	/**
	 * 向右缩进
	 * @param sb
	 */
	public void indentRight(JSONWriter sb) {
		indent(sb,1);
    }
	/**
	 * 向左缩进
	 * @param sb
	 */
	public void indentLeft(JSONWriter sb) {
		indent(sb,-1);
    }

}
