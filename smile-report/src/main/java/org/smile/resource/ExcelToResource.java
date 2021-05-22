package org.smile.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.commons.SmileRunException;
import org.smile.report.excel.XlsImportTemplete;
import org.smile.util.Properties;
import org.smile.util.StringUtils;
/**
 * 从Excel转换面资源文件
 * @author 胡真山
 *
 */
public class ExcelToResource {
	/**
	 * 读取Excel列的配置  
	 * 如:{0：key,1:cn,2:en}
	 */
	protected Map<Integer,String> readConfig;
	
	protected String resourceName="MessageResources";
	
	protected String keyName="key";
	/**
	 * 从excel生成资源文件 保存到目录中
	 * @param xlsfile excel文件
	 * @param dirPath 资源文件保存目录
	 */
	public void toResource(File xlsfile,String dirPath){
		XlsImportTemplete template=new XlsImportTemplete(xlsfile);
		List<Map<String,Object>> list=template.readDataToList(readConfig);
		Map<String,Properties> propertyMap=new HashMap<String, Properties>();
		for(Map<String,Object> entry:list){
			String key=(String)entry.get(keyName);
			for(Map.Entry<String,Object> sub:entry.entrySet()){
				String type=sub.getKey();
				if(!keyName.equals(type)){
					Properties p=propertyMap.get(type);
					if(p==null){
						p=new Properties();
						propertyMap.put(type, p);
					}
					if(sub.getValue()!=null){
						p.put(key, StringUtils.trim(String.valueOf(sub.getValue())));
					}
				}
			}
		}
		try {
			for(Map.Entry<String,Properties> entry:propertyMap.entrySet()){
				entry.getValue().store(new FileOutputStream(dirPath+"/"+resourceName+"_"+entry.getKey()+".properties"), "excel转");
			}
		} catch (IOException e) {
			throw new SmileRunException("保存到资源文件失败", e);
		}
	}
	
	public void setReadConfig(Map<Integer, String> readConfig) {
		this.readConfig = readConfig;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	
}
