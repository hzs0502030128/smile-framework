package org.smile.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.reflect.FieldUtils;


public class JSONFiledSerializer extends JSONSerializer{
	/**缓存类的字段信息*/
	private  PropertyContext propertyContext=new PropertyContext();
	/**
	 * java 按字段转换为json字符串
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@Override
	public  void writeJavaBean(JSONWriter writer,Object value){
		if(value==null){
			writer.write(Strings.NULL);
			return;
		}
        boolean first = true;
        List<Field> propertys= propertyContext.loadFields(value.getClass());
        writer.write('{');
		for(Field field:propertys){
			String key=field.getName();
			Object fieldValue=null;
			try {
				fieldValue = field.get(value);
			} catch (Exception e) {
				throw new SmileRunException("read field "+key,e);
			}
			//显示空属性  忽略属性
			if((fieldValue==null&&!writer.config.isNullValueView())||writer.config.ignore(field,fieldValue)){
				continue;
			}
			if(first){
                first = false;
            }else{
                writer.write(',');
            }
			writer.write('\"');
	        if(key == null){
	            writer.write(Strings.NULL);
	        }else{
	        	writer.write(escape(key));
	        }
	        writer.write('\"').write(':');
			write(writer,fieldValue);
		}
		writer.write('}');
	}
	
	protected  class PropertyContext {
		/***
		 * 缓存类的字段信息
		 */
		private Map<Class<?>, List<Field>> fieldMap = new ConcurrentHashMap<Class<?>, List<Field>>();
		/**
		 * 缓存类的方法信息
		 */
		private Map<Class<?>, List<Method>> methodMap = new ConcurrentHashMap<Class<?>, List<Method>>();
		/**用于过滤掉序列号编号字段*/
		private static final String IDVal = "serialVersionUID";
		/**
		 * 获取某个对象的class
		 * 
		 * @param obj
		 * @return
		 */
		public Class<? extends Object> getObjClass(Object obj) {
			if (obj instanceof Class) {
				return (Class<?>) obj;
			}
			return obj.getClass();
		}

		/**
		 * 获取class的字段对象
		 * 
		 * @param clazz
		 * @param fieldName
		 * @return
		 */
		public Field getField(Class<?> clazz, String fieldName) {
			List<Field> fields = loadFields(clazz);
			if (CollectionUtils.notEmpty(fields)) {
				for (Field f : fields) {
					if (f.getName().equals(fieldName)) {
						return f;
					}
				}
			}
			return null;
		}

		/**
		 * 获取一个类的方法 
		 * @param clazz
		 * @return
		 */
		public List<Method> loadMethods(Class<?> clazz) {
			List<Method> methods = methodMap.get(clazz);
			if (CollectionUtils.notEmpty(methods)) {
				return methods;
			}
			methods = new ArrayList<Method>(Arrays.<Method> asList(clazz.getDeclaredMethods()));
			if (clazz.getSuperclass() != null) {
				methods.addAll(loadMethods(clazz.getSuperclass()));
			}
			methodMap.put(clazz, methods);
			return methods;
		}

		/**
		 * 获取class的字段列表
		 * 
		 * @param clazz
		 * @return
		 */
		public List<Field> loadFields(Class<?> clazz) {
			List<Field> fields = fieldMap.get(clazz);
			if (ArrayUtils.notEmpty(fields)) {
				return fields;
			}
			List<Field> fieldArgs = FieldUtils.getAnyField(clazz);
			fields = new ArrayList<Field>(fieldArgs.size());
			for (Field f : fieldArgs) {
				if (IDVal.equals(f.getName())) {
					continue;
				}
				fields.add(f);
				f.setAccessible(true);
			}
			fieldMap.put(clazz, fields);
			return fields;
		}
	}
}
