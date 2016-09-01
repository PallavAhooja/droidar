package com.droidar2.gl.animations;

/**
 * Created by vishwasrao on 01/09/16.
 */

import com.droidar2.gl.GLCamera;
import com.droidar2.gl.Renderable;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

import javax.microedition.khronos.opengles.GL10;



import com.droidar2.gl.GLCamera;
import com.droidar2.gl.Renderable;
import com.droidar2.gl.scenegraph.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import com.droidar2.util.Vec;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

public class AnimationFaceToObject extends GLAnimation {

    private Obj myTargetObj;
    private float lastUpdateAway = 0;
    private float myUpdateDelay;
    private Vec rotationVec = new Vec();
    private Vec newRotationVec = new Vec();

    private Vec adjustmentVec;
    private Vec myTargetObjPosition;
    private boolean dontChangeXRotation;

    /**
     * @param targetCamera
     * @param targetMesh
     * @param updateDelay
     *            around 0.5f s
     * @param dontChangeXRotation
     *            if this is false, the mesh will also change the rotation x
     *            value, otherwise only the z value to face to the camera
     */
    public AnimationFaceToObject(Obj targetObj, float updateDelay,
                                 boolean dontChangeXRotation) {
        myTargetObj = targetObj;

        myUpdateDelay = updateDelay;
        myTargetObjPosition = myTargetObj.getPosition();
        this.dontChangeXRotation = dontChangeXRotation;
        // Log.d("face camera animation", "created. camera=" + myTargetCamera
        // + " targetMesh class=" + myTargetMesh.getClass()
        // + " update delay=" + myUpdateDelay);
    }

    public AnimationFaceToObject(Obj targetObj, float updateDelay) {
        this(targetObj, updateDelay, true);
    }

    public AnimationFaceToObject(Obj targetObj) {
        this(targetObj, 0.5f, true);
    }

    public AnimationFaceToObject(Obj targetObj, boolean dontChangeXRotation) {
        this(targetObj, 0.1f, dontChangeXRotation);
    }


    /**
     * @param targetCamera
     * @param targetMesh
     * @param updateDelay
     *            0.5f
     * @param adjustmentVec
     */
    public AnimationFaceToObject(Obj targetObj, float updateDelay,
                                 Vec adjustmentVec) {
        this(targetObj, updateDelay);
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
        if (lastUpdateAway > myUpdateDelay) {
            updateRotation(parent);
            // Log.d("face camera animation", "new rotation vec calculated:");
            // Log.d("face camera animation",
            // "x="+newRotationVec.x+" , z="+newRotationVec.z);
            lastUpdateAway = 0;
        }
        if (dontChangeXRotation) {
            Vec.morphToNewAngleVec(rotationVec, 0, 0, newRotationVec.z,
                    timeDelta);
        } else {
            Vec.morphToNewAngleVec(rotationVec, newRotationVec.x,
                    newRotationVec.y, newRotationVec.z, timeDelta);
        }
        return true;
    }

    Vec absolutePosition = new Vec();

    private void updateRotation(Updateable parent) {
        if (parent instanceof MeshComponent) {
            absolutePosition.setToZero();
            ((MeshComponent) parent).getAbsoluteMeshPosition(absolutePosition);
            // Log.d("face camera animation", "mesh position: "+pos);
            newRotationVec.toAngleVec(absolutePosition, myTargetObjPosition);
			/*
			 * substract 90 from the x value becaute calcanglevec returns 90 if
			 * the rotation should be the horizon (which would mean no object
			 * rotation)
			 */
            newRotationVec.x -= 90;
            newRotationVec.z *= -1;
        }
    }

    @Override
    public void render(GL10 gl, Renderable parent) {

        gl.glRotatef(rotationVec.z, 0, 0, 1);
        gl.glRotatef(rotationVec.x, 1, 0, 0);
        gl.glRotatef(rotationVec.y, 0, 1, 0);

        if (adjustmentVec != null) {
			/*
			 * if an adjustment vector is set this adjustment has to be done
			 * AFTER the rotation to be easy to use, see constructor for infos
			 * about adjustment
			 */
            gl.glRotatef(adjustmentVec.x, 1, 0, 0); // TODO find correct order
            gl.glRotatef(adjustmentVec.z, 0, 0, 1);
            gl.glRotatef(adjustmentVec.y, 0, 1, 0);
        }
    }

    @Override
    public boolean accept(Visitor visitor) {
        return visitor.default_visit(this);
    }

}
