package com.droidar2.components;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.widget.TextView;

import com.droidar2.geo.GeoObj;
import com.droidar2.geo.GeoUtils;
import com.droidar2.gl.Color;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.GLText;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.system.ConcreteSimpleLocationManager;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Entity;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.UpdateTimer;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

import java.util.HashMap;

/**
 * Created by pallavahooja on 31/08/16.
 */
public class DistUpdateComp implements Entity {

    HashMap<String, MeshComponent> textMap = new HashMap<String, MeshComponent>();
    private GLCamera myCamera;
    private UpdateTimer timer;
    private Context context;
    private GeoObj geoObj;
    private String distanceText = "";
    private MeshComponent textMesh;
    private float textSize;
    private double reachedDistance;
    private Vec camVec = new Vec();
    private GLText text;


    public DistUpdateComp(GLCamera myCamera, float updateSpeed, Context context, GeoObj geoObj, Obj obj) {
        this(myCamera, updateSpeed, context, geoObj, 1, -1d, obj);
    }

    public DistUpdateComp(GLCamera myCamera, float updateSpeed, Context context, GeoObj geoObj, float textSize, Obj obj) {
        this(myCamera, updateSpeed, context, geoObj, textSize, -1d, obj);
    }

    public DistUpdateComp(GLCamera myCamera, float updateSpeed, Context context, GeoObj geoObj, float textSize, double reachedDistance, Obj obj) {
        this.myCamera = myCamera;
        timer = new UpdateTimer(updateSpeed, null);
        this.context = context;
        this.geoObj = geoObj;
        this.textSize = textSize;
        this.reachedDistance = reachedDistance;
        this.text = new GLText("", context, textMap,
                myCamera);

        MeshComponent mc = obj.getGraphicsComponent();
        if (mc != null) {
            mc.addChild(text);
        }
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
                    text.changeTextTo(distanceS);
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
        v.setTextColor(Color.white().toIntARGB());
        // v.setTextColor(new Color(0, 0, 0, 1).toIntARGB());
        v.setText(text);

        MeshComponent mesh = GLFactory.getInstance().newTexturedSquare("textBitmap"
                + text, com.droidar2.util.IO.loadBitmapFromView(v), textSize);

        return mesh;
    }


}

