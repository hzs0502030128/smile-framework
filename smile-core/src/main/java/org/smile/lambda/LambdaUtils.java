package org.smile.lambda;

import java.beans.PropertyDescriptor;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import org.smile.beans.BeanInfo;
import org.smile.collection.SoftHashMap;
import org.smile.collection.WeakHashMap;
import org.smile.reflect.ClassTypeUtils;

public final class LambdaUtils {
	/*** 缓存 */
	private static final Map<Class<?>, SerializedLambda> FUNC_CACHE = WeakHashMap.newConcurrentInstance();
	/**
	 * 缓存属性名称
	 */
	private static final Map<Class<?>, String> PROPERTY_CACHE = SoftHashMap.newConcurrentInstance();
	
	private static final String WriteReplaceMethod="writeReplace";

	public static <T> SerializedLambda resolve(final Lambda<T, ?> func) {
		final Class<?> clazz = func.getClass();
		return (SerializedLambda) Optional.ofNullable(FUNC_CACHE.get(clazz)).orElseGet(() -> {
			SerializedLambda lambda = _resolve(func);
			FUNC_CACHE.put(clazz, lambda);
			return lambda;
		});
	}
	/**
	 * 序列化lambda
	 * @param lambda
	 * @return
	 */
	private static SerializedLambda _resolve(Lambda<?, ?> lambda) {
	        // 从function取出序列化方法
	        Method writeReplaceMethod;
	        try {
	            writeReplaceMethod = lambda.getClass().getDeclaredMethod(WriteReplaceMethod);
	        } catch (NoSuchMethodException e) {
	            throw new RuntimeException(e);
	        }
	        // 从序列化方法取出序列化的lambda信息
	        boolean isAccessible = writeReplaceMethod.isAccessible();
	        writeReplaceMethod.setAccessible(true);
	        SerializedLambda serializedLambda;
	        try {
	            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(lambda);
	        } catch (IllegalAccessException | InvocationTargetException e) {
	            throw new RuntimeException(e);
	        }
	        writeReplaceMethod.setAccessible(isAccessible);

	       return serializedLambda;
	}

	/**
	 * 
	 * @param reader
	 *            bean的reader lambda
	 * @return
	 */
	public static String getPropertyName(SerializedLambda reader) {
		Class<?> aClass =ClassTypeUtils.getClassType(reader.getImplClass().replace("/", "."));
		BeanInfo beanInfo = BeanInfo.getInstance(aClass);
		PropertyDescriptor pd = beanInfo.getPdByMethodName(reader.getImplMethodName());
		String fieldName = pd.getName();
		return fieldName;
	}
	
	/**
	 * 获取bean的属性名以getter方法的lambda
	 * @param reader
	 * @return
	 */
	public static String getPropertyName(Lambda<?, ?> reader) {
		Class<?> clazz = reader.getClass();
		return  Optional.ofNullable(PROPERTY_CACHE.get(clazz)).orElseGet(() -> {
			String field= getPropertyName(resolve(reader));
			PROPERTY_CACHE.put(clazz, field);
			return field;
		});
	}

	public static String[] getPropertyNames(Lambda<?, ?> first, Lambda<?, ?>... others) {
		String[] fieldNames = new String[others.length + 1];
		fieldNames[0] = LambdaUtils.getPropertyName(first);
		if (others.length > 0) {
			for (int i = 0; i < others.length; ++i) {
				fieldNames[i + 1] = LambdaUtils.getPropertyName(others[i]);
			}
		}
		return fieldNames;
	}

	public static  String[] getPropertyNames(Lambda<?, ?>[] lambdas){
		String[] fieldNames = new String[lambdas.length];
		if (lambdas.length > 0) {
			for (int i = 0; i < lambdas.length; ++i) {
				fieldNames[i] = LambdaUtils.getPropertyName(lambdas[i]);
			}
		}
		return fieldNames;
	}
}
