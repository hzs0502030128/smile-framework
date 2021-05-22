package org.smile.web.author;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.smile.commons.SmileRunException;
import org.smile.io.BufferedReader;
import org.smile.io.IOUtils;
import org.smile.json.JSON;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

public class RoleInfo {
	
	private RoleConfig roleConfig;
	
	private List<RegExp> urlRegs=new LinkedList<RegExp>();
	
	public RoleInfo(RoleConfig roleConfig){
		try {
			this.roleConfig=roleConfig;
			List<String> urls=IOUtils.readLines(new BufferedReader(new StringReader(roleConfig.getUrl())));
			String urlString=null;
			for(String url:urls){
				url=StringUtils.trim(url);
				if(StringUtils.notEmpty(url)){
					urlString=url.replaceAll("\\*", "\\.\\*");
					urlRegs.add(new RegExp("^"+urlString.trim()+"$"));
				}
			}
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
	
	public String getName(){
		return roleConfig.getName();
	}
	
	public boolean testUrl(String url){
		for(RegExp reg:urlRegs){
			if(reg.test(url)){
				return true;
			}
		}
		return false;
	}
	

	public RoleConfig getRoleConfig() {
		return roleConfig;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
}
