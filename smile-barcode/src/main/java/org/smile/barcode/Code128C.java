package org.smile.barcode;

import java.util.LinkedList;
import java.util.List;

import org.smile.collection.CollectionUtils;


public class Code128C extends Code128 {

	public Code128C(String text) {
		super(text);
		createBarcode();
	}
	
	@Override
	protected int getCheckValue() {
		return CHECK_C;
	}

	@Override
	public List<CodeSplit> getSplit() {
		int length=text.length();
		if(length%2==0){
			return CollectionUtils.linkedList(new CodeSplit(TYPE_C, text));
		}else{
			List<CodeSplit> splits=new LinkedList<CodeSplit>();
			splits.add(new CodeSplit(TYPE_C, text.substring(0, length-1)));
			splits.add(new CodeSplit(TYPE_B, text.substring(length-1)));
			return splits;
		}
	}

	@Override
	protected String getStart() {
		return C_START;
	}

}
