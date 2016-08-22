package de.rwth.setups;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.Color;
import com.droidar2.gl.CustomGLSurfaceView;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gui.GuiSetup;
import com.droidar2.system.ErrorHandler;
import com.droidar2.system.EventManager;
import com.droidar2.system.Setup;
import com.droidar2.util.Vec;
import com.droidar2.worldData.MoveComp;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;
import com.droidar2.actions.Action;
import com.droidar2.actions.ActionBufferedCameraAR;
import com.droidar2.actions.ActionCalcRelativePos;
import com.droidar2.actions.ActionMoveCameraBuffered;
import com.droidar2.actions.ActionRotateCameraBuffered;
import android.app.Activity;

import com.droidar2.commands.Command;
import com.droidar2.components.ViewPosCalcerComp;

public class PlaceObjectsSetupTwo extends Setup {

	private GLCamera camera;
	private World world;
	private ViewPosCalcerComp viewPosCalcer;
	private Obj selectedObj;
	private MoveComp moveComp;

	@Override
	public void _a_initFieldsIfNecessary() {

		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");
		camera = new GLCamera(new Vec(0, 0, 15));
		world = new World(camera);
		viewPosCalcer = new ViewPosCalcerComp(camera, 150, 0.1f) {
			@Override
			public void onPositionUpdate(com.droidar2.worldData.Updateable parent,
										 Vec targetVec) {
				if (parent instanceof Obj) {
					Obj obj = (Obj) parent;
					MoveComp m = obj.getComp(MoveComp.class);
					if (m != null) {
						m.myTargetPos = targetVec;
					}
				}
			}
		};
		moveComp = new MoveComp(4);
	}

	@Override
	public void _b_addWorldsToRenderer(GL1Renderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		world.add(newObject());
		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.addOnTouchMoveAction(new ActionBufferedCameraAR(camera));
		Action rot = new ActionRotateCameraBuffered(camera);
		updater.addObjectToUpdateCycle(rot);
		eventManager.addOnOrientationChangedAction(rot);

		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		worldUpdater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity context) {
		guiSetup.addButtonToTopView(new Command() {

			@Override
			public boolean execute() {
				world.add(newObject());
				return true;
			}

		}, "Place next");

		guiSetup.setTopViewCentered();
	}

	private Obj newObject() {
		final Obj obj = new Obj();
		Color c = Color.getRandomRGBColor();
		c.alpha = 0.7f;
		MeshComponent diamond = GLFactory.getInstance().newDiamond(c);
		obj.setComp(diamond);
		setComps(obj);
		diamond.setOnClickCommand(new Command() {
			@Override
			public boolean execute() {
				setComps(obj);
				return true;
			}

		});
		return obj;
	}

	private void setComps(Obj obj) {
		if (selectedObj != null) {
			selectedObj.remove(viewPosCalcer);
			selectedObj.remove(moveComp);
		}
		obj.setComp(viewPosCalcer);
		obj.setComp(moveComp);
		selectedObj = obj;
	}
}
