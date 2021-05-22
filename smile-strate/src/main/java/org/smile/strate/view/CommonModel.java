package org.smile.strate.view;

import org.smile.strate.action.ActionElement;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.jump.JumpType;

public class CommonModel extends AbstractModel<Object>{
	/**服务器跳转  可以是 跳转名称  也可以是跳转的结果 路径*/
	protected String forward;
	/**可指定跳转类型*/
	protected String type;
	
	public CommonModel(String forward){
		this.forward=forward;
	}
	
	public CommonModel(String forword,Object data){
		this.forward=forword;
		this.data=data;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	protected JumpType defaultJumpType() {
		return JumpType.forward;
	}

	@Override
	public ResultConfig getResultConfig(ActionElement actionElement) {
		ResultConfig config=actionElement.getResultConfig(forward);
		if(config==null){
			if(type!=null){
				return new ResultConfig(type, forward);
			}else{
				return new ResultConfig(defaultJumpType().name(), forward);
			}
		}
		return config;
	}
}
