package org.smile.db.handler;

public class RecordSetMap extends ResultSetMap{

	@Override
	public boolean containsKey(Object key) {
		Object realKey=this.realKeyMap.get(key);
		if(realKey==null) {
			return containsUseRealKey(key);
		}
		return containsUseRealKey(realKey);
	}

	@Override
	public Object get(Object key) {
		Object realKey=this.realKeyMap.get(key);
		if(realKey==null) {
			return getUseRealKey(key);
		}
		return getUseRealKey(realKey);
	}

	@Override
	public Object put(String key, Object value) {
		String realKey = convertKey(key);
		this.realKeyMap.put(key, realKey);
        return putUseRealKey(realKey, value);
	}

	@Override
	protected String convertKey(Object key) {
		return this.keyColumnSwaper.columnToKey(String.valueOf(key));
	}



	@Override
	public Object remove(Object key) {
		String realKey=this.realKeyMap.remove(key);
		if(realKey!=null) {
			return removeUseRealKey(realKey);
		}
		return removeUseRealKey(key);
	}
	
}
