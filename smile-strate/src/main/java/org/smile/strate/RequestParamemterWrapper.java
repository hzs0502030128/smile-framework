package org.smile.strate;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.smile.collection.CollectionUtils;
import org.smile.collection.ExtendsMap;
import org.smile.collection.IteratorEnumeration;
import org.smile.log.LoggerHandler;

public class RequestParamemterWrapper extends HttpServletRequestWrapper implements LoggerHandler{
	protected HttpServletRequest realRequest;
	protected RequestParameter parameter;
	public RequestParamemterWrapper(HttpServletRequest request) {
		super(request);
		this.realRequest=request;
	}
	
	public RequestParamemterWrapper(HttpServletRequest request,RequestParameter parameter ){
		this(request);
		this.parameter=parameter;
	}
	
	public void setParameter(RequestParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public String getParameter(String name) {
		if(parameter==null){
			return realRequest.getParameter(name);
		}
		List list=parameter.get(name);
		if(CollectionUtils.isEmpty(list)){
			return realRequest.getParameter(name);
		}
		return String.valueOf(list.get(0));
	}

	@Override
	public Map getParameterMap() {
		Map paramentMap=realRequest.getParameterMap();
		if(parameter!=null){
			paramentMap=new ExtendsMap(paramentMap);
			for(String key:parameter.keySet()){
				paramentMap.put(key,parameter.getParameters(key));
			}
		}
		return paramentMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> names=realRequest.getParameterNames();
		if(this.parameter==null){
			return names;
		}
		List<String> list=new LinkedList<String>(parameter.keySet());
		
		while(names.hasMoreElements()){
			list.add(names.nextElement());
		}
		return new IteratorEnumeration<String>(list);
	}

	@Override
	public String[] getParameterValues(String name) {
		if(parameter==null){
			return realRequest.getParameterValues(name);
		}
		List list=parameter.get(name);
		if(CollectionUtils.isEmpty(list)){
			return realRequest.getParameterValues(name);
		}
		return CollectionUtils.toStringArray(list);
	}
	
}
