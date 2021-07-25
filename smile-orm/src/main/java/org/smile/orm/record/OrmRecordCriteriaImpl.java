package org.smile.orm.record;

import org.smile.collection.MapUtils;
import org.smile.db.criteria.*;
import org.smile.db.handler.HumpKeyColumnSwaper;
import org.smile.db.jdbc.RecordCriteriaImpl;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmProperty;
import org.smile.util.ObjectLenUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 使用jdbc实现的一个Criteria操作
 * @author 胡真山
 *
 */
public class OrmRecordCriteriaImpl<E> extends RecordCriteriaImpl<E>{
	
	public OrmRecordCriteriaImpl(RecordDaoImpl<E> recordDao) {
		super(recordDao);
	}

	@Override
	protected UpdateCriteriaInfo buildUpdateInfo() {
		StringBuilder whereSql=new StringBuilder();
		Map<String,Object> params= MapUtils.resultMap();
		CriterionVisitor visitor=new NamedRecordCriterionVisitor(whereSql,params);
		for(Criterion c:this.criterions) {
			c.accept(visitor);
		}
		String[] setFieldNames= new String[setters.size()];
		int idx=0;
		for(SetCriterion c:setters){
			setFieldNames[idx++] =c.getFieldName();
			params.put(c.getFieldName(),c.getValue());
		}
		return new UpdateCriteriaInfo(setFieldNames,whereSql.toString(), params);
	}

	protected  class NamedRecordCriterionVisitor extends BaseCriterionVisitor {
		/***参数占位符*/
		String parameterFlag=":";
		String valueExt1="_v_1";
		String valueExt2="_v_2";
		/**查询条件参数值*/
		protected Map<String,Object> params;

		OrmTableMapping<E> mapping;

		NamedRecordCriterionVisitor(StringBuilder whereSql,Map<String,Object> params){
			this.whereSql=whereSql;
			this.params=params;
			this.mapping = OrmTableMapping.getType(recordDao.resultClass());
		}

		@Override
		public void visit(OtherFieldCriterion criterion) {
			whereSql.append(toColumnName(criterion.getFieldName())).append(" ").append(criterion.getOp()).append(" ");
			whereSql.append(toColumnName(criterion.getOtherField())).append(" ");
		}

		private String toColumnName(String fieldName){
			OrmProperty p1 =this.mapping.getProperty(fieldName);
			String column=null;
			if(p1 == null ){
				return HumpKeyColumnSwaper.instance.KeyToColumn(fieldName);
			}else{
				return p1.getColumnName();
			}
		}

		@Override
		public void visit(SimpleCriterion criterion) {
			whereSql.append(toColumnName(criterion.getFieldName())).append(" ").append(criterion.getOp()).append(" ");
			Object paramValue=criterion.getValue();
			whereSql.append(" :").append(criterion.getFieldName()).append(valueExt1);
			params.put(criterion.getFieldName()+valueExt1,paramValue);
		}

		@Override
		public void visit(IsNullCriterion criterion) {
			whereSql.append(" ").append(toColumnName(criterion.getFieldName())).append(criterion.isIsnull()?" is null ":" is not null ");
		}

		@Override
		public void visit(OrderbyCriterion criterion) {
			whereSql.append(" ").append(toColumnName(criterion.getFieldName())).append(" ").append(criterion.getDesc());
		}

		@Override
		public void visit(BetweenCriterion criterion) {
			whereSql.append(" ").append(toColumnName(criterion.getFieldName())).append(" between :").append(criterion.getFieldName()).append(valueExt1).append(" and :").append(criterion.getFieldName()).append(valueExt2).append(" ");
			params.put(criterion.getFieldName()+valueExt1,criterion.getValue());
			params.put(criterion.getFieldName()+valueExt2,criterion.getValue2());
		}

	}

}
