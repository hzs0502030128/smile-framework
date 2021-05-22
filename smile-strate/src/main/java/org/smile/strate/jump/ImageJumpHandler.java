package org.smile.strate.jump;

import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.file.ContentType;
import org.smile.http.RequestUtils;
import org.smile.strate.ActionConstants;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;

public class ImageJumpHandler implements JumpHandler {
	
	private static final String DEFAULT_IMG_TYPE="jpg";

	@Override
	public void jump(DispatchContext context,  ResultConfig forward) throws StrateResultJumpException {
		try {
			Object jsonResult = BeanUtils.getValue(context.getAction(), ActionConstants.imageResultProperty);
			jump(jsonResult, context, forward);
		} catch (BeanException e) {
			throw new StrateResultJumpException(e);
		}
	}

	@Override
	public void jump(Object methodResult, DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		HttpServletResponse response=context.getResponse();
		response.setContentType(ContentType.getContextType(DEFAULT_IMG_TYPE));
		RequestUtils.setNoCache(response);
		if(methodResult instanceof RenderedImage){
			try {
				ImageIO.write((RenderedImage) methodResult, DEFAULT_IMG_TYPE, response.getOutputStream());
			} catch (IOException e) {
				throw new StrateResultJumpException(e);
			}
		}
	}
}
