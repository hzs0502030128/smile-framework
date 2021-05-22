package org.smile.strate.jump;

import org.smile.log.LoggerHandler;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;

/**
 * action结果跳转处理器
 * 
 * @author 胡真山
 * @Date 2016年1月28日
 */
public interface JumpHandler extends LoggerHandler {

	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException;

	public void jump(Object methodResult, DispatchContext context, ResultConfig forward) throws StrateResultJumpException;
}
