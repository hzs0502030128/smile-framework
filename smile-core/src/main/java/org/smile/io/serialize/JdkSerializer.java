package org.smile.io.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.smile.io.CustomObjectInputStream;
import org.smile.io.IOUtils;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;
/**
 * jdk序列化实现
 * @author 胡真山
 *
 */
public class JdkSerializer implements Serializer<Object>{
	/**日志*/
	protected final Logger logger = LoggerFactory.getLogger(JdkSerializer.class);
	/**反序列化时类加载器,需要设置容器的加载器,不然会找不到项目的中的定义 的类名*/
	private ClassLoader loader;
	
	public static JdkSerializer DEFAULT=new JdkSerializer();
	
	@Override
	public Object deserialize(byte[] datas) throws SerializeException {
		if(datas==null){
			return null;
		}
		ByteArrayInputStream is = new ByteArrayInputStream(datas); 
		ObjectInputStream ois =null;
		try {
		if(this.loader!=null){
			//有类加载器时使用
			ois=new CustomObjectInputStream(is, loader);
		}else{
			ois=new ObjectInputStream(is);
		}
		}catch(IOException e) {
			throw new SerializeException(e);
		}
		return deserialize(ois);
	}
	
	public Object deserialize(ObjectInputStream  ois) {
		try {
			return ois.readObject();
		} catch (IOException e) {
			throw new SerializeException(e);
		} catch (ClassNotFoundException e) {
			throw new SerializeException(e);
		}finally{
			IOUtils.close(ois);
		}
	}

	@Override
	public byte[] serialize(Object obj) throws SerializeException {
		ByteArrayOutputStream is = new ByteArrayOutputStream(128); 
		ObjectOutputStream ois =null;
		try{
			ois= new ObjectOutputStream(is);
			ois.writeObject(obj);
			ois.flush();
			return is.toByteArray();
		} catch (IOException e) {
			throw new SerializeException(e);
		}finally{
			IOUtils.close(ois);
		}
	}
	@Override
	public void setLoader(ClassLoader loader) {
		this.loader = loader;
	}
	
}
