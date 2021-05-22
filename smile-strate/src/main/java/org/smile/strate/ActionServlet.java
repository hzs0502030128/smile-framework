package org.smile.strate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.http.HttpMethod;
import org.smile.log.LoggerHandler;
import org.smile.strate.dispatch.StrateDispatcher;

/**
 * actionservlet 所有的action请求都是通过此servlet处理
 * 
 * 根据不同的路径实例化不能的action类 调用 方法
 * 
 * @author strive
 *
 */
public class ActionServlet extends HttpServlet implements LoggerHandler {

	private StrateDispatcher dispatcher=new StrateDispatcher();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpMethod method = HttpMethod.valueOf(req.getMethod());
		switch(method){
			case PATCH:
			case GET:
			case POST:
			case PUT:
			case DELETE:
				doMethod(req, resp, method);
				break;
			default:
				super.service(req, resp);
		}
	}
	
	protected void doMethod(HttpServletRequest request, HttpServletResponse response,HttpMethod method) throws ServletException, IOException{
		String uri = this.dispatcher.properedURI(request, response);
		if (dispatcher.isActionURI(request.getContextPath(), uri)) {
			this.dispatcher.execute(request, response, uri, method);
		}
	}
	
	

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doMethod(req, resp,HttpMethod.PUT);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doMethod(req, resp,HttpMethod.DELETE);
	}

	/**
	 * get处理
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doMethod(request, response,HttpMethod.GET);
	}

	/**
	 * POST处理
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doMethod(request, response,HttpMethod.POST);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		StrateContext.getInstance().start(getServletContext());
		String urlParserId=getInitParameter("url-parser");
		this.dispatcher.initActionUrlParser(urlParserId);
	}
}
