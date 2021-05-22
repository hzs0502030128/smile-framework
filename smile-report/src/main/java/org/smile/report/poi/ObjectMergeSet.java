package org.smile.report.poi;

import org.smile.commons.StringBand;

/**
 * 以填充的对象做为合并的关键信息
 * @author 胡真山
 *
 */
public class ObjectMergeSet extends AbstractMergeSet<Object>{
	/***
	 * 关键的列名
	 */
	protected String[] propertyName;
	
	protected ObjectMergeSet(){}
	
	public ObjectMergeSet(String[] propertyName,Integer[] mergeColumn){
		this.propertyName=propertyName;
		this.mergeColumn=mergeColumn;
	}
	
	@Override
	public Object getMergeKeyValue(Object oneData) {
		if(propertyName.length==0){
			Object value=PoiSupport.getPropertyExpValue(oneData, propertyName[0]);
			if(value==null){
				return 0;
			}
			return value;
		}else{
			StringBand code=new StringBand();
			for(int i=0;i<propertyName.length;i++){
				Object value=PoiSupport.getPropertyExpValue(oneData, propertyName[i]);
				if(value!=null){
					code.append(value.hashCode());
				}
				if(i<propertyName.length-1){
					code.append("-");
				}
			}
			return code.toString();
		}
	}

}
