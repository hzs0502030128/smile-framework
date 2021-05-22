package org.smile.web.author;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "authors")
public class AuthorityConfig {
	@XmlElement
	protected String excludes;
	
	protected List<UserConfig> user;
	
	protected List<RoleConfig> role;

	public List<UserConfig> getUser() {
		return user;
	}

	public List<RoleConfig> getRole() {
		return role;
	}

	public String getExcludes() {
		return excludes;
	}
}
