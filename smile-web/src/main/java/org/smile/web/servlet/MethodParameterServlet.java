package org.smile.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.Smile;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;
/**
 * servlet基类
 * @author strive
 *
 */
public abstract class MethodParameterServlet extends HttpServlet implements LoggerHandler {
	
	protected String encode=Smile.ENCODE;
	/**
	 * 获取方法的参数名 
	 * 默认为method 即从request 获取 method 参数的值做为反射的方法名
	 */
	protected String methodParam="method";
	/**
	 * get提交
	 */
	protected static final Integer GET_TYPE=0;
	/**
	 * post提交 
	 */
	protected static final Integer POST_TYPE=1;
	/**
	 * get处理
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doService(request, response,GET_TYPE);
	}
	/**
	 * POST处理
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doService(request, response, POST_TYPE);
	}
	/**
	 * 服务处理，从客户端传来一个参数 method 
	 * 由method决定调用哪个方法
	 * @param request
	 * @param response
	 * @param do_type
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doService(HttpServletRequest request,HttpServletResponse response,int do_type) throws ServletException, IOException
	{
		String requestEncode=request.getCharacterEncoding();
		if(requestEncode==null){
			requestEncode=encode;
		}
		request.setCharacterEncoding(requestEncode);
		response.setCharacterEncoding(requestEncode);
		response.setContentType("text/html");
		//方法参数 
		String m=request.getParameter(this.methodParam);
		if(m==null||"".equals(m)){
			//默认执行execute方法，在没有指定method时
			m="execute";
		}
		try {
			Method method=getClass().getMethod(m,new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			String result=(String)method.invoke(this, new Object[]{request,response});
			if(StringUtils.isNotNull(result)){
				//跳转页面
				request.getRequestDispatcher(result).forward(request, response);
			}
		} catch (SecurityException e) {
			throw new ServletException("反射方法：["+this.getClass().getName()+"."+m+"]出错",e);
		} catch (NoSuchMethodException e) {
			throw new ServletException("反射方法：["+this.getClass().getName()+"."+m+"]出错,此方法没有定义",e);
		}catch(Exception e){
			throw new ServletException("servlet==>"+getClass().getName()+"方法"+m+"处理出错：",e);
		}
	}
	
	/**
	 * 服务器跳转
	 * @param url 跳转路径
	 * @throws ServletException
	 */
	protected void forward(HttpServletRequest request,HttpServletResponse response,String url) throws ServletException{
		try{
			request.getRequestDispatcher(url).forward(request, response);
		}catch(Exception e){
			throw new ServletException("服务器跳转 forward to "+url+"出现错误",e);
		}
	}
	
	/**
	 * 初始化参数
	 */
	public void init() throws ServletException {
		super.init();
		//方法参数名
		String methodStr=this.getInitParameter("methodParam");
		if(methodStr!=null){
			this.methodParam=methodStr;
		}
	}
	/**
	 * 重定向跳转
	 * @param url 重定向路径
	 * @throws IOException 
	 */
	protected void redirect(HttpServletResponse response,String url) throws IOException{
		response.sendRedirect(url);
	}
	/**
	 * 默认方法
	 * @param request
	 * @param response
	 */
	public abstract void execute(HttpServletRequest request,HttpServletResponse response) throws Exception;
}
