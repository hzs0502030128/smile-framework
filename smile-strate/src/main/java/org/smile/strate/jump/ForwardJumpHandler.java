package org.smile.strate.jump;

import javax.servlet.http.HttpServletResponse;

import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.jump.forward.ForwardRequestWrapper;
import org.smile.strate.jump.forward.MethodForwardRequestWrapper;

public class ForwardJumpHandler implements JumpHandler {

	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		forward(new ForwardRequestWrapper(context.getRequest(), context.getAction()), context.getResponse(), forward);
	}

	@Override
	public void jump(Object methodResult,DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		forward(new MethodForwardRequestWrapper(context.getRequest(), context.getAction(), methodResult), context.getResponse(), forward);
	}

	/**
	 * 服务器端跳转
	 * @param request
	 * @param response
	 * @param forward
	 * @throws StrateResultJumpException
	 */
	protected void forward(ForwardRequestWrapper request, HttpServletResponse response, ResultConfig forward) throws StrateResultJumpException {
		try {
			String url=forward.getValue().trim();
			request.getRequestDispatcher(url).forward(request, response);
		} catch (Exception e) {
			throw new StrateResultJumpException(e);
		}
	}
}
