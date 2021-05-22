package org.smile.console.command;

import org.smile.console.config.CommandConfig;

public abstract class AbstractCommand implements ConsoleCommand {
	/***
	 * 命令的配置信息
	 */
	protected CommandConfig config;

	public AbstractCommand(CommandConfig config) {
		this.config = config;
	}

	@Override
	public String getCode() {
		return config.getClazz();
	}

	@Override
	public String getName() {
		return config.getName();
	}

	@Override
	public String getDescript() {
		return config.getDescript();
	}

}
