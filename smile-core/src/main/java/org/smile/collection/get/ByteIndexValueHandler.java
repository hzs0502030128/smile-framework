package org.smile.collection.get;

public class ByteIndexValueHandler extends IndexValueHandler<byte[], Byte>{

	@Override
	public Byte getValueAt(byte[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(byte[] collection) {
		return collection.length;
	}

	

}
