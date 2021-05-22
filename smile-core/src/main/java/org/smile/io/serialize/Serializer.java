package org.smile.io.serialize;

public interface Serializer<T> {
	/**
	 * 反序列化
	 * @param datas
	 * @return
	 * @throws SerializeException
	 */
	public T deserialize(byte[] datas) throws SerializeException;
	/***
	 * 序列化
	 * @param obj
	 * @return
	 * @throws SerializeException
	 */
	public byte[] serialize(T obj) throws SerializeException;
	/***
	 * 设置classloader
	 * @param loader
	 */
	void setLoader(ClassLoader loader);
}
