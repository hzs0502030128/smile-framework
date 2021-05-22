package org.smile.report.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.smile.Smile;
import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.collection.CollectionUtils;
import org.smile.collection.WrapBeanAsMap;
import org.smile.commons.Strings;
import org.smile.expression.DefaultContext;
import org.smile.function.BaseFunctionInfo;
import org.smile.json.JSON;
import org.smile.json.JSONObject;
import org.smile.json.parser.JSONParseException;
import org.smile.report.XlsExportTemplateSupport;
import org.smile.report.function.BaseFunctionHandler;
import org.smile.report.function.FunctionHandler;
import org.smile.report.function.IFunction;
import org.smile.report.html.ToHtmlConvert;
import org.smile.report.poi.MergeSetLoop;
import org.smile.util.StringUtils;

/***
 * xls 模板 工具
 * 
 * templateDir   保存模板文件的根目录    
 * 可以在smile.properties 文件中配置 export.templateDir 的属性
 * @author 胡真山
 * 2015年7月28日
 */
public class XlsExportTemplate extends Template implements XlsExportTemplateSupport{
	/**
	 * 批注配置单元格索引
	 */
	protected static int[] commentConfig=new int[]{0,0};
	
	/**模板保存目录 */
	public static String templateDir=Smile.config.getProperty(Smile.EXPORT_TEMPLATE_DIR_KEY);
	
	/***
	 * 子表记录
	 */
	protected SubDataSourceCellInfo currentListCell;
	/**
	 * 设置合并列的内容
	 */
	protected List<MergeConfig> mergeConfigs;
	/**
	 * 用于统计行的合计值
	 */
	protected Sumer<Object> sumer;
	/***
	 * 数据填充名称行号
	 */
	protected int dataNameRowIndex=1;
	/**
	 * 填充数据名称行
	 */
	protected Row dataNameRow;
	
	/**首列*/
	protected short firstCellNum;
	/**
	 * 尾列
	 */
	protected short lastCellNum;
	/**
	 * 最大列数
	 */
	protected int maxColumnCount=-1;
	/**
	 * 填充后页脚行的索引
	 */
	protected int afterFillBottomRowIndex=-1;
	/**
	 * 填充后页脚的结束行索引
	 */
	protected int afterFillBottomRowEnd=-1;
	
	protected FunctionHandler functionHandler=new BaseFunctionHandler();
	/**
	 * 读取注解配置
	 */
	protected boolean readCommentConfig=true;
	
	/** 字段对应表达式 */
	protected String[] names;
	
	protected ExportContext context=new ExportContext();
	/**需要移动的行数*/
	protected int bottomNeedMove=0;
	
	/**
	 * 读取第一个单元格中的批注信息
	 * 做为配置文件 
	 * @param sheet
	 * @return
	 */
	protected CommentConfig readCommentConfig(Sheet sheet){
		//读取批注
		Comment comment=getCellComment(sheet, commentConfig[0], commentConfig[1]);
		if(comment!=null){
			String text=comment.getString().toString();
			try {
				//解释批注配置json格式为配置对旬
				JSONObject obj=new JSONObject(text);
				CommentConfig config=new CommentConfig();
				BeanUtils.mapToBean(obj,config);
				//删除批注
				sheet.getRow(commentConfig[0]).getCell(commentConfig[1]).removeCellComment();
				return config;
			} catch (JSONParseException e) {
				throw new CommentConfigReadException("读取注解配置信息成json对象失败:"+text+" 配置单元格:"+JSON.toJSONString(commentConfig),e);
			} catch (ConvertException e) {
				throw new CommentConfigReadException("注解配置赋值到"+CommentConfig.class+"失败", e);
			}
		}else if(logger.isDebugEnabled()){
			logger.debug("sheet :"+sheet.getSheetName()+" CommentConfig cell is null ");
		}
		return null;
	}
	/**
	 * 设置 数据填充名称行号
	 * @param dataNameRowIndex
	 */
	public void setDataNameRowIndex(int dataNameRowIndex) {
		this.dataNameRowIndex = dataNameRowIndex;
	}
	/**
	 * 获取文件名的完整路径
	 * @param name
	 * @return
	 */
	public static String getFilePath(String name){
		return templateDir+name;
	}
	
	public void setMaxColumnCount(int maxColumnCount) {
		this.maxColumnCount = maxColumnCount;
	}
	/**
	 * 创建一个工作表从一个模板文件中
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public  void loadXlsTemplate(String filename) throws IOException{
		this.workbook=createWorkbook(filename);
	}
	/**
	 * 从一个workbook中加载模板
	 * @param workbook
	 */
	public void loadXlsTemplate(Workbook workbook){
		this.workbook=workbook;
	}
	
	/**
	 * 读取注解配置信息
	 * @param sheet
	 */
	protected void loadCommentConfig(Sheet sheet){
		CommentConfig config=readCommentConfig(sheet);
		if(config!=null){
			config.setXlsExportTemplate(this);
		}
	}
	/**
	 * 处理表头配置
	 * @param sheet
	 */
	protected void doHeaderConfig(Sheet sheet){
		if(readCommentConfig){
			loadCommentConfig(sheet);
		}
		dataNameRow=sheet.getRow(dataNameRowIndex);
		firstCellNum=dataNameRow.getFirstCellNum();
		lastCellNum=dataNameRow.getLastCellNum();
		names=getRowStringValues(dataNameRow, firstCellNum, lastCellNum);
	}
	/***
	 * 往工作表中填充数据
	 * @param book
	 * @param dataSource
	 */
	public  void fillDataSource(Collection<?> dataSource){
		Sheet sheet =workbook.getSheetAt(sheetIndex);
		//当前行号设置为1
		dataRowNumber=1;
		doHeaderConfig(sheet);
		dataSize=dataSource.size();
		removeBottomRows(sheet);
		if(dataSize>0){
			doFillDataSource(sheet, dataSource);
		}else{
			doFillEmptyData();
		}
	}
	/**
	 * 处理填充数据
	 * @param sheet 
	 * @param dataSource
	 */
	protected void doFillDataSource(Sheet sheet,Collection<?> dataSource){
		int rowIndex=dataNameRowIndex;
		for(Object objV:dataSource){
			this.context.setRoot(objV);
			Row dataRow=onCreateDataRow(sheet, this.dataNameRow, objV, rowIndex);
			//设置一条数据的值，如果存在子集合，会记录子集合的属性
			setData(dataRow, names, firstCellNum, objV);
			if(sumer!=null){
				sumer.loop(objV);
			}
			//填充子集合
			rowIndex=writeSubDataSoruceCell(dataRow, sheet, rowIndex);
			rowIndex++;
			dataRowNumber++;
		}
		merge(sheet, dataSource);
	}
	/**
	 * 设置一行数据之前
	 * @param sheet
	 * @param dataRow
	 * @param currentRowData 当前行的数据 如是填充子数据集时刚是子数据集的数据
	 * @param rowIndex
	 */
	protected Row onCreateDataRow(Sheet sheet,Row keyRow,Object currentRowData,int rowIndex){
		Row dataRow=sheet.getRow(rowIndex);
		if(dataRow==null){
			dataRow=sheet.createRow(rowIndex);
			copyRow(keyRow, dataRow,false);
		}
		return dataRow;
	}
	
	/**
	 * 空数据时对数据填充处理
	 */
	protected void doFillEmptyData(){
		for(int i=firstCellNum;i<lastCellNum;i++){
			Cell cell=dataNameRow.getCell(i);
			setEmptyDataCellValue(cell);
		}
	}
	/**
	 * 没有数据时设置属性名单元格为空
	 * @param cell
	 */
	protected void setEmptyDataCellValue(Cell cell){
		cell.setCellValue(Strings.BLANK);
	}
	/**
	 * 设置单元格为空内容
	 */
	protected void setCellNullValue(Cell cell){
		if(dataRowNumber==1){
			cell.setCellValue(Strings.BLANK);
		}
	}
	
	/**
	 * 移动页脚行
	 * @param sheet
	 */
	protected void removeBottomRows(Sheet sheet){
		if(bottomRowIndex>0){
			//需要增加的空格数
			int blankCount=bottomRowIndex-dataNameRowIndex;
			if(dataSize>blankCount){
				//移动行
				int removeRows=(dataSize-blankCount);
				this.removeBottomRows(sheet, this.bottomRowIndex, this.bottomRowEnd, removeRows);
			}else{
				afterFillBottomRowIndex=bottomRowIndex;
				afterFillBottomRowEnd=bottomRowEnd;
			}
		}
	}
	/**
	 * 移动页尾
	 * @param sheet
	 * @param removeStart
	 * @param removeEnd
	 * @param removeRows
	 */
	public void removeBottomRows(Sheet sheet,int removeStart,int removeEnd,int removeRows){
		short[] bottomRowsHeight=new short[removeEnd-removeStart+1];
		int index=0;
		for(int i=removeStart;i<=removeEnd;i++){
			Row row=sheet.getRow(i);
			if(row==null){
				bottomRowsHeight[index++]=0;
				logger.info("sheet "+sheet.getSheetName() +" row index "+i+" is null ,please check bottom index set ");
			}else{
				bottomRowsHeight[index++]=row.getHeight();
			}
		}
		sheet.shiftRows(removeStart, removeEnd, removeRows);
		//
		int endrow=removeStart+removeRows;
		if(endrow>removeEnd+1){
			endrow=removeEnd+1;
		}
		//处理下移后原页尾的几行的样式处理
		for(int j=removeStart;j<endrow;j++){
			Row dataRow=sheet.getRow(j);
			if(dataRow!=null){
				copyRow(dataNameRow, dataRow,false);
			}
		}
		//处理移动后的页脚高度
		afterFillBottomRowIndex=removeStart+removeRows;
		afterFillBottomRowEnd=removeEnd+removeRows;
		index=0;
		for(int j=afterFillBottomRowIndex;j<=afterFillBottomRowEnd;j++){
			short h=bottomRowsHeight[index++];
			if(h>0){
				sheet.getRow(j).setHeight(h);
			}
		}
	}
	/**
	 * 根据要合并的单元格信息对所有单元格进行合并
	 * @param sheet
	 * @param dataSource
	 */
	protected void merge(Sheet sheet,Collection<?> dataSource){
		if(CollectionUtils.notEmpty(mergeConfigs)){
			List<MergeSetLoop<Object>> loops=new LinkedList<MergeSetLoop<Object>>();
			//初始化合并循环
			for(MergeConfig config:mergeConfigs){
				config.initMerge(names, firstCellNum);
				loops.add(new MergeSetLoop<Object>(config, dataNameRowIndex));
			}
			merge(sheet, dataSource, loops, dataNameRowIndex);;
		}
	}
	
	
	/**
	 * 是否是空内容
	 * @param value
	 * @return
	 */
	protected boolean isNullValue(Object value){
		return value==null;
	}
	
	/***
	 * 写子集合的数据到表格
	 * @param dataRow
	 * @param sheet
	 * @param rowIndex
	 * @return
	 */
	private int writeSubDataSoruceCell(Row dataRow,Sheet sheet,int rowIndex){
		if(this.currentListCell!=null){
			subDataRowNumber=1;
			if(this.currentListCell.isEmpty()){
				setEmptySubListValues(dataRow);
			}else{
				//记录下起始行号
				int subRowStartIndex=rowIndex;
				if(afterFillBottomRowIndex>0){//移动表尾部
					int subSize=this.currentListCell.dataSource.size();
					if(subSize>1){
						removeBottomRows(sheet, afterFillBottomRowIndex, afterFillBottomRowEnd, subSize-1);
					}
				}
				//子集合行的索引
				for(Object subOneData:this.currentListCell.dataSource){
					this.context.setRoot(subOneData);
					rowIndex=writeSubDataOneRow(dataRow, subOneData, sheet, rowIndex);
				}
				//重新设置主行数据为root
				this.context.setRoot(currentListCell.rowData);
				//合并主行单元格
				int start=dataRow.getFirstCellNum();
				int last=dataRow.getLastCellNum();
				for(int i=start;i<last;i++){
					if(mergeSubListColumn(i)){
						sheet.addMergedRegion(new CellRangeAddress(subRowStartIndex, rowIndex, i, i));
					}
				}
			}
			this.currentListCell.clear();
		}
		return rowIndex;
	}
	/**
	 * 
	 * @param dataRow 主数据行
	 * @param subOneData 子数据集的一个对象
	 * @param sheet 当前填充的页
	 * @param rowIndex 当前填充到的行号
	 * @return 填充结束时的行号
	 */
	protected int writeSubDataOneRow(Row dataRow,Object subOneData,Sheet sheet,int rowIndex){
		//不是第一行时复制行一行的主数据行
		if(subDataRowNumber>1){
			dataRow=onCreateDataRow(sheet, dataRow, subOneData, ++rowIndex);
		}
		setSubListCellValues(dataRow, subOneData);
		return rowIndex;
	}
	/**
	 * 是否需要合并子数据集单元格
	 * @param columnIndx
	 * @return
	 */
	protected boolean mergeSubListColumn(int columnIndx){
		return currentListCell.notSubCell(columnIndx);
	}
	/***
	 * 填充list单元格的数据
	 * @param row
	 * @param dataSourceObj
	 */
	protected void setSubListCellValues(Row row,Object dataSourceObj){
		for(Map.Entry<Integer, String> entry:this.currentListCell.fieldNameIndexs.entrySet()){
			Cell cell=row.getCell(entry.getKey());
			Object value=getCellExpValue(dataSourceObj, entry.getValue());
			setCellExpValue(cell, value);
		}
		subDataRowNumber++;
	}
	
	/**
	 * 设置子数据行的空行值
	 * @param row 要设置的目标行
	 */
	protected void setEmptySubListValues(Row row){
		for(Map.Entry<Integer, String> entry:this.currentListCell.fieldNameIndexs.entrySet()){
			Cell cell=row.getCell(entry.getKey());
			setExpCellNullValue(cell);
		}
	}
	
	/***
	 * 设置一行填数据行的值
	 * @param row
	 * @param names
	 * @param firstCellNum
	 * @param oneData
	 */
	private  void setData(Row row,String[] names,short firstCellNum,Object oneData){
		for(int i=0;i<names.length;i++){
			int collIndex=firstCellNum+i;
			//没有设置属性时不设置值
			if(StringUtils.notEmpty(names[i])){
				Cell c=row.getCell(collIndex);
				if(c==null){//没有单元格创建
					c=row.createCell(collIndex);
				}
				String name=names[i];
				Object subValue=getCellExpValue(oneData,name);
				if(subValue instanceof Collection){
					//截取出子表对象的属性   sublist.stutend.name  将把主表的属性sublist去除
					int index=name.indexOf(".");
					if(index>0){
						//如果存在子表记录信息
						remarkListCellInfo(oneData,(Collection)subValue,collIndex, name.substring(index+1));
					}else{
						setCellExpValue(c, subValue);
					}
				}else{
					setCellExpValue(c, subValue);
				}
			}
		}
	}
	
	/**
	 * 从单元格的表达式计算出单元格的值
	 * 一个单元格的值
	 * @param oneData
	 * @param exp 单元格中的表达式 可以是属性  变量  定义的常量
	 * @return
	 */
	private Object getCellExpValue(Object oneData,String exp){
		if(exp.startsWith(VALIBALE)){
			//template属性变量表达式
			try {
				return BeanUtils.getExpValue(this, exp.substring(2));
			} catch (Exception e) {
				logger.error("获取变量"+exp+"出错",e);
				return Strings.BLANK;
			}
		}else if(exp.startsWith(PARAM_FLAG)){
			//常量表达式
			exp=exp.substring(1);
			if(params==null){
				return exp;
			}else{
				Object value=getPropertyExpValue(params,exp);
				if(value==null){
					return exp;
				}else{
					return value;
				}
			}
		}else{
			BaseFunctionInfo fun=functionHandler.getFunctionInfo(exp);
			if(functionHandler.isFunction(fun)){
				return getFunctionValue(fun, oneData);
			}
			return getPropertyExpValue(oneData, exp);
		}
	}
	/**
	 * 获取函数的值
	 * @param fun 函数信息
	 * @param oneData 当前对象的值
	 * @return
	 */
	protected Object getFunctionValue(BaseFunctionInfo fun,Object oneData){
		//函数表达式
		IFunction function= functionHandler.getFunction(fun);
		Object value=null;
		String exp=fun.getArgExpression()[0];
		//函数是不是需要当前的表达式取值用于转换
		if(function.needFieldValue()){
			value=getPropertyExpValue(oneData,exp);
		}
		if(function.needContext()){
			return function.convert(this.context,exp,value);
		}else{
			return function.convert(oneData,exp,value);
		}
	}
	/***
	 * 记录子表信息
	 * @param data  子表的数据信息
	 * @param row    当前主表行
	 * @param collIndex 当前列
	 * @param fieldName 取值属性表达式
	 */
	private void remarkListCellInfo(Object rowData,Collection subRowDatas,int collIndex,String fieldName){
		if(this.currentListCell==null){
			this.currentListCell=new SubDataSourceCellInfo(rowData,subRowDatas);
		}else{
			currentListCell.rowData=rowData;
			currentListCell.dataSource=subRowDatas;
		}
		this.currentListCell.addFieldName(collIndex, fieldName);
	}
	
	
	
	/***
	 * 用于封装子表单元格的信息
	 * @author 胡真山
	 * 2015年7月15日
	 */
	protected class SubDataSourceCellInfo{
		Object rowData;
		/**列索引与子集表达式映射*/
		Map<Integer,String> fieldNameIndexs=new LinkedHashMap<Integer, String>();
		/**子集数据*/
		Collection<?> dataSource;
		/**
		 * 当前行数据对象
		 * @param rowData 行对象
		 * @param subvalues 行的子集合
		 */
		SubDataSourceCellInfo(Object rowData,Collection subvalues){
			this.dataSource=subvalues;
			this.rowData=rowData;
		}
		
		boolean isEmpty(){
			return dataSource==null||dataSource.size()==0;
		}
		/**
		 * 添加子集合单元格
		 * @param collIndex
		 * @param name
		 */
		void addFieldName(int collIndex,String name){
			fieldNameIndexs.put(collIndex, name);
		}
		/**
		 * 子集合的长度
		 * @return
		 */
		int getSize(){
			return dataSource.size();
		}
		/**
		 * 是子集合的列
		 * @param index
		 * @return
		 */
		boolean isSubCell(int index){
			return fieldNameIndexs.containsKey(index);
		}
		/**
		 * 不是子集合的列
		 * @param index
		 * @return
		 */
		boolean notSubCell(int index){
			return !fieldNameIndexs.containsKey(index);
		}

		public Object getRowData() {
			return rowData;
		}

		public Collection<?> getDataSource() {
			return dataSource;
		}
		
		public void clear(){
			this.dataSource.clear();
			this.fieldNameIndexs.clear();
			this.rowData=null;
		}
	}
	
	protected class ExportContext extends DefaultContext{
		@Override
		public Object get(String exp) {
			Object value=null;
			try {
				value=super.get(exp);
				if(value==null){
					return BeanUtils.getExpValue(XlsExportTemplate.this, exp);
				}
			} catch (BeanException e) {}
			return value;
		}
		
		
		@Override
		public void setRoot(Object rootObj) {
			if(this.rootValue==null){
				super.setRoot(rootObj);
			}else{
				if(rootObj instanceof Map){
					this.rootValue=(Map)rootObj;
				}else{
					if(this.rootValue instanceof WrapBeanAsMap){
						((WrapBeanAsMap)this.rootValue).updateBean(rootObj);
					}else{
						super.setRoot(rootObj);
					}
				}
			}
		}
	}
	/**
	 * 内容输出到html
	 * @param os 输出流
	 * @param encode 输出编码
	 * @param close 是否关闭流
	 */
	public void writeToHtml(OutputStream os,String encode,boolean close){
		ToHtmlConvert convert=new ToHtmlConvert();
		convert.setEncode(encode);
		convert.setClose(close);
		convert.convert(this, os);
	}
	/**
	 * 输出成html到流中 使用 默认的编码 并关闭流
	 * @param os
	 */
	public void writeToHtml(OutputStream os){
		ToHtmlConvert convert=new ToHtmlConvert();
		convert.convert(this, os);
	}
	

	/**
	 * 设置合并参数
	 * @param mergeConfig
	 */
	public void setMergeConfig(MergeConfig mergeConfig) {
		this.mergeConfigs=CollectionUtils.linkedList(mergeConfig);
	}
	/**
	 * 增加合并参数
	 * @param mergeConfig
	 */
	public void addMergeConfig(MergeConfig... mergeConfig) {
		if(this.mergeConfigs==null){
			mergeConfigs=new LinkedList<MergeConfig>();
		}
		CollectionUtils.add(mergeConfigs, mergeConfig);
	}

	public Row getDataNameRow() {
		return dataNameRow;
	}

	public int getDataNameRowIndex() {
		return dataNameRowIndex;
	}

	public short getFirstCellNum() {
		return firstCellNum;
	}

	public short getLastCellNum() {
		return lastCellNum;
	}
	/**
	 * 为一行填充$表达式数据
	 * @param row
	 * @param context
	 */
	public void fill$ExpValue(Row row,Object context){
		int first=row.getFirstCellNum();
		int last=row.getLastCellNum();
		for(int j=first;j<last;j++){
			Cell c=row.getCell(j);
			String exp=getStringValue(c);
			if(StringUtils.notEmpty(exp)){
				BaseFunctionInfo fun=functionHandler.getFunctionInfo(exp);
				if(functionHandler.isFunction(fun)){
					setCellExpValue(c, getFunctionValue(fun, context));
				}else if(ExpressionUtils.isExp(exp)){
					setCellExpValue(c, getCellExpValue(context, exp));
				}
			}
		}
	}
	/**
	 * 设置单元格格为表达式的结果
	 * @param c
	 * @param value
	 */
	protected void setCellExpValue(Cell c,Object value){
		if(isNullValue(value)){
			setExpCellNullValue(c);
		}else{
			setCellValue(c, value);
		}
	}
	/**
	 * 设置表达式单元格为空属内容
	 * @param cell
	 */
	protected void setExpCellNullValue(Cell cell){
		cell.setCellValue(Strings.BLANK);
	}
	
	/**
	 * 复制出一个sheet页 
	 * @param fromSheetIndex 原sheet页的索引
	 * @param newSheetName 新sheet页的名字
	 */
	public Sheet copySheet(int fromSheetIndex,String newSheetName){
		Sheet sheet=workbook.getSheetAt(fromSheetIndex);
		return copySheet(sheet, newSheetName);
	}
	
	protected Sheet copySheet(Sheet fromSheet,String newSheetName){
		Sheet newSheet=workbook.createSheet(newSheetName);
		copySheet(fromSheet, newSheet, true);
		int columnCount=maxColumnCount;
		if(columnCount<0){
			columnCount=getSheetColumnCount(fromSheet);
		}
		copySheetWidth(fromSheet, newSheet, columnCount);
		copySheetAllPrintSet(fromSheet, newSheet);
		copySheetPrintMerge(fromSheet, newSheet);
		copyPrintBreaks(fromSheet, newSheet);
		copyPrintArea(fromSheet, newSheet);
		copyCommentConfig(fromSheet, newSheet);
		return newSheet;
	}
	/**
	 * 复制sheet页
	 * @param fromSheetIndex
	 * @param handler
	 * @return
	 */
	public Sheet copySheet(int fromSheetIndex,SheetNameHandler handler){
		Sheet sheet=workbook.getSheetAt(fromSheetIndex);
		String sheetName=handler.createSheetName(sheet.getSheetName());
		return copySheet(sheet, sheetName);
	}
	/**
	 * 复制注解配置信息
	 * @param fromSheet
	 * @param toSheet
	 */
	protected void copyCommentConfig(Sheet fromSheet,Sheet toSheet){
		Comment fromCell=fromSheet.getCellComment(commentConfig[0], commentConfig[1]);
		if(fromCell!=null){
			Cell cell=getCell(toSheet,commentConfig[0], commentConfig[1]);
			if(cell==null){
				throw new NullPointerException("用于comment config的单元格 不能为空");
			}
			cell.setCellComment(fromCell);
		}
	}

	@Override
	public void fillDataSource(Map<String,Object> context) {
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		//从第一行开始计数
		dataRowNumber=1;
		Collection<?> dataSource = (Collection<?>) context.get(MAIN_DATA_SOURCE_KEY);
		if(CollectionUtils.isEmpty(dataSource)){
			dataSize=0;
		}else{
			dataSize=dataSource.size();
		}
		doHeaderConfig(sheet);
		removeBottomRows(sheet);
		// 填充主数据
		if (dataSource != null) {
			if (dataSize > 0) {
				doFillDataSource(sheet, dataSource);
			} else {
				doFillEmptyData();
			}
		}else{
			dataNameRowIndex=sheet.getLastRowNum()+1;
		}
		if (sumer != null) {
			sumer.setToContext(context);
		}
		
		fillHeader(sheet, context);
		
		fillBottom(sheet, context);
		
	}
	/**
	 * 填充页脚
	 * @param sheet 
	 * @param data 要填充的数据map
	 * @param dataSource
	 */
	public void fillBottom(Sheet sheet,Object data){
		if(afterFillBottomRowIndex>0){
			this.context.setRoot(data);
			// 填充表尾数据
			for (int i = afterFillBottomRowIndex; i <=afterFillBottomRowEnd; i++) {
				Row row = sheet.getRow(i);
				if(row!=null){
					fill$ExpValue(row, data);
				}else{
					logger.debug("row:"+i+" is null ");
				}
			}
		}
	}
	/**
	 * 填充表头信息
	 * @param sheet
	 * @param data
	 */
	public void fillHeader(Sheet sheet,Object data){
		this.context.setRoot(data);
		// 填充表头数据
		for (int i = 0; i < dataNameRowIndex; i++) {
			Row row = sheet.getRow(i);
			if(row!=null){
				fill$ExpValue(row, data);
			}else{
				logger.debug("row:"+i+" is null ");
			}
		}
	}
	/**
	 * 统计类
	 * @param sumer
	 */
	public void setSumer(Sumer sumer) {
		this.sumer = sumer;
	}
	
	
	public void registerFunction(String name, IFunction function) {
		this.functionHandler.registerFunction(name, function);
	}
	
	
	public boolean isReadCommentConfig() {
		return readCommentConfig;
	}
	
	
	public void setReadCommentConfig(boolean readCommentConfig) {
		this.readCommentConfig = readCommentConfig;
	}

	
}
