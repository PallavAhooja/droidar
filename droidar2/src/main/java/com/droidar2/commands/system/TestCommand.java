package com.droidar2.commands.system;

import com.droidar2.util.Log;

import com.droidar2.commands.undoable.UndoableCommand;

public class TestCommand extends UndoableCommand {

	private int x = 0;

	@Override
	public boolean override_do() {
		x++;
		Log.out("Test Command do. Count: " + x);
		return true;
	}

	@Override
	public boolean override_undo() {
		x--;
		Log.out("Test Command undo. Count: " + x);
		return true;
	}

}
