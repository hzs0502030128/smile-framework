package org.smile.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.smile.commons.MimeTypes;
import org.smile.commons.Strings;
import org.smile.io.FileNameUtils;
import org.smile.io.IOUtils;
import org.smile.util.Base64;

/**
 * servlet 工具类
 * @author 胡真山
 * 2015年11月25日
 */
public class ServletUtils {
	
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String TYPE_MULTIPART_FORM_DATA = "multipart/form-data";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	private static final String JAVAX_SERVLET_ERROR_EXCEPTION = "javax.servlet.error.exception";

	private static final String SCOPE_APPLICATION = "application";
	private static final String SCOPE_SESSION = "session";
	private static final String SCOPE_REQUEST = "request";
	private static final String SCOPE_PAGE = "page";

	/**
	 * 是否是多媒体的请求 
	 * @param request
	 * @return
	 */
	public static boolean isMultipartRequest(HttpServletRequest request) {
		String type = request.getHeader(HEADER_CONTENT_TYPE);
		return (type != null) && type.startsWith(TYPE_MULTIPART_FORM_DATA);
	}

	
	public static boolean isGzipSupported(HttpServletRequest request) {
		String browserEncodings = request.getHeader(HEADER_ACCEPT_ENCODING);
		return (browserEncodings != null) && (browserEncodings.contains("gzip"));
	}

	// ---------------------------------------------------------------- authorization
	/**
	 * Decodes the "Authorization" header and retrieves the
	 * user's name from it.  Returns <code>null</code> if the header is not present.
	 */
	public static String getAuthUsername(HttpServletRequest request) {
		String header = request.getHeader(HEADER_AUTHORIZATION);
		if (header == null) {
			return null;
		}
		String encoded = header.substring(header.indexOf(' ') + 1);
		String decoded = new String(Base64.decodeUTF(encoded));
		return decoded.substring(0, decoded.indexOf(':'));
	}

	/**
	 * Decodes the "Authorization" header and retrieves the
	 * password from it. Returns <code>null</code> if the header is not present.
	 */
	public static String getAuthPassword(HttpServletRequest request) {
		String header = request.getHeader(HEADER_AUTHORIZATION);
		if (header == null) {
			return null;
		}
		String encoded = header.substring(header.indexOf(' ') + 1);
		String decoded = new String(Base64.decodeUTF(encoded));
		return decoded.substring(decoded.indexOf(':') + 1);
	}

	/**
	 * Sends correct headers to require basic authentication for the given realm.
	 */
	public static void requireAuthentication(HttpServletResponse resp, String realm) throws IOException {
		resp.setHeader(WWW_AUTHENTICATE, "Basic realm=\"" + realm + '\"');
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	// ---------------------------------------------------------------- download and content disposition

	/**
	 * Prepares response for file download. Mime type and size is resolved from the file.
	 */
	public static void prepareDownload(HttpServletResponse response, File file) {
		prepareDownload(response, file, null);
	}

	/**
	 * Prepares response for file download with provided mime type.
	 */
	public static void prepareDownload(HttpServletResponse response, File file, String mimeType) {
		if (file.exists() == false) {
			throw new IllegalArgumentException("File not found: " + file);
		}
		if (file.length() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("File too big: " + file);
		}
		prepareResponse(response, file.getAbsolutePath(), mimeType, (int) file.length());
	}

	/**
	 * Prepares response for various provided data.
	 *
	 * @param response http response
	 * @param fileName file name, if full path then file name will be stripped, if null, will be ignored.
	 * @param mimeType mime type with optional charset, may be <code>null</code>
	 * @param fileSize if less then 0 it will be ignored
	 */
	public static void prepareResponse(HttpServletResponse response, String fileName, String mimeType, int fileSize) {
		if ((mimeType == null) && (fileName != null)) {
			String extension = FileNameUtils.getExtension(fileName);
			mimeType = MimeTypes.getMimeType(extension);
		}

		if (mimeType != null) {
			response.setContentType(mimeType);
		}

		if (fileSize >= 0) {
			response.setContentLength(fileSize);
		}

		if (fileName != null) {
			String name = FileNameUtils.getName(fileName);
			response.setHeader(CONTENT_DISPOSITION, "attachment;filename=\"" + name + '\"');
		}
	}

	// ---------------------------------------------------------------- cookie

	/**
	 * Finds and returns cookie from client by its name.
	 * Only the first cookie is returned.
	 * @see #getAllCookies(javax.servlet.http.HttpServletRequest, String)
	 * @return cookie value or <code>null</code> if cookie with specified name doesn't exist.
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * Returns all cookies from client that matches provided name.
	 * @see #getCookie(javax.servlet.http.HttpServletRequest, String) 
	 */
	public static Cookie[] getAllCookies(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		ArrayList<Cookie> list = new ArrayList<Cookie>(cookies.length);
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				list.add(cookie);
			}
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.toArray(new Cookie[list.size()]);
	}

	// ---------------------------------------------------------------- request body

	/**
	 * Reads HTTP request body. Useful only with POST requests. Once body is read,
	 * it cannot be read again!
	 */
	public static String readRequestBody(HttpServletRequest request) throws IOException {
		BufferedReader buff = request.getReader();
		return IOUtils.readString(buff);
	}


	// ---------------------------------------------------------------- context path

	/**
	 * Returns correct context path, as by Servlet definition. Different
	 * application servers return all variants: "", null, "/".
	 * <p>
	 * The context path always comes first in a request URI. The path
	 * starts with a "/" character but does not end with a "/" character.
	 * For servlets in the default (root) context, this method returns "".
	 */
	public static String getContextPath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		if (contextPath == null || contextPath.equals(Strings.SLASH)) {
			contextPath = Strings.EMPTY;
		}
		return contextPath;
	}

	/**
	 * Returns correct context path, as by Servlet definition. Different
	 * application servers return all variants: "", null, "/".
	 * <p>
	 * The context path always comes first in a request URI. The path
	 * starts with a "/" character but does not end with a "/" character.
	 * For servlets in the default (root) context, this method returns "".
	 */
	public static String getContextPath(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		if (contextPath == null || contextPath.equals(Strings.SLASH)) {
			contextPath = Strings.EMPTY;
		}
		return contextPath;
	}

	/**
	 * @see #getContextPath(javax.servlet.ServletContext)
	 */
	public static String getContextPath(PageContext pageContext) {
		return getContextPath(pageContext.getServletContext());
	}

	/**
	 * Stores context path in server context and request scope.
	 */
	public static void storeContextPath(PageContext pageContext, String contextPathVariableName) {
		String ctxPath = getContextPath(pageContext);

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		request.setAttribute(contextPathVariableName, ctxPath);

		ServletContext servletContext = pageContext.getServletContext();
		servletContext.setAttribute(contextPathVariableName, ctxPath);
	}

	/**
	 * Stores context path in page context and request scope.
	 */
	public static void storeContextPath(ServletContext servletContext, String contextPathVariableName) {
		String ctxPath = getContextPath(servletContext);

		servletContext.setAttribute(contextPathVariableName, ctxPath);
	}

	// ---------------------------------------------------------------- attributes and values

	/**
	 * Returns non-<code>null</code> attribute value. Scopes are examined in the
	 * following order: page, request, session, application.
	 */
	public static Object attribute(PageContext pageContext, String name) {
		Object value = pageContext.getAttribute(name);
		if (value != null) {
			return value;
		}
		return attribute((HttpServletRequest) pageContext.getRequest(), name);
	}
	/**
	 * Returns non-<code>null</code> attribute value. Scopes are examined in the
	 * following order: request, session, application.
	 */
	public static Object attribute(HttpServletRequest request, String name) {
		Object value = request.getAttribute(name);
		if (value != null) {
			return value;
		}
		value = request.getSession().getAttribute(name);
		if (value != null) {
			return value;
		}
		return request.getSession().getServletContext().getAttribute(name);
	}


	/**
	 * Sets scope attribute.
	 */
	public static void setScopeAttribute(String name, Object value, String scope, PageContext pageContext) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String scopeValue = scope != null ? scope.toLowerCase() : SCOPE_PAGE;
		if (scopeValue.equals(SCOPE_PAGE)) {
			pageContext.setAttribute(name, value);
		}
		else if (scopeValue.equals(SCOPE_REQUEST)) {
			request.setAttribute(name, value);
		}
		else if (scopeValue.equals(SCOPE_SESSION)) {
			request.getSession().setAttribute(name, value);
		}
		else if (scopeValue.equals(SCOPE_APPLICATION)) {
            request.getSession().getServletContext().setAttribute(name, value);
        }
		else {
			throw new IllegalArgumentException("Invalid scope: " + scope);
        }
	}

	/**
	 * Removes scope attribute.
	 */
	public static void removeScopeAttribute(String name, String scope, PageContext pageContext) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String scopeValue = scope != null ? scope.toLowerCase() : SCOPE_PAGE;
		if (scopeValue.equals(SCOPE_PAGE)) {
			pageContext.removeAttribute(name);
		}
		else if (scopeValue.equals(SCOPE_REQUEST)) {
			request.removeAttribute(name);
		}
		else if (scopeValue.equals(SCOPE_SESSION)) {
			request.getSession().removeAttribute(name);
		}
		else if (scopeValue.equals(SCOPE_APPLICATION)) {
            request.getSession().getServletContext().removeAttribute(name);
        }
		else {
			throw new IllegalArgumentException("Invalid scope: " + scope);
        }
	}

	// ---------------------------------------------------------------- resolve URL

	/**
	 * Valid characters in a scheme, as specified by RFC 1738.
	 */
	public static final String VALID_SCHEME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+.-";

	/**
     * Returns <code>true</code> if current URL is absolute, <code>false</code> otherwise.
     */
    public static boolean isAbsoluteUrl(String url) {
	    if (url == null) {      	    // a null URL is not absolute
		    return false;
	    }
	    int colonPos;                   // fast simple check first
	    if ((colonPos = url.indexOf(':')) == -1) {
		    return false;
	    }

	    // if we DO have a colon, make sure that every character
	    // leading up to it is a valid scheme character
	    for (int i = 0; i < colonPos; i++) {
		    if (VALID_SCHEME_CHARS.indexOf(url.charAt(i)) == -1) {
			    return false;
		    }
	    }
	    return true;
    }

	/**
	 * Strips a servlet session ID from <code>url</code>.  The session ID
	 * is encoded as a URL "path parameter" beginning with "jsessionid=".
	 * We thus remove anything we find between ";jsessionid=" (inclusive)
	 * and either EOS or a subsequent ';' (exclusive).
	 */
	public static String stripSessionId(String url) {
		StringBuilder u = new StringBuilder(url);
		int sessionStart;
		while ((sessionStart = u.toString().indexOf(";jsessionid=")) != -1) {
			int sessionEnd = u.toString().indexOf(';', sessionStart + 1);
			if (sessionEnd == -1) {
				sessionEnd = u.toString().indexOf('?', sessionStart + 1);
			}
			if (sessionEnd == -1) {
				sessionEnd = u.length();
			}
			u.delete(sessionStart, sessionEnd);
		}
		return u.toString();
	}

	public static String resolveUrl(String url, HttpServletRequest request) {
		if (isAbsoluteUrl(url)) {
			return url;
		}
		if (url.startsWith(Strings.SLASH)) {
			return getContextPath(request) + url;
		} else {
			return url;
		}
	}

	public static String resolveUrl(String url, String context) {
		if (isAbsoluteUrl(url)) {
			return url;
		}
		if (!context.startsWith(Strings.SLASH) || !url.startsWith(Strings.SLASH)) {
			throw new IllegalArgumentException("Values of both 'context' and 'url' must start with '/'.");
		}
		if (context.equals(Strings.SLASH)) {
			return url;
		} else {
			return (context + url);
		}
	}

	// ---------------------------------------------------------------- params

	/**
	 * Returns HTTP request parameter as String or String[].
	 */
	public static Object getRequestParameter(ServletRequest request, String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		if (values.length == 1) {
			return values[0];
		}
		return values;
	}

	

	/**
	 * Prepares parameters for further processing.
	 * @param paramValues	string array of param values
	 * @param trimParams	trim parameters
	 * @param treatEmptyParamsAsNull	empty parameters should be treated as <code>null</code>
	 * @param ignoreEmptyRequestParams	if all parameters are empty, return <code>null</code>
	 */
	public static String[] prepareParameters(
			String[] paramValues,
			boolean trimParams,
			boolean treatEmptyParamsAsNull,
			boolean ignoreEmptyRequestParams) {

		if (trimParams || treatEmptyParamsAsNull || ignoreEmptyRequestParams) {
			int emptyCount = 0;
			int total = paramValues.length;
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				if (paramValue == null) {
					emptyCount++;
					continue;
				}
				if (trimParams) {
					paramValue = paramValue.trim();
				}
				if (paramValue.length() == 0) {
					emptyCount++;
					if (treatEmptyParamsAsNull) {
						paramValue = null;
					}
				}
				paramValues[i] = paramValue;
			}
			if ((ignoreEmptyRequestParams == true) && (emptyCount == total)) {
				return null;
			}
		}
		return paramValues;
	}

	// ---------------------------------------------------------------- version

	private static boolean isVersion2_5;

	static {
		try {
			ServletContext.class.getMethod("getContextPath");
			isVersion2_5 = true;
		} catch (Exception ignore) {
		}
	}

	/**
	 * Returns <code>true</code> if current servlets version is 2.5 or higher.
	 */
	public static boolean isServletsVersion2_5() {
		return isVersion2_5;
	}

	// ---------------------------------------------------------------- errors

	/**
	 * Returns servlet error.
	 */
	public static Throwable getServletError(ServletRequest request) {
		return (Throwable) request.getAttribute(JAVAX_SERVLET_ERROR_EXCEPTION);
	}

	/**
	 * Sets servlet error.
	 */
	public static void setServletError(ServletRequest request, Throwable throwable) {
		request.setAttribute(JAVAX_SERVLET_ERROR_EXCEPTION, throwable);
	}


	// ---------------------------------------------------------------- debug

	/**
	 * Returns a string with debug info from all servlet objects.
	 * @see #debug(HttpServletRequest, PageContext)
	 */
	public static String debug(HttpServletRequest request) {
		return debug(request,  null);
	}
	/**
	 * Returns a string with debug info from all servlet objects.
	 * @see #debug(HttpServletRequest, PageContext)
	 */
	public static String debug(PageContext pageContext) {
		return debug((HttpServletRequest) pageContext.getRequest(),  pageContext);
	}

	/**
	 * Returns a string with debug info from all servlet objects, including the page context.
	 */
	protected static String debug(HttpServletRequest request, PageContext pageContext) {
		StringBuilder result = new StringBuilder();
		result.append("\nPARAMETERS\n----------\n");
		Enumeration enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			Object[] value = request.getParameterValues(name);
			result.append(name).append('=');
			if (value == null) {
				result.append("<null>");
			} else if (value.length == 1) {
				result.append(value[0]).append('\n');
			} else {
				result.append('[');
				for (int i = 0, valueLength = value.length; i < valueLength; i++) {
					if (i == 0) {
						result.append(',');
					}
					result.append(value[i]);
				}
				result.append("]\n");
			}
		}

		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		loop:
		for (int i = 0; i < 4; i++) {
			switch (i) {
				case 0: result.append("\nREQUEST\n-------\n");
						enumeration = request.getAttributeNames();
						break;
				case 1: result.append("\nSESSION\n-------\n");
						enumeration = session.getAttributeNames();
						break;
				case 2: result.append("\nAPPLICATION\n-----------\n");
						enumeration = context.getAttributeNames();
						break;
				case 3:	if (pageContext == null) {
							break loop;
						}
						result.append("\nPAGE\n----\n");
						enumeration = pageContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE);
			}
			while (enumeration.hasMoreElements()) {
				String name = (String) enumeration.nextElement();
				Object value = null;
				switch (i) {
					case 0: value = request.getAttribute(name); break;
					case 1: value = session.getAttribute(name); break;
					case 2: value = context.getAttribute(name); break;
					case 3: value = pageContext.getAttribute(name); break;
				}
				result.append(name).append('=');
				if (value == null) {
					result.append("<null>\n");
				} else {
					String stringValue;
					try {
						stringValue = value.toString();
					} catch (Exception ignore) {
						stringValue = "<" + value.getClass() + ">\n";
					}
					result.append(stringValue).append('\n');
				}
			}
		}
		return result.toString();
	}

	// ---------------------------------------------------------------- cache

	/**
	 * Prevents HTTP cache.
	 */
	public static void preventCaching(HttpServletResponse response) {
		response.setHeader("Cache-Control", "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0");  // HTTP 1.1
		response.setHeader("Pragma","no-cache");        // HTTP 1.0
		response.setDateHeader ("Expires", 0);          // prevents caching at the proxy server
	}


}
