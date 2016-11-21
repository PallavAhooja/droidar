package com.droidar2.system;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import com.droidar2.util.LimitedQueue;

import java.util.ArrayList;

/**
 * Created by pallavahooja on 08/11/16.
 */

public class TestSLM extends SimpleLocationManager {

    private Location currentPosition;
    private LimitedQueue<Location> lastPositions;

    public TestSLM(Context context) {
        super(context);
    }

    public static Location midPoint(double lat1, double lon1, double lat2, double lon2) {

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        Location l = new Location("");
        l.setLatitude(Math.toDegrees(lat3));
        l.setLongitude(Math.toDegrees(lon3));
        //return in degrees
        return l;
    }

    @Override
    public void onLocationEventFromGPS(Location location, ArrayList<LocationListener> listenersToInform) {
        if (currentPosition == null) {
            currentPosition = new Location("AveragePosition");
        }
//        calcFromLastPositions(currentPosition, location);

        for (int i = 0; i < listenersToInform.size(); i++) {
            listenersToInform.get(i).onLocationChanged(currentPosition);
        }

    }

    @Override
    public Location getCurrentBufferedLocation() {
        return currentPosition;
    }

    @Override
    public void onLocationEventFromSteps(Location location, ArrayList<LocationListener> listenersToInform) {

    }

    @Override
    public void setMaxNrOfBufferedLocations(int maxNrOfBufferedLocations) {

    }
}
