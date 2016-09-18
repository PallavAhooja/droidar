package com.droidar2.actions;

import android.content.Context;
import android.hardware.SensorManager;

import com.droidar2.actions.algos.AlgoOrientWithoutGyro;
import com.droidar2.gl.GLCamRotationController;
import com.droidar2.util.Log;


/**
 * Created by vishwasrao on 18/09/16.
 */

public class ActionRotateCameraWithoutGyro extends ActionWithSensorProcessing {

    public ActionRotateCameraWithoutGyro(GLCamRotationController targetCamera) {
        super(targetCamera);
    }

    AlgoOrientWithoutGyro accelAlgo;
    AlgoOrientWithoutGyro magnetAlgo;

    AlgoOrientWithoutGyro accelAlgo2;
    AlgoOrientWithoutGyro magnetAlgo2;

    @Override
    public void initAlgos() {
        accelAlgo = new AlgoOrientWithoutGyro();
        magnetAlgo = new AlgoOrientWithoutGyro();
        accelAlgo2 = new AlgoOrientWithoutGyro();
        magnetAlgo2 = new AlgoOrientWithoutGyro();
    }

    @Override
    public synchronized boolean onAccelChanged(float[] values) {
        myNewAccelValues = accelAlgo.execute(accelAlgo2.execute(values));
        Log.d("AccelValues", Float.toString(myNewAccelValues[0]) + ", " + Float.toString(myNewAccelValues[1]) + ", " + Float.toString(myNewAccelValues[2]));
        float [] orientValues;
        if( myNewMagnetValues != null ) {
            computeRotationMatrix();
        }
        return true;
    }

    @Override
    public synchronized boolean onMagnetChanged(float[] values) {
        myNewMagnetValues = magnetAlgo.execute(magnetAlgo2.execute(values));
        Log.d("MagnetValues", Float.toString(myNewMagnetValues[0]) + ", " + Float.toString(myNewMagnetValues[1]) + ", " + Float.toString(myNewMagnetValues[2]));
        float [] orientValues;
        if( myNewAccelValues != null ) {
            computeRotationMatrix();
        }
        return true;
    }

    float [] m_rotationMatrix = new float[16];
    float [] m_iMatrix = new float[16];
    private void computeRotationMatrix() {

        if (SensorManager.getRotationMatrix(m_rotationMatrix, m_iMatrix,
                myNewAccelValues, myNewMagnetValues)) {
            super.onRotationMatrixChanged(m_rotationMatrix);
        }
    }

//    @Override
//    public synchronized boolean onOrientationChanged (float[] values) {
//
//        return true;
//    }
}
