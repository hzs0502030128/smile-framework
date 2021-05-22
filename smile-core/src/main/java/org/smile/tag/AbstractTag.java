package org.smile.tag;

import java.io.Writer;

import org.smile.tag.parser.TagFragment;

public abstract class AbstractTag<T extends TagContext> implements Tag {
	
	protected T tagContext;
	
	protected Tag parent;
	
	protected TagFragment tagFragment;

	@Override
	public void setTagContext(TagContext tagProcessorContext) {
		this.tagContext = (T) tagProcessorContext;
	}

	public T getTagContext() {
		return tagContext;
	}

	@Override
	public void setFragment(TagFragment fragment) {
		this.tagFragment=fragment;
	}

	@Override
	public void setParent(Tag tag) {
		this.parent=tag;
	}

	public void invokeBody() throws Exception {
		for(Fragment f:this.tagFragment.getSubFragment()){
			if(f.isTag()){
				((TagFragment)f).invoke(tagContext,this);
			}else{
				f.invoke(tagContext);
			}
		}
	}
	/**
	 * 调用标签体内容
	 * @param writer
	 * @throws Exception
	 */
	protected void invokeBody(Writer writer) throws Exception{
		Writer oldWriter=tagContext.getWriter();
		tagContext.setWriter(writer);
		invokeBody();
		tagContext.setWriter(oldWriter);
	}
	/**
	 * 是否存在标签体内容
	 * @return
	 */
	public boolean hasBody(){
		return this.tagFragment.getSubFragment().size()!=0;
	}

	@Override
	public Tag getParent() {
		return parent;
	}
	
	
}
