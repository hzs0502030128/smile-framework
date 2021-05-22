
package org.smile.tag.impl;

import org.smile.tag.SimpleTag;


public class OtherTag extends SimpleTag {
	@Override
    public void doTag() throws Exception {
        if(parent==null){
            throw new TagException("other tag must inside if tag or when tag");
        }
        if(parent instanceof WhenTag){
        	WhenTag whenTag=(WhenTag)parent;
        	if(whenTag.getTest()==false){
        		invokeBody();
        	}
        }else{
        	throw new TagException("other tag must inside if tag or when tag");
        }
    }
	
}