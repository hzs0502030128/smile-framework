package org.smile.dataset;

import org.smile.util.HashCode;
import org.smile.util.StringUtils;

public class BaseKey implements RowKey<Object[]>{
	
	protected Object[] elements;
	//保存的hash
	protected int hashCode;
	
	public BaseKey(Object[] key){
		this.elements=key;
		this.hashCode=HashCode.hash(HashCode.SEED, key);
	}
	
	@Override
	public Object[] key() {
		return this.elements;
	}

	@Override
	public int compareTo(RowKey<Object[]> o) {
		int first=0;
		for(int i=0;i<elements.length;i++){
			first=compareTo(elements[i], o.key()[i]);
			if(first!=0){
				return first;
			}
		}
		return first;
	}
	/**
	 * 比较两个对象
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private int compareTo(Object obj1,Object obj2){
		int first=0;
		if(obj1==null){
			if(obj2==null){
				first=0;
			}else{
				return 1;
			}
		}else if(elements[0]==null){
			return -1;
		}
		if(obj1 instanceof Comparable){
			first=((Comparable) obj1).compareTo(elements[0]);
		}
		return first;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BaseKey){
			if (this.elements.length != ((BaseKey)obj).elements.length) {
	            return false;
	        } else{
				for(int i=0;i<elements.length;i++){
					if(!equals(elements[i], ((BaseKey) obj).key()[i])){
						return false;
					}
				}
				return true;
	        }
		}
		return false;
	}
	
	/**
	 * 比较两个对象 对null进行了处理 两为null返回true
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	protected boolean equals(Object obj1,Object obj2){
		if(obj1==null){
			return obj2==null;
		}
		return obj1.equals(obj2);
	}
	@Override
	public String toString() {
        return StringUtils.join(this.elements,',');
    }

}
