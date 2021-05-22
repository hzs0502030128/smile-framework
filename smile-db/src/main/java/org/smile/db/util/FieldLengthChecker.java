package org.smile.db.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.smile.beans.property.LikePropertyConverter;
import org.smile.beans.property.PropertyConverter;
import org.smile.db.Transaction;
import org.smile.db.meta.ColumnInfo;
import org.smile.db.meta.TableMetaInfo;
import org.smile.db.meta.TableMetaUtil;
import org.smile.db.sql.BasicTransaction;
/**
 * 用于对字段的长度进行验证
 * 编写此类的目的是为了sqlserver字段超长时不返回字段名的提示信息
 * 用此类在一定程度上方便查看定位字段名
 * 
 * @author 胡真山
 *
 */
public class FieldLengthChecker{
	/**
	 * 表名
	 */
	String tableName;
	/**
	 * @param tableName 表名
	 */
	public FieldLengthChecker(String tableName){
		this.tableName=tableName;
	}
	/**
	 * 
	 * @param conn
	 * @param params
	 * @return 超过长度的字段名称提示信息 由于长度的计算方式过于简单，只能大概反应出长度，还需要人工甄别
	 * @throws Exception
	 */
	public List<String> checkOverLengthField(Connection conn,List params) throws Exception{
		return checkOverLengthField(new BasicTransaction(conn), params);
	}
	
	public List<String> checkOverLengthField(Transaction transaction,List params) throws Exception{
		TableMetaUtil utils=new TableMetaUtil();
		TableMetaInfo info=utils.getOneTableInfo(transaction.getConnection(), tableName);
		List<String> list=new LinkedList<String>();
		Collection<ColumnInfo> columns=info.getAllColumns();
		for(Object param:params){
			PropertyConverter<PropertyDescriptor> pc=new LikePropertyConverter(param.getClass());
			for(ColumnInfo c:columns){
				PropertyDescriptor pro=pc.keyToProperty(c.getName());
				if(pro!=null){
					Method m = pro.getReadMethod();
					if (m != null) {
						Object v = m.invoke(param);
						int l = getFiledLen(v);
						if (l > c.getLength()) {
							list.add(c.getName() + "("+c.getLength()+"):" + v);
						}
					}
				}
			}
		}
		return list;
	}
	
	public static int getFiledLen(Object v){
		if(v==null){
			return 0;
		}
		if(v instanceof Boolean){
			return 1;
		}
		String s= v.toString();
		char[] cc=s.toCharArray();
		int l=0;
		for(int i=0;i<cc.length;i++){
			char c=cc[i];
			if(c>>8==0x0){
				l+=1;
			}else if(c>>16==0x0){
				l+=2;
			}else{
				l+=3;
			}
		}
		return l;
	}
}
