package org.smile.io;

import java.io.DataOutputStream;
import java.io.IOException;


public class BaseDataWriter extends AbstractDataWriter {
	
	protected ByteArrayOutputStream bos;
	
	public BaseDataWriter(){
		bos=new ByteArrayOutputStream();
		dos=new DataOutputStream(bos);
	}
	
	@Override
	public byte[] toByteArray() throws IOException {
		return bos.toByteArray();
	}
}
