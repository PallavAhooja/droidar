package com.droidar2.system;

import com.droidar2.R;
import com.droidar2.actions.ActionGroundTilt;
import com.droidar2.actions.ActionRotateCameraWithoutGyro;
import com.droidar2.geo.GeoObj;
import com.droidar2.gl.CustomGLSurfaceView;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gui.GuiSetup;
import com.droidar2.util.Log;
import com.droidar2.util.Support;
import com.droidar2.util.Vec;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;
import com.droidar2.actions.Action;
import com.droidar2.actions.ActionCalcRelativePos;
import com.droidar2.actions.ActionMoveCameraBuffered;
import com.droidar2.actions.ActionRotateCameraBuffered;
import com.droidar2.actions.ActionWASDMovement;
import com.droidar2.actions.ActionWaitForAccuracy;

import android.app.Activity;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.droidar2.commands.Command;

/**
 * This is an example how you can use the default setup: <br>
 * <code>
 * ArActivity.startWithSetup(currentActicity, new DefaultARSetup() { <br>
 * public void addObjectsTo(World world, GLFactory factory) { <br>
 * GeoObj obj = new GeoObj();<br>
 * obj.setComp(factory.newCube()); world.add(obj); <br>
 * obj.setVirtualPosition(new Vec()); <br>
 * } <br>
 * });
 * <code>
 *
 * @author Spobo
 */
public abstract class DefaultARSetup extends Setup {

    protected static final int ZDELTA = 5;
    private static final String LOG_TAG = "DefaultARSetup";

    public GLCamera camera;
    public World world;
    private ActionWASDMovement wasdAction;
    private GL1Renderer myRenderer;
    private boolean addObjCalledOneTieme;
    private ActionWaitForAccuracy minAccuracyAction;
    private Action rotateGLCameraAction;
    private float minAccuracy = 24.0f;
    private boolean cameraPreview = true;
    private ToggleButton button;

    public DefaultARSetup() {

    }

    public DefaultARSetup(float minAccuracy) {
        this.minAccuracy = minAccuracy;
    }

    @Override
    public void _a_initFieldsIfNecessary() {
        camera = new GLCamera(new Vec(0, 0, 2));
        world = new World(camera);
    }

    public World getWorld() {
        return world;
    }

    public GLCamera getCamera() {
        return camera;
    }

    /**
     * This will be called when the GPS accuracy is high enough
     *
     * @param renderer
     * @param world
     * @param objectFactory
     */
    public abstract void addObjectsTo(GL1Renderer renderer, World world,
                                      GLFactory objectFactory);

    @Override
    public void _b_addWorldsToRenderer(GL1Renderer renderer,
                                       GLFactory objectFactory, GeoObj currentPosition) {
        myRenderer = renderer;
        renderer.addRenderElement(world);
    }

    @Override
    public void _c_addActionsToEvents(final EventManager eventManager,
                                      CustomGLSurfaceView arView, SystemUpdater updater) {
//		wasdAction = new ActionWASDMovement(camera, 25, 50, 20);

        if (Support.hasRotVec(getActivity()))
            rotateGLCameraAction = new ActionRotateCameraBuffered(camera);
        else
            rotateGLCameraAction = new ActionRotateCameraWithoutGyro(camera);
        eventManager.addOnOrientationChangedAction(rotateGLCameraAction);
        eventManager.addOnOrientationChangedAction(new ActionGroundTilt(this) {
            @Override
            public void onGroundParallel(boolean parallel) {
                if (button != null && button.isChecked()) {
                    if (parallel && cameraPreview)
                        pauseCameraPreview();
                    else if (!parallel && !cameraPreview)
                        resumeCameraPreview();
                    cameraPreview = !cameraPreview;
                }
            }
        });


//		arView.addOnTouchMoveListener(wasdAction);
        // eventManager.addOnOrientationChangedAction(rotateGLCameraAction);
//		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
//				5, 25));
        eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
                world, camera));
        minAccuracyAction = new ActionWaitForAccuracy(getActivity(), minAccuracy, 10) {
            @Override
            public void minAccuracyReachedFirstTime(Location l,
                                                    ActionWaitForAccuracy a) {
                callAddObjectsToWorldIfNotCalledAlready();
                if (!eventManager.getOnLocationChangedAction().remove(a)) {
                    Log.e(LOG_TAG,
                            "Could not remove minAccuracyAction from the onLocationChangedAction list");
                }
            }
        };
        eventManager.addOnLocationChangedAction(minAccuracyAction);

    }

    protected void callAddObjectsToWorldIfNotCalledAlready() {
        if (!addObjCalledOneTieme) {
            addObjectsTo(myRenderer, world, GLFactory.getInstance());
        } else {
            Log.w(LOG_TAG, "callAddObjectsToWorldIfNotCalledAlready() "
                    + "called more then one time!");
        }
        addObjCalledOneTieme = true;
    }

    @Override
    public void _d_addElementsToUpdateThread(SystemUpdater updater) {
        updater.addObjectToUpdateCycle(world);
        updater.addObjectToUpdateCycle(wasdAction);
        updater.addObjectToUpdateCycle(rotateGLCameraAction);
    }

    @Override
    public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
        guiSetup.setRightViewAllignBottom();

        guiSetup.addViewToTop(minAccuracyAction.getView());
        guiSetup.addViewToBottom(getCameraToggleView());

//		guiSetup.addImangeButtonToRightView(R.drawable.arrow_up_float,
//				new Command() {
//					@Override
//					public boolean execute() {
//						camera.changeZPositionBuffered(+ZDELTA);
//						return false;
//					}
//				});
//		guiSetup.addImangeButtonToRightView(R.drawable.arrow_down_float,
//				new Command() {
//					@Override
//					public boolean execute() {
//						camera.changeZPositionBuffered(-ZDELTA);
//						return false;
//					}
//				});

    }

    private View getCameraToggleView() {
        View viewContainer = View.inflate(getActivity(),
                R.layout.camera_toggle, null);
        button = (ToggleButton) viewContainer.findViewById(R.id.toggle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraPreview)
                    pauseCameraPreview();
                else
                    resumeCameraPreview();
                cameraPreview = !cameraPreview;
            }
        });

        return viewContainer;
    }

}
