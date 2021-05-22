package org.smile.strate.dispatch;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.converter.BeanException;
import org.smile.http.HttpMethod;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;
import org.smile.strate.Strate;
import org.smile.strate.StrateException;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionContext;
import org.smile.strate.action.ActionElement;
import org.smile.strate.action.ActionKey;
import org.smile.strate.action.NoActionFindException;
import org.smile.strate.action.ResultNotFindException;
import org.smile.strate.action.StrateInitException;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.form.ActionFormResult;
import org.smile.strate.jump.JumpType;
import org.smile.strate.upload.UploadConstants;
import org.smile.strate.upload.UploadRequestWrapper;
import org.smile.strate.view.Model;
import org.smile.util.StringUtils;
import org.smile.util.SysUtils;
/**
 * 	处理分发action的类
 * @author 胡真山
 *
 */
public class StrateDispatcher {
	
	protected static Logger logger=LoggerFactory.getLogger(StrateDispatcher.class);
	/**url解析类默认*/
	public static ActionURIParser DefaultParser=new ExtensionActionURIParser();
	/**url解析类*/
	private ActionURIParser urlParser;
	/**
	 * 创建分发上下文实例
	 * @param request
	 * @param response
	 * @param uri
	 * @param httpMothod
	 * @return
	 */
	private DispatchContext createContext(HttpServletRequest request, HttpServletResponse response, String uri, HttpMethod httpMothod) {
		DispatchContext context=new DispatchContext(request, response,httpMothod);
		context.setDispatcher(this);
		ActionURLInfo urlInfo=parseURI(request.getContextPath(), uri);
		context.setUri(uri);
		context.setActionUrlInfo(urlInfo);
		return context;
	}
	
	/**
	 * 服务处理，从配置文件读取信息 实例化 类 反射方法 如果配置文件中没有指定method 也可以从客户端传来一个参数 method
	 * 由method决定调用哪个方法
	 * 
	 * @param request
	 * @param response
	 * @param httpMothod
	 * @throws ServletException
	 * @throws IOException
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response, String url, HttpMethod httpMothod) throws ServletException, IOException {
		DispatchContext context=createContext(request, response, url, httpMothod);
		ActionURLInfo info=context.getActionUrlInfo();
		String namespace = info.getNamespace();
		String actionName = info.getActionName();
		//命名空间中的所有action
		Map<ActionKey,ActionElement> namespaceElement= ActionContext.getActionMap(namespace);
		//使用名称与请求方法做为键获取action配置
		ActionKey actionKey=new ActionKey(actionName,httpMothod.name());
		//当前action
		ActionElement actionElement= namespaceElement.get(actionKey);
		if(actionElement==null){
			throw new NoActionFindException("not action named "+actionName+" support http method "+httpMothod.name()+ " in namespace "+namespace);
		}
		context.setActionElement(actionElement);
		
		Action action = null;
		// 先从配置文件获取方法 如果没有配置 再从提交参数中获取 还是没有 则抛出异常
		String methodName = context.getActionMethodName();
		try {
			//跳转结果名
			Object result;
			// 得到action实例
			action = actionElement.getExecutor().getActionBean(actionElement.getClazz(),request.getSession().getServletContext());
			//放入到分发的上下文中
			context.setAction(action);
			action.setRequestAndResponse(request, response,httpMothod);
			// 处理上传文件
			try {
				String type = request.getContentType();
				if (StringUtils.notEmpty(type)) {
					if (type.startsWith(UploadConstants.MULTIPART_FORM_DATA)) {
						UploadRequestWrapper wrapper = new UploadRequestWrapper(request);
						wrapper.uploadLimit(action.uploadLimit());
						wrapper.initFileItems();
						context.setRequest(wrapper);
						request = wrapper;
					}
				}
				// 初始化方法
				action.initPerterties(request, response, httpMothod);
				ActionFormResult fillResult = actionElement.getExecutor().requestToAction(context);
				if(fillResult!=null&&fillResult.hasDynamicAction()){
					String dynamicAction=fillResult.getDynamicAction();
					// 替换为动态方法
					if (!actionName.equals(fillResult.getDynamicAction())) {
						actionElement = namespaceElement.get(new ActionKey(dynamicAction));
					}
				}
				//执行action方法
				result=actionElement.getExecutor().doActionMethod(context,fillResult);
			} finally{
				if (request instanceof UploadRequestWrapper) {//是上传类型的时候清除上传缓存内容
					((UploadRequestWrapper) request).clearUpload();
				}
			}
			
			if(result!=null){//存在结果的时候需要处理结果跳转
				//返回是字符串类型的时候，当成是处理配置结果跳转
				if(result instanceof String){//是字符串时认为返回的是跳转名称
					// 根据返回结果设置跳转
					jumpResult(context, (String)result);
				}else{
					ResultConfig resultConfig;
					Object methodResult=result;
					if(result instanceof Model){//方法返回是模型的时候处理
						Model<?> model=((Model<?>) result);
						resultConfig=model.getResultConfig(actionElement);
						methodResult=model.getData();
					}else{
						//不是返回视图模型时，认为是成功跳转
						resultConfig=getResultConfig(actionElement, Action.SUCCESS);
					}
					JumpType jump=convertResultConfigJump(resultConfig);
					jump.jump(methodResult,context, resultConfig);
				}
			}
		}catch (SecurityException e) {
			throw new StrateException(" invoke method：[" + actionElement.getClassElement().getActionClass() + "." + methodName + "] have a error ", e);
		} catch (NoSuchMethodException e) {
			throw new StrateException(" invoke method：[" + actionElement.getClassElement().getActionClass() + "." + methodName + "] have a error ,no such method", e);
		} catch (Throwable e) {
			ResultConfig result = ActionContext.getExceptionResult(e.getClass());
			// 处理异常跳转
			if (result != null) {
				String msg="action " + methodName + " error :"+e.getMessage();
				SysUtils.log(msg,e);
				logger.error(msg, e);
				request.setAttribute(Action.EXCEPTION, e);
				jumpResult(context, result);
			} else {
				throw new StrateException(" service ==>" + actionElement.getClassElement().getActionClass() + " do action method " + methodName + " has a error ", e);
			}
		}finally{
			if(action!=null){
				action.clearRequestAndResponse();
			}
		}
	}

	public  boolean isActionURI(String contextPath,String uri) {
		return this.urlParser.isActionURI(contextPath, uri);
	}
	
	public  ActionURLInfo parseURI(String contextPath,String uri){
		return this.urlParser.parseURI(contextPath, uri);
	}
	
	public  String createURI(ActionElement actionElement){
		return this.urlParser.createURI(actionElement);
	}

	/**
	 * 	设置编码
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String properedURI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(Strate.encoding);
		response.setCharacterEncoding(Strate.encoding);
		return request.getRequestURI();
	}
	
	/***
	 * 初始化uri解析器 
	 * @param urlParserId 配置的解析器id 如果为空则使用默认的解析器
	 */
	public void initActionUrlParser(String urlParserId) {
		if(StringUtils.notEmpty(urlParserId)) {
			try {
				this.urlParser=ActionContext.getBeansConfig().getBean(urlParserId);
			} catch (BeanException e) {
				throw new StrateInitException("init action url parser error", e);
			}
		}
		if(this.urlParser==null) {
			this.urlParser=DefaultParser;
		}
	}
	
	/**
	 * 跳转
	 * @param request
	 * @param response
	 * @param forward
	 * @throws StrateException
	 */
	public static void jumpResult(DispatchContext context, ResultConfig forward) throws StrateException {
		JumpType jump=convertResultConfigJump(forward);
		jump.jump(context, forward);
	}
	
	/**
	 * 获取跳转方式类型实现
	 * @param forward
	 * @return
	 * @throws StrateException
	 */
	public static JumpType convertResultConfigJump(ResultConfig forward) throws StrateException{
		String type=forward.getType();
		JumpType jump;
		if(StringUtils.isEmpty(type)){
			jump=JumpType.forward;
		}else{
			try{
				jump=JumpType.valueOf(type);
			}catch(IllegalArgumentException e){
				throw new StrateException("unknown result type:"+type+" ,please check the strate xml config file");
			}
		}
		return jump;
	}
	
	/**
	 * 跳转
	 * @param request
	 * @param response
	 * @param forward
	 * @throws StrateException
	 */
	public static void jumpResult(DispatchContext context, String resultName) throws StrateException {
		ResultConfig result=getResultConfig(context.getActionElement(), resultName);
		jumpResult(context, result);
	}
	
	/**
	 * 获取反回结果 跳转url
	 * 
	 * 先从action配置中查询 没有时再查找全局
	 * 
	 * @param actionElement
	 * @param name 返回结果名称
	 * @return
	 * @throws ResultNotFindException
	 */
	public static ResultConfig getResultConfig(ActionElement actionElement, String name) throws ResultNotFindException {
		ResultConfig result=actionElement.getResultConfig(name);
		if (result == null) {
			throw new ResultNotFindException(actionElement.getClazz() + " action " + actionElement.getName() + " not find result named " + name);
		}
		return result;
	}
}
