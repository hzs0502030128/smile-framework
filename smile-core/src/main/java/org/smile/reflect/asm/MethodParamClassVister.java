package org.smile.reflect.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.smile.collection.CollectionUtils;
import org.smile.plugin.MethodHashKey;

/**
 * 实现asm的方法查看
 * @author 胡真山
 *
 */
public class MethodParamClassVister extends ClassVisitor{
	
	protected Map<String,List<Method>> namedMethodMap;
	
	protected Map<MethodHashKey,String[]> paramNamesMap=new HashMap<MethodHashKey, String[]>();
	
	public MethodParamClassVister(ClassWriter classReader) {
		super(Opcodes.ASM4, classReader);
	}
	
	private Type[] classToTypes(Class[] clazzes){
		Type[] types=new Type[clazzes.length];
		int index=0;
		for(Class clazz:clazzes){
			types[index++]=Type.getType(clazz);
		}
		return types;
	}
	
	private static boolean sameType(Type[] types, Class<?>[] clazzes) {
		// 个数不同
		if (types.length != clazzes.length) {
			return false;
		}
		for (int i = 0; i < types.length; i++) {
			if (!Type.getType(clazzes[i]).equals(types[i])) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public MethodVisitor visitMethod(final int access, final String methodName, final String desc, final String signature, final String[] exceptions) {
		MethodVisitor v = cv.visitMethod(access, methodName, desc, signature, exceptions);
		List<Method> methods=namedMethodMap.get(methodName);
		if(CollectionUtils.notEmpty(methods)){
			for(final Method m:methods){
				Class[] parametClass=m.getParameterTypes();
				Type[] types=Type.getArgumentTypes(desc);
				if(sameType(types, parametClass)){
					final String[] paramNames=new String[parametClass.length]; 
					paramNamesMap.put(new MethodHashKey(m), paramNames);
					return new MethodVisitor(Opcodes.ASM4, v) {
						@Override
						public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
							int i = index - 1;
							// 如果是静态方法，则第一就是参数
							// 如果不是静态方法，则第一个是"this"，然后才是方法的参数
							if (Modifier.isStatic(m.getModifiers())) {
								i = index;
							}
							if (i >= 0 && i < paramNames.length) {
								paramNames[i] = name;
							}
							super.visitLocalVariable(name, desc, signature, start, end, index);
						}
					};
				}
			}
		}
		return v;
	}
}
