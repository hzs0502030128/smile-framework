package org.smile.report;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface XlsExportTemplateSupport {
	/**
	 * 使用map默认的主数据键
	 */
	public static final String MAIN_DATA_SOURCE_KEY="dataSource";
	/**
	 * 加载导出模板
	 * @param fileName 模板文件路径
	 */
	public void loadXlsTemplate(String fileName) throws IOException;
	/***
	 * 填充集合数据
	 * @param dataList
	 */
	public void fillDataSource(Collection<?> dataList);
	
	/***
	 * 填充集合数据
	 * @param dataMap
	 */
	public void fillDataSource(Map<String,Object> dataMap);
}
