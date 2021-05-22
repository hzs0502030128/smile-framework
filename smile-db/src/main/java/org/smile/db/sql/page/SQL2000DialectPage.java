package org.smile.db.sql.page;

import java.sql.SQLException;

import org.smile.util.RegExp;
import org.smile.util.StringUtils;

public class SQL2000DialectPage extends  SQLServerDialectPage {
	
	protected long total;

	public SQL2000DialectPage(String sql) throws SQLException{
		super(sql);
	}
	
	public String getDataSql(int page,int size) {
		int offset=(page-1)*size;
		long endRow=offset+size;
		if(total>0&&endRow>total){
			endRow=total;
			size=(int)(total-offset);
		}
		StringBuffer middleOrderby=new StringBuffer();
		String[] orderbys=orderby.split(",");
		for(int i=0;i<orderbys.length;i++){
			RegExp descReg=new RegExp(" desc *$",false);
			RegExp ascReg=new RegExp(" asc *$",false);
			String[] str=descReg.find(orderbys[i]);
			if(str!=null){
				middleOrderby.append(descReg.replaceAll(orderbys[i], ""));
			}else{
				str=ascReg.find(orderbys[i]);
				if(str!=null){
					middleOrderby.append(ascReg.replaceAll(orderbys[i], " DESC "));
				}else{
					middleOrderby.append(orderbys[i] +" DESC ");
				}
			}
			if(i<orderbys.length-1){
				middleOrderby.append(",");
			}
		}
		RegExp selectReg=new RegExp("select",false);
		int index=selectReg.firstIndexEnd(sql);
		String selectSub=sql.substring(index);
		//数据sql
		StringBuffer querySql=new StringBuffer(this.sql.length()+100);
		if(StringUtils.notEmpty(withSql)){
			querySql.append(withSql);
		}
		querySql.append("SELECT * FROM (SELECT TOP "+size+" * FROM (");
		querySql.append("SELECT ").append(" TOP "+endRow);
		querySql.append(selectSub).append(" ORDER BY "+ orderby +") T ORDER BY "+middleOrderby+") T ORDER BY "+ orderby);
		return querySql.toString();
	}

	@Override
	public void setTotal(long total) {
		this.total=total;
	}
	
	

}
