package org.smile.orm.mapping;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.annotation.AnnotationUtils;
import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.db.sql.SQLHelper;
import org.smile.orm.AssociationConfig;
import org.smile.orm.OrmInitException;
import org.smile.orm.ann.Association;
import org.smile.orm.ann.Table;
import org.smile.orm.mapping.flag.PropertyFlag;
import org.smile.orm.mapping.property.EnableFlagProperty;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.orm.mapping.property.OrmIdProperty;
import org.smile.orm.mapping.property.OrmProperty;
import org.smile.reflect.FieldUtils;

/**
 * 与数据库表对象的一个mapper
 * 
 * @author 胡真山
 * @param <V>
 */
public class OrmTableMapping<V> extends OrmObjMapping<V>{
	/***
	 * 所有的注解了table的类的解析
	 */
	private final static Map<Class, OrmTableMapping> persistenceTypeMap = new ConcurrentHashMap<Class, OrmTableMapping>();
	/***
	 * 所有的注解了table的类的解析
	 */
	private final static Map<String, OrmTableMapping> nameTypeMap = new ConcurrentHashMap<String, OrmTableMapping>();
	
	/**查询所有数据的SQL*/
	private String selectAllSql;
	private String selectByIdSql;
	private String insertSql;
	private String updateAllSql;
	private String updateByIdSql;
	private String deleteAllSql;
	private String deleteByIdSql;
	private String enableAllSql;
	private String enableByIdSql;
	/**
	 * 需要插入的字段 当是自增字段时 是不需要插入的
	 */
	private Map<String,OrmProperty> needInsertColumns=new LinkedHashMap<String, OrmProperty>();
	/**
	 * 主键字段属性
	 */
	private OrmIdProperty primaryProperty;
	/**启用功能字段 */
	private EnableFlagProperty enableProperty;
	
	/**不包括主键的所有列名*/
	private  List<String> nokeyPropertyNames;
	
	/**一对多的配置信息*/
	private final Map<String,AssociationConfig> oneToManyConfig=new LinkedHashMap<String,AssociationConfig>();
	
	/**
	 * 注入一个映射
	 * @param clazz
	 * @param mapper
	 */
	private static void registMapper(Class clazz,OrmTableMapping mapper){
		persistenceTypeMap.put(clazz, mapper);
		nameTypeMap.put(clazz.getSimpleName(), mapper);
	}
	
	public static <T> OrmTableMapping<T> getType(String type){
		if(type.indexOf('.')>0){//如是有点存在 则认为是以类做为名称的映射
			try {
				return getType(Class.forName(type));
			} catch (ClassNotFoundException e) {
				throw new SmileRunException("not has a mapper class "+type, e);
			}
		}
		return nameTypeMap.get(type);
	}
	
	/**
	 * 获取映射 如没初始化 则初化
	 * 但此方法初始化的只是 OrmTableMapper 类型 
	 * 占位符是? 
	 * @param clazz
	 * @return
	 */
	public static <V> OrmTableMapping<V> getType(Class clazz) {
		OrmTableMapping<V> persistenceType = persistenceTypeMap.get(clazz);
		if (persistenceType == null) {
			synchronized (clazz) {
				persistenceType = persistenceTypeMap.get(clazz);
				if (persistenceType == null) {
					persistenceType = new OrmTableMapping<V>();
					try {
						persistenceType.initType(clazz);
					} catch (MappingException e) {
						throw new OrmInitException("init tablemapper class "+clazz+" error ",e);
					}
					persistenceType.initProperties();
					registMapper(clazz, persistenceType);
				}
			}
		}
		return persistenceType;
	}

	/**
	 * 主键列名
	 * @return
	 */
	public String getPrimaryKey() {
		return primaryProperty.getColumnName();
	}

	/**
	 * 获取当前表映射的插入语句
	 * @return
	 */
	public String getInsertSql() {
		return insertSql;
	}
	
	/**
	 * 构建一个update语句
	 * @param propertyNames 需要更新的字段
	 * @return
	 */
	public StringBuilder getUpdateSql(String[] propertyNames) {
		return SQLHelper.getUpdateAllSql(name, propertyNamesToColumns(propertyNames));
	}
	
	/**
	 * 多个属性名转成字段名
	 * @param propertyNames
	 * @return
	 */
	private Collection<String> propertyNamesToColumns(String[] propertyNames){
		if(ArrayUtils.isEmpty(propertyNames)) {
			return this.columnMap.keySet();
		}
		List<String> columns = new ArrayList<String>(propertyNames.length);
		for (String propertyName : propertyNames) {
			OrmProperty property = getProperty(propertyName);
			if (property == null) {
				logger.error(this.rawClass.getName() + "不存在的字段名称:" + propertyName);
			}else if (property.isPersistence()) {
				columns.add(property.getColumnName());
			}
		}
		return columns;
	}
	/**
	 *    以属性名获取查询相应字段的查询语句
	 * @param propertyNames
	 * @return
	 */
	public StringBuilder getSelectSql(String[] propertyNames){
		return SQLHelper.getSelectAllSql(name, propertyNamesToColumns(propertyNames));
	}
	
	@Override
	public void initType(Class clazz) throws MappingException{
		rawClass = clazz;
		this.tableFlag = flagHandler.getTableFlag(clazz);
		if (tableFlag == null||!tableFlag.isTable()) {
			throw new MappingException(clazz+" 没有对应的注解:"+Table.class);
		} else {
			name = tableFlag.getName();
		}
		initProperties();
		initMixOrmMappings();
	}

	@Override
	protected void initProperties() {
		Map<String,Field> fields = FieldUtils.getAnyNoStaticField(rawClass);
		/**id属性的名称 */
		for (Field field : fields.values()) {
			try {
				Association association =AnnotationUtils.getAnnotation(field,Association.class);
				if(association!=null){
					AssociationConfig config=new AssociationConfig(this,field, association.className(), association.foreignKey(),association.type());
					this.oneToManyConfig.put(field.getName(), config);
				}else{
					PropertyFlag propertyFlag=flagHandler.getPropertyFlag(tableFlag,field);
					if (propertyFlag != null) {
						OrmFieldProperty property=propertyFlag.getProperty(this);
						propertyMap.put(property.getPropertyName(), property);
						columnMap.put(property.getColumnName(), property);
						enableProperty=propertyFlag.getEnableFlagProperty(property);
						//主键字段
						if(propertyFlag.isPrimaryKey()){
							this.primaryProperty=propertyFlag.getIdProperty(property);
						}
						initNote(field, property);
					}else {
						initComponent(field);
					}
				}
			} catch (Exception e) {
				logger.error("初始化属性异常"+rawClass, e);
			}
		}
		this.nokeyPropertyNames=new LinkedList<String>(this.propertyMap.keySet());
		if(hasPrimaryKey()){//不包含主键的属性列表
			this.nokeyPropertyNames.remove(primaryProperty.getPropertyName());
		}else{
			this.primaryProperty=new OrmIdProperty(propertyMap.get(nokeyPropertyNames.get(0)));
			logger.warn("not config a id field "+rawClass+" use first field for key field when delete and update ");
		}
		convertOneToManyKey();
		initSql();
	}
	/**
	 * 是否有主键
	 * @return
	 */
	public boolean hasPrimaryKey(){
		return this.primaryProperty!=null;
	}
	/**
	 * 转换一下onetomay的外键名 
	 * 当注解中指定的时属性名的时候转换为数据库字段名
	 */
	private void convertOneToManyKey(){
		for(AssociationConfig config:oneToManyConfig.values()){
			String key=config.getForeignKey();
			OrmTableMapping mapper=OrmTableMapping.getType(config.getClazz());
			OrmProperty property=mapper.getProperty(key);
			if(property!=null){
				config.setForeignKey(property.getColumnName());
			}
		}
	}
	/***
	 * 读取目标对象的主键值
	 * @param target
	 * @return
	 */
	public Object getPrimaryKeyValue(Object target){
		return primaryProperty.getKeyValue(target);
	}
	/**
	 * 设置主键
	 * @param target
	 * @param value
	 */
	public void setPrimarKeyValue(Object target,Object value){
		primaryProperty.setKeyValue(target, value);
	}
	/**
	 * 初始化一些sql语句
	 */
	protected void initSql(){
		String primaryKey=getPrimaryKey();
		//需要存贮的字段
		List<String> columns =new ArrayList<String>(columnMap.size());
		//插入语句会根据是否是自动增长设置字段
		for(OrmProperty p:columnMap.values()){
			if(p.isPersistence()){
				if(!p.isAtuoincrement()){
					needInsertColumns.put(p.getColumnName(),p);
				}
				columns.add(p.getColumnName());
			}
		}
		selectAllSql = SQLHelper.getSelectAllSql(name, columns).toString();
		deleteAllSql = SQLHelper.getDeleteAllSql(name).toString();
		deleteByIdSql = deleteAllSql + " WHERE " + primaryKey + " = "+getKeyPropertyExp();
		selectByIdSql=selectAllSql+" WHERE " +primaryKey+" = "+getKeyPropertyExp();
		
		//插入语句
		insertSql = SQLHelper.getInsertSql(needInsertColumns.values(),name).toString();
		updateAllSql = SQLHelper.getUpdateAllSql(needInsertColumns.values(),name).toString();
		updateByIdSql=updateAllSql+" WHERE "+primaryKey+" = "+getKeyPropertyExp();
		if(supportDisable()){//支持启用的语句
			enableAllSql="UPDATE "+name+" SET "+enableProperty.getColumnName()+"="+enableProperty.getPropertyExp();
			enableByIdSql=enableAllSql+" WHERE "+primaryKey+" = "+getKeyPropertyExp();
			enableAllSql+=" WHERE ("+enableProperty.getColumnName()+"="+enableProperty.getPropertyExp()+" OR "+enableProperty.getColumnName()+" IS NULL)";
		}
	}
	/**
	 * 主键占位匹配符
	 * @return
	 */
	protected String getKeyPropertyExp(){
		return primaryProperty.getPropertyExp();
	}
	/**
	 * 主键
	 * @return
	 */
	public OrmIdProperty getPrimaryProperty() {
		return primaryProperty;
	}
	/**
	 * 可是使用数据有效设置功能
	 * @return
	 */
	public boolean supportDisable(){
		return enableProperty!=null;
	}
	/**
	 * 字段名注释
	 * @param propertyName
	 * @return
	 */
	public String getPropertyNote(String propertyName){
		OrmProperty ofp= propertyMap.get(propertyName);
		if(ofp==null){
			return Strings.BLANK;
		}
		return ofp.getNote();
	}
	/**
	 * 以id为关键的删除语句
	 * @return
	 */
	public String getDeleteByIdSql() {
		return deleteByIdSql;
	}

	public void setDeleteByIdSql(String deleteByIdSql) {
		this.deleteByIdSql = deleteByIdSql;
	}

	public String getSelectAllSql() {
		return selectAllSql;
	}

	public String getSelectByIdSql() {
		return selectByIdSql;
	}

	public String getUpdateAllSql() {
		return updateAllSql;
	}

	public String getUpdateByIdSql() {
		return updateByIdSql;
	}
	
	public String getDeleteAllSql() {
		return deleteAllSql;
	}
	/**
	 * 需要插入和更新的字段
	 * 不包括自动增长的字段 
	 * @return 需要插入的更新的字段的属性
	 */
	public Collection<OrmProperty> columnPropertys(){
		return this.needInsertColumns.values();
	}
	
	public List<String> getNokeyPropertyNames() {
		return this.nokeyPropertyNames;
	}
	@Override
	public boolean hasOneToMany(){
		return !this.oneToManyConfig.isEmpty();
	}

	public Map<String, AssociationConfig> getOneToManyConfig() {
		return oneToManyConfig;
	}
	/**
	 * 有效设置功能字段的属性
	 * @return
	 */
	public EnableFlagProperty getEnableProperty() {
		return enableProperty;
	}

	public String getEnableAllSql() {
		return enableAllSql;
	}

	public String getEnableByIdSql() {
		return enableByIdSql;
	}

	@Override
	public String toString() {
		return getClass().getName()+" mapper "+name;
	}
	/**
	 * 是否是自动增长的主键
	 * @return
	 */
	public boolean isAtuoincrementPrimaryKey(){
		return primaryProperty!=null&&primaryProperty.isAtuoincrement();
	}
}
