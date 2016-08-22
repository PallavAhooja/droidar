package com.droidar2.components;

import com.droidar2.commands.Command;

import com.droidar2.worldData.Entity;
import com.droidar2.worldData.UpdateTimer;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

public class TimerComp implements Entity {

	private UpdateTimer timer;
	private Updateable myParent;

	public TimerComp(float countdownTimeInSeconds, Command commandToExecute) {
		timer = new UpdateTimer(countdownTimeInSeconds, commandToExecute);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent) {
		setMyParent(parent);
		if (timer.update(timeDelta, this)) {
			/*
			 * if the timer returns true that means its time was up and it was
			 * executed correctly so the TImerComp can be removed from its
			 * parent -> return false
			 */
			return false;
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public Updateable getMyParent() {
		return myParent;
	}

	@Override
	public void setMyParent(Updateable parent) {
		myParent = parent;
	}

}
