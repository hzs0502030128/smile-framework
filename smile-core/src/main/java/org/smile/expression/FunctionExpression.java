package org.smile.expression;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.smile.expression.Engine.FunctionKey;
import org.smile.expression.function.ExpressionFn;
import org.smile.expression.visitor.NamedExpressionVisitor;
import org.smile.function.Function;
import org.smile.function.FunctionAware;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.MethodUtils;
/**
 * 函数表达式
 * @author 胡真山
 *
 */
public class FunctionExpression extends NamedExpression implements FunctionAware<Expression> {
	/** 函数的参数 */
	protected List<Expression> args = new ArrayList<Expression>();
	
	protected FunctionKey key=new FunctionKey();

	@Override
	public Object evaluate(Context root) {
		Function f = getFunction(root);
		if(f instanceof ExpressionFn){// 自定义函数
			return ((ExpressionFn) f).evaluate(root,args);
		}else{
			Object[] argsValue = new Object[args.size()];
			for (int i = 0; i < args.size(); i++) {
				argsValue[i] = args.get(i).evaluate(root);
			}
			if (f != null) {//通用函数
				return f.getFunctionValue(argsValue);
			} else if(root.canExecuteCode()){// 以代码方法执行
				return evaluateCode(root,argsValue);
			}
			throw new EvaluateException("not find a function named"+name);
		}
	}

	/** 以代码方法执行 */
	private Object evaluateCode(Context root, Object[] argsValue) {
		int methodIdx = name.lastIndexOf('.');
		if (methodIdx > 0) {
			String invoker = name.substring(0, methodIdx);
			String method = name.substring(methodIdx + 1, name.length());
			if (root != null) {
				Object invokeObj = root.get(convertKeyToProperty(invoker));
				if (invokeObj != null) {
					Class clazz = invokeObj.getClass();
					Method m=getInvokeMethod(clazz, method, argsValue);
					if(m!=null){
						try {
							return m.invoke(invokeObj, argsValue);
						} catch (Exception e) {
							throw new EvaluateException(this.toString(),e);
						}
					}else{
						throw new EvaluateException("not support  mothod "+method+" of class "+clazz.getName());
					}
				}else if(invoker.indexOf('.')<0){
					throw new NullPointerException(invoker+" is null ");
				}
			}
			return evaluteCodeStaticMethod(invoker, method, argsValue);
		}
		throw new EvaluateException("can invoke a function named " + name+", "+this);
	}
	/***
	 * 匹配静态方法执行
	 * @param invoker
	 * @param method
	 * @param argsValue
	 * @return
	 */
	private Object evaluteCodeStaticMethod(String invoker,String method,Object[] argsValue){
		try{
			Class clazz=Class.forName(invoker);
			Method m=getInvokeMethod(clazz, method, argsValue);
			if(m!=null){
				try {
					return m.invoke(null, argsValue);
				} catch (Exception e) {
					throw new EvaluateException(this.toString(),e);
				}
			}
		}catch(ClassNotFoundException e){
			throw new NullPointerException(invoker+" is null ");
		}
		throw new EvaluateException("invoke "+this+"  error ");
	}
	/**
	 * 匹配方法
	 * @param clazz 类名
	 * @param method 方法名
	 * @param argsValue 传入参数
	 * @return
	 */
	private Method getInvokeMethod(Class clazz,String method,Object[] argsValue){
		List<Method> methods = MethodUtils.getMethodByNameAndParamCount(clazz, method, argsValue.length);
		for (Method m : methods) {
			if (checkMethodArg(m, argsValue)) {
				return m;
			}
		}
		return null;
	}
	/***
	 * 较难方法参数
	 * @param m
	 * @param argsValue
	 * @return
	 */
	private boolean checkMethodArg(Method m, Object[] argsValue) {
		for (int i = 0; i < argsValue.length; i++) {
			if (!isTypeMatched(m.getParameterTypes()[i], argsValue[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 较验参数是否匹配类型
	 * @param clazz 方法参数类型
	 * @param otherClazz 参数值
	 * @return
	 */
	public static boolean isTypeMatched(Class clazz, Object otherClazz) {
		if ((clazz == null) || (otherClazz == null)) {
			return true;
		}
		Class basicClass=ClassTypeUtils.getBasicTypeObjectClass(clazz);
		if(basicClass!=null){
			clazz=basicClass;
		}
		return clazz.isAssignableFrom(otherClazz.getClass());
	}

	/**
	 * 添加参数
	 * 
	 * @param arg
	 */
	public void addArg(Expression arg) {
		args.add(arg);
		this.key.setArgsCount(args.size());
	}

	public void setArgs(List<Expression> args) {
		this.args = args;
		this.key.setArgsCount(args.size());
	}

	/**
	 * 获取函数 先从上下文中获取引擎中获取
	 * 如果上下文中未绑定引擎,从默认引擎中获取函数
	 **/
	protected Function getFunction(Context context) {
		if(context==null){
			return Engine.getInstance().getFunction(this.key);
		}
		Function f = context.getFunction(this.key);
		if (f == null&&!context.isBinddEngin()) {
			return Engine.getInstance().getFunction(this.key);
		}
		return f;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(100);
		str.append(name).append('(');
		int index = 0;
		for (Expression exp : args) {
			if (index != 0) {
				str.append(',');
			}
			str.append(exp);
			index++;
		}
		str.append(')');
		return str.toString();
	}

	@Override
	public Expression[] getArgExpression() {
		return args.toArray(new Expression[args.size()]);
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		this.key.setName(name);
	}
	
	@Override
	public void accept(NamedExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
}
