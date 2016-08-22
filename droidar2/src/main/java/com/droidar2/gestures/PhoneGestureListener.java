package com.droidar2.gestures;

/**
 * Implement this interface if you want to be notified of phone com.droidar2.gestures.
 * 
 * @author marmat (Martin Matysiak)
 */
public interface PhoneGestureListener {
	/**
	 * Will be called whenever a phone gesture has been detected.
	 * 
	 * @param phoneGesture
	 *            The type of gesture that has been detected.
	 */
	void onPhoneGesture(PhoneGesture phoneGesture);
}
