package org.smile.strate.view;

import org.smile.strate.jump.JumpType;

public class JsonModel extends AbstractModel<Object>{
	
	public JsonModel(Object bean){
		this.data=bean;
	}

	@Override
	protected JumpType defaultJumpType() {
		return JumpType.json;
	}
}
