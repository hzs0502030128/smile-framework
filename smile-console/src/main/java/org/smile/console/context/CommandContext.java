package org.smile.console.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.console.command.ConsoleCommand;
import org.smile.console.config.CommandConfig;
import org.smile.console.config.CommandsConfig;
import org.smile.file.ClassPathFileScaner;
import org.smile.file.ClassPathFileScaner.BaseVisitor;
import org.smile.log.LoggerHandler;
import org.smile.util.XmlUtils;


public class CommandContext implements LoggerHandler{
	/**控制台命令配置文件*/
	protected static final String COMMAND_CONFIG_FILE = "CommandConfig.xml";
	/**控制台命令配置文件目录*/
	protected static final String CONFIG_DIR = "META-INF/";
	/***
	 * 所有的命令集合
	 */
	private Map<String, ConsoleCommand> commandMap = new HashMap<String, ConsoleCommand>();

	/**
	 * 获取命令实例
	 * @param commandCode
	 * @return
	 */
	public ConsoleCommand getCommand(String commandCode) {
		return commandMap.get(commandCode);
	}


	/**
	 * 加载一个配置文件
	 * @param is
	 * @throws IOException
	 */
	private void loadConfigFile(InputStream is) throws IOException{
		CommandsConfig config=XmlUtils.parserXml(CommandsConfig.class, is);
		List<CommandConfig> commands=config.getCommand();
		if(CollectionUtils.notEmpty(commands)){
			for(CommandConfig command:commands){
				try {
					ConsoleCommand c=(ConsoleCommand) Class.forName(command.getClazz()).getConstructor(CommandConfig.class).newInstance(command);
					c.init();
					commandMap.put(c.getClass().getName(), c);
				} catch (Throwable e) {
					logger.error("init console config fail "+command.getClazz(), e);
				}
			}
		}
	}
	/**
	 * 命令集合
	 * @return
	 */
	public Map<String, ConsoleCommand> getCommandMap() {
		return commandMap;
	}

	public void loadCommandConfig() {
		ClassPathFileScaner scaner=new ClassPathFileScaner(CONFIG_DIR,new BaseVisitor() {
			@Override
			public boolean visit(String fileName,InputStream is) throws IOException {
				loadConfigFile(is);
				return false;
			}
			
			@Override
			public boolean accept(String fileName, String protocol) {
				return fileName.endsWith(COMMAND_CONFIG_FILE);
			}
		});
		try {
			scaner.scanning();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
}
