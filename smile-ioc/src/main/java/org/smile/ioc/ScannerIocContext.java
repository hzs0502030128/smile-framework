package org.smile.ioc;

import java.util.ArrayList;
import java.util.Set;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.scaner.IocBeanScanner;

public class ScannerIocContext extends ClassPathIocContext{
	
	public ScannerIocContext(Set<String> packages) {
		try {
			IocBeanScanner scanner=new IocBeanScanner();
			scanner.setPackageString(new ArrayList<String>(packages));
			scanner.processBeanRegistry(beanFactory);
		} catch (BeanException e) {
			throw new IocInitException(e);
		}
	}
}
