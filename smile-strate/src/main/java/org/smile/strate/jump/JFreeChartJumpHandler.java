package org.smile.strate.jump;

import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.jump.adapter.JFreeChartAdapter;

public class JFreeChartJumpHandler implements JumpHandler {

	private JumpHandler adapter;

	public JFreeChartJumpHandler() {
		try {
			adapter = new JFreeChartAdapter();
		} catch (Throwable e) {
			logger.info("not jfreechart support ");
		}
	}

	@Override
	public void jump(DispatchContext context,  ResultConfig forward) throws StrateResultJumpException {
		adapter.jump(context, forward);
	}

	@Override
	public void jump(Object methodResult, DispatchContext context,  ResultConfig forward) throws StrateResultJumpException {
		adapter.jump(methodResult, context, forward);
	}
}
