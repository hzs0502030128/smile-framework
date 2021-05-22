
package org.smile.tag.impl;

import org.smile.tag.SimpleTag;
import org.smile.tag.Tag;

public abstract class UrlTagSupport extends SimpleTag{
	
	public UrlTag getUrlTag(){
		Tag pTag=parent;
		while(pTag!=null){
			if(pTag instanceof UrlTag){
				return (UrlTag)pTag;
			}
			pTag=pTag.getParent();
		}
		return null;
	}
	
}