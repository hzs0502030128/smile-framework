package org.smile.console;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.console.command.ConsoleCommand;
import org.smile.console.context.CommandContext;
import org.smile.console.context.ConsoleContext;
import org.smile.console.context.ServletConsoleContext;
import org.smile.http.HttpConstans;
import org.smile.io.IOUtils;
import org.smile.template.StringTemplate;
import org.smile.template.Template;
import org.smile.util.StringUtils;
/**
 * servlet控制台
 * @author 胡真山
 */
public class ConsoleServlet extends HttpServlet {
	/****命令容器*/
	protected CommandContext commandContext;
	/**显示命令列表的模板*/
	protected Template commandListTemplate;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	private void listAllCommand(ConsoleContext context,HttpServletResponse resp) throws IOException {
		resp.setContentType(HttpConstans.HTML_UTF_CONTENT_TYPE);
		commandListTemplate.process(context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletConsoleContext context=new ServletConsoleContext(req, resp);
		context.setCommandMap(commandContext.getCommandMap());
		String code=context.getCommandCode();
		if(StringUtils.isEmpty(code)){
			listAllCommand(context,resp);
		}else{
			ConsoleCommand command=commandContext.getCommand(code);
			String method=req.getParameter(ConsoleCommand.METHOD_PARAM_NAME);
			if(ConsoleCommand.INIT_PAGE_METHOD.equals(method)){
				command.initPage(context);
			}else if(ConsoleCommand.DO_COMMAND_METHOD.equals(method)){
				command.doCommand(context);
			}
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		commandContext=new CommandContext();
		try {
			commandContext.loadCommandConfig();
			InputStream is=getClass().getResourceAsStream("stl/command_list.stl.xml");
			commandListTemplate=new StringTemplate(Template.SMILE,IOUtils.readString(is));
		} catch (IOException e) {
			throw new ServletException("init console fail ",e);
		}
	}
}
