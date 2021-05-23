package org.smile.tag;


/**
 * 标签的解析片断
 * 可能是标签 广本
 *
 */
public interface Fragment {
	/**
	 * 添加子片断
	 * @param sub
	 */
	public void addSub(Fragment sub);

	/**
	 * 执行此片断
	 * @param tagContext
	 * @throws Exception
	 */
	public void invoke(TagContext tagContext) throws Exception;

	/**
	 * 是否是标签
	 * @return
	 */
	public boolean isTag();
	
}
