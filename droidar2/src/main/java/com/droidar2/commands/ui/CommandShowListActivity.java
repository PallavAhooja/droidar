package com.droidar2.commands.ui;

import com.droidar2.gui.CustomBaseAdapter;
import com.droidar2.gui.CustomListActivity;
import com.droidar2.gui.ListSettings;
import com.droidar2.system.ActivityConnector;
import com.droidar2.system.Container;
import com.droidar2.util.Log;
import com.droidar2.util.Wrapper;
import android.app.Activity;
import android.widget.BaseAdapter;

import com.droidar2.commands.Command;
import com.droidar2.commands.undoable.UndoableCommand;

public class CommandShowListActivity extends Command {

	private Wrapper myListItemsWrapper;
	private Activity myCurrentActivity;
	private boolean closeOnCorrectClick;
	private Command myDefaultClickCommand;
	private UndoableCommand myDefaultLongClickCommand;
	private Command myOnCorrectClickCommand;
	private Command myOnCorrectLongClickCommand;
	private UndoableCommand myMenuCommands;
	private String myActivityName;

	public CommandShowListActivity(Wrapper listItemsWrapper,
			Activity currentActivity) {
		this(listItemsWrapper, currentActivity, true, null, null, null, null,
				null, null);
	}

	/**
	 * @param listItemsWrapper
	 * @param currentActivity
	 * @param closeOnCorrectClick
	 * @param defaultOnClickCommand
	 * @param defaultOnLongClickCommand
	 * @param onCorrectClickCommand
	 * @param onCorrectLongClickCommand
	 * @param menuCommands
	 * @param activityName
	 */
	public CommandShowListActivity(Wrapper listItemsWrapper,
			Activity currentActivity, boolean closeOnCorrectClick,
			Command defaultOnClickCommand,
			UndoableCommand defaultOnLongClickCommand,
			Command onCorrectClickCommand, Command onCorrectLongClickCommand,
			UndoableCommand menuCommands, String activityName) {
		myListItemsWrapper = listItemsWrapper;
		this.closeOnCorrectClick = closeOnCorrectClick;
		myCurrentActivity = currentActivity;
		myDefaultClickCommand = defaultOnClickCommand;
		myDefaultLongClickCommand = defaultOnLongClickCommand;
		myOnCorrectClickCommand = onCorrectClickCommand;
		myOnCorrectLongClickCommand = onCorrectLongClickCommand;
		myMenuCommands = menuCommands;
		myActivityName = activityName;
	}

	@Override
	public boolean execute() {
		/*
		 * create an adapter and pass it to the listactivity by using the
		 * ActivityConnector
		 */

		if (myListItemsWrapper.getObject() instanceof Container) {

			BaseAdapter adapter = new CustomBaseAdapter(
					(Container) myListItemsWrapper.getObject());

			ListSettings listSettings = new ListSettings(adapter,
					closeOnCorrectClick, myOnCorrectClickCommand,
					myOnCorrectLongClickCommand, myDefaultClickCommand,
					myDefaultLongClickCommand, myMenuCommands, myActivityName);

			ActivityConnector.getInstance().startActivity(myCurrentActivity,
					CustomListActivity.class, listSettings);

			return true;
		} else {
			Log.d("Commands",
					"No activity will be created because you did not pass a CanBeShownInList-Class of ListItems in the Wrapper!");
		}

		return false;
	}

}
