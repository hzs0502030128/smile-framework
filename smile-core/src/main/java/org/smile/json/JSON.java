package org.smile.json;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.Smile;
import org.smile.collection.CollectionUtils;
import org.smile.json.adapter.JsonAdpater;
import org.smile.json.adapter.SmileJsonAdpater;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.SysUtils;

/**
 * json 操作工具类  
 * 对 fastjson 类型作了适配
 * @author 胡真山
 * @Date 2016年2月19日
 */
public class JSON{
	/**
	 * 用于尝试json转换
	 */
	private static Map<String,String> TEST=CollectionUtils.hashMap("info", "测试json适配");
	
	private static JSON instance=new JSON();
	/**
	 * 适配器实现
	 */
	private JsonAdpater adpater;
	
	private JSON(){
		try{
			Class<JsonAdpater> clazz=Smile.config.getValue(Smile.JSON_ADPATER_CONFIG_KEY, Class.class);
			if(clazz!=null){
				adpater=ClassTypeUtils.newInstance(clazz);
			}
		}catch(Throwable e){
			SysUtils.log("初始化配置的json适配器失败"+Smile.JSON_ADPATER_CONFIG_KEY, e);
		}
		tryJsonAdpater(SmileJsonAdpater.class);
	}
	/**
	 * 尝试json适配器是否可用
	 * @param adpaterClass
	 */
	private synchronized void tryJsonAdpater(Class<? extends JsonAdpater> adpaterClass){
		if(adpater==null){
			try{
				JsonAdpater ja=ClassTypeUtils.newInstance(adpaterClass);
				String json=ja.toJSONString(TEST);
				adpater=ja;
				SysUtils.log(adpaterClass+"适配成功->"+json);
			}catch(Throwable e){
				SysUtils.log("无法使用:"+adpaterClass);
			}
		}
	}
	/**
	 * 转成JSON字符串
	 * @param json
	 * @return
	 */
	public static String toJSONString(Object json){
		return instance.adpater.toJSONString(json);
	}
	/**
	 * 解析数组类型的json字符串
	 * @param jsonStr
	 * @return
	 */
	public static List parseJSONArray(String jsonStr){
		return instance.adpater.parseJSONArray(jsonStr);
	}
	/**
	 * 解析对象类型的JSON字符串
	 * @param jsonStr
	 * @return
	 */
	public static Map parseJSONObject(String jsonStr){
		return instance.adpater.parseJSONObject(jsonStr);
	}
	/**
	 * 解析JSON字符串
	 * @param jsonStr
	 * @return
	 */
	public static Object parse(String jsonStr){
		return instance.adpater.parseJSON(jsonStr);
	}
	
	/**
	 * 转成json字符串
	 * @param json 
	 * @return
	 */
	public static String toJSONString(Collection json){
		return instance.adpater.toJSONString(json);
	}
	
	public static <T> T parseJSONObject(String json,Class<T> javaType){
		return instance.adpater.parseJSONObject(json, javaType);
	}
	
	public static <T> List<T> parseJSONArray(String json,Class<T> javaType){
		return instance.adpater.parseJSONArray(json, javaType);
	}
	
}
