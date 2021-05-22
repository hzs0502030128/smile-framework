package org.smile.reflect;

import java.util.HashMap;
import java.util.Map;

public class ClassNameUtils {
	
	private static final Map<String,String> toBaseName=new HashMap<String,String>();
	
	static{
		toBaseName.put("java.lang.Integer", "int");
		toBaseName.put("java.lang.Byte", "byte");
		toBaseName.put("java.lang.Short", "short");
		toBaseName.put("java.lang.Long", "long");
		toBaseName.put("java.lang.Float", "float");
		toBaseName.put("java.lang.Double", "double");
		toBaseName.put("java.lang.Character", "char");
		toBaseName.put("java.lang.Boolean", "boolean");
	}
	
	
	public static String convertToBasicType(String className){
		return toBaseName.get(className);
	}
}
