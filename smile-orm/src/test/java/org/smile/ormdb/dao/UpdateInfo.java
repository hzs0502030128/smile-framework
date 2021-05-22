package org.smile.ormdb.dao;

import java.util.Date;

import org.smile.orm.ann.Column;

public class UpdateInfo {
	@Column
	private String createUser;
	@Column
	private String updateUser;
	@Column
	private Date createTime;
	@Column
	private Date updateTime;
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
