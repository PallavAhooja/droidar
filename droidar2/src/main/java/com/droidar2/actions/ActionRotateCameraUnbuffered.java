package com.droidar2.actions;

import com.droidar2.gl.GLCamRotationController;

public class ActionRotateCameraUnbuffered extends ActionWithSensorProcessing {

	private static final String LOG_TAG = "ActionRotateCameraUnbuffered";

	public ActionRotateCameraUnbuffered(GLCamRotationController targetCamera) {
		super(targetCamera);
	}

	@Override
	protected void initAlgos() {
		// no buffering at all so dont init any algos
	}

}
