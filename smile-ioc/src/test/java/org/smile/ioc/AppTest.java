package org.smile.ioc;

import org.junit.Before;
import org.junit.Test;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.config.BeanConfig;
import org.smile.config.ListConfig;
import org.smile.config.ValueConfig;
import org.smile.ioc.bean.DefaultBeanFactory;
import org.smile.ioc.bean.XmlBeanBuilder;
import org.smile.ioc.scaner.IocBeanScanner;
import org.smile.ioc.test.bean.BeanConfigs2;
import org.smile.ioc.test.bean.TestBean;
import org.smile.ioc.test.bean.TestInteceptor;
import org.smile.ioc.test.bean.TestService;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
	BeanFactory factory=new DefaultBeanFactory();
	@Before
	public void init() throws BeanException {
		BeanConfig beanConfig=new BeanConfig();
		beanConfig.setClazz(IocBeanScanner.class.getName());
		ListConfig list=new ListConfig();
		list.setName("packageString");
		ValueConfig v=new ValueConfig();
		v.setValue("org.smile.ioc.test.bean");
		list.setValue(CollectionUtils.linkedList(v));
		beanConfig.setList(CollectionUtils.linkedList(list));
		factory.registInterceptor(new TestInteceptor("你好"));
		factory.registInterceptor(new TestInteceptor("谢谢"));
		XmlBeanBuilder builder=new XmlBeanBuilder(beanConfig);
		builder.regsitToFactory(factory);
		factory.onBeanRegistry();
		factory.afterLoad();
	}
    @Test
    public void testService() throws BeanException
    {
        TestService service=factory.getBean(TestService.class);
        System.out.println(service.test());
    }
    @Test
    public void testScannerContext() throws BeanException {
    	ScannerIocContext context=new ScannerIocContext(CollectionUtils.hashSet("org.smile.ioc.test.bean"));
    	context.getBeanFactory().registInterceptor(new TestInteceptor("你好"));
    	context.getBeanFactory().registInterceptor(new TestInteceptor("谢谢"));
    	context.afterLoad();
    	TestBean testBean=(TestBean) context.getBean("testBean4");
    	testBean.test();
    	BeanConfigs2 config2=context.getBean(BeanConfigs2.class);
    	config2.testService();
    }
}
