package org.smile.strate.jump;

import org.smile.strate.action.ActionElement;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.dispatch.StrateDispatcher;
/***
 * 跳转到其它方法的action跳转类型
 * @author 胡真山
 *
 */
public class MethodJumpHandler implements JumpHandler {

	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		// 重定向方法
		String method = forward.getValue().trim();
		ActionElement actionElement=context.getActionElement();
		ActionElement other=actionElement.getClassElement().getActionElementByMethod(method);
		if(other==null){
			throw new StrateResultJumpException("not exists a action method named "+method+" config in "+actionElement.getName()+"'s result "+forward.getName()+" type:"+forward.getType());
		}
		try {
			context.setActionElement(other);
			Object result=context.onOtherActionDo();
			if(result!=null && result instanceof String){
				StrateDispatcher.jumpResult(context,(String)result);
			}
		} catch (Exception e) {
			throw new StrateResultJumpException(e);
		}
	}

	@Override
	public void jump(Object methodResult,DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		jump(context, forward);
	}
}
