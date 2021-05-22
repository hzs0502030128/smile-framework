package org.smile.strate.jump;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;

public class RedirectJumpHandler implements JumpHandler {

	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		// 重定向
		String resultUrl = forward.getValue().trim();
		try {
			redirect(resultUrl, context.getRequest(), context.getResponse());
		} catch (IOException e) {
			throw new StrateResultJumpException(e);
		}
	}

	@Override
	public void jump(Object methodResult,DispatchContext context,  ResultConfig forward) throws StrateResultJumpException {
		jump(context, forward);
	}

	/**
	 * 服务器重定向跳转
	 * @param url
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static final void redirect(String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (url.startsWith("http")) {
			response.sendRedirect(url);
		} else {
			response.sendRedirect(request.getContextPath() + url);
		}

	}
}
