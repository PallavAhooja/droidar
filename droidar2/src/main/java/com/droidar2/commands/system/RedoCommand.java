package com.droidar2.commands.system;

import com.droidar2.commands.Command;
import com.droidar2.commands.undoable.CommandProcessor;

public class RedoCommand extends Command {

	@Override
	public boolean execute() {
		return CommandProcessor.getInstance().redo();
	}

}
