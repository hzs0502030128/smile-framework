package org.smile.io;

import java.io.IOException;

import org.junit.Test;
import org.smile.io.buff.ByteBuffer;
import org.smile.json.TestJsonIgoreBean;

public class TestByteBuffer {
	@Test
	public void test() throws IOException{
		ByteBuffer bb=new ByteBuffer(136);
		for(int i=0;i<1000;i++){
			bb.writeUTFString("我是胡真山"+i);
			System.out.println(bb.readabled());
			System.out.println(bb.writeabled());
			String str=bb.readUTF();
			System.out.println(str);
		}
		System.out.println(bb.readabled());
		for(int i=0;i<10;i++){
			bb.write(i);
			System.out.println(bb.read());
		}
	}
	
	@Test
	public void test2() throws IOException{
		ByteBuffer bb=new ByteBuffer(1264);
		String name="12345677890000000000000005558888888888888";
		byte[] bs=name.getBytes();
		bb.write(bs, 28, 5);
		byte[] r=new byte[100];
		int len=bb.read(r);
		System.out.println(new String(r,0,len));
	}
	@Test
	public void test3() throws IOException{
		ByteBuffer buffer=new ByteBuffer(1000);
		buffer.writeUTF("1234567788");
		String i=buffer.readUTF();
		System.out.println(i);
	}
	
	@Test
	public void testBean() throws IOException, ClassNotFoundException{
		TestJsonIgoreBean bean=new TestJsonIgoreBean();
		bean.setAge(120);
		bean.setName("胡真山");
		ByteBuffer bb=new ByteBuffer(1064);
		bb.writeBean(bean);
		TestJsonIgoreBean bean2=(TestJsonIgoreBean) bb.readBean();
		System.out.println(bean2);
	}
}
