package org.smile.barcode;

import java.util.List;

import org.smile.collection.CollectionUtils;

public class Code128B extends Code128{

	public Code128B(String text) {
		super(text);
		createBarcode();
	}
	protected int getCheckValue(){
		return CHECK_B;
	}
	@Override
	protected String getStart() {
		return B_START;
	}
	
	public List<CodeSplit> getSplit(){
		return CollectionUtils.linkedList(new CodeSplit(TYPE_B,text));
	}

}
