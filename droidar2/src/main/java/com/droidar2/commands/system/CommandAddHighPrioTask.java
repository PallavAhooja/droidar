package com.droidar2.commands.system;

import com.droidar2.system.TaskManager;

import com.droidar2.commands.Command;

public class CommandAddHighPrioTask extends Command {

	private Command myCommandToAdd;

	public CommandAddHighPrioTask(Command commandToAdd) {
		myCommandToAdd = commandToAdd;
	}

	@Override
	public boolean execute() {
		TaskManager.getInstance().addHighPrioTask(myCommandToAdd);
		return true;
	}

}
