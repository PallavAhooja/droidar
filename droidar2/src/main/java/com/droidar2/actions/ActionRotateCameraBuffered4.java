package com.droidar2.actions;

import com.droidar2.gl.GLCamRotationController;
import com.droidar2.actions.algos.BufferAlgo2;
import com.droidar2.actions.algos.SensorAlgo1;

public class ActionRotateCameraBuffered4 extends ActionWithSensorProcessing {

	public ActionRotateCameraBuffered4(GLCamRotationController targetCamera) {
		super(targetCamera);
	}

	@Override
	protected void initAlgos() {
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);
		accelBufferAlgo = new BufferAlgo2(0.2f);
		magnetBufferAlgo = new BufferAlgo2(0.2f);

		orientAlgo = new SensorAlgo1(0.5f);// TODO
		orientationBufferAlgo = new BufferAlgo2(0.2f); // TODO
	}

}
