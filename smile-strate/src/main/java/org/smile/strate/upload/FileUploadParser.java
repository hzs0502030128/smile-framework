package org.smile.strate.upload;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface FileUploadParser {
	/**
	 * 解析上传表单
	 * @param request
	 * @return
	 * @throws StrateUploadException
	 */
	public List<FieldItem> parseRequestItems(HttpServletRequest request) throws StrateUploadException;
	/**
	 * 当次上传最大大限制
	 * @param sizeMax
	 */
	public void setSizeMax(long sizeMax);
	/**
	 * 单个文件最大限制
	 * @param sizeMax
	 */
	public void setFileSizeMax(long sizeMax);
}
