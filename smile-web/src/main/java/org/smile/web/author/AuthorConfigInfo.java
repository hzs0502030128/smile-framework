package org.smile.web.author;

import java.util.HashSet;
import java.util.Set;



public class AuthorConfigInfo extends AuthorInfo{
	
	private Set<RoleInfo> roleInfos=new HashSet<RoleInfo>();
	
	public AuthorConfigInfo(UserConfig author){
		super(author.getUsername(), author.getPassword());
	}
	
	public void addRoleInfo(RoleInfo roleInfo){
		roleInfos.add(roleInfo);
	}
	
	
	public boolean checkPassword(String password){
		return this.password.equals(password);
	}
	
	public boolean testUrl(String url){
		for(RoleInfo info:roleInfos){
			if(info.testUrl(url)){
				return true;
			}
		}
		return false;
	}
}
