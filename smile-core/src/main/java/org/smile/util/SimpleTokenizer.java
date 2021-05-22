package org.smile.util;

public class SimpleTokenizer extends StringTokenizer{
	private String separator;
	private int start;
	private int end;
	private int mark;
	
	public SimpleTokenizer(String text,String separator){
		this.text=text;
		this.separator=separator;
	}
	
	@Override
	public boolean hasMoreTokens() {
		return end<text.length();
	}

	@Override
	public String nextToken() {
		int index=text.indexOf(separator, end);
		String result;
		if(index!=-1){
			result= text.substring(this.end,index);
			this.start=index;
			this.end=index+this.separator.length();
		}else{
			result=text.substring(this.end);
			this.end=this.text.length();
		}
		return result;
	}
	
	@Override
	public String nextToken(String separator){
		this.separator=separator;
		return nextToken();
	}

	@Override
	public void mark() {
		this.mark=this.start;
	}

	@Override
	public void reset() {
		this.start=this.mark;
		this.end=this.start+this.separator.length();
	}

	@Override
	public String separator() {
		return this.separator;
	}

	@Override
	public StringTokenizer skipBlank() {
		int index=text.indexOf(separator, end);
		while(index!=-1){
			if(this.end!=index){
				break;
			}
			this.start=index;
			this.end=index+this.separator.length();
			index=text.indexOf(separator, end);
		}
		return this;
	}

}
