package org.smile.db.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.db.JdbcTemplate;
import org.smile.db.SqlRunException;
import org.smile.db.handler.RecordSetMap;
import org.smile.db.sql.BoundSql;
import org.smile.expression.DefaultContext;
import org.smile.expression.Engine;

/**
 * 包含表信息的一个map
 * @author 胡真山
 *
 */
public  class JdbcMapRecord extends RecordSetMap implements EnableSupportRecord{
	/**操作数据库jdbc模板*/
	protected JdbcTemplate  jdbcTemplate;
	/**表配置信息*/
	private TableInfoCfg  cfg;
	
	protected JdbcMapRecord(TableInfoCfg cfg){
		this.cfg=cfg;
		this.jdbcTemplate=JdbcRecordConfig.instance.getJdbcTemplate();
		this.keyColumnSwaper=cfg.keyColumnSwaper();
	}
	
	public TableInfoCfg cfg(){
		return this.cfg;
	}
	
	/**
	 * 设置主键的值
	 * @param value
	 */
	public JdbcMapRecord setKeyFields(Object ... value){
		if(ArrayUtils.isEmpty(cfg.keyFields)){
			throw new SmileRunException("key field is cant null");
		}
		if(cfg.keyFields.length!=value.length){
			throw new SmileRunException("key filed length must "+cfg.keyFields.length);
		}
		for(int i=0;i<cfg.keyFields.length;i++){
			put(cfg.keyFields[i],value[i]);
		}
		return this;
	}
	/**
	 * 设置属性值
	 * @param field 名称
	 * @param value 值
	 * @return
	 */
	public JdbcMapRecord set(String field,Object value){
		this.put(field, value);
		return this;
	}
	
	/***
	 * 设置主键的值
	 * @param value 主键值
	 * @return
	 */
	public JdbcMapRecord setId(Object value){
		if(ArrayUtils.isEmpty(cfg.keyFields)){
			throw new IllegalArgumentException("key field is cant null");
		}
		if(cfg.keyFields.length!=1){
			throw new IllegalArgumentException("key filed length must "+cfg.keyFields.length);
		}
		String keyField=cfg.keyFields[0];
		this.put(keyField, value);
		return this;
	}

	@Override
	public void update(){
		jdbcTemplate.update(this);
	}

	@Override
	public void update(String[] fields) {
		jdbcTemplate.update(cfg, this,fields);
	}
	/**
	 * 复制字段值
	 * @param keys
	 * @return
	 */
	protected Map<String,Object> clone(String[] keys){
		Map<String,Object> map=new HashMap<String,Object>();
		for(String key:keys){
			map.put(key, this.get(key));
		}
		return map;
	}

	@Override
	public void delete(){
		jdbcTemplate.delete(this);
	}

	@Override
	public void delete(String where)  {
		Boolean delete=(Boolean) Engine.getInstance().evaluate(new DefaultContext(this), where);
		if(delete){
			jdbcTemplate.delete(this);
		}
	}

	@Override
	public void insert(){
		if(cfg.enabledSupport()) {
			if(!this.containsKey(cfg.enabledField)) {
				put(cfg.enabledField, cfg.enableValue);
			}
		}
		jdbcTemplate.insert(this);
	}

	@Override
	public void load(){
		load((String[])null);
	}

	@Override
	public void load(String[] fields) {
		BoundSql sql=cfg.getSelectBoundSql(fields, this);
		jdbcTemplate.query(sql, new RecordMapRowHandler(cfg) {
			@Override
			protected Map<String, Object> buildMap() {
				return JdbcMapRecord.this;
			}
		});
	}

	@Override
	public void save(){
		jdbcTemplate.saveOrUpdate(cfg,this);
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void enabled() {
		this.set(cfg.enabledField, cfg.enableValue);
		update(new String[] {cfg.enabledField});
	}

	@Override
	public void disabled() {
		this.set(cfg.enabledField, cfg.disableValue);
		update(new String[] {cfg.enabledField});
	}

	@Override
	public void update(String first, String... strings) {
		String[] fields=new String[strings.length+1];
		fields[0]=first;
		if(strings.length>0) {
			System.arraycopy(strings, 0, fields, 1, strings.length);
		}
		jdbcTemplate.update(cfg, this,fields);
	}

	@Override
	public void load(String first, String... strings) {
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
			this.put(fields[i], values[i]);
		}
		this.update(fields);
	}

}