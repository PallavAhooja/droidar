package droidar2.sample.exampleClasses;

import com.droidar2.system.EventManager;
import android.app.Activity;
import android.location.Location;

public class ExampleSpecialEventManager extends EventManager {

	@Override
	protected void registerSensorUpdates(Activity myTargetActivity,
			boolean useAccelAndMagnetoSensors) {
		// TODO Auto-generated method stub
		super.registerSensorUpdates(myTargetActivity, useAccelAndMagnetoSensors);
	}

	@Override
	public void onLocationChanged(Location location) {

		// do something with the location here (e.g. buffering)
		location.setLatitude(location.getLatitude() + 1);

		super.onLocationChanged(location);
	}

}
