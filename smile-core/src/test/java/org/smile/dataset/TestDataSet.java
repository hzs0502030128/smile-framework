package org.smile.dataset;


import java.util.Iterator;

import org.junit.Test;
import org.smile.dataset.field.IndexField;
import org.smile.dataset.group.GroupRowSet;
import org.smile.dataset.index.CrossIndexRowSet;
import org.smile.dataset.index.IndexAxis;
import org.smile.dataset.index.IndexRowSet;
import org.smile.dataset.sort.Orderby;
import org.smile.expression.Context;
import org.smile.expression.DefaultContext;
import org.smile.expression.Engine;
import org.smile.json.JSON;

public class TestDataSet {
	@Test
	public void test1(){
		DataSetMetaData metaData=new DataSetMetaDataImpl(new String[]{"name","age","address"});
		DataSet dataSet=new BaseDataSet(metaData);
		for(int i=0;i<10;i++){
			Row row=new ArrayRow(metaData, new Object[metaData.getColumnCount()]);
			row.set("name", "胡真山"+i);
			row.set("age",i);
			row.set("address", "小村"+i);
			dataSet.addRow(row);
		}
		for(Row r:dataSet){
			System.out.println(r.readOnlyMap());
		}
		dataSet.toFirst();
		while(dataSet.rollNext()){
			System.out.println(dataSet.get("name"));
			System.out.println(dataSet.get("age"));
			System.out.println(dataSet.get("address"));
		}
		RowSet rowSet=dataSet.randomRowSet(new int[]{1,3,5,7,9});
		DataSourceContext context=new DataSourceContext(rowSet);
		while(rowSet.rollNext()){
			System.out.println(context.evaluate("age.sum()"));
		}
		rowSet=dataSet.rangeRowSet(2,7);
		while(rowSet.rollNext()){
			Row row=rowSet.currentRow();
			System.out.println(rowSet.get("name"));
			System.out.println(rowSet.get("age"));
			System.out.println(rowSet.get("address"));
		}
		System.out.println(rowSet.toArray());
	}
	@Test
	public void testIndex(){
		DataSetMetaData metaData=new DataSetMetaDataImpl(new String[]{"name","age","address"});
		DataSet dataSet=new BaseDataSet(metaData);
		for(int i=0;i<10;i++){
			Row row=new ArrayRow(metaData, new Object[metaData.getColumnCount()]);
			row.set("name", "胡真山"+(i%3));
			row.set("age",i);
			row.set("address", "小村"+i);
			dataSet.addRow(row);
		}
		IndexRowSet indexd=dataSet.index(new String[]{"name"});
		IndexField field=indexd.field("age");
		for(Key key:indexd.keySet()){
			System.out.println(field.avg()+" "+JSON.toJSONString(field.values()));
		}
	}
	
	@Test
	public void testCrossIndexL2(){
		DataSetMetaData metaData=new DataSetMetaDataImpl(new String[]{"area","name","age","address"});
		DataSet dataSet=new BaseDataSet(metaData);
		for(int i=0;i<10;i++){
			Row row=new ArrayRow(metaData, new Object[metaData.getColumnCount()]);
			row.set("name", "胡真山"+(i%3));
			row.set("area", (i%2)+"区");
			row.set("age",i);
			row.set("address", "小村"+i%2);
			dataSet.addRow(row);
		}
		CrossIndexRowSet  indexd=dataSet.crossIndex(new String[]{"area","name"},new String[]{"address"});
		IndexField field=indexd.field("age");
		DataSourceContext ds=new DataSourceContext(indexd);
		Iterator<Key> keyIterator=indexd.keyIterator();
		int index=0;
		while(keyIterator.hasNext()){
			Key key=keyIterator.next();
			CrossRowSet rs=indexd.locate(key);
			if(index==0){
				System.out.print("     ");
				for(Key key2:indexd.columnKeys()){
					System.out.print(key2==Key.STAR?"汇总":key2);
					System.out.print("  ");
				}
				System.out.println();
			}
			System.out.print(key==Key.STAR?"汇总":((key.elements.length==1?" 小计:":"")+ds.evaluate("key.parent")+""));
			System.out.print(key.elements.length>0?(key.elements[key.elements.length-1])+"  ":"");
			System.out.print(" ");
		
			for(Key key2:indexd.columnKeys()){
				IndexAxis.indexColumnKey(key2);
//				System.out.print(field.sum()+" ");
				RowSet rs2=rs.locate(key2);
//				while(rs2.rollNext()){
					Object obj=ds.evaluate("ifnull(age.sum(),0)");
					System.out.print(obj+" ");
//				}
			}
			System.out.println();
			index++;
		}
	}
	@Test
	public void testCrossIndex(){
		DataSetMetaData metaData=new DataSetMetaDataImpl(new String[]{"name","age","address"});
		DataSet dataSet=new BaseDataSet(metaData);
		for(int i=0;i<10;i++){
			Row row=new ArrayRow(metaData, new Object[metaData.getColumnCount()]);
			row.set("name", "胡真山"+(i%3));
			row.set("age",i);
			row.set("address", "小村"+i%2);
			dataSet.addRow(row);
		}
		CrossIndexRowSet  indexd=dataSet.crossIndex(new String[]{"name"},new String[]{"address"});
		IndexField field=indexd.field("age");
		DataSourceContext ds=new DataSourceContext(indexd);
		int index=0;
		for(Key key:indexd.keySet()){
			CrossRowSet rs=indexd.locate(key);
			if(index==0){
				System.out.print("     ");
				for(Key key2:rs.keySet()){
					System.out.print(key2==Key.STAR?"汇总":key2);
					System.out.print("  ");
				}
				System.out.println();
			}
			System.out.print(key==Key.STAR?"汇总":key);
			System.out.print(" ");
			IndexAxis.indexKey(key);
		
			for(Key key2:rs.keySet()){
				IndexAxis.indexColumnKey(key2);
//				System.out.print(field.sum()+" ");
				RowSet rs2=rs.locate(key2);
//				while(rs2.rollNext()){
					Object obj=ds.evaluate("age.avg()");
					System.out.print(obj+" ");
//				}
			}
			System.out.println();
			index++;
		}
	}
	
	@Test
	public void testCrossIndex2(){
		DataSetMetaData metaData=new DataSetMetaDataImpl(new String[]{"name","age","address"});
		DataSet dataSet=new BaseDataSet(metaData);
		for(int i=0;i<10;i++){
			Row row=new ArrayRow(metaData, new Object[metaData.getColumnCount()]);
			row.set("name", "胡真山"+(i%3));
			row.set("age",i);
			row.set("address", "小村"+i%2);
			dataSet.addRow(row);
		}
		CrossIndexRowSet  indexd=dataSet.crossIndex(new String[]{"name"},new String[]{"address"});
		Context context=new DefaultContext();
		context.set("姓名地址", indexd);
		Object obj=Engine.getInstance().evaluate(context, "姓名地址.");
		System.out.println(obj);
	}
	@Test
	public void testGroup(){
		DataSetMetaData metaData=new DataSetMetaDataImpl(new String[]{"name","age","address"});
		DataSet dataSet=new BaseDataSet(metaData);
		for(int i=0;i<10;i++){
			Row row=new ArrayRow(metaData, new Object[metaData.getColumnCount()]);
			row.set("name", "胡真山"+(i%3));
			row.set("age",i);
			row.set("address", "小村"+i%2);
			dataSet.addRow(row);
		}
		GroupRowSet  indexd=dataSet.group("name",Orderby.ASC);
		DataSourceContext ds=new DataSourceContext(indexd);
		Iterator<Key> keyIter=indexd.keyIterator();
		while(keyIter.hasNext()){
			Key k=keyIter.next();
			System.out.println(k);
			RowSet rs=indexd.locate(k);
			System.out.println(ds.evaluate("age.avg()"));	
			while(rs.rollNext()){
				IndexAxis.currentRow(rs.currentRow());
				GroupRowSet  indexd2=rs.group("address",Orderby.ASC);
				System.out.println(indexd2.keySet());
				System.out.println(ds.evaluate("age"));	
			}
			
		}
	}
}
