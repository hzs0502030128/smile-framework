package org.smile.db.jdbc;

import java.util.LinkedList;
import java.util.List;

import org.smile.db.PageModel;
import org.smile.db.criteria.AbstractCriteria;
import org.smile.db.criteria.BaseCriterionVisitor;
import org.smile.db.criteria.BetweenCriterion;
import org.smile.db.criteria.ConditionCriterion;
import org.smile.db.criteria.Criterion;
import org.smile.db.criteria.IsNullCriterion;
import org.smile.db.criteria.OperatorCriterion;
import org.smile.db.criteria.OrderbyCriterion;
import org.smile.db.criteria.OtherFieldCriterion;
import org.smile.db.criteria.Restrictions;
import org.smile.db.criteria.SimpleCriterion;
import org.smile.db.handler.OneFieldRowHandler;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.util.ObjectLenUtils;
/**
 *    使用jdbc实现的一个Criteria操作
 * @author 胡真山
 *
 */
public abstract class RecordCriteriaImpl<E> extends AbstractCriteria<E>{
	/**用于操作record的Dao实例*/
	protected RecordDao<E> recordDao;
	
	public RecordCriteriaImpl(RecordDao<E> recordDao) {
		this.recordDao=recordDao;
	}
	
	@Override
	public List<E> list(){
		BoundSql boundSql=buildBoundSql();
		if(this.limit>0) {
			return recordDao.queryLimit(offset, limit,boundSql.getSql(), (Object[])boundSql.getParams());
		}
		return recordDao.query(boundSql.getSql(), (Object[])boundSql.getParams());
	}
	
	@Override
	public E first(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryFirst(boundSql.getSql(), (Object[])boundSql.getParams());
	}
	
	@Override
	public PageModel<E> queryPage(int page,int size){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryPage(page, size,boundSql.getSql(), (Object[])boundSql.getParams());
	}
	
	
	@Override
	public List<E> queryAll(){
		return recordDao.queryAll();
	}

	@Override
	public int deleteAll(){
		return recordDao.deleteAll();
	}

	@Override
	public E uinque(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryUnique(boundSql.getSql(), (Object[])boundSql.getParams());
	}

	/**
	 * 生成条件语句的查询与参数
	 * @return
	 */
	protected BoundSql buildBoundSql() {
		StringBuilder whereSql=new StringBuilder();
		List<Object> params=new LinkedList<Object>();
		RecordCriterionVisitor visitor=new RecordCriterionVisitor(whereSql,params);
		for(Criterion c:this.criterions) {
			c.accept(visitor);
		}
		if(this.orderby.size()>0) {
			whereSql.append(" ORDER BY ");
			for(Criterion c:this.orderby) {
				c.accept(visitor);
			}
		}
		return new ArrayBoundSql(whereSql.toString(), params.toArray());
	}
	
	protected  class RecordCriterionVisitor extends BaseCriterionVisitor{
		/***参数占位符*/
		String parameterFlag="?";
		/**条件语句*/
		StringBuilder whereSql;
		/**查询条件参数值*/
		protected List<Object> params;
		
		RecordCriterionVisitor(StringBuilder whereSql,List<Object> params){
			this.whereSql=whereSql;
			this.params=params;
		}

		@Override
		public void visit(ConditionCriterion criterion) {
			whereSql.append("(");
			criterion.getLeft().accept(this);
			whereSql.append(" ").append(criterion.getOp()).append(" ");
			criterion.getRight().accept(this);
			whereSql.append(")");
		}
		@Override
		public void visit(OtherFieldCriterion criterion) {
			whereSql.append(criterion.getFieldName()).append(" ").append(criterion.getOp()).append(" ").append(criterion.getOtherField()).append(" ");
		}
		
		@Override
		public void visit(SimpleCriterion criterion) {
			whereSql.append(criterion.getFieldName()).append(" ").append(criterion.getOp()).append(" ");
			Object paramValue=criterion.getValue();
			if(Restrictions.IN.equalsIgnoreCase(criterion.getOp())) {
				if(ObjectLenUtils.hasLength(paramValue)){
					int len=ObjectLenUtils.len(paramValue);
					StringBuilder sb=new StringBuilder("(");
					for(int j=0;j<len;j++){
						sb.append(" ? ");
						if(j<len-1){
							sb.append(",");
						}
						Object indexVal=ObjectLenUtils.get(paramValue,j);
						this.params.add(indexVal);
					}
					sb.append(")");
					whereSql.append(sb);
				}else {
					whereSql.append("(?)");
					this.params.add(paramValue);
				}
			}else {
				whereSql.append("?");
				this.params.add(paramValue);
			}
			whereSql.append(" ");
		}
		
		@Override
		public void visit(IsNullCriterion criterion) {
			whereSql.append(" ").append(criterion.getFieldName()).append(criterion.isIsnull()?" is null ":" is not null ");
		}

		@Override
		public void visit(OperatorCriterion criterion) {
			whereSql.append(" ").append(criterion.getValue()).append(" ");
		}
		
		@Override
		public void visit(OrderbyCriterion criterion) {
			whereSql.append(" ").append(criterion.getFieldName()).append(" ").append(criterion.getDesc());
		}
		
		@Override
		public void visit(BetweenCriterion criterion) {
			whereSql.append(" ").append(criterion.getFieldName()).append(" between ").append(parameterFlag).append(" and ").append(parameterFlag).append(" ");
			params.add(criterion.getValue());
			params.add(criterion.getValue2());
		}
	}

	@Override
	public int delete(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.delete(boundSql.getSql(), (Object[])boundSql.getParams());
	}
	@Override
	public long count(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryCount(boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public List<E> top(int top){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryTop(top,boundSql.getSql(), (Object[])boundSql.getParams());
	}
	/**
	 * 转换单个查询字段
	 * @return
	 */
	private String oneField() {
		return this.queryFields.get(0);
	}
	/**
	 * 转换单个字段查询为数组形式
	 * @return
	 */
	private String[] oneFields() {
		return new String[]{this.queryFields.get(0)};
	}
	/**
	 * 转换查询字段
	 * @return
	 */
	private String[] allFields() {
		return this.queryFields.toArray(new String[this.queryFields.size()]);
	}

	@Override
	public <T> List<T> listField(Class<T> resClass){
		BoundSql boundSql=buildBoundSql();
		if(this.limit>0) {
			return this.recordDao.queryLimitList(offset, limit, oneFields(), OneFieldRowHandler.instance(resClass),boundSql.getSql(), (Object[])boundSql.getParams());
		}
		return recordDao.queryOneFieldList(oneField(), resClass,boundSql.getSql(), (Object[])boundSql.getParams());
		
	}

	@Override
	public <T> T forField(Class<T> resClass){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryOneField(oneField(), resClass,boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public Integer forInt(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryForInt(oneField(),boundSql.getSql(), (Object[])boundSql.getParams());
		
	}

	@Override
	public Long forLong(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryForLong(oneField(),boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public String forString(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryForString(oneField(),boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public Double forDouble(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryForDouble(oneField(),boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public List<String> listString() {
		BoundSql boundSql=buildBoundSql();
		if(this.limit>0) {
			return recordDao.queryLimitList(offset, limit, oneFields(), OneFieldRowHandler.STRING,boundSql.getSql(), (Object[])boundSql.getParams());
		}
		return recordDao.queryForStringList(oneField(),boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public <T> List<T> listObject(Class<T> res){
		BoundSql boundSql=buildBoundSql();
		if(this.limit>0) {
			return recordDao.queryLimitObjectList(offset, limit, allFields(), res, boundSql.getSql(), (Object[])boundSql.getParams());
		}
		return recordDao.queryForObjectList(allFields(),res,boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public <T> T forObject(Class<T> res){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryForObject(allFields(),res,boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public List<ResultSetMap> listMap() {
		BoundSql boundSql=buildBoundSql();
		if(this.limit>0) {
			return recordDao.queryLimitMapList(offset, limit, allFields(),boundSql.getSql(), (Object[])boundSql.getParams());
		}
		return recordDao.queryForMapList(allFields(),boundSql.getSql(), (Object[])boundSql.getParams());
	}

	@Override
	public ResultSetMap forMap(){
		BoundSql boundSql=buildBoundSql();
		return recordDao.queryForMap(allFields(),boundSql.getSql(), (Object[])boundSql.getParams());
	}

}
