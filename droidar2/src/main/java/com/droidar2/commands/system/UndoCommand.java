package com.droidar2.commands.system;

import com.droidar2.commands.Command;
import com.droidar2.commands.undoable.CommandProcessor;

public class UndoCommand extends Command {

	@Override
	public boolean execute() {
		return CommandProcessor.getInstance().undo();
	}

}
