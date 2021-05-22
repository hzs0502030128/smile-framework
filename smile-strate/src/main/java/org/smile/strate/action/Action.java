package org.smile.strate.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.http.HttpMethod;
import org.smile.http.RequestAndResponseAware;
import org.smile.log.LoggerHandler;
import org.smile.strate.upload.UploadLimit;
import org.smile.validate.ValidateSupport;
/**
 * action 接口
 * @author strive
 *
 */
public interface Action extends LoggerHandler,ValidateSupport,RequestAndResponseAware{
	/**成功时返回结果*/
	public static final String SUCCESS = "success";
	/**失败时返回结果*/
	public static final String FAIL = "fail";
	/**系统在验证没有通过时会返回结果*/
	public static final String INPUT = "input";
	/**预定义错误返回名称*/
	public static final String ERROR="error";
	/**预定义登录返回名称*/
	public static final String LOGIN="login";
	/**预定义异常反回界面*/
	public static final String EXCEPTION="exception";
	
	public static final String NOPERMISSION="permission";
	
	public static final String ACTION_ERROR="actionErrors";
	
	public static final String ACTION_MESSAGE="actionMessages";	
	/**
	 * 初始化信息
	 * @param request
	 * @param response
	 * @param type
	 * @return 如果是可以动态支持方法调用那么
	 * 		这里可以返回方法名   
	 * @throws Exception
	 */
	public void initPerterties(HttpServletRequest request, HttpServletResponse response, HttpMethod type) throws Exception;
	/**
	 * 获取当前的request
	 * @return
	 */
	public abstract HttpServletRequest request();
	/**
	 * 获取当前的response
	 * @return
	 */
	public abstract HttpServletResponse response();
	
	/**上传文件大小限制*/
	public UploadLimit uploadLimit();
	
	/**
	 * 添加错误信息
	 * 
	 * @param error
	 */
	public  void addActionError(String error) ;
	/**
	 * 添加提示信息
	 * 
	 * @param msg
	 */
	public  void addActionMessage(String msg);
	/**
	 * 获取客户端方言
	 * @return
	 */
	public Locale locale();
	/**
	 * 添加消息
	 * @param key 消息名
	 * @param msg 消息内容
	 */
	public void addActionMessage(String key, String msg);
	/**
	 * 添加错误信息
	 * @param key 错误名
	 * @param error 错误内容
	 */
	public void addActionError(String key, String error);
	/**
	 * 全局的验证方法
	 * @return
	 * @throws Exception
	 */
	public boolean validate() throws Exception;
	/**
	 * 是否需要从request赋值到action属性
	 * @return
	 */
	public boolean needRequestToAction();
	/**
	 * 当前讲求的方法
	 * @return
	 */
	public HttpMethod method();
}