package org.smile.orm.base.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.collection.CollectionUtils;
import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.db.Transaction;
import org.smile.db.handler.RowHandler;
import org.smile.db.result.BeanResultParser;
import org.smile.db.result.QueryResult;
import org.smile.db.result.ResultParser;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.db.sql.SqlExecutor;
import org.smile.orm.AssociationConfig;
import org.smile.orm.mapping.OrmMixMapping;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmProperty;

/**
 * 把结果集转成Bean的RowHandler
 * 
 * @author 胡真山
 */
public class OrmTableRowHandler extends RowHandler {
	/**
	 * 默认的接口实例
	 */
	private static Map<Class, Class> collectionType = new HashMap<Class, Class>();

	private static Map<Class, Class> mapType = new HashMap<Class, Class>();
	
	/**结果集解析*/
	protected static ResultParser parser = new BeanResultParser();
	/**结果集的映射*/
	protected OrmObjMapping ormMapping=null;
	/**是否需要加载混合*/
	private boolean needLoadMix=false;

	static {
		collectionType.put(List.class, ArrayList.class);
		collectionType.put(Set.class, HashSet.class);
		collectionType.put(Collection.class, ArrayList.class);
		mapType.put(Map.class, LinkedHashMap.class);
	}
	/**只是给子类继承*/
	protected OrmTableRowHandler() {}

	public OrmTableRowHandler(Class resultClass) {
		this.resultClass = resultClass;
		ormMapping =OrmTableMapping.getType(resultClass);
	}
	
	public OrmTableRowHandler(Class resultClass,boolean needLoadMix) {
		this(resultClass);
		this.needLoadMix=needLoadMix;
	}
	
	@Override
	public <E> E handle(QueryResult rs) throws SQLException {
		E bean;
		try {
			bean = (E) ormMapping.parseResultSet(parser,newInstance(), rs);
			if(this.needLoadMix&&ormMapping.hasImmediateMixes()) {
				handleMixes(bean);
			}
		} catch (Exception e) {
			throw new SQLException("转换结果集成对象错误：" + resultClass + " " + e.getMessage(), e);
		}
		return bean;
	}
	/**
	 * orm封装对象
	 * @return
	 */
	protected Object newInstance(){
		return this.ormMapping.newRawInstance();
	}

	/**
	 * 处理混合的数据
	 * @param bean
	 */
	private  void handleMixes(Object bean) {
		if(this.ormMapping.hasMixes()) {
			Collection<OrmMixMapping> mixes=ormMapping.getOrmMixMappings();
			for(OrmMixMapping mixMapping:mixes) {
				if(mixMapping.isImmediate()) {//即时加载数量
					mixMapping.loadDatasToBean(bean);
				}
			}
		}
	}
	

	@Override
	public boolean needDoRelate() {
		return true;
	}

	@Override
	public <E> void doRelate(Collection<E> dataList, Transaction conn) throws SQLException {
		OrmTableMapping<E> tableMapper = (OrmTableMapping)ormMapping;
		if (tableMapper.hasOneToMany()) {
			for (Map.Entry<String, AssociationConfig> entry : tableMapper.getOneToManyConfig().entrySet()) {
				AssociationConfig config = entry.getValue();
				BoundSql boundSql = FieldBeanRelateUtil.createOneToManyRelateSql(config, dataList);
				SqlExecutor runner = new SQLRunner(conn, new OrmTableRowHandler(config.getClazz()));
				OrmTableMapping manyMapper = config.getManyMapper();
				final OrmProperty foreignKeyProperty = manyMapper.getPropertyByColumn(config.getForeignKey());
				GroupKey<Object, Object> groupKey = new GroupKey<Object, Object>() {
					@Override
					public Object getKey(Object value) {
						return foreignKeyProperty.readValue(value);
					}
				};
				Map<Object, List<Object>> groupMap = runner.queryForGroupMap(boundSql, groupKey);
				Class fieldType = config.getFieldType();
				for (Object data : dataList) {
					try {
						Object dataKey = tableMapper.getPrimaryKeyValue(data);
						List<Object> subDataList = groupMap.get(dataKey);
						if (subDataList != null && fieldType.isAssignableFrom(subDataList.getClass())) {
							config.getField().set(data, subDataList);
						} else if (Collection.class.isAssignableFrom(fieldType)) {// collectioin关联
							Collection fieldData;
							Class fieldTypeClass = collectionType.get(fieldType);
							if (fieldTypeClass != null) {
								fieldData = (Collection) fieldTypeClass.newInstance();
							} else {
								fieldData = (Collection) fieldType.newInstance();
							}
							CollectionUtils.addAll(fieldData,subDataList);
							config.getField().set(data, subDataList);
						} else if (Map.class.isAssignableFrom(fieldType)) {// map关联
							Map fieldData;
							Class fieldTypeClass = mapType.get(fieldType);
							if (fieldTypeClass != null) {
								fieldData = (Map) fieldTypeClass.newInstance();
							} else {
								fieldData = (Map) fieldType.newInstance();
							}
							if(subDataList!=null){
								for (Object obj : subDataList) {
									fieldData.put(manyMapper.getPrimaryKeyValue(obj), obj);
								}
							}
							config.getField().set(data, fieldData);
						} else {// 关个对关联
							if (CollectionUtils.notEmpty(subDataList)) {
								config.getField().set(data, subDataList.get(0));
							}
						}
					} catch (Exception e) {
						throw new SQLException("设置 " + data + "关联" + manyMapper + "失败", e);
					}
				}
			}
		}
	}

	public void setNeedLoadMix(boolean needLoadMix) {
		this.needLoadMix = needLoadMix;
	}
}
