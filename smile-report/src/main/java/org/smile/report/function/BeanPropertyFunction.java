package org.smile.report.function;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;
/**
 * bean属性获取函数
 * @author 胡真山
 *
 */
public class BeanPropertyFunction extends AbstractFunction{
	protected static BeanPropertyFunction instance=new BeanPropertyFunction();
	@Override
	public Object convert(Object oneData, String exp, Object expValue) {
		try {
			return BeanUtils.getExpValue(oneData, exp);
		} catch (BeanException e) {
			throw new SmileRunException("获取表达式"+exp+"值失败,root:"+oneData,e);
		}
	}
}
