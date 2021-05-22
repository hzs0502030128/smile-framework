package org.smile.collection;
/**
 * 有最大长度的map 
 * 大于时先进先出
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class FixedLenHashMap<K,V> extends LinkedHashMap<K, V>{
	
	protected int fixedLen;
	
	public FixedLenHashMap(int size){
		super(size);
		this.fixedLen=size;
	}
	
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return this.size()>fixedLen;
	}
}
