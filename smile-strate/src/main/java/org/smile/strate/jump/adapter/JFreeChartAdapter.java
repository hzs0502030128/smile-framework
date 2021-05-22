package org.smile.strate.jump.adapter;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.strate.ActionConstants;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.jump.JumpHandler;
import org.smile.strate.jump.StrateResultJumpException;
/**
 * 用于对jfreechar的一个适配 
 * 当没有引入jfreechar包时不影响期它类型正常使用
 * @author 胡真山
 * @Date 2016年2月1日
 */
public class JFreeChartAdapter implements JumpHandler{

	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		try {
			Object jsonResult = BeanUtils.getValue(context.getAction(), ActionConstants.chartResultProperty);
			jump(jsonResult,context, forward);
		} catch (BeanException e) {
			throw new StrateResultJumpException(e);
		}
	}

	@Override
	public void jump(Object methodResult, DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		
	}
	
	 
	
}
