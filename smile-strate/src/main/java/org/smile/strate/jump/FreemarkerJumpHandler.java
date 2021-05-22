package org.smile.strate.jump;

import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.jump.adapter.FreemarkerAdapter;

/**
 * 跳转到freemarker模板
 * @author 胡真山
 * @Date 2016年2月1日
 */
public class FreemarkerJumpHandler implements JumpHandler {
	
	private JumpHandler adapter;
	
	public FreemarkerJumpHandler(){
		try{
			adapter=new FreemarkerAdapter();
		}catch(Throwable e){
			logger.info("not freemarker support ");
		}
	}
	@Override
	public void jump(DispatchContext context , ResultConfig forward) throws StrateResultJumpException {
		adapter.jump(context, forward);
	}
	@Override
	public void jump(Object methodResult,DispatchContext context , ResultConfig forward) throws StrateResultJumpException {
		adapter.jump(methodResult, context, forward);
	}
}
