package org.smile.report.excel.target;

import java.util.HashMap;
import java.util.Map;

import org.smile.beans.BeanProperties;
import org.smile.report.poi.ExcelException;
/**
 * 以map做为导入对象封装
 * @author 胡真山
 *
 */
public class MapImportTarget extends ImportTarget{
	
	protected BeanProperties properties=new BeanProperties();
	
	protected Class<? extends Map> mapClass;
	
	public MapImportTarget(Class<? extends Map> mapClass){
		if(mapClass==Map.class){
			this.mapClass=HashMap.class;
		}else{
			this.mapClass=mapClass;
		}
	}
	
	public  MapImportTarget() {
		this.mapClass=HashMap.class;
	}
	
	@Override
	public Map newTargetInstanse() {
		try {
			return mapClass.newInstance();
		} catch (Exception e) {
			throw new ExcelException(e);
		}
	}

	@Override
	public BeanProperties getBeanProperties() {
		return properties;
	}
}
