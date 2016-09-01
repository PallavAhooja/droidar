package com.droidar2.components;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.util.Log;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Entity;
import com.droidar2.worldData.MoveComp;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.UpdateTimer;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

/**
 * Created by pallavahooja on 31/08/16.
 */
public class DirectionComp implements Entity {

    private GLCamera myCamera;
    private UpdateTimer timer;
    private GeoObj geoObj;

    private Vec camVec = new Vec();


    public DirectionComp(GLCamera myCamera, float updateSpeed) {
        this.myCamera = myCamera;
        timer = new UpdateTimer(updateSpeed, null);
   //     this.geoObj = geoObj;
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


            float[] rayDir = new float[4];
            float[] rayPos = new float[4];
            myCamera.getCameraViewDirectionRay(rayPos, rayDir);

//            camVec.x = rayPos[0];
//            camVec.y = rayPos[1];
//            camVec.z = rayPos[2];

            float [] cameraPos = new float[4];
            float [] cameraRay = new float[4];
            myCamera.getCameraViewDirectionRay(cameraPos, cameraRay);
            camVec = new Vec(cameraRay[0], cameraRay[1], cameraRay[2]);
//            camVec  = myCamera.getPosition().copy();

            Vec dirVec = new Vec(5,5,5);//geoObj.getVirtualPosition().copy();
//            dirVec.normalize();


            if (parent instanceof Obj) {
                MeshComponent mc = ((Obj) parent).getGraphicsComponent() ;
             //   mc.setRotation(dirVec);


                MoveComp m =((Obj)parent).getComp(MoveComp.class);
                if (m != null) {
                    m.myTargetPos = new Vec(-10,0,0);

                }
            }


//            Vec targetVec = myCamera
//                    .getPositionOnGroundWhereTheCameraIsLookingAt();

//            if (targetVec.getLength() > myMaxDistance) {
//                targetVec.setLength(myMaxDistance);
//            }
//
//            onPositionUpdate(parent, targetVec);
        }
        return true;

    }

    @Override
    public boolean accept(Visitor visitor) {
        return false;
    }
}
