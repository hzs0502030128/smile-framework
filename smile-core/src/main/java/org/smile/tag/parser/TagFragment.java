package org.smile.tag.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smile.commons.SmileRunException;
import org.smile.commons.StringBand;
import org.smile.tag.Fragment;
import org.smile.tag.Tag;
import org.smile.tag.TagContext;
import org.smile.tag.parser.TagInfo.Type;

public class TagFragment implements Fragment{
	
	CustomTagInfo tagInfo;
	/**子片断*/
	List<Fragment> subList=new ArrayList<Fragment>();
	
	public TagFragment(CustomTagInfo tagInfo){
		this.tagInfo=tagInfo;
	}

	@Override
	public void addSub(Fragment sub) {
		this.subList.add(sub);
	}

	@Override
	public void invoke(TagContext tagContext) throws Exception {
		invoke(tagContext, null);
	}
	
	public void invoke(TagContext tagContext,Tag parent) throws Exception {
		Tag tag=createTag(tagContext, parent);
		tag.process();
		tag.setParent(null);
	}
	/**
	 * 创建标签实例 有可能是当前线程缓存的实例
	 * @param tagContext
	 * @param parent
	 * @return
	 */
	public Tag createTag(TagContext tagContext,Tag parent){
		Tag tag=tagContext.currentState().getTag(tagInfo);
		tag.setTagContext(tagContext);
		tag.setParent(parent);
		tag.setFragment(this);
		return tag;
	}
	
	
	@Override
	public boolean isTag() {
		return true;
	}

	@Override
	public String toString() {
		StringBand str=new StringBand();
		this.tagInfo.writeTo(str);
		for(Fragment f:subList){
			str.append(f);
		}
		if(tagInfo.getType()==Type.OPEN){
			writeEnd(str);
		}
		return str.toString();
	}

	/**
	 * 输出标签
	 * @param writer
	 */
	public void writeTo(Appendable writer){
		this.tagInfo.writeTo(writer);
	}

	/**
	 * 输出结束标签
	 * @param writer
	 * @throws IOException
	 */
	public void writeEnd(Appendable writer) {
		try {
			writer.append("</"+this.tagInfo.getName()+">");
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}

	public List<Fragment> getSubFragment() {
		return this.subList;
	}
	
	public String getTagName(){
		return this.tagInfo.getName();
	}
	
	public TagInfo getTagInfo(){
		return tagInfo;
	}
	
}
