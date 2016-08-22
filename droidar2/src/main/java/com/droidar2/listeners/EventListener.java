package com.droidar2.listeners;

import com.droidar2.listeners.eventManagerListeners.CamRotationVecUpdateListener;
import com.droidar2.listeners.eventManagerListeners.LocationEventListener;
import com.droidar2.listeners.eventManagerListeners.OrientationChangedListener;
import com.droidar2.listeners.eventManagerListeners.TouchMoveListener;
import com.droidar2.listeners.eventManagerListeners.TrackBallEventListener;

@Deprecated
public interface EventListener extends LocationEventListener,
		OrientationChangedListener, TouchMoveListener, TrackBallEventListener,
		CamRotationVecUpdateListener {

}