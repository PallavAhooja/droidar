package com.droidar2.listeners;

import android.view.View;
import android.view.ViewGroup;

/**
 * This listener is used to define a custom com.droidar2.gui view for an object and it is
 * called everytime a com.droidar2.gui view is needed
 * 
 * @author Spobo
 * 
 */
public interface ItemGuiListener {

	View requestItemView(View viewToUseIfNotNull, ViewGroup parentView);

}
