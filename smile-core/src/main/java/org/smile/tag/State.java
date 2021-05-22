package org.smile.tag;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.beans.converter.BeanException;
import org.smile.collection.ThreadLocalMap;
import org.smile.commons.SmileRunException;
import org.smile.reflect.ClassTypeUtils;
import org.smile.tag.config.TagLibContext;
import org.smile.tag.parser.TagInfo;

public class State {
	/***默认的*/
	private static final Map<String,Class> DEFAULT_TYPES=new ConcurrentHashMap<String,Class>();

    private final ThreadLocalMap<TagInfo, Tag> tags=new ThreadLocalMap<TagInfo, Tag>();
    
    private final Map<String, Class> tagTypes;
    
    private static volatile State DEFAULT;
    
    static {
    	//默认的函数库
    	TagLibContext.getInstance().registTags(DEFAULT_TYPES, "c",TagLibContext.DEFAULT_CORE);
    }
    
    public State(){
    	this.tagTypes= new ConcurrentHashMap<String, Class>(DEFAULT_TYPES);
    }
    /**
     * 获取默认的单例
     * @return
     */
    public static State getDefault(){
    	if(DEFAULT==null){
    		synchronized (State.class) {
				if(DEFAULT!=null){
					return DEFAULT;
				}
				DEFAULT=new State();
			}
    	}
    	return DEFAULT;
    }
    /**
     * 注入一个标签类型
     * @param tagName 需要带上前缀
     * @param tagClass
     */
    public void registTag(String tagName,Class<Tag> tagClass){
    	this.tagTypes.put(tagName, tagClass);
    }
    /**
             * 注入一个标签库
     * @param prefix 前缀
     * @param labName 标签库名称
     */
    public void registTags(String prefix,String labName){
    	TagLibContext.getInstance().registTags(tagTypes,prefix,labName);
    }

    /**
     * 是否需要处理此标签名称
     * 当前状态是否注入了此标签名称的实现类
     * 有实现类则会把此标签名称当成标签处理，反之则当文本处理
     * @param tagName 解析出来的标签名称
     * @return
     */
    public boolean shouldProcessTag(String tagName) {
        return tagTypes.containsKey(tagName);
    }

    /**
     * 获取标签实例
     * @param tagInfo 以此为key 可以获取的是当前线程缓存的
     * @return
     */
    public Tag getTag(TagInfo tagInfo) {
    	Tag tag=tags.get(tagInfo);
    	if(tag==null){
    		Class<Tag> clazz= tagTypes.get(tagInfo.getName());
    		try {
				tag=ClassTypeUtils.newInstance(clazz);
			} catch (BeanException e) {
				throw new SmileRunException("tag instance error "+tagInfo.getName(),e);
			}
    		tags.put(tagInfo, tag);
    	}
        return tag;
    }
    /**
     * 往默认标签集合中注入一个标签类型
     * @param name 需带前缀的名称
     * @param tagClass 标签的实现类
     */
    public static <T  extends Tag> void defaultTags(String name,Class<T> tagClass){
    	DEFAULT_TYPES.put(name, tagClass);
    }

    /**
     * 往默认标签集合中注入一个标签库
     * @param prefix 标签库的前缀
     * @param labName 标签库的名称
     */
    public static <T  extends Tag> void defaultTagLab(String prefix,String labName){
    	TagLibContext.getInstance().registTags(DEFAULT_TYPES,prefix,labName);
    }
}
