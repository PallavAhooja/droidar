package com.droidar2.actions;

import android.content.Context;
import android.view.Display;


/**
 * Created by pallavahooja on 10/11/16.
 */

public abstract class ActionOrientationChange extends Action {
    public static final String TAG = ActionOrientationChange.class.getSimpleName();
    public static final int ORIENTATION_UNKNOWN = -1;
    private static final int _DATA_X = 0;
    private static final int _DATA_Y = 1;
    private static final int _DATA_Z = 2;
    public ScreenOrientation screenOrientation;
    int orientation = -1;
    private int mOrientation = ORIENTATION_UNKNOWN;

    public ActionOrientationChange() {
    }

    @Override
    public boolean onAccelChanged(float[] values) {

//        Log.d(TAG,"rotation = " + rotation);
//        Log.d(TAG, "values = " + values[0] + " - " + values[1] + " - " + values[2]);

        int orientation = ORIENTATION_UNKNOWN;
        float X = -values[_DATA_X];
        float Y = -values[_DATA_Y];
        float Z = -values[_DATA_Z];
        float magnitude = X * X + Y * Y;
        // Don't trust the angle if the magnitude is small compared to the y value
        if (magnitude * 4 >= Z * Z) {
            float OneEightyOverPi = 57.29577957855f;
            float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
            orientation = 90 - Math.round(angle);
            // normalize to 0 - 359 range
            while (orientation >= 360) {
                orientation -= 360;
            }
            while (orientation < 0) {
                orientation += 360;
            }
        }
        if (orientation != mOrientation) {
            mOrientation = orientation;
            onOrientationAngleChanged(orientation);
        }

        return false;
    }

    private void onOrientationAngleChanged(int orientation) {
        if (orientation == ORIENTATION_UNKNOWN) {
            return;
        }
        ScreenOrientation newOrientation;
        if (orientation >= 60 && orientation <= 140) {
            newOrientation = ScreenOrientation.REVERSED_LANDSCAPE;
        } else if (orientation >= 140 && orientation <= 220) {
            newOrientation = ScreenOrientation.REVERSED_PORTRAIT;
        } else if (orientation >= 220 && orientation <= 300) {
            newOrientation = ScreenOrientation.LANDSCAPE;
        } else {
            newOrientation = ScreenOrientation.PORTRAIT;
        }
        if (newOrientation != screenOrientation) {
            screenOrientation = newOrientation;
            onOrientChange(newOrientation);
        }
    }


    public abstract void onOrientChange(ScreenOrientation orientation);

    @Override
    public boolean onOrientationChanged(float[] values) {
        return true;
    }

    public enum ScreenOrientation {
        REVERSED_LANDSCAPE, LANDSCAPE, PORTRAIT, REVERSED_PORTRAIT
    }
}