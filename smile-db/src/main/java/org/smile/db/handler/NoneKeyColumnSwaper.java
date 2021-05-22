package org.smile.db.handler;

public class NoneKeyColumnSwaper implements KeyColumnSwaper{
	
	public static KeyColumnSwaper instance=new NoneKeyColumnSwaper();
	
	@Override
	public String columnToKey(String column) {
		return column;
	}

	@Override
	public String KeyToColumn(String key) {
		return key;
	}

}
