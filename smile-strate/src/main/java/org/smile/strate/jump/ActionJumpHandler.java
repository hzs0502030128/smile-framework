package org.smile.strate.jump;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.collection.CollectionUtils;
import org.smile.commons.StringParam;
import org.smile.http.HttpMethod;
import org.smile.http.URLInfo;
import org.smile.strate.RequestParamemterWrapper;
import org.smile.strate.RequestParameter;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionContext;
import org.smile.strate.action.ActionElement;
import org.smile.strate.action.NoActionFindException;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.ActionURLInfo;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.dispatch.StrateDispatcher;
import org.smile.strate.jump.forward.MethodForwardRequestWrapper;
import org.smile.util.StringUtils;

public class ActionJumpHandler implements JumpHandler {

	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		HttpServletRequest request=context.getRequest();
		ActionElement actionElement=context.getActionElement();
		HttpServletResponse response=context.getResponse();
		// 重定向方法
		String url=forward.getValue().trim();
		URLInfo urlInfo = new URLInfo(url);
		urlInfo.parse();
		String actionName = urlInfo.getName();
		String namespace = urlInfo.getPath();
		/**跳转 action信息*/
		ActionElement other;
		/**跳转的action*/
		Action jumpAction;
		ActionURLInfo actionUrlinfo=null;
		if (context.getDispatcher().isActionURI(request.getContextPath(), urlInfo.getUri())) {
			actionUrlinfo=context.parseURI(urlInfo.getUri());
			actionName =actionUrlinfo.getActionName();
			namespace=actionUrlinfo.getNamespace();
		}
		try {
			if (StringUtils.isEmpty(namespace)) {
				//如果没有namespace 在当前空间查询action
				namespace = actionElement.getNamespace();
				other = actionElement.getPackageElement().getActioneElement(actionName);
			} else {
				other = ActionContext.getActionElement(namespace,actionName);
			}
		} catch (NoActionFindException e) {
			throw new StrateResultJumpException("not exists a action  named " + actionName + " namespace " + namespace + " case by result " + forward.getName(), e);
		}
		try {
			//是否为同一个action实现类
			if(actionElement.getClazz().equals(other.getClazz())){
				jumpAction=context.getAction();
			}else{
				//当前action切换
				jumpAction = other.getExecutor().getActionBeanHandler().getActionBean(other.getClazz(), request.getSession().getServletContext());
				jumpAction.initPerterties(request, response, HttpMethod.valueOf(request.getMethod()));
			}
			context.setActionElement(other);
			context.setAction(jumpAction);
			context.setActionUrlInfo(actionUrlinfo);
			
			List<StringParam> params=urlInfo.getParams();
			if(CollectionUtils.notEmpty(params)){
				RequestParameter parameter=new RequestParameter();
				for(StringParam p:params){
					parameter.addParam(p.name, p.value);
				}
				context.setRequest(new RequestParamemterWrapper(request,parameter));
				other.getExecutor().requestToAction(context);
			}
			Object result = context.onOtherActionDo();
			if(result!=null && result instanceof String){
				StrateDispatcher.jumpResult(context,(String)result);
			}
		} catch (Exception e) {
			throw new StrateResultJumpException(e);
		}
	}

	@Override
	public void jump(Object methodResult,DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		HttpServletRequest request=new MethodForwardRequestWrapper(context.getRequest(), context.getAction(), methodResult);
		context.setRequest(request);
		this.jump(context, forward);
	}
}
