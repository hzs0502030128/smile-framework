package org.smile.tag.impl;


public class ElseIfTag extends IfTag{
	
    @Override
    public void doTag() throws Exception {
        boolean result=getIfElseSuccess(parent);
        /**是否是已经出现TRUE*/
    	boolean ifResult=test||result;
        if(!result&&test){// 当前条件为true,之前无条件为false
        	invokeBody();
        }
        setIfElseSuccess(parent, ifResult);
    }
}
