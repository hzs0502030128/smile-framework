package org.smile.strate.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.commons.SmileRunException;
import org.smile.io.IOUtils;
import org.smile.json.JSONAware;
import org.smile.json.JSONValue;
import org.smile.strate.Strate;
import org.smile.strate.StrateException;
import org.smile.strate.i18n.I18nResource;
import org.smile.strate.i18n.I18nSupport;
import org.smile.strate.jump.RedirectJumpHandler;
import org.smile.strate.jump.forward.ForwardRequestWrapper;
import org.smile.strate.upload.UploadLimit;

/**
 * Action 的父类 所有的action必须继承此类
 * 
 * @author strive
 *
 */
public abstract class AbstractActionSupport implements Action, I18nSupport {
	/**
	 * 获取参数值
	 * 
	 * @param paramName
	 * @return
	 */
	protected String getParamter(String paramName) {
		return request().getParameter(paramName);
	}

	/**
	 * 获取参数值 编码是ISO8859-1的参数 常用于GET提交时
	 * 
	 * @param paramName
	 * @return
	 */
	protected String getParamterISO88591(String paramName) {
		try {
			return new String(request().getParameter(paramName).getBytes("ISO-8859-1"), Strate.encoding);
		} catch (UnsupportedEncodingException e) {
			return request().getParameter(paramName);
		}
	}

	/**
	 * 打印一个字符串到response中
	 * 
	 * @param str
	 * @throws IOException
	 */
	protected void printString(String str) throws IOException {
		HttpServletResponse response=response();
		response.setCharacterEncoding(Strate.encoding);
		response.setContentType("text/html");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(str);
		} finally {
			pw.close();
		}
	}

	/**
	 * 服务器跳转
	 * 
	 * @param url 跳转路径
	 * @throws ServletException
	 */
	protected void forward(String url) throws StrateException {
		try {
			HttpServletResponse response=response();
			HttpServletRequest request=request();
			request.getRequestDispatcher(url).forward(new ForwardRequestWrapper(request, this), response);
		} catch (Exception e) {
			throw new StrateException("服务器跳转 forward to " + url + "出现错误", e);
		}
	}
	/**
	 * 重定向跳转
	 * 
	 * @param url 重定向路径
	 * @throws IOException
	 */
	protected void redirect(String url) throws IOException {
		RedirectJumpHandler.redirect(url,request(), response());
	}

	@Override
	public String getText(String key) {
		return I18nResource.message.getStringKeyDefault(locale(), key);
	}

	@Override
	public String getText(Locale local, String key) {
		return I18nResource.message.getStringKeyDefault(local, key);
	}

	@Override
	public UploadLimit uploadLimit() {
		return null;
	}

	@Override
	public boolean validate() throws Exception {
		return true;
	}

	@Override
	public void addValidateError(String name, String msg) {
		this.addActionError(name, msg);
	}

	@Override
	public boolean needRequestToAction() {
		return true;
	}

	@Override
	public Object getTarget() {
		return this;
	} 
	
	protected HttpServletRequest getRequest() {
		return request();
	}
	
	protected HttpServletResponse getResponse() {
		return response();
	}
	
	/**
	 * 获取request的输入流
	 * @return
	 * @throws IOException
	 */
	protected InputStream requestInputStream() throws IOException{
		return request().getInputStream();
	}
	/**
	 * 读取request输入流成一个字符串
	 * @return
	 * @throws IOException
	 */
	protected String readRequestInputStream() throws IOException{
		return IOUtils.readString(new InputStreamReader(request().getInputStream(),Strate.encoding));
	}
	/**
	 * 从request输入流中读取内容到字符串中
	 * @param charset 读取字符中的字符集编码
	 * @return
	 * @throws IOException
	 */
	protected String readRequestInputStream(String charset) throws IOException{
		return IOUtils.readString(new InputStreamReader(requestInputStream(),charset));
	}
	
	/**
	 * 读取请求体内容
	 * @return
	 */
	protected String requestBody(){
		try {
			return IOUtils.readString(new InputStreamReader(request().getInputStream(),Strate.encoding));
		} catch (Exception e) {
			throw new SmileRunException("read request body error ",e);
		}
	}
	/**
	 * 从request中读取一个json对象
	 * @return
	 */
	protected <T extends JSONAware> T requestJsonBody(){
		try {
			String json= IOUtils.readString(new InputStreamReader(request().getInputStream(),Strate.encoding));
			return (T)JSONValue.parse(json);
		} catch (Exception e) {
			throw new SmileRunException("read request body error ",e);
		}
	}
	
	/**
	 * 获取response的输入流
	 * @return
	 * @throws IOException
	 */
	protected OutputStream responseOutputStream() throws IOException{
		return response().getOutputStream();
	}
	/**
	 * 当前action的错误信息
	 * @return
	 */
	public abstract Map<String, String> getActionErrors();

	/**
	 * 获取当前action的提示消息
	 * @return
	 */
	public abstract Map<String, String> getActionMessages();
	
}
