package org.smile.orm.record;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;
import org.smile.db.SqlRunException;
import org.smile.db.jdbc.Record;
import org.smile.expression.DefaultContext;
import org.smile.expression.Engine;
import org.smile.orm.base.EnableSupportDAO;

public class OrmRecord implements Record{
	/**orm dao 操作支持*/
	private static EnableSupportDAO ormDaoSupport=OrmRecordConfig.instance.getOrmDaoSupport();
	
	@Override
	public void update()  {
		dao().update(entity());
	}

	@Override
	public void delete()  {
		dao().delete(entity());
	}

	@Override
	public void insert()  {
		dao().insert(entity());
	}

	@Override
	public void load()  {
		dao().load(entity());
	}

	/***
	 * 设置数据操作dao支持
	 */
	public static void setOrmDaoSupport(EnableSupportDAO dao) {
		ormDaoSupport = dao;
	}

	@Override
	public void update(String[] fields)  {
		this.dao().update(entity(), fields);
	}

	@Override
	public void delete(String where)  {
		Boolean delete=(Boolean) Engine.getInstance().evaluate(new DefaultContext(entity()), where);
		if(delete){
			this.dao().delete(entity());
		}
	}

	protected EnableSupportDAO dao(){
		if(ormDaoSupport==null){
			ormDaoSupport=OrmRecordConfig.instance.getOrmDaoSupport();
		}
		return ormDaoSupport;
	}

	@Override
	public void save()  {
		this.dao().saveOrUpdate(entity());
	}

	@Override
	public void load(String[] fields)  {
		dao().load(entity(), fields);
	}
	/**
	 * entity
	 * @return
	 */
	protected Object entity() {
		return this;
	}

	@Override
	public void update(String first, String... strings)  {
		String[] fields=new String[strings.length+1];
		fields[0]=first;
		if(strings.length>0) {
			System.arraycopy(strings, 0, fields, 1, strings.length);
		}
		this.ormDaoSupport.update(entity(), fields);
	}

	@Override
	public void load(String first, String... strings)  {
		String[] fields=new String[strings.length+1];
		fields[0]=first;
		if(strings.length>0) {
			System.arraycopy(strings, 0, fields, 1, strings.length);
		}
		load(fields);
	}

	@Override
	public void update(String[] fields, Object[] values) {
		if(fields.length!=values.length) {
			throw new SqlRunException("The arg count of field and value is inconsistent");
		}
		for(int i=0;i<fields.length;i++) {
			try {
				BeanUtils.setValue(entity(), fields[i], values[i]);
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}
		this.update(fields);
		
	}
}
