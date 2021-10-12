package org.smile.barcode;

import java.util.List;

import org.smile.collection.CollectionUtils;

public class Code128A extends Code128 {

	public Code128A(String text) {
		super(text);
		createBarcode();
	}
	
	protected int getCheckValue(){
		return CHECK_A;
	}

	@Override
	protected String getStart() {
		return A_START;
	}

	@Override
	public List<CodeSplit> getSplit(){
		return CollectionUtils.linkedList(new CodeSplit(TYPE_A,text));
	}
}
