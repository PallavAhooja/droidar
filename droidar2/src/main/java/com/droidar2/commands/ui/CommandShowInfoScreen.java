package com.droidar2.commands.ui;

import com.droidar2.gui.simpleUI.EditItem;
import com.droidar2.gui.simpleUI.SimpleUIv1;
import android.app.Activity;

import com.droidar2.commands.Command;

public class CommandShowInfoScreen extends Command {

	private Activity myCurrentActivity;
	private EditItem myObjectToDisplay;
	private Object myOptionalMessage;

	public CommandShowInfoScreen(Activity currentActivity, EditItem objectToEdit) {
		myCurrentActivity = currentActivity;
		myObjectToDisplay = objectToEdit;
	}

	public CommandShowInfoScreen(Activity currentActivity,
			EditItem objectToEdit, Object optionalMessage) {
		myCurrentActivity = currentActivity;
		myObjectToDisplay = objectToEdit;
		myOptionalMessage = optionalMessage;
	}

	@Override
	public boolean execute() {
		SimpleUIv1.showInfoScreen(myCurrentActivity, myObjectToDisplay,
				myOptionalMessage);
		return true;
	}
}
