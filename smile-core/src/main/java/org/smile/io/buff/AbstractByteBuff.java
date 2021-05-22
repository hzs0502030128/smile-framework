package org.smile.io.buff;

import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;

import javax.xml.bind.JAXBException;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.Strings;
import org.smile.json.JSON;
import org.smile.util.XmlUtils;

public abstract class AbstractByteBuff implements ByteBuff {
	/**
	 * working by read long
	 */
	private byte readBuffer[] = new byte[8];

	private byte writeBuffer[] = new byte[8];
	
	/**
	 * working arrays initialized on demand by readUTF
	 */
	private byte bytearr[] = new byte[80];

	private char chararr[] = new char[80];

	/**
	 * working arrays initialized on demand by writeUTF
	 * */
	private byte[] bytearrWr = null;
	
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	@Override
	public final long readLong() throws IOException {
		readFully(readBuffer, 0, 8);
		return (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32)
				+ ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
	}
	
	@Override
	public String readUTF() throws IOException {
		int utflen = readUnsignedShort();
		byte[] bytearr;
		char[] chararr;
		if (this.bytearr.length < utflen) {
			this.bytearr = new byte[utflen * 2];
			this.chararr = new char[utflen * 2];
		}
		chararr = this.chararr;
		bytearr = this.bytearr;

		int c, char2, char3;
		int count = 0;
		int chararr_count = 0;

		readFully(bytearr, 0, utflen);

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			if (c > 127)
				break;
			count++;
			chararr[chararr_count++] = (char) c;
		}

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					/* 0xxxxxxx */
					count++;
					chararr[chararr_count++] = (char) c;
					break;
				case 12:
				case 13:
					/* 110x xxxx 10xx xxxx */
					count += 2;
					if (count > utflen)
						throw new UTFDataFormatException("malformed input: partial character at end");
					char2 = (int) bytearr[count - 1];
					if ((char2 & 0xC0) != 0x80)
						throw new UTFDataFormatException("malformed input around byte " + count);
					chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
					break;
				case 14:
					/* 1110 xxxx 10xx xxxx 10xx xxxx */
					count += 3;
					if (count > utflen)
						throw new UTFDataFormatException("malformed input: partial character at end");
					char2 = (int) bytearr[count - 2];
					char3 = (int) bytearr[count - 1];
					if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
						throw new UTFDataFormatException("malformed input around byte " + (count - 1));
					chararr[chararr_count++] = (char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
					break;
				default:
					/* 10xx xxxx, 1111 xxxx */
					throw new UTFDataFormatException("malformed input around byte " + count);
			}
		}
		// The number of chars produced may be less than utflen
		return new String(chararr, 0, chararr_count);
	}
	
	@Override
	public void writeLong(long v) throws IOException {
		writeBuffer[0] = (byte) (v >>> 56);
		writeBuffer[1] = (byte) (v >>> 48);
		writeBuffer[2] = (byte) (v >>> 40);
		writeBuffer[3] = (byte) (v >>> 32);
		writeBuffer[4] = (byte) (v >>> 24);
		writeBuffer[5] = (byte) (v >>> 16);
		writeBuffer[6] = (byte) (v >>> 8);
		writeBuffer[7] = (byte) (v >>> 0);
		write(writeBuffer, 0, 8);
	}

	
	@Override
	public void writeUTF(String str) throws IOException {
		int strlen = str.length();
		int utflen = 0;
		int c, count = 0;

		/* use charAt instead of copying String to char array */
		for (int i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}

		if (utflen > 65535) {
			throw new UTFDataFormatException("encoded string too long: " + utflen + " bytes");
		}

		byte[] bytearr = null;

		if (this.bytearrWr == null || (this.bytearrWr.length < (utflen + 2))) {
			this.bytearrWr = new byte[(utflen * 2) + 2];
		}
		bytearr = this.bytearrWr;

		bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
		bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

		int i = 0;
		for (i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F)))
				break;
			bytearr[count++] = (byte) c;
		}

		for (; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				bytearr[count++] = (byte) c;

			} else if (c > 0x07FF) {
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			} else {
				bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			}
		}
		write(bytearr, 0, utflen + 2);
	}

	@Override
	public final boolean readBoolean() throws IOException {
		int ch = read();
		if (ch < 0)
			throw new EOFException();
		return (ch != 0);
	}

	@Override
	public final byte readByte() throws IOException {
		int ch = read();
		if (ch < 0)
			throw new EOFException();
		return (byte) (ch);
	}

	@Override
	public int readUnsignedByte() throws IOException {
		int ch = read();
		if (ch < 0)
			throw new EOFException();
		return ch;
	}

	@Override
	public short readShort() throws IOException {
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	@Override
	public int readUnsignedShort() throws IOException {
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (ch1 << 8) + (ch2 << 0);
	}

	@Override
	public final char readChar() throws IOException {
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	@Override
	public final int readInt() throws IOException {
		int ch1 = read();
		int ch2 = read();
		int ch3 = read();
		int ch4 = read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}
	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}
	@Override
	public void readFully(byte b[], int off, int len) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		write(v ? 1 : 0);
	}

	@Override
	public void writeByte(int v) throws IOException {
		write(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
	}

	@Override
	public void writeChar(int v) throws IOException {
		writeShort(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		write((v >>> 24) & 0xFF);
		write((v >>> 16) & 0xFF);
		write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		writeInt(Float.floatToIntBits(v));
	}

	@Override
	public void writeDouble(double v) throws IOException {
		writeLong(Double.doubleToLongBits(v));
	}

	@Override
	public void writeBytes(String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			write((char)s.charAt(i));
		}
	}

	@Override
	public void writeChars(String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int v = s.charAt(i);
			write((v >>> 8) & 0xFF);
			write((v >>> 0) & 0xFF);
		}
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		readFully(b, 0, b.length);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return (int)skip(n);
	}

	@Override
	public String readLine() throws IOException {
		return readUTF();
	}
	
	
	@Override
	public void writeBytes(byte[] bytes) throws IOException {
		write(bytes);
	}

	@Override
	public void writeByteArray(byte[] bytes) throws IOException {
		writeInt(bytes.length);
		write(bytes);
	}

	@Override
	public void writeInteger(int i) throws IOException {
		writeInt(i);
		
	}

	@Override
	public void writeString(String str) throws IOException {
		writeUTF(str);
	}

	@Override
	public void writeUTFBytes(byte[] bytes) throws IOException {
		String str=new String(bytes,Strings.UTF_8);
		writeUTF(str);
	}

	@Override
	public void writeBean(Object obj) throws IOException {
		String name=obj.getClass().getName();
		writeUTF(name);
		writeUTF(JSON.toJSONString(obj));
	}

	@Override
	public void writeUTFString(String str) throws IOException {
		byte[] bytes=str.getBytes(Strings.UTF_8);
		if(bytes.length>65535){
			throw new IOException("string is too long than 65535");
		}
		writeShort(bytes.length);
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
		writeUTF(name);
		writeUTF(xml);
	}

	@Override
	public void writeChar(char chr) throws IOException {
		writeShort(chr);
	}
	
	@Override
	public int readInteger() throws IOException {
		return readInt();
	}

	@Override
	public String readString() throws IOException {
		return readUTF();
	}

	@Override
	public byte[] readByteArray() throws IOException {
		int len =readInt();
		byte[] b = new byte[len];
		readFully(b);
		return b;
	}

	@Override
	public byte[] readUTFBytes() throws IOException {
		String str = readUTF();
		return str.getBytes(Strings.UTF_8);
	}

	@Override
	public String readUTFString() throws IOException {
		int len = readUnsignedShort();
		byte[] bytes = new byte[len];
		readFully(bytes);
		return new String(bytes, Strings.UTF_8);
	}

	@Override
	public Object readBean() throws IOException, ClassNotFoundException {
		String name = readUTF();
		Class clazz = Class.forName(name);
		String str = readUTF();
		Object value = JSON.parse(str);
		if (clazz.isAssignableFrom(value.getClass())) {
			clazz.cast(value);
		}
		try {
			return BasicConverter.getInstance().convert(clazz, value);
		} catch (ConvertException e) {
			throw new IOException("转换成" + name + "失败", e);
		}
	}

	@Override
	public Object readXmlObject() throws IOException, ClassNotFoundException {
		String name = readUTF();
		Class clazz = Class.forName(name);
		String xml = readUTF();
		return XmlUtils.parserXml(clazz, xml);
	}
}
