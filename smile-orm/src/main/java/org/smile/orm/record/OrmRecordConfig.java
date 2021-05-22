package org.smile.orm.record;

import org.smile.orm.base.EnableSupportDAO;

public class OrmRecordConfig {
	/**单例 用于暂存record使用的默认 daosupport*/
	static OrmRecordConfig instance = new OrmRecordConfig();

	protected EnableSupportDAO ormDaoSupport;

	public EnableSupportDAO getOrmDaoSupport() {
		return ormDaoSupport;
	}

	public void setOrmDaoSupport(EnableSupportDAO ormDaoSupport) {
		this.ormDaoSupport = ormDaoSupport;
	}

	public static OrmRecordConfig getInstance() {
		return instance;
	}

}
