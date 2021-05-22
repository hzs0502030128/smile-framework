package org.smile.function;

import org.smile.commons.SmileRunException;
/**
 * 需要检查函数参数个数的函数
 * @author 胡真山
 *
 */
public abstract class CheckArgsCountFunction implements Function{
	/**
	 * 检查参数
	 * @param args
	 */
	protected void checkArgs(Object... args){
		if(args.length!=getSupportArgsCount()){
			throw new SmileRunException(getName()+" only "+getSupportArgsCount()+" arguments are supported, but "+args.length+" are specified");
		}
	}

	@Override
	public int getSupportArgsCount() {
		return -1;
	}
	
	
}
