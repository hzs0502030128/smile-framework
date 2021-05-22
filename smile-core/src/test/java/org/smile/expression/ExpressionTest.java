package org.smile.expression;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.smile.bean.Student;
import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.collection.MapUtils;
import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.expression.parser.ParseException;
import org.smile.function.Function;
import org.smile.json.JSONValue;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;

public class ExpressionTest implements LoggerHandler{
	Map contextMap;
	Map parameter;
	@Before
	public void before(){
		List<Map> list = new LinkedList<Map>();
		Map map = null;
		Map param = new HashMap();
		for (int i = 0; i < 100; i++) {
			map = new HashMap();
			map.put("name", "胡" + i);
			map.put("age", i);
			list.add(map);
		}
		contextMap=new HashMap();
		contextMap.put("no", 1001);
		contextMap.put("list", list);
		contextMap.put("lvl", false);
		contextMap.put("name", "三年级");
		contextMap.put("姓名", "胡真山");
		parameter=new HashMap();
		parameter.put("name", "胡5");
		parameter.put("age", "5");
	}
	@Test
	public void testJavacc(){
		String exp="org.smile.util.StringUtils.substr(姓名,1,2)";
		Expression condition=Engine.getInstance().parseExpression(exp);
		DefaultContext context=new DefaultContext(contextMap);
		Object res=condition.evaluate(context);
		TestCase.assertEquals("真山", res);
	}
	@Test
	public void testParse() {
		
		String exp="(firstName + '10')>'111'";
		Expression condition=Engine.getInstance().parseExpression(exp);
		System.out.println(condition);
	}
	@Test
	public void testFunction(){
		String exp="字串(姓名,1,2)";
		Expression condition=Engine.getInstance().parseExpression(exp);
		DefaultContext context=new DefaultContext(contextMap);
		context.registFunction(new Function(){
			@Override
			public Object getFunctionValue(Object... args) {
				return StringUtils.substr((String)args[0],(Integer)args[1],(Integer)args[2]);
			}
			@Override
			public String getName() {
				return "字串";
			}
			@Override
			public int getSupportArgsCount() {
				return -1;
			}
		});
		System.out.println(condition.evaluate(context));
	}
	@Test
	public void testParese(){
		String exp="not  (name  instanceof  java.lang.Integer ? 1+3 : (!lvl ? age : name))";
		Expression condition=Engine.getInstance().parseExpression(exp);
		Map map=CollectionUtils.linkedHashMap("lvl",true);
		map.put("name", 1);
		map.put("age", 10);
		System.out.println(condition.evaluate(new DefaultContext(map)));
		
		exp="((12*5-23/3+23%2)*23-2)>0";
		condition=Engine.getInstance().parseExpression(exp);
		System.out.println(condition.evaluate());
	}
	@Test
	public void testFilter() {
		List<Map> list = new LinkedList<Map>();
		JSONValue.printToConsle(CollectionUtils.filter(list, "(name  like '胡1%' or not name like '胡_3') and (name <'胡5' or not name like '胡2%') "));
		JSONValue.printToConsle(CollectionUtils.filter(list, "( (name not like '胡_3') and (name <> '胡25'))"));
		JSONValue.printToConsle(CollectionUtils.delete(list, "!age  >#age", CollectionUtils.hashMap("age", 5)));
		JSONValue.printToConsle(list);
		Map<String, Integer> counts = CollectionUtils.count(CollectionUtils.filter(list, "!age  >#age", CollectionUtils.hashMap("age", 5)), new GroupKey<String, Map>() {
			@Override
			public String getKey(Map value) {
				return (String) value.get("name");
			}
		});
		System.out.println(counts);
	}

	@Test
	public void testEvalute() throws ParseException, BeanException {
		Expression c = Engine.getInstance().parseExpression("name is NOT  null || name==true ");
		Context cxt = new DefaultContext(contextMap);
		cxt.setParameters(parameter);
		logger.debug(c.evaluate(cxt));
		c = Engine.getInstance().parseExpression("age==5?'Y':2+3");
		TestCase.assertEquals(5,c.evaluate(cxt));
		c = Engine.getInstance().parseExpression(" lvl ? 'Y' : (lvl ? age : list.1)");
		TestCase.assertEquals("胡1",BeanUtils.getExpValue(c.evaluate(cxt), "name"));
	}
	@Test
	public void test3(){
		Expression c ;
		Context cxt = new DefaultContext(contextMap);
		c = Engine.getInstance().parseExpression("not name==null");
		c = Engine.getInstance().parseExpression("! name==null");
		logger.debug(c.evaluate(cxt));
		c = Engine.getInstance().parseExpression("!lvl like '7' ? 'Y' : (lvl ? age : list.0)");
		logger.debug(c.evaluate(cxt));
		c = Engine.getInstance().parseExpression("not(not (name like '胡_3') or ((name <'胡5' or not name like '胡2%') and (name <> '胡25')))");
		logger.debug(c.evaluate(cxt));
		c = Engine.getInstance().parseExpression("! ^lvl like '7' ? 'Y' : (lvl ? #name  like '2' and (1==1 or name like '胡') : list.0)");
		logger.debug(c.evaluate(cxt));
		c = Engine.getInstance().parseExpression("1 + '7' > '10' ? (^lvl like 'tr_e' ? 'Y' : (lvl ? 22 : 33)) : list.0");
		logger.debug(c.evaluate(cxt));
		c = Engine.getInstance().parseExpression("(100+20)/10*2-10%4");
		TestCase.assertEquals(c.evaluate(cxt),22);
	}
	@Test
	public void testCompare(){
		Expression c ;
		Context cxt = new DefaultContext(contextMap);
		c = Engine.getInstance().parseExpression("'6'>'5' and 5<6");
		TestCase.assertEquals(c.evaluate(cxt),true);
		c = Engine.getInstance().parseExpression("'6'<>'5'");
		TestCase.assertEquals(c.evaluate(cxt),true);
		c = Engine.getInstance().parseExpression("'5' =='5'");
		TestCase.assertEquals(c.evaluate(cxt),true);
		c = Engine.getInstance().parseExpression("5566<='5678'");
		TestCase.assertEquals(c.evaluate(cxt),true);
		c = Engine.getInstance().parseExpression("'5'<='6'");
		TestCase.assertEquals(c.evaluate(cxt),true);
		c = Engine.getInstance().parseExpression("'6'>='5'");
		TestCase.assertEquals(c.evaluate(cxt),true);
		c = Engine.getInstance().parseExpression("'5'=='5'");
		TestCase.assertEquals(c.evaluate(cxt),true);
		c = Engine.getInstance().parseExpression("'5'!='5'");
		TestCase.assertEquals(c.evaluate(cxt),false);
	}
	@Test
	public void testUseTime(){
		String exp="6.7-100>39.6?(5==5? 4+5:6-1 ):(!(100%3-39.0<27) ? 8*2-199: 100%3)";
		Context cxt=new DefaultContext();
		Expression  expression=Engine.getInstance().parseExpression(exp);
		TestCase.assertEquals(expression.evaluate(), 1);
		cxt.divideScale(2);
		Object res=Engine.getInstance().evaluate(cxt, "10/3");
		TestCase.assertEquals(res, 3.33);
		
		res=Engine.getInstance().evaluate(cxt, "min(12000l,200000.2)");
		System.out.println(res);
	}
	@Test
	public void testExpression(){
		String s="'233'+'ddd'+round(divide(10,3)*4)+true+randomInt(1,10);reg=regexp('[a-z]+[0-9]*');reg.matches('q10')?'是':'否'";
		Expression e=null;
		e=	Engine.getInstance().parseExpression(s);
		System.out.println(e.evaluate(new DefaultContext()));
		Student st=new Student();
		s="stu.name='胡真山';name='222';#age=123;!notEmpty(#name);if(5-4==1,'是','否')";
		e=Engine.getInstance().parseExpression(s);
		System.out.println(e);
		Context context=new DefaultContext(CollectionUtils.hashMap("stu",st));
		System.out.println(e.evaluate(context));
		System.out.println(st.getName());
	}
	@Test
	public void testIf() {
		String s="s=2;if(2==1,s=s+1,s=s+2);s";
		Expression e=	Engine.getInstance().parseExpression(s);
		System.out.println(e.evaluate(new DefaultContext()));
	}
	
	@Test
	public void testForeach(){
		String s="s='3';foreach(map(1,2,3,4,5,6),s=s+it.value);s";
		Expression e=	Engine.getInstance().parseExpression(s);
		Context context=new DefaultContext(CollectionUtils.hashMap("list",CollectionUtils.linkedList(1,2,3)));
		System.out.println(e.evaluate(context));
	}
	
	@Test
	public void testWhile(){
		String s="i=1;j=0;while(i<10,j=j+i,i=i+1);j";
		Expression e=	Engine.getInstance().parseExpression(s);
		Context context=new DefaultContext();
		System.out.println(e.evaluate(context));
	}
	
	@Test
	public void testWhile2(){
		String s="i=1;j=0;while(i==1,i=2,j=j+1,j=i+100);j";
		Expression e=	Engine.getInstance().parseExpression(s);
		Context context=new DefaultContext();
		System.out.println(e.evaluate(context));
	}
	
	@Test
	public void testArray(){
		String s="arr=emptyarr(10);i=0;while(i<len(arr),j=i+1,set(arr,i,arr),print(len(arr[0])),i=i+1);join(arr,',')";
		Expression e=	Engine.getInstance().parseExpression(s);
		Context context=new DefaultContext();
		System.out.println(e.evaluate(context));
	}
	
	@Test
	public void testSet(){
		String s="set=hset(1,2,3,4,3,4);i=0;while(i<len(set),print(get(set,i)),i=i+1);i";
		Expression e=	Engine.getInstance().parseExpression(s);
		Context context=new DefaultContext();
		System.out.println(e.evaluate(context));
	}
	
	@Test
	public void testNew(){
		String s="stu=new(trim('org.smile.bean.Student '),'胡真山',4);\r\n\r\n\tstu.getName()";
		System.out.println(s);
		Expression e=	Engine.getInstance().parseExpression(s);
		Context context=new DefaultContext();
		System.out.println(e.evaluate(context));
	}
	@Test
	public void test(){
		String exp="name == \"胡\" or name is null";
		Expression b=Engine.getInstance().parseExpression(exp);
		Context cxt=Engine.getInstance().createContext(MapUtils.hashMap("name","胡"));
		TestCase.assertEquals(true,b.evaluate(cxt));
	}
	
	@Test
	public void testInstance(){
		String s="now() instanceof date";
		Expression e=Engine.getInstance().parseExpression(s);
		TestCase.assertEquals(true,e.evaluate());
	}
	@Test
	public void testAnd() {
		String s="search.hasSearchContent()==true and search!=null ";
		Expression e=Engine.getInstance().parseExpression(s);
		System.out.println(e);
	}
}
