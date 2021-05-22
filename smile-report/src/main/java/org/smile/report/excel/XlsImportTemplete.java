package org.smile.report.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.smile.beans.MapBeanClass;
import org.smile.beans.PropertyValue;
import org.smile.beans.converter.BeanException;
import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.SmileRunException;
import org.smile.io.IOUtils;
import org.smile.json.JSON;
import org.smile.report.excel.target.BeanImportTarget;
import org.smile.report.excel.target.ImportTarget;
import org.smile.report.excel.target.MapBeanImportTarget;
import org.smile.report.excel.target.MapImportTarget;
import org.smile.report.poi.ExcelException;
import org.smile.util.StringUtils;
/**
 * excel导入工具类
 * @author 胡真山
 *
 */
public class XlsImportTemplete extends Template{
	
	private Map<String,Object> currentDetailValue=new HashMap<String, Object>();
	/**读取到的标题*/
	protected String[] titles;
	/**当前导入页名称*/
	protected String sheetName;
	/**导入封装目标*/
	protected ImportTarget importTarget;
	/**
	 * 导入的文件名称
	 */
	protected String fileName;
	/**
	 * 导入字段内容转换函数
	 * 可以通过 regFieldConvert 方法 注入
	 */
	protected Map<String,FieldConvert> fieldConvert;
	/**
	 * 从一个文件创建导入模板
	 * @param file
	 */
	public XlsImportTemplete(File file){
		FileInputStream is=null;
		try {
			is=new FileInputStream(file);
			this.workbook=createWorkbook(is);
			fileName=file.getName();
		} catch (Exception e) {
			throw new SmileRunException("无法从文件加载"+file+"成一个excel",e);
		}finally{
			IOUtils.close(is);
		}
	}
	/**
	 * 从一个流创建导入模板
	 * @param is
	 */
	public XlsImportTemplete(InputStream is){
		this.workbook=createWorkbook(is);
	}
	/**
	 * 从一个工作表创建导入模板
	 * @param workbook
	 */
	public XlsImportTemplete(Workbook workbook){
		this.workbook=workbook;
	}
	/**
	 * 赋值对象类型
	 * @param clazz
	 */
	public void initTargetClass(Class clazz){
		if(Map.class.isAssignableFrom(clazz)){
			this.importTarget=new MapImportTarget(clazz);
		}else{
			this.importTarget=new BeanImportTarget(clazz);
		}
	}
	/***
	 * 使用类型封装
	 * @param mapBeanClass
	 */
	public void initTargetClass(MapBeanClass mapBeanClass){
		this.importTarget=new MapBeanImportTarget(mapBeanClass);
	}
	/**
	 * 初始化一个对象用于赋值
	 * @return
	 */
	protected Object newTargetInstanse(){
		return importTarget.newTargetInstanse();
	}
	/***
	 * 反射get方法取属性值
	 * @param oneData
	 * @param field
	 * @return
	 * @throws Exception
	 */
	protected  void invokeSetValue(Object oneData,String field,Object value) throws Exception {
		if(StringUtils.isNotNull(value)){
			importTarget.setFieldValue(oneData, field, value);
		}
	}
	/**
	 * 字段转换
	 * @param exp
	 * @param value
	 * @return
	 */
	private Object convertField(String exp,Object value){
		if(this.fieldConvert==null){
			return value;
		}
		FieldConvert c=this.fieldConvert.get(exp);
		if(c!=null){
			return c.convert(value);
		}
		return value;
	}
	/**
	 * 设置值
	 * @param target
	 * @param exp
	 * @param value
	 * @throws Exception
	 */
	protected void invokeExpValue(Object target,String exp,Object value) throws Exception{
		value =convertField(exp, value);
		if(exp.startsWith(PARAM_FLAG)){
			this.addParam(exp.substring(1), value);
		}else{
			int index=exp.indexOf(".");
			if(index>0){
				String name=exp.substring(0,index);
				Object subObject=getSubObject(target, name);
				if(subObject instanceof Collection){
					//是列表集合的时候，对明细列表进行设置值
					invokeSetValue(target,name, subObject);
					invokeExpValue(this.currentDetailValue.get(name), exp.substring(index+1), value);
				}else{
					invokeExpValue(subObject, exp.substring(index+1), value);
				}
			}else{
				try{
					invokeSetValue(target,exp, value);
				}catch(Exception e){
					throw new BeanException("设置"+target +"的属性"+exp+" 值:"+value+"出错",e);
				}
			}
		}
	}
	/***
	 * 获取子对象的属性
	 * @param targetObject
	 * @param name
	 * @return
	 * @throws Exception
	 */
	protected Object getSubObject(Object targetObject,String name) throws Exception{
		PropertyValue value=importTarget.getFieldValue(targetObject, name, true);
		if(value!=null){
			Class type=value.type();
			if(type==List.class&&this.currentDetailValue.get(name)==null){
				Object obj=null;
				if(!value.hasGeneric()){
					Class objClass=value.getDeclare().getGeneric().values[0];
					obj=objClass.newInstance();
				}else{
					obj=new HashMap();
				}
				List list=(List)value;
				list.add(obj);
				this.currentDetailValue.put(name, obj);
			}
			return value.value();
		}
		return null;	
	}
	/**
	 * 对标题内容进行验证
	 * @return
	 */
	protected boolean validateTitles() throws ExcelException{
		return true;
	}
	
	/***
	 * 
	 * @param properties 要读取的列索引与封闭对象字段的映射
	 * @return
	 */
	public <T> List<T> readDataToList(Map<Integer,String> properties){
		if(importTarget==null){//没有设置导入封装目标的时候使用map封装
			importTarget=new MapImportTarget();
		}
		//读取数据
		Sheet sheet =workbook.getSheetAt(sheetIndex);
		this.sheetName=sheet.getSheetName();
		int lastRow=sheet.getLastRowNum();
		if(bottomRowCount>0){
			lastRow=lastRow-bottomRowCount;
		}
		Row titleRow=sheet.getRow(titleRowIndex);
		short lastColNum=titleRow.getLastCellNum();
		short firstColNum=titleRow.getFirstCellNum();
		titles=getRowStringValues(titleRow,firstColNum , lastColNum);
		if(!validateTitles()){
			throw new ExcelException("不正确的模板标题:"+JSON.toJSONString(titles));
		}
		T targetObject=null;
		List<T> list=new ArrayList<T>(lastRow);
		//开始读取数据
		for(int index=titleRowIndex+1;index<=lastRow;index++){
			Row dataRow=sheet.getRow(index);
			if(dataRow!=null){
				if(!onBeforeReadRow(dataRow,properties)){
					continue;
				}
				int collIndex=0;
				String colPropertyName=null;
				//关键列
				Integer keyCol=null;
				for(Map.Entry<Integer,String> entry:properties.entrySet()){
					collIndex=entry.getKey();
					colPropertyName=entry.getValue();
					Cell cell=dataRow.getCell(collIndex);
					Object value=getCellValue(cell,colPropertyName,index,collIndex);
					if(keyCol==null){//处理关键列，第一列为关键列，新建对象
						keyCol=collIndex;
						Object tempObject=createRowTargetObj(dataRow, value);
						if(tempObject!=null){
							targetObject=(T)tempObject;
							list.add(targetObject);
						}
					}
					try{
						setTargetObjectField(targetObject, colPropertyName, value, index, collIndex);
					}catch(SmileRunException ee){
						throw ee;
					}catch(Exception e){
						logger.error(fileName+" "+properties+" "+this.sheetName+":row->"+index+" column->"+collIndex,e);
					}
				}
				onAfterReadRow(targetObject,dataRow);
			}else if(onReadNullRowData(properties, index)){
				break;
			}
		}
		return list;
	}
	/**
	 * 读取空行时
	 * 当返回true时会结束整个文件的读取 
	 * @return 是否结束整个文件的读取
	 */
	protected boolean onReadNullRowData(Map<Integer,String> properties,int rowIndex){
		logger.warn(fileName+" "+properties+" "+this.sheetName+":row->"+rowIndex+" 存在空行");
		return false;
	}
	/**
	 * 读取关键属性的时候创建对象
	 * @param dataList
	 * @param dataRow
	 * @param keyPropertyValue
	 * @return
	 */
	protected Object createRowTargetObj(Row dataRow,Object keyPropertyValue){
		Object result=null;
		//第一列的时候创建新对象，所以第一列不能为空
		if(StringUtils.isNotNull(keyPropertyValue)){
			result=newTargetInstanse();
		}
		this.currentDetailValue.clear();
		return result;
	}
	
	protected Object getCellValue(Cell cell,String propertyExp,int rowIndex,int colIndex){
		return getCellValue(cell);
	}
	/**
	 * 设置一个字段的值
	 * @param targetObject 当前对象目标
	 * @param propertyName 属性表达式
	 * @param value 单元格的值
	 * @param rowIndex 行索引
	 * @param colIndex 列索引
	 * @throws Exception 
	 */
	protected void setTargetObjectField(Object targetObject,String propertyName,Object value,int rowIndex,int colIndex) throws Exception{
		invokeExpValue(targetObject,propertyName,value);
	}
	/**
	 * 读取一行结束
	 * @param targetObject
	 * @param currentRow
	 */
	protected void onAfterReadRow(Object targetObject,Row currentRow){
		//to nothing
	}
	/**
	 * 在读取一行数据之前
	 * @param currentRow
	 * @return 如果返回false 跳过这一行
	 */
	protected boolean onBeforeReadRow(Row currentRow,Map<Integer,String> properties){
		return true;
	}
	
	/**
	 * 标是题行的
	 * @return
	 */
	public String[] getTitles() {
		return titles;
	}
	/**
	 * 注册一个字段转换函数
	 * @param exp 字段名
	 * @param convert 转换接口
	 */
	public void regFieldConvert(String exp,FieldConvert convert){
		if(fieldConvert==null){
			fieldConvert=new KeyNoCaseHashMap<FieldConvert>();
		}
		fieldConvert.put(exp, convert);
	}
	
	/**
	 * sheet页的名称
	 * @return
	 */
	public String getSheetName() {
		return workbook.getSheetName(sheetIndex);
	}
	
	
}
