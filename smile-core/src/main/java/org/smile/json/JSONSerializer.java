package org.smile.json;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.smile.beans.BeanInfo;
import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.json.format.SerializeConfig;
import org.smile.json.parser.JSONParseException;
import org.smile.json.parser.JSONTokener;
import org.smile.json.parser.StringValue;
import org.smile.reflect.ClassTypeUtils;

public class JSONSerializer {
	
	/**
	 * 对javabean的指定属性写入序列化
	 * @param writer
	 * @param propertys
	 * @param value
	 */
	public void writeJavaBean(JSONWriter writer,List<PropertyDescriptor> propertys,Object value) {
		if(value==null){
			writer.write(Strings.NULL);
			return;
		}
        boolean first = true;
        writer.write('{');
		for(PropertyDescriptor pd:propertys){
			Method reader=pd.getReadMethod();
			if(reader==null){
				continue;
			}
			Object readValue=null;
			try {
				readValue=reader.invoke(value);
			} catch (Exception e) {
				throw new SmileRunException("read property "+pd.getName(),e);
			}
			//显示空属性  忽略属性
			if((readValue==null&&!writer.config.isNullValueView())||writer.config.ignore(pd,readValue)){
				continue;
			}
			if(first){
                first = false;
            }else{
            	writer.write(',');
            }
			writer.write('\"');
			//添加内容
			Object key=pd.getName();
	        if(key == null){
	        	writer.write(Strings.NULL);
	        }else{
	        	writer.write(escape(key.toString()));
	        }
	        writer.write('\"').write(':');
	        write(writer,readValue);
		}
		writer.write('}');
	}
	
	/**
	 * 把一个javabean写入到writer中
	 * @param writer
	 * @param value
	 */
	public void writeJavaBean(JSONWriter writer,Object value){
        List<PropertyDescriptor> propertys= BeanInfo.getInstance(value.getClass()).beanUtilPdList();
        writeJavaBean(writer, propertys, value);
	}
	
	/**
	 * java bean转json字符串
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public  String serializeJavaBean(Object value,SerializeConfig config){
		if(value==null){
			return Strings.NULL;
		}
        JSONStringWriter writer=new JSONStringWriter(config);
        writeJavaBean(writer, value);
        return writer.toString();
	}
	/**
	 * 把一个map转成json格式
	 * @param map
	 * @return
	 */
	public  void writeMap(JSONWriter writer,Map<String,Object> map){
		if(map == null){
			 writer.write(Strings.NULL);
			 return;
		}
        boolean first = true;
        writer.write('{');
		for(Map.Entry<String,Object> entry:map.entrySet()){
			Object key=entry.getKey();
			Object readValue=entry.getValue();
			if(readValue==null&&!writer.config.isNullValueView()){//显示空属性
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
	        	writer.write(escape(key.toString()));
	        }
	        writer.write('\"').write(':');
	        write(writer,readValue);
		}
		writer.write('}');
	}
	/**
	 * 把一个map转成json格式
	 * @param map
	 * @return
	 */
	public  String serializeMap(Map<String,Object> map,SerializeConfig config){
		JSONWriter writer=new JSONStringWriter(config);
		writeMap(writer, map);
		return writer.toJSONString();
	}
	
	/***
	 * 转换数组
	 */
	protected  void writeBaseArray(JSONWriter writer,Object array){
		if(array==null){
			writer.write(Strings.NULL);
			return;
		}
		int len=ArrayUtils.size(array);
		writer.write('[');
		boolean first=true;
		for(int i=0;i<len;i++){
			if(first){
                first = false;
			}else{
                writer.write(',');
            }
			Object value=ArrayUtils.get(array, i);
			if(value == null){
				writer.write(Strings.NULL);
				continue;
			}
			write(writer,value);
		}
		writer.write(']');
	}
	
	/***
	 * 转换数组
	 */
	protected  String serializeBaseArray(Object array,SerializeConfig config){
		JSONWriter writer=new JSONStringWriter(config);
		writeBaseArray(writer, array);
		return writer.toJSONString();
	}
	
	/**
	 * 转换列表转换成一个json字符串
	 * @param list
	 * @return
	 */
	public void writeCollection(JSONWriter writer,Collection list){
		if(list==null){
			writer.write(Strings.NULL);
			return;
		}
        boolean first = true;
		Iterator<Object> iterator=list.iterator();
		writer.write('[');
		while(iterator.hasNext()){
            if(first){
                first = false;
            }else{
            	writer.write(',');
            }
			Object value=iterator.next();
			if(value==null){
				writer.write(Strings.NULL);
				continue;
			}
			write(writer,value);
		}
        writer.write(']');
	}
	
	/**
	 * 转换数组
	 * @param array
	 * @return
	 */
	public String serializeArray(Object[] array,SerializeConfig config){
		JSONWriter writer=new JSONStringWriter(config);
		writeArray(writer, array);
		return writer.toJSONString();
	}
	/**
	 * 转换数组
	 * @param array
	 * @return
	 */
	public void writeArray(JSONWriter writer,Object[] array){
		if(array == null){
			writer.write(Strings.NULL);
			return;
		}
        boolean first = true;
        writer.write('[');
        for(int i=0;i<array.length;i++){
            if(first){
                first = false;
            }else{
            	writer.write(',');
            }
			Object value=array[i];
			if(value == null){
				writer.write(Strings.NULL);
				continue;
			}
			write(writer,value);
		}
        writer.write(']');
	}
	
	/**
	 * 转成字符串值
	 * @param value
	 * @return
	 */
	protected  void writeChar(JSONWriter writer,String value){
		writer.write(Strings.SINGLE_QUOTE);
		writer.write(value);
		writer.write(Strings.SINGLE_QUOTE);
	}
	
	/**
	 * 转成json字符串
	 * @param value
	 * @return
	 */
	public  void writeObjct(JSONWriter writer,Object value) {
		if (value==null){
			writer.write(Strings.NULL);
		}else if(ClassTypeUtils.isBasicType(value.getClass())){
			writer.write(value.toString());
		}else if (value instanceof String){
			writeString(writer,escape((String)value));
		}else if(value instanceof Character){
			writeChar(writer,escape(String.valueOf(value)));
		}else if (value instanceof Double) {
			if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
				writer.write(Strings.NULL);
			} else {
				writer.write(value.toString());
			}
		}else if (value instanceof Float) {
			if (((Float) value).isInfinite() || ((Float) value).isNaN()){
				writer.write(Strings.NULL);
			} else {
				writer.write(value.toString());
			}
		}else if (value instanceof Number||value instanceof Boolean) {
			writer.write(value.toString());
		}else if(value instanceof java.sql.Date){
			writeSqlDate(writer,(java.sql.Date)value);
		}else if(value instanceof Date){
			writeDate(writer,(Date)value);
		}else if(value instanceof JSONAware){
			((JSONAware) value).serializeWriter(writer);
		}else if (value instanceof Map) {
			writeMap(writer,(Map)value);
		}else if(value instanceof Class){
			writeString(writer,((Class) value).getName());
		}else if(value instanceof Enum){
			writeString(writer,value.toString());
		}else{
			try {
				writeJavaBean(writer,value);
			} catch (Exception e) {
				throw new SmileRunException("JSON化对象失败:"+value.getClass().getName()+""+value,e);
			}
		}
	}
	
	/**
	 * 转一个obejct成一个json格式
	 * @param value
	 * @return
	 */
	public  void write(JSONWriter writer,Object value) {
		try{
			if(value==null){
				writer.write(Strings.NULL);
			}else if (value instanceof Collection) {
				writeCollection(writer,(Collection) value);
			}else if(value instanceof Object[]){
				 writeArray(writer,(Object[])value);
			}else if(ClassTypeUtils.isBasicArrayType(value.getClass())){
				writeBaseArray(writer,value);
			}else {
				writeObjct(writer,value);
			}
		}catch(StackOverflowError e){
			throw new StackOverflowError("解析成json字符串出错,对象的引用可能存在一个环状引用");
		}
	}
	/**
	 * 转一个obejct成一个json格式
	 * @param value
	 * @return
	 */
	public  String serialize(Object value,SerializeConfig config) {
		JSONWriter writer=new JSONStringWriter(config);
		write(writer, value);
		return writer.toJSONString();
	}
	/**
	 * 序列化一个json对象成字符串
	 * @param jsonObj
	 * @return
	 */
	public String serialize(Object jsonObj){
		return serialize(jsonObj,SerializeConfig.NULL_NOT_VIEW);
	}
	
	/**
	 * 转成字符串值
	 * @param value
	 * @return
	 */
	protected void writeString(JSONWriter writer,CharSequence value){
		writer.write(Strings.QUOTE);
		writer.write(value);
		writer.write(Strings.QUOTE);
	}
	/**
	 * 序列化sqlDate
	 * @param writer
	 * @param date
	 */
	protected void writeSqlDate(JSONWriter writer,java.sql.Date date){
		writer.write(Strings.QUOTE);
		writer.writeSqlDate(date);
		writer.write(Strings.QUOTE);
	}
	/**
	 * 序列化日期
	 * @param writer
	 * @param date
	 */
	protected void writeDate(JSONWriter writer,Date date){
		writer.write(Strings.QUOTE);
		writer.write(date);
		writer.write(Strings.QUOTE);
	}
	
	/**
	 * @param 转义字符
	 *           
	 * @param sb
	 */
	protected String escape(CharSequence s) {
		if (s == null){
			return null;
		}
		char ch = '\000';
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char b=ch;
			ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				if (b == '<') {
					sb.append('\\');
				}
				sb.append('/');
				break;
			default:
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 解析一个字符串为JSON对象
	 * @param jsonStr
	 * @return
	 * @throws JSONParseException
	 */
	public JSONAware parse(String jsonStr) throws JSONParseException{
		if(jsonStr==null){
			return null;
		}else if(Strings.NULL.equals(jsonStr)){
			return JSONValue.NULL;
		}
		JSONTokener tokener=new JSONTokener(jsonStr);
		String json=jsonStr.trim();
		char startChar=json.charAt(0);
		switch(startChar){
			case '[' :return new JSONArray(tokener);
			case '{' :return new JSONObject(tokener);
			case '"':
			case '\'':String string=StringValue.valueOf(jsonStr);
				return new JSONString(string);
			default:
				if(JSONValue.INTEGER.test(json)){
		        	Long l=new Long(json);
		        	if(l.longValue()==l.intValue()){
		        		return new JSONInteger(l.intValue());
		        	}else{
		        		return new JSONInteger(Integer.parseInt(json));
		        	}
		        }else if(JSONValue.DOUBLE.test(json)){
		        	return new JSONDouble(Double.parseDouble(json));
		        }else if(JSONValue.BOOLEAN.test(json)){
		        	return new JSONBoolean(Boolean.valueOf(json));
		        }else{
		        	throw new JSONParseException("不规范的JSON格式字符串:must start withd [ or {");
		        }
		}
		
	}
	/**
	 *       解析时 转换字符串为对象
	 *      转换为基本类型 不转换对象 数组类型
	 * @param string
	 * @return
	 */
	public Object parseString(String string) {
		
        if (string.equals(Strings.BLANK)) {
            return Strings.BLANK;
        }
        if (string.equalsIgnoreCase(Strings.TRUE)) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase(Strings.TRUE)) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase(Strings.NULL)) {
            return null;
        }
        char b = string.charAt(0);
        if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
            try {
            	Double d;
                if (string.indexOf('.') > -1 ||
                        string.indexOf('e') > -1 || string.indexOf('E') > -1) {
                    d = Double.valueOf(string);
                    if (!d.isInfinite() && !d.isNaN()) {
                        return d;
                    }
                } else {
                    Long myLong = new Long(string);
                    if (myLong.longValue() == myLong.intValue()) {
                        return new Integer(myLong.intValue());
                    } else {
                        return myLong;
                    }
                }
            } catch (Exception ignore) {}
        }
        return string;
    }
	
	/**
	 *      把一个对象序列化成一个字符串
	 * @param value
	 * @param config
	 * @return
	 */
	public String serializeObjct(Object value, SerializeConfig config) {
		JSONWriter writer=new JSONStringWriter(config);
		writeObjct(writer, value);
		return writer.toJSONString();
	}
	
	/**
	 * 把一个集合序列化成一个字符串
	 * @param collection
	 * @param config
	 * @return
	 */
	public String serializeCollection(Collection collection, SerializeConfig config) {
		JSONWriter writer=new JSONStringWriter(config);
		writeCollection(writer, collection);
		return writer.toJSONString();
	}
}
