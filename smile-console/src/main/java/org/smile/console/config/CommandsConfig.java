package org.smile.console.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "commands")
public class CommandsConfig {
	
	List<CommandConfig> command;

	public List<CommandConfig> getCommand() {
		return command;
	}

	public void setCommand(List<CommandConfig> command) {
		this.command = command;
	}
}
