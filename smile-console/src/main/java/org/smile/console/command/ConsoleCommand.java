package org.smile.console.command;

import org.smile.console.context.ConsoleContext;

public interface ConsoleCommand {
	
	public static final String INIT_PAGE_METHOD="initPage";
	
	public static final String DO_COMMAND_METHOD="doCommand";
	
	public static final String METHOD_PARAM_NAME="method";
	/**
	 * 初始化时执行
	 */
	public void init();
	/***
	 * 处理命令
	 * @param context
	 */
	public void doCommand(ConsoleContext context);
	/***
	 * 实始化控制台页面
	 * @param context
	 */
	public void initPage(ConsoleContext context);
	/**
	 * 命令代号
	 * @return
	 */
	public String getCode();
	/**
	 * 命令名称
	 * @return
	 */
	public String getName();
	/**
	 * 命令描述
	 * @return
	 */
	public String getDescript();
}
