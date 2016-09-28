package com.droidar2.actions;

import android.widget.Button;
import android.widget.ToggleButton;

import com.droidar2.system.Setup;

/**
 * Created by pallavahooja on 27/09/16.
 */

public abstract class ActionGroundTilt extends Action {

    private Setup setup;

    public ActionGroundTilt(Setup setup) {
        this.setup = setup;
    }

    @Override
    public boolean onAccelChanged(float[] values) {

        float norm_Of_g = (float) Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);

        // Normalize the accelerometer vector
        values[0] = values[0] / norm_Of_g;
        values[1] = values[1] / norm_Of_g;
        values[2] = values[2] / norm_Of_g;

        int inclination = (int) Math.round(Math.toDegrees(Math.acos(values[2])));

        if (inclination < 25 || inclination > 155) {
            // device is flat
            onGroundParallel(true);
        } else {
            // device is not flat
            onGroundParallel(false);
        }

        return false;

    }

    public abstract void onGroundParallel(boolean parallel);

    @Override
    public boolean onOrientationChanged(float[] values) {
        return true;
    }
}
