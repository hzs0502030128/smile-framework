package org.smile.strate.view;

import org.smile.strate.action.ActionElement;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.jump.JumpType;


public class ForwardModel extends AbstractModel<Object>{
	/**服务器跳转  可以是 跳转名称  也可以是跳转的结果 路径*/
	protected String forward;
	
	public ForwardModel(String forward){
		this.forward=forward;
	}
	
	public ForwardModel(String forword,Object data){
		this.forward=forword;
		this.data=data;
	}

	@Override
	protected JumpType defaultJumpType() {
		return JumpType.forward;
	}

	@Override
	public ResultConfig getResultConfig(ActionElement actionElement) {
		ResultConfig config=actionElement.getResultConfig(forward);
		if(config==null){
			return new ResultConfig(defaultJumpType().name(), forward);
		}
		return config;
	}
	
}
