package org.smile.tag;

import java.io.Writer;


public interface TagContext {

	/**
	 * 当前的解析状态
	 * @return
	 */
    State currentState();

    /**
     * 变更解析状态
     * @param newState
     */
    void changeState(State newState);

    /**
     * 获取写入对象
     * @return
     */
    Writer getWriter();
	
	void setTagEngine(TagEngine engin);
	
	TagEngine getTagEngine();
	/**
	 * 修改写入对象
	 * @param writer
	 */
	void setWriter(Writer writer);

}

