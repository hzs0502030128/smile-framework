package org.smile.report.runqian;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.smile.beans.BeanUtils;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;
import org.smile.report.Header;
import org.smile.util.ObjectLenUtils;

import com.runqian.report4.model.ReportDefine2;
import com.runqian.report4.usermodel.Area;
import com.runqian.report4.usermodel.IColCell;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.usermodel.IRowCell;
import com.runqian.report4.util.ReportUtils;

/***
 * 对润乾报表类的扩展
 * @author 胡真山
 *
 */
public class ReportDefineExt extends ReportDefine2 implements LoggerHandler{
	/**
	 * 表头信息
	 */
	private Header header;
	/**
	 * 数据
	 */
	private List data;
	/**
	 * 表格列数
	 */
	private int cols;
	/**
	 * 表体行数
	 */
	private int bodyRows;
	/**
	 * 表头行数
	 */
	private int headerRows;
	
	private int rows;
	
	public ReportDefineExt(int rows,int cols){
		super(rows,cols);
	}
	/**
	 * @param header 表头信息
	 * @param data 数据
	 * @throws Exception 
	 */
	public ReportDefineExt(Header header,Object[][] data) throws Exception{
		//如果要显示行号则要多加一列
		super(header.getHeaderTexts().length,header.getHeaderCols().length+(header.isRowCount()?1:0));
		this.headerRows=header.getHeaderTexts().length;
		this.bodyRows=data.length;
		this.cols=header.getHeaderCols().length+(header.isRowCount()?1:0);
		this.header=header;
		this.initHeader(header);
		this.pushReportData(header, data);
	}
	/**
	 * @param header 表头信息
	 * @param data 数据
	 * @throws Exception 
	 */
	public ReportDefineExt(Header header,List data) throws Exception{
		//如果要显示行号则要多加一列
		super(header.getHeaderTexts().length,header.getHeaderCols().length+(header.isRowCount()?1:0));
		this.headerRows=header.getHeaderTexts().length;
		this.bodyRows=data.size();
		this.cols=header.getHeaderCols().length+(header.isRowCount()?1:0);
		this.header=header;
		this.initHeader(header);
		this.pushReportData(header, data);
	}
	/**
	 * 写出报表模板到文件中
	 * @param file
	 */
	public void writeToFile(File file)
	{
		try {
			ReportUtils.write(new FileOutputStream(file), this);
		} catch (Exception e) {
			throw new SmileRunException(e);
		} 
	}
	/**
	 * 初始化表头
	 * @param header
	 */
	public void initHeader(Header header)
	{
		int i=0,j=0;
		INormalCell headerCell;
		//是否有序号列 要添加的列 
		int addNumber=header.isRowCount()?1:0;
		//循环添加表头
		for(i=0;i<header.getHeaderTexts().length;i++){
			//添加序号列
			if(header.isRowCount()){
				headerCell=new HeaderCell();
				headerCell.setValue("序号");
				this.setCell(i+1, (short)(1), headerCell);
			}
			//合并单元格用的标记
			int startIndex=0;
			//添加单元格
			for(j=0;j<header.getHeaderCols().length;j++){
				headerCell=new HeaderCell();
				headerCell.setValue(header.getHeaderTexts()[i][j]);
				this.setCell(i+1, (short)(j+1+addNumber), headerCell);
				//融合相同的列单元格
				if(header.getHeaderTexts()[i][j].equals(j<header.getHeaderCols().length-1?header.getHeaderTexts()[i][j+1]:null))
				{
					//
				}else{
					if(startIndex!=j){
						this.merge(i+1, startIndex+1+addNumber, i+1, j+1+addNumber);
						startIndex=j+1;
					}else{
						startIndex++;
					}
				}
			}
		}
		//如果有序号整合序号
		if(header.isRowCount()){
			this.merge(1, 1, header.getHeaderTexts().length, 1);
		}
		//表头相同行融合
		for(j=0;j<header.getHeaderCols().length;j++){
			int startRowIndex=0;
			for(i=0;i<header.getHeaderTexts().length;i++)
			{
				if(header.getHeaderTexts()[i][j].equals(i<header.getHeaderTexts().length-1?header.getHeaderTexts()[i+1][j]:null)){
				}else{
					if(startRowIndex!=i){
						this.merge(startRowIndex+1, j+1+addNumber,i+1,j+1+addNumber);
						startRowIndex=i+1;
					}else{
						startRowIndex++;
					}
				}
			}
		}
		//冻结列
		int colnum = header.getFreeze();
		if(header.isRowCount()){
			colnum+=1;
		}
		//循环设置列的左表头属性,有把几列设为左表头就循环几次
		for(i = 0 ;i<colnum;i++){
			IColCell colCell = this.getColCell((short)(i+1));
			//设置左表头
			colCell.setColType(IColCell.TYPE_LEFT_HEADER);
		}
		int freezerow=header.getHeaderTexts().length;
		//循环设置列的左表头属性,有把几列设为左表头就循环几次
		for(i = 0 ;i<freezerow;i++){
			IRowCell colCell = this.getRowCell((short)(i+1));
			//设置左表头
			colCell.setRowType(IRowCell.TYPE_TITLE_HEADER);
		}
	}
	/**
	 * 添加数据  
	 *  根据 表头信息给出的 要整合的列名 进行单元格整合
	 * @param data 数组
	 * @throws Exception 
	 */
	public void pushReportData(Header header,Object[][] data) throws Exception{
		
		this.setData(data);
		if(data==null){
			return;
		}
		int i=0,j=0;
		//
		int addNumber=header.isRowCount()?1:0;
		INormalCell tempCell;
		//添加数据
		for(i=0;i<data.length;i++){
			this.addRow();
			this.rows++;
			//序号列
			if(header.isRowCount()){
				tempCell=new BodyCell();
				tempCell.setValue(i+1);
				this.setCell(i+header.getHeaderTexts().length+1, (short)(1), tempCell);
			}
			int newCols=data[i].length+addNumber-cols;
			if(newCols>0){
				this.addCol((short)newCols);
				this.cols+=newCols;
			}
			for(j=0;j<data[i].length;j++){
				tempCell=new BodyCell();
				tempCell.setValue(data[i][j]);
				this.setCell(i+header.getHeaderTexts().length+1, (short)(j+1+addNumber), tempCell);
			}
		}
		mergeCols();
	}
	/**
	 * 添加数据  
	 *  根据 表头信息给出的 要整合的列名 进行单元格整合
	 * @param data 一个存放了List的List
	 * @throws Exception 
	 */
	public void pushReportData(Header header,List data) throws Exception{
		this.data=data;
		if(data==null){
			return;
		}
		int i=0,j=0;
		//
		int addNumber=header.isRowCount()?1:0;
		INormalCell tempCell;
		
		//添加数据
		for(i=0;i<data.size();i++){
			Object rowData=data.get(i);
			this.addRow();
			this.rows++;
			int newCols=ObjectLenUtils.len(data.get(i))+addNumber-cols;
			if(newCols>0){
				this.addCol((short)newCols);
				this.cols+=newCols;
			}
			//序号列
			if(header.isRowCount()){
				tempCell=new BodyCell();
				tempCell.setValue(i+1);
				this.setCell(i+header.getHeaderTexts().length+1, (short)(1), tempCell);
			}
			if(rowData instanceof List){
				for(j=0;j<((List)data.get(i)).size();j++){
					tempCell=new BodyCell();
					tempCell.setValue(((List)data.get(i)).get(j));
					this.setCell(i+header.getHeaderTexts().length+1, (short)(j+1+addNumber), tempCell);
				}
			}else if(rowData instanceof Object[]){
				for(j=0;j<((Object[])rowData).length;j++){
					tempCell=new BodyCell();
					tempCell.setValue(((Object[])rowData)[j]);
					this.setCell(i+header.getHeaderTexts().length+1, (short)(j+1+addNumber), tempCell);
				}
			}else{
				String[] cols=header.getHeaderCols();
				for(j=0;j<cols.length;j++){
					tempCell=new BodyCell();
					tempCell.setValue(BeanUtils.getExpValue(rowData, cols[j]));
					this.setCell(i+header.getHeaderTexts().length+1, (short)(j+1+addNumber), tempCell);
				}
			}
		}
		mergeCols();
	}
	/**
	 * 融合列
	 * @throws Exception 
	 */
	public void mergeCols() throws Exception{
		int j=0;
		int i=0;
		int addNumber=header.isRowCount()?1:0;
		//设置融合
		if(header.getMergeCols()!=null){
			for(j=0;j<header.getMergeCols().length;j++){
				//数据列整合
				int startRowIndex=0;
				Integer colIndex=header.getColIndex(header.getMergeCols()[j]);
				if(colIndex==null){
					throw new Exception("指定的融合列名不存在"); 
				}
				for(i=0;i<data.size();i++)
				{
					
					if(this.isEqualsNextRowCell(i,colIndex, data)){
					}else{
						if(startRowIndex!=i){
							this.merge(header.getHeaderTexts().length+startRowIndex+1, header.getColIndex(header.getMergeCols()[j])+1+addNumber, header.getHeaderTexts().length+i+1, header.getColIndex(header.getMergeCols()[j])+1+addNumber);
							startRowIndex=i+1;
						}else{
							startRowIndex++;
						}
					}
				}
			}
		}
	}
	/**
	 * 检查是不是数据是否与下一行同列数据一样
	 * @param i
	 * @param j
	 * @param data
	 * @return
	 */
	private boolean isEqualsNextRowCell(int i,int j,List data){
		try{
			Object  current=data.get(i);
			Object currentData=current instanceof Object[]?((Object[])current)[j]:((List)current).get(j);
			Object next=i<data.size()-1?data.get(i+1):null;
			if(next!=null){
				Object nextData=next instanceof Object[]?((Object[])next)[j]:((List)next).get(j);
				if(currentData!=null&&currentData.equals(nextData)){
					return true;
				}else if(currentData==null&&nextData==null){
					return true;
				}
			}
		}catch(Exception e){
			logger.error(e);
		}
		return false;
	}
	/**
	 * 合并单元格
	 * @param rowIndex1
	 * @param colIndex1
	 * @param rowIndex2
	 * @param colIndex2
	 */
	public void merge(int rowIndex1,int colIndex1,int rowIndex2,int colIndex2){
		try {
			ReportUtils.mergeReport(this, new Area(rowIndex1,(short)colIndex1,(short)rowIndex2,(short)colIndex2));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	/**
	 * 添加一行数据
	 * @param index 添加数据的行索引
	 * @param rowData 新行的数据
	 */
	public void addRow(int index,Object[] rowData){
		this.insertRow(index+1+header.getHeaderTexts().length);
		this.rows++;
		INormalCell tempCell;
		//序号列
		if(header.isRowCount()){
			tempCell=new BodyCell();
			tempCell.setValue(index+1);
			this.setCell(index+header.getHeaderTexts().length+1, (short)(1), tempCell);
		}
		int addNumber=header.isRowCount()?1:0;
		int newCols=rowData.length+addNumber-cols;
		if(newCols>0){
			this.addCol((short)newCols);
			this.cols+=newCols;
		}
		for(int i=0;i<rowData.length;i++){
			tempCell=new BodyCell();
			tempCell.setValue(rowData[i]);
			this.setCell(index+header.getHeaderTexts().length+1, (short)(i+1+addNumber), tempCell);
		}
	}
	/**
	 * 表头行的行数
	 * @return
	 */
	public Integer getHeadRowSize(){
		return this.header.getHeaderCols().length;
	}
	/**
	 * 得到表头
	 * @return
	 */
	public Header getReportHeader() {
		return header;
	}
	/**
	 * 设置表头
	 * @param reportHeader
	 */
	public void setReportHeader(Header reportHeader) {
		this.header = reportHeader;
	}
	/**
	 * 得到数据
	 * @return
	 */
	public List getData() {
		return data;
	}
	/**
	 * 设置数据
	 * @param reportData
	 */
	public void setData(Object[][] data) {
		if(data==null){
			return;
		}
		List list=new ArrayList();
		for(int i=0;i<data.length;i++){
			list.add(data[i]);
		}
		this.data = list;
	}
	
	public Header getHeader() {
		return header;
	}
	
	public int getCols() {
		return cols;
	}
	
	public int getBodyRows() {
		return bodyRows;
	}
	
	public int getHeaderRows() {
		return headerRows;
	}
	
	public int getRows() {
		return rows;
	}
}
