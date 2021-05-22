package org.smile.tag.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.smile.commons.StringBand;
import org.smile.tag.Fragment;
import org.smile.tag.State;
import org.smile.tag.parser.TagTokenizer.TokenHandler;
/**
 * 标签解析结果
 * @author 胡真山
 *
 */
public class ParseResult implements TokenHandler{
	/**
	 * 解析后的标签片断保存
	 */
	private List<Fragment> framents=new ArrayList<Fragment>();
	/**只是标签片断*/
	private Stack<TagFragment> tagStack=new Stack<TagFragment>();
	
	private State state;
	
	public ParseResult(State state){
		this.state=state;
	}
	
	public ParseResult(){
		this.state=State.getDefault();
	}
	

	public void process(TagInfo tag) throws IOException {
		
	}
	
	public Fragment currentFragment(){
		if(tagStack.isEmpty()){
			return null;
		}
		return tagStack.elementAt(tagStack.size() - 1);
	}

	@Override
	public boolean shouldProcessTag(String name) {
		return state.shouldProcessTag(name);
	}

	private void pushFragment(Fragment fragment){
		Fragment current=currentFragment();
		while(current!=null){
			if(current.isTag()){
				current.addSub(fragment);
				pushTagStack(fragment);
				return;
			}else{
				this.tagStack.pop();
				current=currentFragment();
			}
		}
		framents.add(fragment);
		pushTagStack(fragment);
	}
	/**
	 * 如果是标签片断会把标签片断放入至标签片断的栈中
	 * @param fragment
	 */
	private void pushTagStack(Fragment fragment){
		if(fragment.isTag()){
			tagStack.push((TagFragment)fragment);
		}
	}

	@Override
	public void tag(TagInfo tag) throws IOException {
		switch (tag.getType()) {
		case OPEN: { // <tag>
			pushFragment(new TagFragment(new CustomTagInfo(tag)));
			break;
		}
		case CLOSE: { // </tag>
			TagFragment fragment=this.tagStack.pop();
			if(!tag.getName().equals(fragment.getTagName())){
				throw new IOException(fragment.getTagName()+" end with "+tag.toString());
			}
			break;
		}
		case EMPTY: { // <tag/>
			pushFragment(new TagFragment(new CustomTagInfo(tag)));
			this.tagStack.pop();
			break;
		}
		default:
			break;
		}
	}


	@Override
	public void text(CharSequence text) throws IOException {
		pushFragment(new TextFragment(text));
	}


	@Override
	public void warning(String message, int line, int column) {
		
	}

	@Override
	public String toString() {
		StringBand str=new StringBand();
		for(Fragment f: framents){
			str.append(f);
		}
		return str.toString();
	}
	
	public List<Fragment> getResults(){
		return this.framents;
	}
}
