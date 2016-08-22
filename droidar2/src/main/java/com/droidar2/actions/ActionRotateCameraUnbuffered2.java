package com.droidar2.actions;

import com.droidar2.gl.GLCamRotationController;
import com.droidar2.actions.algos.SensorAlgo1;

public class ActionRotateCameraUnbuffered2 extends ActionWithSensorProcessing {

	public ActionRotateCameraUnbuffered2(GLCamRotationController targetCamera) {
		super(targetCamera);
	}

	@Override
	protected void initAlgos() {
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);
		orientAlgo = new SensorAlgo1(0.5f);// TODO
	}

}
