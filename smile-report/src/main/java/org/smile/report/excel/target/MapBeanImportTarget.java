package org.smile.report.excel.target;

import org.smile.beans.BeanProperties;
import org.smile.beans.MapBeanClass;

public class MapBeanImportTarget extends ImportTarget{
	
	public MapBeanClass clazz;

	public MapBeanImportTarget(MapBeanClass mapBeanClass){
		this.clazz=mapBeanClass;
	}
	
	@Override
	public Object newTargetInstanse() {
		return clazz.newInstance();
	}

	@Override
	public BeanProperties getBeanProperties() {
		return clazz.getBeanProperties();
	}

}
