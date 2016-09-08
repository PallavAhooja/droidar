package com.droidar2.system;

import java.io.IOException;

import com.droidar2.util.Log;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String LOG_TAG = "CameraView";
	SurfaceHolder mHolder;
	Camera myCamera;

	public CameraView(Context context) {
		super(context);
		intiCameraView(context);
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		intiCameraView(context);

	}

	private void intiCameraView(Context context) {
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		Log.d("Activity", "Camera holder created");
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it
		// where to draw.
		myCamera = Camera.open();
		Log.d("Activity", "Camera opened");
		try {
			myCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Now that the size is known, set up the camera parameters and
		// begin the preview.
		// Camera.Parameters parameters = mCamera.getParameters();
		// parameters.setPreviewSize(w, h);
		// mCamera.setParameters(parameters);

//		setPreviewAccordingToScreenOrientation(width, height);
		setCameraDisplayOrientation(findBackFacingCameraID(),myCamera);
		resumeCamera();
	}

	/**
	 * http://stackoverflow.com/questions/3841122/android-camera-preview-is-
	 * sideways
	 */
	public void setPreviewAccordingToScreenOrientation(int width, int height) {
		Parameters parameters = myCamera.getParameters();
		Display display = ((WindowManager) this.getContext().getSystemService(
				Activity.WINDOW_SERVICE)).getDefaultDisplay();
		/*
		 * int rotation = display.getRotation();
		 * 
		 * this does not work on older devices so use reflection
		 */
		int rotation = 0;
		try {
			rotation = (Integer) display.getClass()
					.getMethod("getRotation", null).invoke(display, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rotation == Surface.ROTATION_0) {
			parameters.setPreviewSize(height, width);
			setDisplayOrientation(90);
		} else if (rotation == Surface.ROTATION_90) {
			parameters.setPreviewSize(width, height);
			setDisplayOrientation(0);
		} else if (rotation == Surface.ROTATION_180) {
			parameters.setPreviewSize(height, width);
			setDisplayOrientation(0);
		} else if (rotation == Surface.ROTATION_270) {
			parameters.setPreviewSize(width, height);
			setDisplayOrientation(180);
		}
		try {
			myCamera.setParameters(parameters);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Could not set camera parameters:");
			e.printStackTrace();
		}
	}

	private void setDisplayOrientation(int inDegree) {
		/*
		 * myCamera.setDisplayOrientation(inDegree);
		 * 
		 * does not work on older devices so use reflection
		 */
		try {
			myCamera.getClass().getMethod("setDisplayOrientation", int.class)
					.invoke(myCamera, inDegree);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the
		// preview.
		// Because the CameraDevice object is not a shared resource,
		// it's very important to release it when the activity is paused.
		releaseCamera();
	}

	public void resumeCamera() {
		if (myCamera != null) {
			myCamera.startPreview();
			Log.d("Activity", "Camera preview started (camera=" + myCamera
					+ ")");
		} else {
			Log.d("Activity",
					"Camera preview not started because no camera set til now");
		}
	}

	public void pause() {
		if (myCamera != null) {
			Log.d("Activity", "Camera preview stopped");
			myCamera.stopPreview();
		}
	}

	public void releaseCamera() {
		if (myCamera != null) {
			myCamera.stopPreview();
			myCamera.release();
			myCamera = null;
			Log.d("Activity", "Camera released");
		}
	}

	public void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info =
				new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		Display display = ((WindowManager) this.getContext().getSystemService(
				Activity.WINDOW_SERVICE)).getDefaultDisplay();

		int rotation = display.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0: degrees = 0; break;
			case Surface.ROTATION_90: degrees = 90; break;
			case Surface.ROTATION_180: degrees = 180; break;
			case Surface.ROTATION_270: degrees = 270; break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	private int findBackFacingCameraID() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				Log.d(LOG_TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}


}
