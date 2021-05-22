package org.smile.orm.xml.execut;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.orm.SqlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "mapper")
public class MapperXml {
	/** 部分方法实现目标*/
	@XmlAttribute
	protected String target;
	@XmlAttribute
	protected String namespace;
	@XmlAttribute
	protected String template;
	@XmlAttribute
	protected String include;
	@XmlAttribute
	protected boolean single = true;
	@XmlAttribute
	protected String sqlType = SqlType.SQL;
	/**用于包含的片断*/
	protected List<Snippet> snippet;
	/**查询标签配置*/
	protected List<SelectOperator> select;
	/**插入标签配置*/
	protected List<InsertOperator> insert;
	/**删除标签配置*/
	protected List<DeleteOperator> delete;
	/**更新标签配置*/
	protected List<UpdateOperator> update;
	/** 批量操作*/
	protected List<BatchOperator> batch;
	/**xml最后修改时间,自动加载时使用*/
	protected long xmlUpdateTimes = -1;
	/**class最后修改时间,自动加载时使用*/
	protected long classUpdateTimes = -1;

	public List<SelectOperator> getSelect() {
		return select;
	}

	public List<InsertOperator> getInsert() {
		return insert;
	}

	public List<DeleteOperator> getDelete() {
		return delete;
	}

	public List<UpdateOperator> getUpdate() {
		return update;
	}

	public List<BatchOperator> getBatch() {
		return batch;
	}

	public List<Snippet> getSnippet() {
		return snippet;
	}

	public String getTarget() {
		return target;
	}

	/**
	 * 是否是单例的
	 * @return
	 */
	public boolean isSingle() {
		return single;
	}

	/**
	 * 模板类型  可为空 为空是系统会设置一默认的值为 freemark
	 * @return freemark groovy
	 */
	public String getTemplate() {
		return template;
	}

	public long getXmlUpdateTimes() {
		return xmlUpdateTimes;
	}

	public void setXmlUpdateTimes(long xmlUpdateTimes) {
		this.xmlUpdateTimes = xmlUpdateTimes;
	}

	public long getClassUpdateTimes() {
		return classUpdateTimes;
	}

	public void setClassUpdateTimes(long classUpdateTimes) {
		this.classUpdateTimes = classUpdateTimes;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	
}
