package org.smile.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.smile.collection.get.IndexValueHandler;
import org.smile.commons.Strings;
import org.smile.math.Numbers;
import org.smile.math.Probability;
import org.smile.util.StringUtils;

/**
 * 集合操作类
 * @author 胡真山
 */
public class CollectionUtils extends MapUtils{
	
	/**
	 * 对一个集合分组成Map
	 * @param list
	 * @param groupKey 分组的关键规则
	 * @return
	 */
	public static <K, T> Map<K, List<T>> group(Collection<T> list, GroupKey<K, T> groupKey) {
		Map<K, List<T>> groupMap = new LinkedHashMap<K, List<T>>();
		return group(list, groupKey, groupMap);
	}
	/***
	 * 分组到一个Map中
	 * @param list
	 * @param groupKey
	 * @param groupMap 目标map
	 * @return
	 */
	public static <K,T> Map<K,List<T>> group(Collection<T> list, GroupKey<K, T> groupKey,Map<K,List<T>> groupMap){
		for (T value : list) {
			K key = groupKey.getKey(value);
			List<T> sub = groupMap.get(key);
			if (sub == null) {
				sub = new LinkedList<T>();
				groupMap.put(key, sub);
			}
			sub.add(value);
		}
		return groupMap;
	}
	/**
	 * 进行记数
	 * @param list 需要计数的列表
	 * @param groupKey 计数的分组
	 * @return
	 */
	public static <K,T> Map<K,Integer> count(Collection<T> list, GroupKey<K, T> groupKey){
		Map<K,Integer> groupMap = new LinkedHashMap<K,Integer>();
		for (T value : list) {
			K key = groupKey.getKey(value);
			Integer count = groupMap.get(key);
			if (count == null) {
				count=0;
			}
			count++;
			groupMap.put(key, count);
		}
		return groupMap;
	}

	/**
	 * 把一个集合中的元素连接到另一个集合中
	 * @param desc 目标集合
	 * @param source 源集合
	 * @param recursion 是否递归连接
	 */
	public static Collection concat(Collection desc,Collection source,boolean recursion){
		for(Object obj:source){
			if(recursion&&obj instanceof Collection){
				concat(desc, (Collection)obj, recursion);
			}else{
				desc.add(obj);
			}
		}
		return desc;
	}
	/**
	 * 把多个集合连接到一个集合中
	 * 递归连接
	 * @param desc 目标集合
	 * @param source 源集合
	 * @return
	 */
	public static <T extends Collection> T concat(T desc,Collection ...source){
		for(Collection c:source){
			concat(desc, c,true);
		}
		return desc;
	}
	

	/**
	 * @param list  能通过的结果集
	 * @param filter 过滤器
	 * @return
	 */
	public static <T> List<T> filter(Collection<T> list, Filter<T> filter) {
		List<T> result = new LinkedList<T>();
		for (T t : list) {
			if (filter.pass(t)) {
				result.add(t);
			}
		}
		return result;
	}

	/***
	 * @param srcList 从源集合中过滤
	 * @param where 过滤语句
	 * @return 过滤后的结果
	 */
	public static <T> List<T> filter(Collection<T> srcList, String where) {
		ExpressionFilter queyr = new ExpressionFilter(where);
		return queyr.listResult(srcList);
	}
	
	/***
	 * @param srcList 从源集合中过滤
	 * @param where 过滤语句 name like :name && age >?age
	 * @param params 占位符参数
	 * @return 过滤后的结果
	 */
	public static <T> List<T> filter(Collection<T> srcList, String where,Object params) {
		ExpressionFilter query = new ExpressionFilter(where);
		query.setParamters(params);
		return query.listResult(srcList);
	}
	
	

	/**
	 * 删除对象
	 * @param srcList 要删除对象的源集合
	 * @param where 删除对象的条件
	 * @return 被删除的对象的集合
	 * @throws JoqlParseException
	 */
	public static <T> List<T> delete(Collection<T> srcList, String where) {
		ExpressionFilter query = new ExpressionFilter(where);
		return query.deleteResult(srcList);
	}
	
	/**
	 * 删除对象
	 * @param srcList 要删除对象的源集合
	 * @param where 删除对象的条件  name like :name && age >?age
	 * @param params 占位符参数
	 * @return 被删除的对象的集合
	 * @throws JoqlParseException
	 */
	public static <T> List<T> delete(Collection<T> srcList, String where,Object params) {
		ExpressionFilter query = new ExpressionFilter(where);
		query.setParamters(params);
		return query.deleteResult(srcList);
	}

	/**
	 * 查询一个对象
	 * @param srcList
	 * @param where
	 * @return
	 * @throws JoqlParseException
	 */
	public static <T> T findOne(Collection<T> srcList, String where) {
		ExpressionFilter query = new ExpressionFilter(where);
		return query.oneResult(srcList);
	}

	/***
	 * 循环调用 toString方法
	 * @param collection
	 * @return
	 */
	public static List<String> toString(Collection collection) {
		List<String> list = new ArrayList<String>(collection.size());
		for (Object obj : collection) {
			list.add(obj.toString());
		}
		return list;
	}

	/***
	 * 循环调用 toString方法
	 * @param collection
	 * @return
	 */
	public static String[] toStringArray(Collection collection) {
		String[] list = new String[collection.size()];
		int index = 0;
		for (Object obj : collection) {
			list[index++] = obj.toString();
		}
		return list;
	}

	/***
	 * 子列表
	 * @param list
	 * @param start 起始位置
	 * @param end 到达位置
	 * @return 子列表
	 */
	public static <T> List<T> subList(List<T> list, int start, int end) {
		int size = list.size();
		if (size == 0) {
			return list;
		}
		if (size < start) {
			throw new RuntimeException("start greater than size");
		}
		if (end >= size) {
			end = size;
		}
		return list.subList(start, end);
	}
	/**
	 * 
	 * @param collection
	 * @param offset 路过的索引数
	 * @param length 长度
	 * @return
	 */
	public static <T> Collection<T> subCollectoin(Collection<T> collection, int offset,int length){
		List<T> list=new ArrayList<T>(length);
		int i=1;
		int end=offset+length;
		for(T t:collection){
			if(i>offset){
				list.add(t);
				if(i==end){
					break;
				}
			}
			i++;
		}
		return list;
	}

	/**排序list*/
	public static <T> void sort(List<T> list, Comparator<T> comparator) {
		Collections.sort(list, comparator);
	}

	/**
	 * 获取数组或collection的对应索引的值
	 * @param collection 
	 * @param index
	 * @return
	 */
	public static <T> T get(Collection<T> collection, int index) {
		IndexValueHandler<Collection, T> handler = (IndexValueHandler<Collection, T>) GetHandlers.getCollectionHandler(collection.getClass());
		return handler.getValueAt(collection, index);
	}

	
	/**往集合中添加一个数组*/
	public static <E> void add(Collection<E> collection, E[] arrays) {
		for (E e : arrays) {
			collection.add(e);
		}
	}

	/**
	 * 不为null并且列表中有值
	 * @param list
	 * @return 
	 */
	public static boolean notEmpty(Collection<?> list) {
		return list != null && list.size() > 0;
	}

	/**
	 * 新建一个hashset
	 * @param vs
	 * @return
	 */
	public static <V> Set<V> hashSet(V... vs) {
		Set<V> set = new HashSet<V>();
		for (V v : vs) {
			set.add(v);
		}
		return set;
	}
	/**
	 * 从数组构建一个新的TreeSet
	 * @param vs
	 * @return
	 */
	public static <V> TreeSet<V> treeSet(V ...vs){
		TreeSet<V> set=new TreeSet<V>();
		for(V v:vs){
			set.add(v);
		}
		return set;
	}

	/**
	 * 数组转成linkedlist
	 * @param vs
	 * @return
	 */
	public static <V> List<V> linkedList(V... vs) {
		List<V> list = new LinkedList<V>();
		add(list, vs);
		return list;
	}
	
	/**
	 * 新建一个arraylist
	 * @param size
	 * @return
	 */
	public static <V> ArrayList<V> arrayList(int size){
		return new ArrayList<V>(size);
	}
	

	/**
	 * 数组转成Set
	 * @param vs
	 * @return
	 */
	public static <V> Set<V> linkedHashSet(V... vs) {
		Set<V> set = new LinkedHashSet<V>();
		for (V v : vs) {
			set.add(v);
		}
		return set;
	}

	/**新建一个ArrayList*/
	public static <T> List<T> arrayList(T... ts) {
		int length=ts.length+ts.length>>1;//设置扩容
		List<T> list = new ArrayList<T>(length);
		for (T t : ts) {
			list.add(t);
		}
		return list;
	}

	/**
	 * Set 转 list
	 * @param set
	 * @return
	 */
	public static <T> List<T> convertSetToList(Set<T> set) {
		List<T> list = new LinkedList<T>();
		if (notEmpty(set)) {
			list.addAll(set);
		}
		return list;
	}

	/**
	 * 以; 号连接成一个字符串
	 * @param collection
	 * @return
	 */
	public static <T> String joinToString(Collection<T> collection) {
		if (isEmpty(collection)) {
			return Strings.BLANK;
		}
		return StringUtils.join(collection, ';');
	}

	/**
	 * 连接成字符串 
	 * @param collection
	 * @param seperator 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String joinToString(Collection<T> collection, String seperator) {
		if (isEmpty(collection)) {
			return Strings.BLANK;
		}
		return StringUtils.join(collection, seperator);
	}

	

	/**
	 * 集合中随机出一个对象
	 * @param collection
	 * @return
	 */
	public static <T> T randomItem(Collection<T> collection) {
		if (collection == null || collection.size() == 0) {
			return null;
		}
		int t = (int) (collection.size() * Math.random());
		int i = 0;
		for (Iterator<T> item = collection.iterator(); i <= t && item.hasNext();) {
			T next = item.next();
			if (i == t) {
				return next;
			}
			i++;
		}
		return null;
	}

	/**
	 * 从一个List中随机得到几个值
	 */
	public static <T> List<T> randomGetListFromCollection(Collection<T> collection, int count, T... excludes) {

		if (notEmpty(collection)) {
			List<T> copyList = new LinkedList<T>(collection);
			if (excludes != null && excludes.length > 0) {
				for (T t : excludes) {
					copyList.remove(t);
				}
			}
			Collections.shuffle(copyList);
			return new LinkedList<T>(copyList.subList(0, Math.min(count, collection.size())));
		} else {
			return new LinkedList<T>();
		}
	}

	/**
	 * 从一个List中随机得到几个值
	 */
	public static <T> List<T> randomGetListFromCollection(Collection<T> collection, int count, Collection<T> excludes) {
		if (collection != null && collection.size() > 0) {
			List<T> copyList = new LinkedList<T>(collection);
			if (excludes != null && excludes.size() > 0) {
				for (T t : excludes) {
					copyList.remove(t);
				}
			}
			Collections.shuffle(copyList);

			return new LinkedList<T>(copyList.subList(0, Math.min(count, collection.size())));
		} else {
			return new LinkedList<T>();
		}
	}

	/**
	 * 从一个List中必须得到几个值,可以重复
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> mustRandomGetListFromCollection(Collection<T> collection, int count) {
		List<T> randomList = new LinkedList<T>();
		if (collection.size() == 0) {
			return randomList;
		}
		List<T> list = new ArrayList<T>(collection);

		for (int i = 0; i < count; i++) {
			randomList.add(list.get(Probability.randomGetInt(0, list.size() - 1)));
		}
		return randomList;
	}

	/**
	 * 随机一个对象
	 * @param list
	 * @return
	 */
	public static <T> T randomGetFromList(List<T> list) {
		if (list.size() == 0) {
			return null;
		}
		int targetIndex = Probability.randomGetInt(0, list.size() - 1);
		return list.get(targetIndex);
	}

	/***
	 * 是否为空  或内容为空
	 * @param datas
	 * @return
	 */
	public static <T> boolean isEmpty(Collection<T> datas) {
		return datas == null || datas.isEmpty();
	}

	
	/***
	 * 存在其中一个
	 * @param map
	 * @param keys
	 * @return 存在就返回true
	 */
	public static <K, V> boolean isExistsOneKey(Map<K, V> map, Collection<K> keys) {
		for (Object obj : keys) {
			if (map.containsKey(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 两个集合中的相同元素的集合
	 * @param one
	 * @param two
	 * @return
	 */
	public static <T> Collection<T> theSameObjCollection(Collection<T> one, Collection<T> two) {
		List<T> list = new ArrayList<T>();
		for (T t : one) {
			if (two.contains(t)) {
				list.add(t);
			}
		}
		return list;
	}

	/**
	 * 往集合上添加对象
	 * @param datas
	 * @param array
	 */
	public static <T> void addItem(Collection<T> datas, T... array) {
		for (T t : array) {
			datas.add(t);
		}
	}

	/**
	 * 添加所有的到一个个集合中
	 * @param des
	 * @param add
	 */
	public static <E> void addAll(Collection<E> des, Collection<E> add) {
		if (notEmpty(add)) {
			des.addAll(add);
		}
	}
	/**
	 * 把一个map的值添加到另一个map中
	 * @param target
	 * @param source
	 * @param excludes 排除的键
	 */
	public static <K,V> void putAll(Map<K,V> target,Map<K,V> source,Set<?> excludes){
		for(Map.Entry<K,V> entry:source.entrySet()){
			if(!excludes.contains(entry.getKey())){
				target.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 以索引获取子列表
	 * @param list
	 * @param indexs
	 * @return
	 */
	public static <E> List<E> getSubList(List<E> list, List<Integer> indexs) {
		if (list instanceof ArrayList) {
			return getSubList((ArrayList<E>) list, indexs);
		} else if(list instanceof LinkedList){
			return getSubList((LinkedList<E>) list, indexs);
		}else{
			List<E> subList = new ArrayList<E>(indexs.size());
			for(Integer idx:indexs){
				subList.add(get(list, idx));
			}
			return subList;
		}
	}

	/**
	 * 以索引获取子列表
	 * @param list
	 * @param indexs
	 * @return
	 */
	public static <E> List<E> getSubList(ArrayList<E> list, List<Integer> indexs) {
		if (notEmpty(list) && notEmpty(indexs)) {
			List<E> subList = new ArrayList<E>(indexs.size());
			for (Integer index : indexs) {
				subList.add(list.get(index));
			}
			return subList;
		}
		return null;
	}
	/**
	 * 移除多个元素
	 * @param collection
	 * @param removes 
	 */
	public static void removeAll(Collection collection,Iterable removes){
		for(Object t:removes){
			collection.remove(t);
		}
	}
	/***
	 * 移除多个元素
	 * @param collection
	 * @param removes 要移除的数组
	 */
	public static void removeAll(Collection collection,Object... removes){
		for(Object t:removes){
			collection.remove(t);
		}
	}

	/**
	 * 以索引获取子列表
	 * @param list
	 * @param indexs
	 * @return
	 */
	public static <E> List<E> getSubList(LinkedList<E> list, List<Integer> indexs) {
		if (notEmpty(list) && notEmpty(indexs)) {
			List<E> subList = new ArrayList<E>(indexs.size());
			int i = 0;
			for (E e : list) {
				if (indexs.contains(i)) {
					subList.add(e);
				}
				i++;
			}
			return subList;
		}
		return null;
	}
	
	/***
	 * 并集
	 * @param a
	 * @param b
	 * @return
	 */
    public static Collection union(final Collection a, final Collection b) {
        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while(it.hasNext()) {
            Object obj = it.next();
            for(int i=0,m=Math.max(getFreq(obj,mapa),getFreq(obj,mapb));i<m;i++) {
                list.add(obj);
            }
        }
        return list;
    }
    /**
     * 	装饰一个不可修改的set
     * @param <E>
     * @param set
     * @return
     */
    public static <E> UnmodifiableSet<E> unmodifiableSet(Set<E> set) {
    	return new UnmodifiableSet<E>(set);
    }

    /**
     * 	交集
     * @param a
     * @param b
     * @return
     */
    public static Collection intersection(final Collection a, final Collection b) {
        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while(it.hasNext()) {
            Object obj = it.next();
            for(int i=0,m=Math.min(getFreq(obj,mapa),getFreq(obj,mapb));i<m;i++) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 并集-交集
     */
    public static Collection disjunction(final Collection a, final Collection b) {
        ArrayList list = new ArrayList();
        Map<Object,Integer> mapa = getCardinalityMap(a);
        Map<Object,Integer> mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while(it.hasNext()) {
            Object obj = it.next();
            for(int i=0,m=((Math.max(getFreq(obj,mapa),getFreq(obj,mapb)))-(Math.min(getFreq(obj,mapa),getFreq(obj,mapb))));i<m;i++) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 差集
     * @param a
     * @param b
     * @return
     */
    public static Collection subtract(final Collection a, final Collection b) {
        ArrayList list = new ArrayList( a );
        for (Iterator it = b.iterator(); it.hasNext();) {
            list.remove(it.next());
        }
        return list;
    }

    /**
     * 存在任意一个
     * @param coll1
     * @param coll2
     * @return
     */
    public static boolean containsAny(final Collection coll1, final Collection coll2) {
        if (coll1.size() < coll2.size()) {
            for (Iterator it = coll1.iterator(); it.hasNext();) {
                if (coll2.contains(it.next())) {
                    return true;
                }
            }
        } else {
            for (Iterator it = coll2.iterator(); it.hasNext();) {
                if (coll1.contains(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 是否存在指定的元素
     * @param c
     * @param e
     * @return
     */
    public static boolean contains(Collection c,Object e){
    	return c==null?false:c.contains(e);
    }

    /**
     * 元素个数
     * @param coll
     * @return
     */
    public static Map<Object,Integer> getCardinalityMap(final Collection coll) {
        Map<Object,Integer> count = new HashMap<Object,Integer>();
        for (Iterator it = coll.iterator(); it.hasNext();) {
            Object obj = it.next();
            Integer c = (Integer) (count.get(obj));
            if (c == null) {
                count.put(obj,Numbers.ONE);
            } else {
                count.put(obj,new Integer(c.intValue() + 1));
            }
        }
        return count;
    }

    /**
     * 是否是子集
     * @param a 子集
     * @param b 父集
     * @return
     */
    public static boolean isSubCollection(final Collection a, final Collection b) {
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Iterator it = a.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (getFreq(obj, mapa) > getFreq(obj, mapb)) {
                return false;
            }
        }
        return true;
    }

   /**
    * 真子集
    * @param a 子集
    * @param b 父集
    * @return
    */
    public static boolean isProperSubCollection(final Collection a, final Collection b) {
        return (a.size() < b.size()) && CollectionUtils.isSubCollection(a,b);
    }

    /**
     * 集合内容是否一样
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqualCollection(final Collection a, final Collection b) {
        if(a.size() != b.size()) {
            return false;
        } else {
            Map mapa = getCardinalityMap(a);
            Map mapb = getCardinalityMap(b);
            if(mapa.size() != mapb.size()) {
                return false;
            } else {
                Iterator it = mapa.keySet().iterator();
                while(it.hasNext()) {
                    Object obj = it.next();
                    if(getFreq(obj,mapa) != getFreq(obj,mapb)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
    
    /**
     * 获取元素个数
     * @param obj
     * @param freqMap
     * @return
     */
    private static final int getFreq(final Object obj, final Map<Object,Integer> freqMap) {
        Integer count = (Integer) freqMap.get(obj);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

	/***
	 * 分组生成键接口
	 * @author 胡真山
	 *
	 * @param <K> 反回键的类型
	 * @param <T> 生成键的对象
	 */
	public interface GroupKey<K, T> {
		public K getKey(T value);
	}

	/**
	 * 列表过滤器
	 * @author 胡真山
	 *
	 * @param <E>
	 */
	public interface Filter<E> {
		/**
		 * 是否通过该对象
		 * @param e
		 * @return
		 */
		public boolean pass(E e);
	}
	
	
}
