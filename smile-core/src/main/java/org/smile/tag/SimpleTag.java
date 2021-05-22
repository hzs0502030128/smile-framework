package org.smile.tag;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.smile.beans.converter.BaseTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.collection.LinkedHashMap;
import org.smile.commons.SmileRunException;
import org.smile.commons.ann.Attribute;
import org.smile.log.LoggerHandler;
import org.smile.reflect.FieldUtils;
import org.smile.tag.impl.TagException;
import org.smile.util.StringUtils;


public abstract class SimpleTag extends AbstractTag<SimpleContext> implements LoggerHandler{
	/**
	 * 替换符起始标记
	 */
	public static final String DEFAULT_MACRO_START = "${";
	/**
	 * 替换符结束标记
	 */
	public static final String DEFAULT_MACRO_END = "}";
	/**
	 * 指令 开始标记
	 */
	private String macroStart = DEFAULT_MACRO_START;
	/**
	 * 指令结束标记
	 */
	private String macroEnd = DEFAULT_MACRO_END; 
	/***
	 * 需要赋值的属性
	 */
	protected Map<Field,Attribute> attributeMap;
	
	public SimpleTag(){
		initAttributeMap();
	}

	@Override
	public void process() throws Exception {
		fillAttributes();
		doTag();
		reset();
	}
	
	protected void initAttributeMap(){
		if(attributeMap==null){
			attributeMap=new LinkedHashMap<Field,Attribute>();
			List<Field> fields=FieldUtils.getAnyField(getClass());
			for(Field f:fields){
				Attribute ann=f.getAnnotation(Attribute.class);
				if(ann!=null){
					f.setAccessible(true);
					attributeMap.put(f, ann);
				}
			}
		}
	}
	/**
	 * 对标签的属性赋值
	 * @throws TagException
	 */
	public void fillAttributes() throws  TagException{
		if(attributeMap.isEmpty()){
			return;
		}
		for(Map.Entry<Field, Attribute> entry:attributeMap.entrySet()){
			Field f=entry.getKey();
			Attribute attr=entry.getValue();
			String attrName=StringUtils.isEmpty(attr.value())?f.getName():attr.value();
			if(this.tagFragment.getTagInfo().hasAttribute(attrName)){//有传入属性才赋值
				Object value=this.evaluateAttribute(attrName);
				if(attr.required()&&value==null){
					throw new TagException("attribute "+f.getName()+" of tag "+this.tagFragment.getTagName()+" is required");
				}
				try {
					FieldUtils.setFieldValue(f, this, value);
				} catch (ConvertException e) {
					throw new TagException(f.getName()+" set value ",e);
				}
			}
		}
	}
	/**
	 * 重置此标签
	 */
	protected  void reset(){}
	
	/***
	 * 处理标签
	 * @throws Exception
	 */
	protected  void doTag() throws Exception{
		invokeBody();
	}
	
	/**
	 * 获取标签属性
	 * @param name
	 * @return
	 */
	protected String getAttribute(String name){
		String value= this.tagFragment.getTagInfo().getAttributeValue(name);
		return value;
	}
	/**
	 * 如果是表达式则会解析表达式
	 * @param name
	 * @return
	 */
	protected Object evaluateAttribute(String name){
		String value= this.tagFragment.getTagInfo().getAttributeValue(name);
		if(value==null){
			return null;
		}
		int start=value.indexOf(macroStart);
		if(start==0){
			if(value.endsWith(macroEnd)){//是表达式时
				//表达式内容
				String expression=StringUtils.trim(value.substring(macroStart.length(), value.length()-macroEnd.length()));
				return this.tagContext.evaluate(expression);
			}
		}
		return value;
	}
	
	/**
	 * 获取标签属性
	 * @param name
	 * @return
	 */
	protected <T> T getAttribute(String name,Class<T> resultType){
		String value=this.tagFragment.getTagInfo().getAttributeValue(name);
		if(value==null){
			return null;
		}
		try {
			return BaseTypeConverter.getInstance().convert(resultType, value);
		} catch (ConvertException e) {
			throw new SmileRunException("get attribute "+name,e);
		}
	}

}
