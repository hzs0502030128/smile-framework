package org.smile.reflect.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.smile.collection.ArrayUtils;
import org.smile.collection.IndexList;
import org.smile.commons.ann.Param;
import org.smile.commons.ann.Params;

/**
 * 从方法 的注解中获取参数的名称
 * @author 胡真山
 *
 */
public class SimpleParamNameReader implements ParamNameReader {
	
	protected static final String NULL_ANN_PARAM_ANME="arg";
	@Override
	public String[] getParameterNames(Method method){
		//参数注解
		Annotation[][] parameterAnns=method.getParameterAnnotations();
		if(ArrayUtils.notEmpty(parameterAnns)){
			IndexList<String> names=new IndexList<String>();
			int index=0;
			for(Annotation[] anns:parameterAnns){
				for(Annotation ann:anns){
					if(ann instanceof Param){
						names.add(index,((Param)ann).name());
						break;
					}
				}
				index++;
			}
			if(names.size()==parameterAnns.length){
				return names.toArray(new String[names.size()]);
			}else if(names.size()>0){
				String[] res=new String[parameterAnns.length];
				//填充未标记的
				for(int i=0;i<parameterAnns.length;i++){
					if(names.containsIndex(i)){
						res[i]=names.get(i);
					}else{
						res[i]=NULL_ANN_PARAM_ANME+i;
					}
				}
				return res;
			}
		}
		//方法注解
		Params methodParam=method.getAnnotation(Params.class);
		if(methodParam!=null){
			return methodParam.names();
		}
		//返回null 使用默认名称
		return null;
	}

}
