package org.smile.collection;

import java.util.Iterator;

import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;

public class Loop implements Iterable<Integer>{
	/**起始索引*/
	protected final  int start;
	/**结束索引*/
	protected final  int end;
	/** 步长*/
	protected final int step;
	
	public Loop(Integer start,Integer end){
		this.start=start;
		this.end=end;
		this.step=1;
	}
	
	/**
	 * @param start 循环起始
	 * @param end 循环结束
	 * @param step 步长
	 */
	public Loop(int start,int end,int step){
		this.start=start;
		this.end=end;
		this.step=step;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new LoopIteratorValues();
	}
	
	private class LoopIteratorValues implements Iterator<Integer>{
		Integer current;
		@Override
		public boolean hasNext() {
			if(current==null){
				return true;
			}
			return current+step<=end;
		}

		@Override
		public Integer next() {
			if(current==null){
				current= start;
			}else{
				current=current+step;
				if(current>end){
					throw new SmileRunException("out of end "+end);
				}
			}
			return current;
		}

		@Override
		public void remove() {
			throw new NotImplementedException("not support this method ");
		}
	}

	/**
	 * 循环的起始
	 * @return
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 循环的结束值
	 * @return
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * 每次循环的步长
	 * @return
	 */
	public int getStep() {
		return step;
	}
}
