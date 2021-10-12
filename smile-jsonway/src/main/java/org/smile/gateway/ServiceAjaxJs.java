package org.smile.gateway;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.commons.StringBand;
import org.smile.gateway.invoke.ServiceMethods;
import org.smile.util.StringUtils;
/**
 * 封装service类的方法 生成的ajax js文件 内容
 * @author 胡真山
 *
 */
public class ServiceAjaxJs {
	/**服务名称  与前端js 一致*/
	protected String serviceName;
	
	protected GatewayExecuter executer;
	/***
	 * 缓存下service生成的js代码
	 */
	private Map<String,JsCode> serviceJsMap=new ConcurrentHashMap<String,JsCode>();
	
	public ServiceAjaxJs(GatewayExecuter executer,String serviceName){
		this.serviceName=serviceName;
		this.executer=executer;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public JsCode getJs(String gateway,String path) throws Exception{
		JsCode js=serviceJsMap.get(path);
		if(js==null){
			js=initJs(gateway,path);
			serviceJsMap.put(path, js);
		}
		return js;
	}
	/**
	 * 构造 service对象提供给 前端js类的方法代码
	 * @param gateway
	 * @param path
	 * @return
	 * @throws Exception
	 */
	protected JsCode initJs(String gateway,String path) throws Exception{
		StringBand javasrcipt =new StringBand();
		Object service = executer.getServiceBean(serviceName);
		//初始化方法 
		ServiceMethods serviceMethods=executer.invkeContext.getServiceMethods(serviceName,service.getClass());
		Set<String> methodNames=serviceMethods.getMethodNames();
		String name=StringUtils.getFirstCharUpper(serviceName);
		javasrcipt.append("var ").append(name).append("=RemoteJsonService.extend({");
		javasrcipt.append("jsonGateway:\"").append(path).append(gateway).append("\",");
		javasrcipt.append("serviceName:\"").append(serviceName).append("\",");
		int idx=0;
		int size=methodNames.size();
		for (String methodName:methodNames) {
			javasrcipt.append("").append(methodName).append(":function(){");	
			javasrcipt.append("var preprocessResult = this.preprocess.apply(this,arguments);");
			javasrcipt.append("var data = preprocessResult.data;");
			javasrcipt.append("data.method = \"").append(methodName).append("\";");
			javasrcipt.append("this.ajaxCall(data,preprocessResult.callerResponder);");
			javasrcipt.append("}");
			if (idx != size-1) {
				javasrcipt.append(",");
			}
			idx++;
		}
		javasrcipt.append("});");
		javasrcipt.append("var ").append(serviceName).append("=new ").append(name).append("();");
		return new JsCode(javasrcipt.toString());
	}
	
	protected class JsCode{
		long times;
		/**代码内容*/
		String content;
		
		JsCode(String content){
			this.times=System.currentTimeMillis();
			this.content=content;
		}
	}
}
