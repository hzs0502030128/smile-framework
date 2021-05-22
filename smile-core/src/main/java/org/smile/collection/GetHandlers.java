package org.smile.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.collection.get.ArrayIndexValueHandler;
import org.smile.collection.get.BooleanIndexValueHandler;
import org.smile.collection.get.ByteIndexValueHandler;
import org.smile.collection.get.CollectionIndexValueHandler;
import org.smile.collection.get.CommonArrayIndexValueHandler;
import org.smile.collection.get.DoubleIndexValueHandler;
import org.smile.collection.get.FloatIndexValueHandler;
import org.smile.collection.get.IndexValueHandler;
import org.smile.collection.get.IntIndexValueHandler;
import org.smile.collection.get.ListIndexValueHandler;
import org.smile.collection.get.LongIndexValueHandler;
import org.smile.collection.get.ShortIndexValueHandler;

public class GetHandlers {
    private  static  Map<Class,IndexValueHandler> collectionHandlers=new HashMap<Class,IndexValueHandler>();
    private  static  Map<Class,IndexValueHandler> arrayHandlers=new HashMap<Class,IndexValueHandler>();
    /**获取list索引值*/
    private static  IndexValueHandler LIST_HANDLER=new ListIndexValueHandler();
    private static  IndexValueHandler COLLECTION_HANDLER=new CollectionIndexValueHandler();
	static{
		arrayHandlers.put(int[].class, new IntIndexValueHandler());
		arrayHandlers.put(long[].class, new LongIndexValueHandler());
		arrayHandlers.put(float[].class, new FloatIndexValueHandler());
		arrayHandlers.put(double[].class, new DoubleIndexValueHandler());
		arrayHandlers.put(byte[].class, new ByteIndexValueHandler());
		arrayHandlers.put(short[].class, new ShortIndexValueHandler());
		arrayHandlers.put(boolean[].class, new BooleanIndexValueHandler());
		arrayHandlers.put(char[].class, new CommonArrayIndexValueHandler());
		arrayHandlers.put(Object[].class, new ArrayIndexValueHandler());
		collectionHandlers.put(List.class, LIST_HANDLER);
		collectionHandlers.put(LinkedList.class, LIST_HANDLER);
		collectionHandlers.put(ArrayList.class, LIST_HANDLER);
		collectionHandlers.put(Collection.class,COLLECTION_HANDLER);
	}
	
	protected static <C,T> IndexValueHandler<C,T> getArrayHandler(Class<C> clazz){
		IndexValueHandler handler=arrayHandlers.get(clazz);
		if(handler==null){
			if(Object[].class.isAssignableFrom(clazz)){
				return arrayHandlers.get(Object[].class);
			}
		}
		return handler;
	}
	
	protected static <C,T> IndexValueHandler<C,T> getCollectionHandler(Class<C> clazz){
		IndexValueHandler handler=collectionHandlers.get(clazz);
		if(handler==null){
			if(List.class.isAssignableFrom(clazz)){
				return LIST_HANDLER;
			}else if(Collection.class.isAssignableFrom(clazz)){
				return COLLECTION_HANDLER;
			}
		}
		return handler;
	}
}
