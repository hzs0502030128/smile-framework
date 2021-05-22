package org.smile.web.author;

import org.smile.json.JSON;

public class AuthorInfo {
	
	protected String username;
	
	protected String password;
	
	public AuthorInfo(String username,String password){
		this.username=username;
		this.password=password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
