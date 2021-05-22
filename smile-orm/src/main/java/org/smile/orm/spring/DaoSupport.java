package org.smile.orm.spring;

import org.smile.db.spring.SpringTransactionHandler;
import org.smile.orm.base.OrmDataTemplate;
import org.smile.orm.record.OrmRecordConfig;
import org.springframework.beans.factory.InitializingBean;
/**
 * 这是一个orm 对spring 支持的 实现
 * @author 胡真山
 * @Date 2016年1月14日
 */
public class DaoSupport extends OrmDataTemplate implements InitializingBean{
	
	public DaoSupport(){
		this.transactionHandler=new SpringTransactionHandler();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		OrmRecordConfig.getInstance().setOrmDaoSupport(this);
	}
}
