package com.droidar2.actions;

import com.droidar2.gl.GLCamRotationController;
import com.droidar2.actions.algos.BufferAlgo3;
import com.droidar2.actions.algos.SensorAlgo1;

public class ActionRotateCameraBuffered3 extends ActionWithSensorProcessing {

	public ActionRotateCameraBuffered3(GLCamRotationController targetCamera) {
		super(targetCamera);
	}

	@Override
	protected void initAlgos() {
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);

		orientAlgo = new SensorAlgo1(0.5f);// TODO
		orientationBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4); // TODO

		accelBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4);
		magnetBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4);
	}

}
