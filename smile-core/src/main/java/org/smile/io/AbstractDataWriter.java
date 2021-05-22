package org.smile.io;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.smile.commons.Strings;
import org.smile.json.JSON;
import org.smile.util.XmlUtils;

public abstract class AbstractDataWriter implements DataWriter{
	
	protected DataOutputStream dos;
	
	@Override
	public void writeByte(int value) throws IOException {
		dos.write(value);
	}

	@Override
	public void writeBoolean(boolean value) throws IOException {
		dos.writeBoolean(value);
	}

	@Override
	public void writeBytes(byte[] value) throws IOException {
		dos.write(value);
	}

	@Override
	public void writeShort(int value) throws IOException {
		dos.writeShort(value);
	}

	@Override
	public void writeInteger(int value) throws IOException {
		dos.writeInt(value);
	}

	@Override
	public void writeLong(long value) throws IOException {
		dos.writeLong(value);
	}

	@Override
	public void writeFloat(float value) throws IOException {
		dos.writeFloat(value);
	}

	@Override
	public void writeDouble(double value) throws IOException {
		dos.writeDouble(value);
	}

	@Override
	public void writeString(String value) throws IOException {
		dos.writeUTF(value);
	}

	@Override
	public void writeBean(Object obj) throws IOException {
		String name=obj.getClass().getName();
		dos.writeUTF(name);
		dos.writeUTF(JSON.toJSONString(obj));
	}

	

	@Override
	public void writeByteArray(byte[] bytes) throws IOException {
		dos.writeInt(bytes.length);
		dos.write(bytes);
	}

	@Override
	public void writeUTFBytes(byte[] bytes) throws IOException {
		String str=new String(bytes,Strings.UTF_8);
		dos.writeUTF(str);
	}

	@Override
	public void writeUTFString(String str) throws IOException {
		byte[] bytes=str.getBytes(Strings.UTF_8);
		if(bytes.length>65535){
			throw new IOException("string is too long than 65535");
		}
		dos.writeShort(bytes.length);
		writeBytes(bytes);
	}

	@Override
	public void writeXmlObject(Object obj) throws IOException {
		String name=obj.getClass().getName();
		String xml;
		try {
			xml = XmlUtils.encodeXml(obj);
		} catch (JAXBException e) {
			throw new IOException("encode xml error "+obj,e);
		}
		dos.writeUTF(name);
		dos.writeUTF(xml);
	}
	
	@Override
	public void writeChar(char chr) throws IOException{
		dos.writeShort(chr);
	}

	@Override
	public void write(int b) throws IOException {
		dos.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		dos.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		dos.write(b, off, len);
	}

	@Override
	public void writeChar(int v) throws IOException {
		dos.writeChar(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		dos.writeInt(v);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		dos.writeBytes(s);
	}

	@Override
	public void writeChars(String s) throws IOException {
		dos.writeChars(s);
	}

	@Override
	public void writeUTF(String s) throws IOException {
		dos.writeUTF(s);
	}
}
