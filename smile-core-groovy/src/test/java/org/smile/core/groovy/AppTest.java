package org.smile.core.groovy;

import java.util.Map;

import org.junit.Test;
import org.smile.collection.MapUtils;
import org.smile.script.ScriptExecutor;
import org.smile.script.ScriptType;
import org.smile.template.StringTemplate;
import org.smile.template.Template;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	@Test
	public void test(){
		long s=System.currentTimeMillis();
		for(int i=0;i<1;i++){
			ScriptExecutor se=ScriptType.GROOVY.createExecutor();
			Object v=null;
			Map param=new java.util.HashMap();
			param.put("b", "wew");
			se.setResultVar("ww");
			for(int j=0;j<100;j++){
				v=se.execute(" result=0;def i=0;for(i=0;i<=100;i++){result+=i;};  ww= result+'yyy'; result;");
				System.out.println(v);
			}
			System.out.println(se.getLanguage());
		}
		System.out.println(System.currentTimeMillis()-s);
	}
	
	@Test
	public void testTemplate(){
		StringTemplate template=new StringTemplate(Template.GROOVY, "${name} 你好");
		System.out.println(template.processToString(MapUtils.hashMap("name", "胡真山")));
	}
	
}
