package org.smile.orm.base.impl;

import java.util.Collection;

import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.orm.AssociationConfig;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.EnableFlagProperty;

public class FieldBeanRelateUtil {
	/**
	 * 生成一对多关联的查询语句
	 * @param ormMapper
	 * @param dataList
	 * @return
	 */
	public static <O, M> BoundSql createOneToManyRelateSql(AssociationConfig<O, M> config, Collection<O> dataList) {
		OrmTableMapping<M> manyMapper = config.getManyMapper();
		StringBuilder sql = new StringBuilder(manyMapper.getSelectAllSql());
		sql.append(" WHERE  ");
		Object[] params;
		if (manyMapper.supportDisable()) {
			params = new Object[dataList.size() + 1];
			EnableFlagProperty property = manyMapper.getEnableProperty();
			sql.append(property.getColumnName()).append("=").append(property.getPropertyExp());
			params[0] = property.getEnable();
		} else {
			params = new Object[dataList.size()];
		}
		addForeignKey(manyMapper.supportDisable(), config.getForeignKey(), config.getOneMapper(), dataList, sql, params);
		return new ArrayBoundSql(sql.toString(), params);
	}

	private static final <O, M> void addForeignKey(boolean disableSupport, String foreignKey, OrmTableMapping<M> oneMapper, Collection<O> dataList, StringBuilder sql, Object[] params) {
		int index = 0;
		if (disableSupport) {
			sql.append(" AND (");
			index += 1;
		}
		for (O bean : dataList) {
			sql.append(foreignKey).append("=?");
			params[index++] = oneMapper.getPrimaryKeyValue(bean);
			if (index < params.length) {
				sql.append(" OR ");
			}
		}
		if (disableSupport) {
			sql.append(" )");
		}
	}
}
