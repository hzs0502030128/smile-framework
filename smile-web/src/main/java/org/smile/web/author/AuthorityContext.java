package org.smile.web.author;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.util.RegExp;
import org.smile.util.StringUtils;

public class AuthorityContext {
	
	private Map<String,RoleInfo> roleInfoMap=new HashMap<String, RoleInfo>();
	
	private Map<String,AuthorConfigInfo> authorConfigMap=new HashMap<String,AuthorConfigInfo>();
	
	private Set<RegExp> excludesRegExp;
	
	public AuthorityContext(AuthorityConfig config){
		List<RoleConfig> list=config.getRole();
		for(RoleConfig role:list){
			RoleInfo info=new RoleInfo(role);
			roleInfoMap.put(info.getName(), info);
		}
		List<UserConfig> users=config.getUser();
		for(UserConfig auth:users){
			AuthorConfigInfo info=new AuthorConfigInfo(auth);
			String roleString=auth.getRole();
			String[] rolenames=roleString.split(",");
			for(String role:rolenames){
				info.addRoleInfo(roleInfoMap.get(role));
			}
			authorConfigMap.put(info.getUsername(), info);
		}
		if(StringUtils.notEmpty(config.getExcludes())){
			List<String> strs=RegExp.DEF_STR_SPLIT.splitAndTrimNoBlack(config.getExcludes());
			if(strs.size()>0){
				excludesRegExp=new HashSet<RegExp>();
				for(String str:strs){
					excludesRegExp.add(new RegExp(StringUtils.configString2Reg(str)));
				}
			}
		}
	}
	
	public AuthorConfigInfo getAuthorConfigInfo(String username){
		return authorConfigMap.get(username); 
	}
	/**
	 * 是否排除
	 * @param url
	 * @return
	 */
	public boolean isExcludes(String url){
		if(excludesRegExp==null){
			return false;
		}
		for(RegExp regexp:excludesRegExp){
			if(regexp.test(url)){
				return true;
			}
		}
		return false;
	}
	
}
