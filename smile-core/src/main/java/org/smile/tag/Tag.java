package org.smile.tag;

import java.io.IOException;

import org.smile.tag.parser.TagFragment;

public interface Tag {
	/**
	 * 设置执行的上下文
	 * @param context
	 */
    void setTagContext(TagContext context);
    /**
     * 处理标签
     * @param tag
     * @throws IOException
     */
    void process() throws Exception;
    /**'
     * 设置当前标签的解析片断
     * @param fragment
     */
    void setFragment(TagFragment fragment);
    /**
     * 设置父标签
     * @param tag
     */
    void setParent(Tag tag);
    /**
     * 获取父标签
     * @return
     */
    Tag getParent();
}
