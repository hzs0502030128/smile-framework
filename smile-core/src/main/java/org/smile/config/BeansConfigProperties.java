package org.smile.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.smile.beans.BeanFactorySupport;
import org.smile.beans.BeanProducer;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.config.parser.TagConfigParser;
import org.smile.io.IOUtils;
import org.smile.reflect.ClassTypeUtils;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.Template;
import org.smile.util.XmlUtils;

public class BeansConfigProperties implements Config,BeanFactorySupport<BeanProducer>{
	/**以ID为key映射*/
	private Map<String,BeanProducer> configMap=new LinkedHashMap<String,BeanProducer>();
	/**以类为key映射*/
	private Map<Class,BeanProducer> classConfigMap=new LinkedHashMap<Class,BeanProducer>();
	
	@Override
	public void load(File file) throws IOException {
		InputStream is=new FileInputStream(file);
		load(is);
	}

	@Override
	public void load(InputStream is) throws IOException {
		BeansConfig beansConfig=XmlUtils.parserXml(BeansConfig.class,is);
		regist(beansConfig);
	}
	/**
	 * 批量注入生产者
	 * @param beansConfig
	 */
	public void regist(BeansConfig beansConfig){
		List<BeanConfig> list=beansConfig.getBean();
		if(CollectionUtils.notEmpty(list)){
			for(BeanConfig b:list){
				BeanCreator c=new BeanCreator(b,new TagConfigParser(this));
				registProducer(c.getBeanId(), c);
			}
		}
	}

	@Override
	public void load(File file, Object initParam) throws IOException {
		load(new FileInputStream(file), initParam);
	}

	@Override
	public void load(InputStream is, Object initParam) throws IOException {
		String xml=IOUtils.readString(is);
		Template template=new SimpleStringTemplate(xml);
		BeansConfig beansConfig=XmlUtils.parserXml(BeansConfig.class,template.processToString(initParam));
		regist(beansConfig);
	}

	@Override
	public <T> T getValue(String key) {
		BeanProducer bc=configMap.get(key);
		if(bc!=null){
			try {
				return (T)bc.getBean();
			} catch (BeanException e) {
				throw new SmileRunException(bc.toString(),e);
			}
		}
		return null;
	}

	@Override
	public Collection getValues() {
		return new AbstractSet() {
			@Override
			public int size() {
				return configMap.size();
			}

			@Override
			public Iterator iterator() {
				return new ValueIterator();
			}
		};
	}
	
	class ValueIterator implements Iterator{
		Iterator<Entry<String,BeanProducer>> iterator=configMap.entrySet().iterator();
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Object next() {
			try {
				return iterator.next().getValue().getBean();
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}

		@Override
		public void remove() {
			iterator.remove();
		}
	}

	@Override
	public Set getKeys() {
		return configMap.keySet();
	}

	@Override
	public <T> T getBean(String id,boolean noExsitsError) throws BeanException {
		T t= getValue(id);
		if(t==null&&noExsitsError) {
			throw new BeanException(id+" can not find ");
		}
		return t;
	}

	@Override
	public <T> T getBean(Class<T> beanClass) throws BeanException {
		BeanProducer bc= classConfigMap.get(beanClass);
		if(bc!=null){
			return (T)bc.getBean();
		}
		return null;
	}

	@Override
	public void registProducer(String id, BeanProducer producter) {
		configMap.put(producter.getBeanId(), producter);
		classConfigMap.put(producter.getBeanClass(), producter);
		Class[] interfaces=ClassTypeUtils.getAllInterfaces(producter.getBeanClass());
		for(Class clazz:interfaces){
			classConfigMap.put(clazz, producter);
		}
	}

	@Override
	public <T> T getBean(String id) throws BeanException {
		return getValue(id);
	}

}
