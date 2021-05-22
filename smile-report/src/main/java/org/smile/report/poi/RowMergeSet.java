package org.smile.report.poi;

import org.apache.poi.ss.usermodel.Row;
import org.smile.commons.StringBand;
/**
 * 以行做为合并的信息
 * @author 胡真山
 *
 */
public class RowMergeSet extends AbstractMergeSet<Row>{

	protected Integer[] keyColumns;
	
	public RowMergeSet(Integer[] keyColumns,Integer[] mergeColumns){
		this.keyColumns=keyColumns;
		this.mergeColumn=mergeColumns;
	}
	
	@Override
	public Object getMergeKeyValue(Row oneData) {
		if(keyColumns.length==0){
			Object value=PoiSupport.getCellValue(oneData.getCell(keyColumns[0]));
			if(value==null){
				return 0;
			}
			return value;
		}else{
			StringBand code=new StringBand();
			for(int i=0;i<keyColumns.length;i++){
				Object value=PoiSupport.getCellValue(oneData.getCell(keyColumns[i]));
				if(value!=null){
					code.append(value.hashCode());
				}
				if(i<keyColumns.length-1){
					code.append("-");
				}
			}
			return code.toString();
		}
	}

}
