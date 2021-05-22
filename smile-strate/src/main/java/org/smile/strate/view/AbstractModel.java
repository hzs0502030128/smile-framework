package org.smile.strate.view;

import org.smile.strate.action.ActionElement;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.jump.JumpType;

public abstract class AbstractModel<T> implements Model<T>{
	
	protected T data;
	
	@Override
	public Class<T> getDataClass() {
		return (Class<T>) (data==null?null:data.getClass());
	}

	@Override
	public T getData() {
		return data;
	}

	@Override
	public ResultConfig getResultConfig(ActionElement actionElement) {
		return new ResultConfig(defaultJumpType().name(),null);
	}
	/***
	 * 默认的跳转方式 
	 * @return
	 */
	
	protected abstract JumpType defaultJumpType();

	public void setData(T data) {
		this.data = data;
	}

}
