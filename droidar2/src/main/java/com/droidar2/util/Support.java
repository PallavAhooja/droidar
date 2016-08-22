package com.droidar2.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;


/**
 * Created by pallavahooja on 22/08/16.
 */
public class Support {
    public static final float minGLESVerion = 0F;
//    public static final float minGLESVerion = 131072.0F;

    public static int getSupportedFeaturesForDevice(Context context) {
        int features = 0;
        boolean hasCamera = context.getPackageManager().hasSystemFeature("android.hardware.camera.any")
                || context.getPackageManager().hasSystemFeature("android.hardware.camera.front")
                || context.getPackageManager().hasSystemFeature("android.hardware.camera");
        if (getGLESVerison(context) >= minGLESVerion && hasCamera) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            boolean magnetometer = sensorManager != null
                    && sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null;
            boolean location = locationManager != null && locationManager.getAllProviders() != null
                    && locationManager.getAllProviders().size() > 0;

            if (location && magnetometer) {
                features |= 1;
            }
        }

        return features;
    }

    private static float getGLESVerison(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        return (float) info.reqGlEsVersion;
    }

    public interface Features {
        int GeoCompass = 1;

    }


    public boolean supportsAR(Context context) {
        return (getSupportedFeaturesForDevice(context) & Features.GeoCompass) != 0;
    }


}
