package org.smile.ormdb.dao;

import java.util.Date;

import lombok.Data;
import org.smile.orm.ann.Column;
@Data
public class UpdateInfo {
	@Column
	private String createUser;
	@Column
	private String updateUser;
	@Column
	private Date createTime;
	@Column
	private Date updateTime;

}
