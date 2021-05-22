package org.smile.beans;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.Smile;
import org.smile.collection.ArrayUtils;
import org.smile.collection.KeyLikeHashMap;
import org.smile.collection.KeyNoCaseHashMap;
import org.smile.collection.SoftHashMap;
import org.smile.collection.UnmodifiableList;
import org.smile.collection.UnmodifiableMap;
import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.json.JSONSerializer;
import org.smile.json.JSONStringWriter;
import org.smile.json.JSONWriter;
import org.smile.json.format.SerializeConfig;

/**
 * 对javabean工具类进行扩展
 * @author 胡真山
 *
 */
public class BeanInfo implements java.beans.BeanInfo {
	/**缓存类的bean信息*/
	private static final Map<Class,BeanInfo> beanInfos=SoftHashMap.newConcurrentInstance();
	/**用于JSON序列化*/
	private static final JSONSerializer jsonSerialier=new JSONSerializer();
	/**源javabean信息*/
	protected java.beans.BeanInfo beanInfo;
	/**所有的方法对应的属性*/
	protected Map<Method,PropertyDescriptor> methods=new LinkedHashMap<Method,PropertyDescriptor>();
	/**方法名称对应属性*/
	protected Map<String,PropertyDescriptor> methodNames=new LinkedHashMap<String,PropertyDescriptor>();
	/** 缓存java bean 的属性信息*/
	protected Map<String,PropertyDescriptor> pdMap=new HashMap<String,PropertyDescriptor>();
	/**以nocase方式保存属性信息*/
	private Map<String,PropertyDescriptor> nocasePdMap=new KeyNoCaseHashMap<PropertyDescriptor>();
	/**以like方式保存属性信息*/
	private Map<String,PropertyDescriptor> likePdMap=new KeyLikeHashMap<PropertyDescriptor>();
	/**定义的读取属性*/
	private Map<String,PropertyDescriptor> declareReadPdMap=new KeyNoCaseHashMap<PropertyDescriptor>();
	/**主要是在beanutil等方式的时候使用，不包括class的描述*/
	private PropertyDescriptor[] beanUtilProArray;
	/**用于beanutils的属性集*/
	private List<PropertyDescriptor> beanUitlProperties;

	private BeanInfo(Class clazz) throws IntrospectionException {
		clazz=convertClass(clazz);
		this.beanInfo = Introspector.getBeanInfo(clazz);
		beanUitlProperties=new ArrayList<PropertyDescriptor>();
		for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
			this.pdMap.put(pd.getName(), pd);
			this.nocasePdMap.put(pd.getName(), pd);
			this.likePdMap.put(pd.getName(), pd);
			//读方法
			Method method=pd.getReadMethod();
			if(method!=null){
				this.methods.put(method, pd);
				this.methodNames.put(method.getName(), pd);
				if(method.getDeclaringClass()==clazz){
					this.declareReadPdMap.put(pd.getName(), pd);
				}
			}
			//写方法
			method=pd.getWriteMethod();
			if(method!=null){
				this.methodNames.put(method.getName(), pd);
				this.methods.put(method, pd);
			}
			//class不加入
			if(!Strings.CLASS.equals(pd.getName())){
				beanUitlProperties.add(pd);
			}
		}
		this.beanUtilProArray=beanUitlProperties.toArray(new PropertyDescriptor[beanUitlProperties.size()]);
	}
	
	/**获取类的beanInfo*/
	public static BeanInfo getInstance(Class clazz){
		BeanInfo beanInfo=beanInfos.get(clazz);
		if(beanInfo==null){
			try {
				beanInfo=new BeanInfo(clazz);
				beanInfos.put(clazz, beanInfo);
			} catch (IntrospectionException e) {
				throw new SmileRunException("init bean infos "+clazz,e );
			}
		}
		return beanInfo;
	}
	/**
	 *      复制属性
	 * @param source
	 * @param target
	 */
	public void copyProperties(Object source,Object target,boolean nullTo) {
		Collection<PropertyDescriptor> pds=this.beanUitlProperties;
		for(PropertyDescriptor pd:pds) {
			Method reader=pd.getReadMethod();
			if(reader!=null) {
				try {
					Object value=reader.invoke(source);
					if(value!=null||nullTo) {
						Method setter=pd.getWriteMethod();
						if(setter!=null) {
							setter.invoke(target, value);
						}
					}
				}catch(Exception e) {
					throw new SmileRunException(e);
				}
			}
		}
	}
	/**
	 * 复制属性 原对象属性为null时也会复制
	 * @param source
	 * @param target
	 */
	public void copyProperties(Object source,Object target) {
		this.copyProperties(source, target, true);
	}
	
	public PropertyDescriptor getPropertyDescriptor(Method method){
		return this.methods.get(method);
	}
	/**
	 * 对类型进行一个转换
	 * 处理cglib代理的类型
	 * @param clazz
	 * @return
	 */
	private Class convertClass(Class clazz) {
		if(clazz.getName().contains(Smile.CGLIB_CLASS_FLAG)) {
			Class resClass= clazz.getSuperclass();
			if(resClass==Object.class) {
				Class[] interfaces=clazz.getInterfaces();
				if(ArrayUtils.isEmpty(interfaces)) {
					return resClass;
				}else{
					return interfaces[0];
				}
			}
			return resClass;
		}
		return clazz;
	}
	
	/**
	 * 对一个对象进行json序列化
	 * @param target 类型必须是此beaninfo的封装的类型
	 */
	public String toJSONString(Object target) {
		JSONWriter writer=new JSONStringWriter(SerializeConfig.NULL_NOT_VIEW);
		jsonSerialier.writeJavaBean(writer,beanUitlProperties, target);
		return writer.toJSONString();
	}
	/**
	 * 对集合进行序列化json
	 * @param targets 集合中的类型必须与此beaninfo的封装的类型一样
	 * @return
	 */
	public String toJSONString(Collection<Object> targets) {
		if (targets == null) {
			return Strings.NULL;
		}
		JSONWriter writer = new JSONStringWriter(SerializeConfig.NULL_NOT_VIEW);
		boolean first = true;
		Iterator<Object> iterator = targets.iterator();
		writer.write('[');
		while (iterator.hasNext()) {
			if (first) {
				first = false;
			} else {
				writer.write(',');
			}
			Object value = iterator.next();
			if (value == null) {
				writer.write(Strings.NULL);
				continue;
			}
			jsonSerialier.writeJavaBean(writer, beanUitlProperties, value);
		}
		writer.write(']');
		return writer.toJSONString();
	}
	
	/***
	 * 以不区分大小写的方式获取属性
	 * @param nocaseName
	 * @return
	 */
	public PropertyDescriptor getPropertyDescriptorNocase(String nocaseName) {
		return nocasePdMap.get(nocaseName);
	}
	/**
	 * 以like方式获取属性
	 * @param likeName
	 * @return
	 */
	public PropertyDescriptor getPropertyDescriptorLike(String likeName) {
		return likePdMap.get(likeName);
	}

	public Set<Method> getReaders() {
		return new AbstractSet<Method>() {
			@Override
			public Iterator<Method> iterator() {
				return new MethodIterator() {
					@Override
					protected Method getMethod(PropertyDescriptor pd) {
						return pd.getReadMethod();
					}
				};
			}
			@Override
			public int size() {
				throw new NotImplementedException("not support this method");
			}
		};
	}
	
	public Set<Method> getWriters() {
		return new AbstractSet<Method>() {
			@Override
			public Iterator<Method> iterator() {
				return new MethodIterator() {
					@Override
					protected Method getMethod(PropertyDescriptor pd) {
						return pd.getWriteMethod();
					}
				};
			}
			@Override
			public int size() {
				throw new NotImplementedException("not support this method");
			}
		};
	}
	
	public Set<PropertyDescriptor> getReadPropertyDescriptors(){
		return new AbstractSet<PropertyDescriptor>() {
			@Override
			public Iterator<PropertyDescriptor> iterator() {
				return new PropertyDescriptorIterator() {
					@Override
					protected PropertyDescriptor filter(PropertyDescriptor pd) {
						if(pd.getReadMethod()==null){
							return null;
						}
						return pd;
					}
				};
			}
			@Override
			public int size() {
				throw new NotImplementedException("not support this method");
			}
		};
	}
	
	public Set<PropertyDescriptor> getWritePropertyDescriptors(){
		return new AbstractSet<PropertyDescriptor>() {
			@Override
			public Iterator<PropertyDescriptor> iterator() {
				return new PropertyDescriptorIterator() {
					@Override
					protected PropertyDescriptor filter(PropertyDescriptor pd) {
						if(pd.getWriteMethod()==null){
							return null;
						}
						return pd;
					}
				};
			}
			@Override
			public int size() {
				throw new NotImplementedException("not support this method");
			}
		};
	}
	
	abstract class  MethodIterator implements Iterator<Method>{
		Iterator<PropertyDescriptor> it = pdMap.values().iterator();
		Method next;
		Method current;
		@Override
		public boolean hasNext() {
			while(next==null&&it.hasNext()){
				next=getMethod(it.next());
			}
			return next!=null;
		}
		
		protected abstract Method getMethod(PropertyDescriptor pd);

		@Override
		public Method next() {
			current=next;
			next=null;
			return current;
		}

		@Override
		public void remove() {
			throw new NotImplementedException("not support this method,can not remove from  beaninfo");
		}
	}
	
	abstract class  PropertyDescriptorIterator implements Iterator<PropertyDescriptor>{
		Iterator<PropertyDescriptor> it = pdMap.values().iterator();
		PropertyDescriptor next;
		PropertyDescriptor current;
		@Override
		public boolean hasNext() {
			while(next==null&&it.hasNext()){
				next=filter(it.next());
			}
			return next!=null;
		}
		
		protected abstract PropertyDescriptor filter(PropertyDescriptor pd);

		@Override
		public PropertyDescriptor next() {
			current=next;
			next=null;
			return current;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("can not remove from  beaninfo");
		}
	}

	/***
	 * 获取java  bean的属性 
	 * @param name 必须与java bean 的属性完全匹配
	 * @return
	 */
	public PropertyDescriptor getPropertyDescriptor(String name) {
		return pdMap.get(name);
	}

	@Override
	public BeanDescriptor getBeanDescriptor() {
		return beanInfo.getBeanDescriptor();
	}

	@Override
	public EventSetDescriptor[] getEventSetDescriptors() {
		return beanInfo.getEventSetDescriptors();
	}

	@Override
	public int getDefaultEventIndex() {
		return beanInfo.getDefaultEventIndex();
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return beanInfo.getPropertyDescriptors();
	}

	@Override
	public int getDefaultPropertyIndex() {
		return beanInfo.getDefaultPropertyIndex();
	}

	@Override
	public MethodDescriptor[] getMethodDescriptors() {
		return beanInfo.getMethodDescriptors();
	}

	@Override
	public java.beans.BeanInfo[] getAdditionalBeanInfo() {
		return beanInfo.getAdditionalBeanInfo();
	}

	@Override
	public Image getIcon(int iconKind) {
		return beanInfo.getIcon(iconKind);
	}
	
	/**
	 * 所有的
	 * @return
	 */
	public Collection<PropertyDescriptor> propertyDescriptors(){
		return pdMap.values();
	}

	/**
	 * 不区分大小写
	 * @return
	 */
	public Map<String, PropertyDescriptor> getNocasePdMap() {
		return new UnmodifiableMap<String, PropertyDescriptor>(this.nocasePdMap);
	}

	/**
	 * 键使用模糊匹配　不区分大小写　忽略掉下划线
	 * @return
	 */
	public Map<String, PropertyDescriptor> getLikePdMap() {
		return new UnmodifiableMap<String, PropertyDescriptor>(this.likePdMap);
	}
	
	/**
	 * 正常的属性
	 * @return
	 */
	public Map<String, PropertyDescriptor> getPdMap() {
		return new UnmodifiableMap<String, PropertyDescriptor>(this.pdMap);
	}

	/***
	 * 不包含class
	 * @return
	 */
	protected PropertyDescriptor[] beanUtilProperties() {
		return beanUtilProArray;
	}
	
	/**
	 * 可用于beanUtil的属性的个数
	 * @return
	 */
	public int beanUtilPdSize() {
		return beanUitlProperties.size();
	}
	/**
	 * 用于beanutil的属性列表
	 * @return
	 */
	public List<PropertyDescriptor> beanUtilPdList(){
		return new UnmodifiableList<PropertyDescriptor>(this.beanUitlProperties);
	}
	/**
	 * 使用方法名获取PropertyDescriptor
	 * @param methodName
	 * @return
	 */
	public PropertyDescriptor getPdByMethodName(String methodName) {
		return this.methodNames.get(methodName);
	}
	
	/**
	 * 获取此类定义的读取方法属性
	 * 不包括继承来的方法
	 * @param name
	 * @return
	 */
	public PropertyDescriptor getDeclareReadPd(String name){
		return declareReadPdMap.get(name);
	}
	/**
	 * 此类定义的方法 不包括继承来的方法
	 * @return
	 */
	public Map<String, PropertyDescriptor> getDeclareReadPd() {
		return new UnmodifiableMap<String, PropertyDescriptor>(this.declareReadPdMap);
	}
	
}
