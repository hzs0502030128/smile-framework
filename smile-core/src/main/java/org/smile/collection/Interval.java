package org.smile.collection;

/**
 * 数字区间
 * @author 胡真山
 *
 */
public class Interval<T extends Number>{
	/**
	 * 起始
	 */
	protected T start;
	/**
	 * 结束
	 */
	protected T end;
	
	protected boolean startEqual=true;
	
	protected boolean endEqual=true;
	
	public Interval(T start,T end){
		this.start=start;
		this.end=end;
	}
	
	public int getStartInt(){
		return start.intValue();
	}
	
	public int getEndInt(){
		return end.intValue();
	}
	
	
	public double getEndDouble(){
		return end.doubleValue();
	}
	
	public double getStartDouble(){
		return start.doubleValue();
	}

	public T getStart() {
		return start;
	}

	public void setStart(T start) {
		this.start = start;
	}

	public T getEnd() {
		return end;
	}

	public void setEnd(T end) {
		this.end = end;
	}

	public boolean isStartEqual() {
		return startEqual;
	}

	public void setStartEqual(boolean startEqual) {
		this.startEqual = startEqual;
	}

	public boolean isEndEqual() {
		return endEqual;
	}

	public void setEndEqual(boolean endEqual) {
		this.endEqual = endEqual;
	}
}
