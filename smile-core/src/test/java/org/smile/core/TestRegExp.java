package org.smile.core;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.smile.json.JSON;
import org.smile.util.RegExp;
import org.smile.util.RegExp.MatchInfo;
import org.smile.util.SimpleTokenizer;
import org.smile.util.StringTokenizer;

public class TestRegExp {
	RegExp exp=new RegExp("\\([0-9]+\\)");
	@Test
	public void test() throws IOException{
		String[] args=new RegExp("b").split("erbb h b q");
		System.out.println(JSON.toJSONString(args));
	}
	@Test
	public void testSplit(){
		RegExp exp=new RegExp("\\([0-9]+\\)");
		String s="12(1)23333(2)33444(3)444(4)33";
		List<MatchInfo> infos=exp.findAll(s);
		
		for(MatchInfo info:infos){
			System.out.println(info);
			System.out.println(info.subStrBetweenPrevious(s));
			System.out.println(info.subStrBetweenNext(s));
		}
	}
	@Test
	public void testRegToken(){
		RegExp exp=new RegExp("\\([0-9]+\\)");
		String s="(0)(5)12(1)23333(2)33444(3)444(4)33";
		StringTokenizer token=exp.tokenizer(s);
		token.mark();
		while(token.hasMoreTokens()){
			System.out.println(token.nextElement());
			System.out.println(token.separator());
		}
		token.reset();
		System.out.println(token.nextToken("(3)"));
	}
	@Test
	public void testSimpleToken(){
		String s="()()12(1)23333()33444(1)444()33";
		StringTokenizer token=new SimpleTokenizer(s, "()");
		while(token.hasMoreTokens()){
			System.out.println(token.nextToken());
		}
	}
	@Test
	public void testSimpleToken2(){
		String s="()()12(1)23333()33444(1)444()33";
		StringTokenizer token=new SimpleTokenizer(s, "()");
		System.out.println(token.nextToken("(1)"));
		System.out.println(token.nextToken("()"));
		System.out.println(token.nextToken("(1)"));
		System.out.println(token.nextToken("()"));
		System.out.println(token.nextToken());
	}
}
