package org.smile.strate.upload;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.smile.beans.converter.BeanException;
import org.smile.commons.ExceptionUtils;
import org.smile.commons.SmileRunException;
import org.smile.io.FileNameUtils;
import org.smile.reflect.ClassTypeUtils;
import org.smile.strate.RequestParamemterWrapper;
import org.smile.strate.RequestParameter;
import org.smile.strate.Strate;
import org.smile.util.StringUtils;

public class UploadRequestWrapper extends RequestParamemterWrapper{
	
	private FileUploadParser uploadParser;
	
	private List<FieldItem> files=new LinkedList<FieldItem>();
	
	public UploadRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			this.uploadParser=(FileUploadParser)ClassTypeUtils.newInstance(UploadConstants.uploadParser);
		} catch (BeanException e) {
			throw new SmileRunException("实始化上传表单解析器失败",e);
		}
	}
	
	public void uploadLimit(UploadLimit limit){
		if(limit!=null){//限制上传文件大小 
			limit.limit(uploadParser);
		}else{//使用全局配置
			uploadParser.setSizeMax(UploadConstants.sizeMax * UploadConstants.unit);
			uploadParser.setFileSizeMax(UploadConstants.fileSizeMax * UploadConstants.unit);
		}
	}
	
	public void initFileItems() throws StrateUploadException, UnsupportedEncodingException{
		List<FieldItem> list=this.uploadParser.parseRequestItems(realRequest);
		this.parameter=new RequestParameter();
		for(FieldItem item:list){
			//如果是表单数据则把表单数据封装到一个map中
			if (item.isFormField())
			{
				String fieldName=item.getFieldName();
				this.parameter.addParam(fieldName,item.getString(Strate.encoding));
			}
			//处理文件类型
			else{
				//表单的字段名
				String fieldName = item.getFieldName();
				String value = item.getFileName();
				//如果名字不为空
				if(StringUtils.notEmpty(value))
				{
					//文件名
					String fileName =FileNameUtils.getName(value);
					this.parameter.addParam(fieldName, item);
					this.parameter.addParam(fieldName+UploadConstants.fileName,fileName);
					this.parameter.addParam(fieldName+UploadConstants.fileContentType, item.getContentType());
					this.parameter.addParam(fieldName+UploadConstants.fileFileType,FileNameUtils.getExtension(fileName));
				}
				this.files.add(item);
			}
		}
	}

	/**
	 * 清理上传临时文件
	 */
	public void clearUpload(){
		for(FieldItem item:this.files){
			try{
				item.delete();
				logger.debug(item);
			}catch(Throwable e){
				logger.warn(ExceptionUtils.getExceptionMsg(e));
			}
		}
		this.files.clear();
	}
	
}
