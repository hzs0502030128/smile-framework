package org.smile.strate.jump;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.file.ContentType;
import org.smile.http.RequestUtils;
import org.smile.json.JSON;
import org.smile.strate.ActionConstants;
import org.smile.strate.Strate;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;

public class JsonJumpHandler implements JumpHandler {

	@Override
	public void jump(DispatchContext context,  ResultConfig forward) throws StrateResultJumpException {
		try {
			Object jsonResult = BeanUtils.getValue(context.getAction(), ActionConstants.jsonResultProperty);
			jump(jsonResult,context, forward);
		} catch (BeanException e) {
			throw new StrateResultJumpException(e);
		}
	}

	@Override
	public void jump(Object methodResult,DispatchContext context,  ResultConfig forward) throws StrateResultJumpException {
		HttpServletResponse response=context.getResponse();
		response.setContentType(ContentType.getContextType("json") + ";charset=" + Strate.encoding);
		RequestUtils.setNoCache(response);
		String json =null;
		if(methodResult instanceof String){
			json=(String)methodResult;
		}else{
			json=JSON.toJSONString(methodResult);
		}
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			throw new StrateResultJumpException(e);
		}
	}

}
