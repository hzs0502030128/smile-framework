package org.smile.ioc.aware;

public interface InitializingBean {
	/**在初始化完成后调用*/
	public void afterInit();
}
