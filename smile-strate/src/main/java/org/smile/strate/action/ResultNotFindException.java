package org.smile.strate.action;

import org.smile.strate.jump.StrateResultJumpException;

/**
 * 没有配置返回结果时抛出的异常
 * @author 胡真山
 * @Date 2016年1月18日
 */
public class ResultNotFindException extends StrateResultJumpException {
	public ResultNotFindException(String msg){
		super(msg);
	}
}
