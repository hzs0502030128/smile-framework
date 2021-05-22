package org.smile.report;

import java.util.List;
/**
 * 导出 源支持接口
 * @author 胡真山
 *
 */
public interface ExportSourceSupport {
	/**
	 * 用于填充excel导出的数据对象
	 * @return
	 */
	public List getExportList();
}
