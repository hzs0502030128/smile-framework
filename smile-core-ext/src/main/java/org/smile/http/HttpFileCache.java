package org.smile.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.file.ContentType;
import org.smile.file.ClassFileCache;

public class HttpFileCache extends ClassFileCache {
	/**
	 * 设置缓存时间
	 * @param response
	 * @param fileExt
	 * @param cacheTimes
	 */
	public static void setHeaderCache(HttpServletResponse response, String fileExt, long cacheTimes) {
		String contentType = ContentType.getContextType(fileExt);
		response.setContentType(contentType);
		// 本页面允许在浏览器端或缓存服务器中缓存，时限为20秒。
		// 20秒之内重新进入该页面的话不会进入该servlet的
		long time = System.currentTimeMillis();
		// Last-Modified:页面的最后生成时间
		response.setDateHeader("Last-Modified", time);
		// Expires:过时期限值
		response.setDateHeader("Expires", time + cacheTimes);
		// Cache-Control来控制页面的缓存与否,public:浏览器和缓存服务器都可以缓存页面信息；
		response.setHeader("Cache-Control", "public");
		// Pragma:设置页面是否缓存，为Pragma则缓存，no-cache则不缓存
		response.setHeader("Pragma", "Pragma");

		// 不允许浏览器端或缓存服务器缓存当前页面信息。
		/*
		 * response.setHeader( "Pragma", "no-cache" );
		 * response.setDateHeader("Expires", 0); response.addHeader(
		 * "Cache-Control", "no-cache" );//浏览器和缓存服务器都不应该缓存页面信息
		 * response.addHeader( "Cache-Control", "no-store"
		 * );//请求和响应的信息都不应该被存储在对方的磁盘系统中； response.addHeader( "Cache-Control",
		 * "must-revalidate" );
		 */// 于客户机的每次请求，代理服务器必须想服务器验证缓存是否过时；
	}

	/**
	 * 设置文件没有修改
	 * @param response
	 */
	public static void setHeaderNoModified(HttpServletResponse response) {
		// 不需要更新
		response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
	}

	/**
	 * 是否使用客户端的缓存
	 * @param req
	 * @param lastModified
	 * @return
	 */
	public boolean isUseCache(HttpServletRequest req, String pathName) {
		Long lastModified = cacheTimes.get(pathName);
		if (lastModified == null) {
			return false;
		}
		return isUseCache(req, lastModified);
	}
	
	/***
	 * 是否使用缓存
	 * @param req 
	 * @param lastModified 文件最后修改时时间
	 * @return
	 */
	public static boolean isUseCache(HttpServletRequest req,Long lastModified){
		String etag = "\"" + lastModified + '\"';
		long modifiedSince = -1;
		try {
			modifiedSince = req.getDateHeader(HEADER_IF_MODIFIED);
		} catch (RuntimeException ex) {
		}

		if (modifiedSince != -1) {
			modifiedSince -= modifiedSince % 1000;
		}
		String givenEtag = req.getHeader(HEADER_IF_NONE);
		if (givenEtag == null) {
			if (modifiedSince >= lastModified) {
				return true;
			}
			return false;
		}
		if (modifiedSince == -1) {
			if (!etag.equals(givenEtag)) {
				return true;
			}
			return false;
		}
		if (etag.equals(givenEtag) || modifiedSince >= lastModified) {
			return true;
		}
		return false;
	}
}
