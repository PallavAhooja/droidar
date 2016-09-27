package com.droidar2.actions;

import android.location.Location;

import com.droidar2.geo.GeoObj;
import com.droidar2.geo.GeoUtils;
import com.droidar2.util.Log;

/**
 * Created by pallavahooja on 27/09/16.
 */

public abstract class ActionReachDestination extends Action {

    private boolean reached;
    private double reachedDistance;

    public ActionReachDestination( double reachedDistance) {
        this.reachedDistance = reachedDistance;
    }

    @Override
    public abstract boolean onLocationChanged(Location location);

    public boolean checkDestination(GeoObj destination,Location location){
        if (destination==null){
//            Log.e("REACH","destination is null");
            return true;
        }

        if (!reached && nearDestination(destination,location)){
//            Log.e("REACH","reachdest");
            reachDestination();
            reached = true;
            return true;
        }
        else if(reached && !nearDestination(destination,location)){
//            Log.e("REACH","leavedest");
            leaveDestination();
            reached = false;
            return true;
        }

        Log.e("REACH","did nothing");
        return true;

    }

    public abstract void reachDestination();

    public abstract void leaveDestination();

    private boolean nearDestination(GeoObj destination, Location location) {
        double distance = GeoUtils.distance(destination.getLatitude(), location.getLatitude(), destination.getLongitude(), location.getLongitude(), 0d, 0d);
        return distance <= reachedDistance;
    }
}
