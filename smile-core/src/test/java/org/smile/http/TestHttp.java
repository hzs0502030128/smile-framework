package org.smile.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.smile.file.ContentType;
import org.smile.http.client.DefaultHttpRequest;
import org.smile.http.client.HttpClients;
import org.smile.http.client.HttpGet;
import org.smile.http.client.HttpPost;
import org.smile.http.client.HttpResponse;
import org.smile.io.FileNameUtils;
import org.smile.io.IOUtils;

import junit.framework.TestCase;

public class TestHttp extends TestCase{
	
	public void testPost() throws IOException{
		DefaultHttpRequest post=new DefaultHttpRequest(HttpMethod.PUT,"http://localhost:8080/strate/action/uploadFiles");
		post.timeout(10000000);
//		post.data("name","胡真山").file("file", new File("D:/uplaodtext.txt"));
//		post.data("date.1","2010-02-03").data("name","小白");
//		HttpResponse response=HttpClients.execute(post);
//		System.out.println(response.readBody());
	}
	
	public void testPost2() throws IOException{
		DefaultHttpRequest post=new DefaultHttpRequest(HttpMethod.POST,"http://localhost:8080/strate/strate/student/student?name=hzs");
//		post.requestJsonBody("{name:'这是一个测试的内容'}");
//		post.data("name","胡真山");
//		HttpResponse response=HttpClients.execute(post);
//		System.out.println(response.readBody());
	}
	
	public void testPost3() throws IOException{
		DefaultHttpRequest post=new DefaultHttpRequest(HttpMethod.POST,"http://localhost:8080/strate/strate/student/student?name=hzs");
		post.requestBody("<name>这是一个测试的内容</name>");
//		post.data("name","胡真山");
		post.contentType(ContentType.getContextType("xml"));
//		HttpResponse response=HttpClients.execute(post);
//		System.out.println(response.readBody());
	}
	
	public void testBaidu() throws UnsupportedEncodingException, IOException {
		HttpPost post=new HttpPost("https://baidu.com");
		post.validateTLSCertificates(true);
//		String response=HttpClients.execute(post).readBody();
//		System.out.println(response);
	}
	
	
	public  void testimage() throws IOException {
		String url="https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";
		HttpGet get=new HttpGet(url);
//		HttpResponse<ByteBuffer> response=HttpClients.execute(get);
//		ByteBuffer bytes=response.getContent();
//		String type=response.contentType();
//		IOUtils.write(new File("d:/"+FileNameUtils.getName(url)), bytes.array());
//		System.out.println(response.headers());
	}
	
}
