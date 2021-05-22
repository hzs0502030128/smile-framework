package org.smile.tag;



public interface Fragment {
	/**
	 * 添加子片断
	 * @param sub
	 */
	public void addSub(Fragment sub);
	
	public void invoke(TagContext tagContext) throws Exception;
	
	public boolean isTag();
	
}
