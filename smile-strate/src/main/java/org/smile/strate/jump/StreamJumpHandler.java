package org.smile.strate.jump;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.BeanProperties;
import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.io.FileNameUtils;
import org.smile.io.IOUtils;
import org.smile.strate.Strate;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.util.StringUtils;

public class StreamJumpHandler implements JumpHandler {
	
	protected BeanProperties beanProperties = new BeanProperties(false);
	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		try {
			String fileName = (String) beanProperties.getFieldValue(context.getAction(), Strate.downloadNameParamName);
			Object download = beanProperties.getFieldValue(context.getAction(), Strate.downloadTargetParamName);
			if (download == null) {
				throw new StrateResultJumpException("not exists a property named " + Strate.downloadTargetParamName + " in " + context.getAction());
			}
			jump(download, fileName, context.getRequest(), context.getResponse());
		} catch (BeanException e) {
			throw new StrateResultJumpException(e);
		}
	}

	@Override
	public void jump(Object methodResult,DispatchContext context,  ResultConfig forward) throws StrateResultJumpException {
		String downloadName=null;
		Object download=methodResult;
		if(methodResult instanceof InputStream||methodResult instanceof File){
			downloadName=forward.getValue();
		}else{
			try{
				downloadName=(String) BeanUtils.getExpValue(methodResult,Strate.downloadNameParamName);
				download=BeanUtils.getExpValue(methodResult,Strate.downloadTargetParamName);
			}catch(BeanException e){
				throw new StrateResultJumpException(context.getActionElement().getName(),e);
			}
		}
		jump(download, downloadName, context.getRequest(), context.getResponse());
	}
	
	public void jump(Object download,String downloadName, HttpServletRequest request, HttpServletResponse response) throws StrateResultJumpException {
		if (StringUtils.isEmpty(downloadName) && download instanceof File) {
			downloadName = FileNameUtils.getName(((File) download).getName());
		}
		try {
			downloadName = new String(downloadName.getBytes(Strate.encoding), Strate.decoding);
		} catch (UnsupportedEncodingException e) {
			throw new StrateResultJumpException(e);
		}
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + downloadName);
		InputStream stream=null;
		try {
			if (download instanceof InputStream) {
				stream = (InputStream) download;
			} else if (download instanceof File) {
				stream = new FileInputStream((File) download);
			}
			IOUtils.copy(stream, response.getOutputStream());
		} catch (Exception e) {
			throw new StrateResultJumpException(e);
		}
	}

}
