package org.smile.strate.jump;

import org.smile.strate.StrateException;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;

/**
 * 反回结果跳转类型
 * @author 胡真山
 * @Date 2016年1月18日
 */
public enum JumpType {
	/**服务器跳转*/
	forward(new ForwardJumpHandler()),
	/**服务器重定向*/
	redirect(new RedirectJumpHandler()),
	/**模板跳转*/
	template(new TemplateJumpHandler()),
	/**跳到另一个方法*/
	method(new MethodJumpHandler()),
	/**跳转到另一个action*/
	action(new ActionJumpHandler()),
	/**用于下载文件*/
	stream(new StreamJumpHandler()),
	/**与freemark整合*/
	freemarker(new FreemarkerJumpHandler()),
	/**JFreeChar整合*/
	chart(new JFreeChartJumpHandler()),
	/**json跳转**/
	json(new JsonJumpHandler()),
	/**图片跳转*/
	image(new ImageJumpHandler());
	/**
	 * 跳转处理类
	 */
	private JumpHandler handler;
	
	private JumpType(JumpHandler handler){
		this.handler=handler;
	}
	/**
	 * 没有返回值的跳转(其实是返回string类型的跳转配置名称)
	 * @param action
	 * @param actionElement
	 * @param request
	 * @param response
	 * @param forward
	 * @throws StrateException
	 */
	public  void jump(DispatchContext context, ResultConfig forward) throws StrateException{
		handler.jump(context, forward);
	}
	/***
	 * 存在返回值的方法跳转
	 * @param methodResult 返回值
	 * @param action 当前action对象
	 * @param actionElement  匹配信息
	 * @param request 请求
	 * @param response 响应
	 * @param forward 跳转配置
	 * @throws StrateException
	 */
	public  void jump(Object methodResult,DispatchContext context, ResultConfig forward) throws StrateException{
		handler.jump(methodResult,context, forward);
	}

}
