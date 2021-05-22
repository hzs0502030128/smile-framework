package org.smile.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * 可以用不分大小写的键获取元素
 * 键存放是有顺序的 
 * @author 胡真山
 * @Date 2016年1月25日
 * @param <V>
 */
public class KeyNoCaseHashMap<V> extends LinkedHashMap<String, V>{
	/**用来映射真实的键*/
	protected final Map<String, String> realKeyMap = new HashMap<String, String>();

    public KeyNoCaseHashMap(){
    	super();
    }
    
    public KeyNoCaseHashMap(int size){
    	super(size);
    }
    
    @Override
    public boolean containsKey(Object key) {
        Object realKey = realKeyMap.get(convertKey(key));
        return super.containsKey(realKey);
    }
    
    protected boolean containsUseRealKey(Object key) {
    	return super.containsKey(key);
    }
    /**
     * 	用真实的键去获取值
     * @param realKey 真实的键
     * @return
     */
    protected V getUseRealKey(Object realKey){
    	return super.get(realKey);
    }
    /**
     * 	使用真实键put
     * @param realKey
     * @param value
     * @return
     */
    protected V putUseRealKey(String realKey,V value) {
    	return super.put(realKey, value);
    }
    /**
     * 以一个名称去获取真实的键名   
     * 如获取不取则返回 null
     * @param key
     * @return
     */
    public String findRealKey(String key){
    	String convertKey=convertKey(key);
    	return realKeyMap.get(convertKey);
    }

    @Override
    public V get(Object key) {
        Object realKey = realKeyMap.get(convertKey(key));
        return super.get(realKey);
    }
    
    @Override
    public V put(String key, V value){
        Object oldKey = realKeyMap.put(convertKey(key), key);
        V oldValue = super.remove(oldKey);
        super.put(key, value);
        return oldValue;
    }
    
    @Override
    public void putAll(Map<? extends String,? extends  V> m) {
        for (Map.Entry<? extends String, ? extends V> entry : m.entrySet()) {
            String key = entry.getKey();
            V value = entry.getValue();
            this.put(key, value);
        }
    }
    
    @Override
    public V remove(Object key) {
        Object realKey = realKeyMap.remove(convertKey(key));
        return super.remove(realKey);
    }
    
    protected V removeUseRealKey(Object key) {
    	return super.remove(key);
    }
    /**
     * 	对键进行转换
     * @param key
     * @return
     */
    protected String convertKey(Object key){
    	return String.valueOf(key).toLowerCase();
    }
    /**
     * 
     * @param realKey
     * @param convertedKey
     * @return
     */
    protected String choiceRealKey(String realKey,String convertedKey) {
    	return realKey;
    }
    
    /**
     * 真正的key的 
     * @return
     */
    public Set<String> realKeySet(){
    	return super.keySet();
    }

	@Override
	public Object clone() {
		KeyNoCaseHashMap hashMap=new KeyNoCaseHashMap();
		hashMap.putAll(this);
		return hashMap;
	}
    
    
}
