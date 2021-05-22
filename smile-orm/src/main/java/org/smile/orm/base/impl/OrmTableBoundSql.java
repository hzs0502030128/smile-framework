package org.smile.orm.base.impl;

import java.util.Iterator;

import org.smile.db.sql.BoundSql;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmProperty;
/**
 * 用于ORM封装数据
 * @author 胡真山
 * @Date 2016年2月26日
 */
public abstract class OrmTableBoundSql extends BoundSql {
	/**参数填充*/
	protected static FieldPropertyFiller filler = new FieldPropertyFiller();
	/**
	 * ORM映射对象
	 */
	protected OrmTableMapping type;
	
	/**当前参数封装对象*/
	protected Object params;

	public OrmTableBoundSql(OrmTableMapping type) {
		this.type = type;
	}
	
	public OrmTableBoundSql(OrmTableMapping type,Object params) {
		this.type = type;
		this.params=params;
	}

	@Override
	public Object getParams() {
		return params;
	}

	@Override
	public Iterator<OrmProperty> iteratorMapping() {
		return type.columnPropertys().iterator();
	}

}
