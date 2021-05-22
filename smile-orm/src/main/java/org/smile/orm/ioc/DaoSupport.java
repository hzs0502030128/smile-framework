package org.smile.orm.ioc;

import org.smile.ioc.aware.InitializingBean;
import org.smile.orm.base.OrmDataTemplate;
import org.smile.orm.record.OrmRecordConfig;
import org.smile.transaction.SmileTransactionHandler;
/**
 * 这是一个orm 对ioc 支持的 实现
 * @author 胡真山
 * @Date 2016年1月14日
 */
public class DaoSupport extends OrmDataTemplate implements InitializingBean{
	
	public DaoSupport(){
		this.transactionHandler=new SmileTransactionHandler();
	}

	@Override
	public void afterInit() {
		OrmRecordConfig.getInstance().setOrmDaoSupport(this);
	}
}
