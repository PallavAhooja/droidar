package de.rwth.setups;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.Color;
import com.droidar2.gl.CustomGLSurfaceView;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gl.scenegraph.Shape;
import com.droidar2.gui.GuiSetup;
import com.droidar2.gui.InfoScreenSettings;
import com.droidar2.system.ErrorHandler;
import com.droidar2.system.EventManager;
import com.droidar2.system.Setup;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;
import com.droidar2.actions.Action;
import com.droidar2.actions.ActionCalcRelativePos;
import com.droidar2.actions.ActionMoveCameraBuffered;
import com.droidar2.actions.ActionRotateCameraBuffered;
import android.app.Activity;

import com.droidar2.commands.ui.CommandShowToast;
import com.droidar2.components.ProximitySensor;

import de.rwth.R;

public class CollectItemsSetup extends Setup {

	private GLCamera camera;
	private World world;
	private int catchedNumber;

	@Override
	public void _a_initFieldsIfNecessary() {
		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in CollectItemsSetup");

	}

	@Override
	public void _b_addWorldsToRenderer(GL1Renderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		camera = new GLCamera(new Vec(0, 0, 1));
		world = new World(camera);

		MeshComponent arrow = GLFactory.getInstance().newArrow();
		arrow.setPosition(new Vec(0, 0, 4));

		MeshComponent circle = GLFactory.getInstance().newCircle(
				Color.redTransparent());
		circle.setScale(new Vec(4, 4, 4));
		// circle.myAnimation = new AnimationPulse(2, new Vec(0, 0, 0), new
		// Vec(4, 4, 4), 0.2f);

		final MeshComponent itemMesh = new Shape();
		itemMesh.addChild(arrow);
		itemMesh.addChild(circle);
		itemMesh.setPosition(Vec.getNewRandomPosInXYPlane(camera.getPosition(),
				5, 10));

		Obj itemToCollect = new Obj();
		itemToCollect.setComp(new ProximitySensor(camera, 3f) {
			@Override
			public void onObjectIsCloseToCamera(GLCamera myCamera2, Obj obj,
					MeshComponent m, float currentDistance) {
				catchedNumber++;
				new CommandShowToast(myTargetActivity, "You got me "
						+ catchedNumber + " times").execute();
				itemMesh.setPosition(Vec.getNewRandomPosInXYPlane(
						camera.getPosition(), 5, 20));
			}
		});

		itemToCollect.setComp(itemMesh);
		world.add(itemToCollect);
		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		ActionMoveCameraBuffered move = new ActionMoveCameraBuffered(camera, 5,
				25);
		arView.addOnTouchMoveAction(move);
		eventManager.addOnTrackballAction(move);
		Action rot = new ActionRotateCameraBuffered(camera);
		updater.addObjectToUpdateCycle(rot);
		eventManager.addOnOrientationChangedAction(rot);
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {

		worldUpdater.addObjectToUpdateCycle(world);

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity context) {
	}

	@Override
	public void _f_addInfoScreen(InfoScreenSettings infoScreenData) {
		infoScreenData
				.addText("There will an object spawned close to you which you have to collect!");
		infoScreenData
				.addTextWithIcon(
						R.drawable.infoboxblue,
						"Step into the red area to collect the object. A counter will signalize how often you collected the object. Instead of actually walking to the object you can simulate movement by using the trackball or the touchscreen.");
	}

}
