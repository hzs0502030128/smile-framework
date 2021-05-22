
package org.smile.commons;

import java.util.Iterator;

import org.smile.collection.Loop;

/**
 * Loop iterator that provides various counting data about the loop cycles.
 * Should be used as in <code>while</code> construct:
 *
 * <pre><code>
 * while (loopIterator.next()) {
 * 		// do something
 * }
 * </code></pre>
 */
public class LoopIterator extends Loop{

	protected final int modulus;

	protected boolean first;
	protected boolean last;
	protected int value;
	protected int count;
	protected final boolean looping;

	public LoopIterator(int start, int end) {
		this(start, end, 1, 2);
	}

	public LoopIterator(int start, int end, int step) {
		this(start, end, step,2);
	}

	public LoopIterator(int start, int end, int step, int modulus) {
		super(start, end, step);
		this.modulus=modulus;
		this.looping = step > 0 ? start <= end : start >= end;
	}

	/**
	 * Returns current item count (1-based).
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Returns current item index (0-based).
	 */
	public int getIndex() {
		return count - 1;
	}

	/**
	 * Returns current loop value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns <code>true</code> if current count is even.
	 */
	public boolean isEven() {
		return (count % 2) == 0;
	}

	/**
	 * Returns <code>true</code> if current count is odd.
	 */
	public boolean isOdd() {
		return (count % 2) == 1;
	}

	/**
	 * Calculates modulus of current count.
	 */
	public int modulus(int operand) {
		return count % operand;
	}

	/**
	 * Returns modulus of current count.
	 */
	public int getModulus() {
		return count % modulus;
	}

	/**
	 * Returns modulus value.
	 */
	public int getModulusValue() {
		return modulus;
	}

	/**
	 * Returns modulus of current index.
	 */
	public int getIndexModulus() {
		return (count - 1) % modulus;
	}

	/**
	 * Returns <code>true</code> if current item is the first one.
	 */
	public boolean isFirst() {
		return first;
	}

	/**
	 * Returns <code>true</code> if current item is the last one.
	 */
	public boolean isLast() {
		return last;
	}


	// ---------------------------------------------------------------- iterate

	/**
	 * Iterates to next value at the beginning of the loop.
	 */
	public boolean next() {
		if (looping == false) {
			return false;
		}
		if (last == true) {
			return false;
		}
		if (count == 0) {
			value = start;
			first = true;
		} else {
			value += step;
			first = false;
		}
		count++;
		last = isLastIteration(value + step);
		return true;
	}

	/**
	 * Resets the loop from the beginning.
	 */
	public void reset() {
		count = 0;
		last = false;
	}

	protected boolean isLastIteration(int value) {
		return step > 0 ? value > end : value < end;
	}


	@Override
	public String toString() {
		return looping ?
				value + ":" + count + ':' + (first ? 'F':'_') + ':' + (last ? 'L':'_') + ':' + getModulus()
				:
				"N.A.";
	}

	@Override
	public Iterator<Integer> iterator() {
		return new LoopIteratorValues();
	}

	/**
	 * 对值的迭代器
	 * @author 胡真山
	 *
	 */
	private class LoopIteratorValues implements Iterator<Integer>{
		@Override
		public boolean hasNext() {
			return looping&&!last;
		}

		@Override
		public Integer next() {
			if(LoopIterator.this.next()){
				return value;
			}
			return null;
		}

		@Override
		public void remove() {
			throw new NotImplementedException("not support this method ");
		}
	}
}
