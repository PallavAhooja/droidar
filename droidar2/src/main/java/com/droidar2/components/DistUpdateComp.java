package com.droidar2.components;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.widget.TextView;

import com.droidar2.geo.GeoObj;
import com.droidar2.geo.GeoUtils;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.system.ConcreteSimpleLocationManager;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Entity;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.UpdateTimer;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

/**
 * Created by pallavahooja on 31/08/16.
 */
public class DistUpdateComp implements Entity {

    private GLCamera myCamera;
    private UpdateTimer timer;
    private Context context;
    private GeoObj geoObj;
    private String distanceText = "";
    private MeshComponent textMesh;
    private float textSize;
    private double reachedDistance;

    private Vec camVec = new Vec();


    public DistUpdateComp(GLCamera myCamera, float updateSpeed, Context context, GeoObj geoObj) {
        this(myCamera, updateSpeed, context, geoObj, 1, -1d);
    }

    public DistUpdateComp(GLCamera myCamera, float updateSpeed, Context context, GeoObj geoObj, float textSize) {
        this(myCamera, updateSpeed, context, geoObj, textSize, -1d);
    }

    public DistUpdateComp(GLCamera myCamera, float updateSpeed, Context context, GeoObj geoObj, float textSize, double reachedDistance) {
        this.myCamera = myCamera;
        timer = new UpdateTimer(updateSpeed, null);
        this.context = context;
        this.geoObj = geoObj;
        this.textSize = textSize;
        this.reachedDistance = reachedDistance;
    }

    @Override
    public Updateable getMyParent() {
        return null;
    }

    @Override
    public void setMyParent(Updateable parent) {

    }

    @Override
    public boolean update(float timeDelta, Updateable parent) {
        if (timer.update(timeDelta, this)) {

            Location currentLocation = ConcreteSimpleLocationManager.getInstance(context)
                    .getCurrentLocation();
            if (currentLocation == null)
                return true;

            double distance = GeoUtils.distance(currentLocation.getLatitude(), geoObj.getLatitude()
                    , currentLocation.getLongitude(), geoObj.getLongitude(), 0, 0);
            String distanceS;
            if (reachedDistance != -1d && distance <= reachedDistance)
                distanceS = "You have arrived";
            else
                distanceS = GeoUtils.getDistanceString(distance);

            if (!distanceText.equals(distanceS)) {
                distanceText = distanceS;
                if (parent instanceof Obj) {
                    MeshComponent mc = ((Obj) parent).getGraphicsComponent();
                    if (mc != null) {
                        if (textMesh != null)
                            mc.remove(textMesh);
                        textMesh = getText(distanceS);
                        mc.addChild(textMesh);
                    }
                }
            }
        }
        return true;


    }

    @Override
    public boolean accept(Visitor visitor) {
        return false;
    }

    private MeshComponent getText(String text) {

        TextView v = new TextView(context);
        v.setTypeface(null, Typeface.BOLD);
        // Set textcolor to black:
        // v.setTextColor(new Color(0, 0, 0, 1).toIntARGB());
        v.setText(text);

        MeshComponent mesh = GLFactory.getInstance().newTexturedSquare("textBitmap"
                + text, com.droidar2.util.IO.loadBitmapFromView(v), textSize);

        return mesh;
    }
}

