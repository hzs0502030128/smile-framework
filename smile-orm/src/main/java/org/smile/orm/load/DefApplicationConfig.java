package org.smile.orm.load;


public class DefApplicationConfig extends ApplicationConfig{
	
	private static DefApplicationConfig instance=new DefApplicationConfig();
	/**
	 * 	设置默认应用
	 * @param application
	 */
	public void setDefault(Application application){
		addApplication(DEFAULT, application);
	}
	
	public static DefApplicationConfig getInstance(){
		return instance;
	}
	
}
