package org.smile.barcode;

import java.io.IOException;

import junit.framework.TestCase;

public class TestCode128 extends TestCase{
	
	public void testCreate() throws IOException{
		Code128 code=new Code128Auto("12ASD33333333");
		code.setImageType("png");
		code.setPadding(20);
		code.setPaddingHyaline(true);
		code.writeToFile("D:/testCode.png");
	}
}
