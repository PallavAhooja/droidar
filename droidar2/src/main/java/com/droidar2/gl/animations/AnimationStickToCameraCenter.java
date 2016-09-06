package com.droidar2.gl.animations;

/**
 * Created by vishwasrao on 01/09/16.
 */

import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLRenderer;
import com.droidar2.gl.Renderable;
import com.droidar2.gl.scenegraph.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import com.droidar2.util.Log;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

public class AnimationStickToCameraCenter extends GLAnimation {

    private GLCamera myTargetCamera;
    private float lastUpdateAway = 0;
    private float myUpdateDelay;
    private Vec positionVec = new Vec();
    private Vec newPositionVec = new Vec();

    private Vec adjustmentVec;
    private Vec myTargetCameraPosition;
    private boolean dontChangeXRotation;

    private Vec offset = new Vec();

    /**
     * @param targetCamera
     * @param targetMesh
     * @param updateDelay
     *            around 0.5f s
     * @param dontChangeXRotation
     *            if this is false, the mesh will also change the rotation x
     *            value, otherwise only the z value to face to the camera
     */
    public AnimationStickToCameraCenter(GLCamera targetCamera, float updateDelay,
                                 boolean dontChangeXRotation) {
        myTargetCamera = targetCamera;

        myUpdateDelay = updateDelay;
        myTargetCameraPosition = myTargetCamera.getPosition().copy();
        this.dontChangeXRotation = false;
        // Log.d("face camera animation", "created. camera=" + myTargetCamera
        // + " targetMesh class=" + myTargetMesh.getClass()
        // + " update delay=" + myUpdateDelay);
    }

    public AnimationStickToCameraCenter(GLCamera targetCamera, float updateDelay) {
        this(targetCamera, updateDelay, true);
    }

    public AnimationStickToCameraCenter(GLCamera targetCamera) {
        this(targetCamera, 0.5f, true);
        positionVec.x = 0;
        positionVec.y = 0;
        positionVec.z = 0;
    }




    /**
     * @param targetCamera
     * @param targetMesh
     * @param updateDelay
     *            0.5f
     * @param adjustmentVec
     */
    public AnimationStickToCameraCenter(GLCamera targetCamera, float updateDelay,
                                 Vec adjustmentVec) {
        this(targetCamera, updateDelay);
        this.adjustmentVec = adjustmentVec;
    }

    @Override
    public boolean update(float timeDelta, Updateable parent) {

		/*
		 * TODO use mesh instead of assigning a mesh while creating this
		 * animation!
		 */
        timeDelta = Math.abs(timeDelta);
        lastUpdateAway += timeDelta;
       // if (lastUpdateAway > myUpdateDelay) {
            updatePosition(parent);
            // Log.d("face camera animation", "new rotation vec calculated:");
            // Log.d("face camera animation",
            // "x="+newRotationVec.x+" , z="+newRotationVec.z);
            lastUpdateAway = 0;
        //}
        return true;
    }


    private void updatePosition(Updateable parent) {
        if (parent instanceof MeshComponent) {
            // Log.d("face camera animation", "mesh position: "+pos);
            Vec rayPos = new Vec();
            Vec rayDir = new Vec();
            myTargetCamera.getPickingRay(rayPos, rayDir, GLRenderer.halfWidth, GLRenderer.halfHeight);
            newPositionVec = rayPos.copy().add(rayDir.setLength(5));
            if (adjustmentVec!=null)
                newPositionVec.add(adjustmentVec);
            ((MeshComponent) parent).setPosition(newPositionVec);

        }
    }

    @Override
    public void render(GL10 gl, Renderable parent) {

    }

    @Override
    public boolean accept(Visitor visitor) {
        return visitor.default_visit(this);
    }

}
