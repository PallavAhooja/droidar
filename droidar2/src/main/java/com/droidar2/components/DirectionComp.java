package com.droidar2.components;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.GLRenderer;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gl.scenegraph.Shape;
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


    public DirectionComp(GLCamera myCamera, float updateSpeed, GeoObj geoObj) {
        this.myCamera = myCamera;
        timer = new UpdateTimer(updateSpeed, null);
        this.geoObj = geoObj;
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

            Vec dir =  new Vec();
            Vec pos = new Vec();

//            myCamera.getPickingRay(dir,pos,0,0);
            myCamera.getPickingRay(pos, dir, GLRenderer.halfWidth,
                     GLRenderer.halfHeight);

            dir.setLength(5f);

           camVec = dir.add(pos);

//            camVec  = myCamera.getPositionOnGroundWhereTheCameraIsLookingAt();
//            camVec  = myCamera.getPosition().copy();

            Vec dirVec = geoObj.getVirtualPosition().copy();
//            dirVec.normalize();
            Vec arrowPos = camVec;
//            arrowPos.normalize();
//            arrowPos.mult(6);

            dirVec.sub(arrowPos);
            dirVec.normalize();
            dirVec.mult(180f);



            if (parent instanceof Obj) {
                MeshComponent mc = ((Obj) parent).getGraphicsComponent() ;
//                mc.setRotation(dirVec);


                MoveComp m =((Obj)parent).getComp(MoveComp.class);
                if (m != null) {
                    m.myTargetPos = arrowPos;

                }
            }


//            Vec targetVec = myCamera
//                    .getPositionOnGroundWhereTheCameraIsLookingAt();

//            if (targetVec.getLength() > myMaxDistance) {
//                targetVec.setLength(myMaxDistance);
//            }
//
//            onPositionUpdate(parent, targetVec);


//            Vec direction = pos.copy().sub(myCamera.getPositionOnGroundWhereTheCameraIsLookingAt());
//            float length = direction.getLength();
//            direction.mult(-1);

//            Vec lineEndPos = direction.copy().setLength(-10);
//            ((Shape)parent).setMyRenderData(GLFactory.getInstance()
//                    .newDirectedPath(lineEndPos, null).getMyRenderData());

        }
        return true;

    }

    @Override
    public boolean accept(Visitor visitor) {
        return false;
    }
}
