package droidar2.sample.setups;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.Color;
import com.droidar2.gl.CustomGLSurfaceView;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gui.GuiSetup;
import com.droidar2.system.EventManager;
import com.droidar2.system.Setup;
import com.droidar2.util.Vec;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;
import com.droidar2.actions.ActionCalcRelativePos;
import com.droidar2.actions.ActionMoveCameraBuffered;
import com.droidar2.actions.ActionRotateCameraBuffered;
import android.R;
import android.app.Activity;

import com.droidar2.commands.Command;
import com.droidar2.commands.DebugCommandPositionEvent;
import com.droidar2.commands.ui.CommandInUiThread;
import com.droidar2.commands.ui.CommandShowToast;

public class PositionTestsSetup extends Setup {

	protected static final int ZDELTA = 5;
	private final GLCamera camera;
	private final World world;
	private final ActionCalcRelativePos gpsAction;
	private final GeoObj posA;
	private final GeoObj posB;
	private final GeoObj posC;
	private final GeoObj posD;
	private final GeoObj posE;

	public PositionTestsSetup() {
		camera = new GLCamera();
		world = new World(camera);
		gpsAction = new ActionCalcRelativePos(world, camera);
		posA = new GeoObj(50.778922, 6.060461);
		posB = new GeoObj(50.780815, 6.06662);
		posC = new GeoObj(50.780557, 6.06735);
		posD = new GeoObj(50.779892, 6.065955);
		posE = new GeoObj(50.780408, 6.066492);

	}

	@Override
	public void _a_initFieldsIfNecessary() {

	}

	@Override
	public void _b_addWorldsToRenderer(GL1Renderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		spawnObj(posA, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posB, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posC, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posD, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posE, GLFactory.getInstance().newCircle(Color.blue()));

		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.addOnTouchMoveListener(new ActionMoveCameraBuffered(camera, 5,
				25));

		ActionRotateCameraBuffered rot = new ActionRotateCameraBuffered(camera);
		updater.addObjectToUpdateCycle(rot);
		eventManager.addOnOrientationChangedAction(rot);

		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		// eventManager.addOnLocationChangedAction(gpsAction);
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.setRightViewAllignBottom();

		guiSetup.addImangeButtonToRightView(R.drawable.arrow_up_float,
				new Command() {
					@Override
					public boolean execute() {
						camera.changeZPositionBuffered(+ZDELTA);
						return false;
					}
				});
		guiSetup.addImangeButtonToRightView(R.drawable.arrow_down_float,
				new Command() {
					@Override
					public boolean execute() {
						camera.changeZPositionBuffered(-ZDELTA);
						return false;
					}
				});

		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				gpsAction.resetWorldZeroPositions(camera.getGPSLocation());
				return false;
			}
		}, "Reset world zero pos");

		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posA), "Go to pos A");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posB), "Go to pos B");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posC), "Go to pos C");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posD), "Go to pos D");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posE), "Go to pos E");

		addSpawnButtonToUI(posA, "Spawn at posA", guiSetup);
		addSpawnButtonToUI(posB, "Spawn at posB", guiSetup);
		addSpawnButtonToUI(posC, "Spawn at posC", guiSetup);
		addSpawnButtonToUI(posD, "Spawn at posD", guiSetup);
		addSpawnButtonToUI(posE, "Spawn at posE", guiSetup);

		addGpsPosOutputButtons(guiSetup);

	}

	private void addGpsPosOutputButtons(GuiSetup guiSetup) {
		guiSetup.addButtonToBottomView(new CommandInUiThread() {

			@Override
			public void executeInUiThread() {
				Vec pos = camera.getGPSPositionVec();
				String text = "latitude=" + pos.y + ", longitude=" + pos.x;
				CommandShowToast.show(myTargetActivity, text);
			}
		}, "Show Camera GPS pos");

		guiSetup.addButtonToBottomView(new CommandInUiThread() {

			@Override
			public void executeInUiThread() {
				GeoObj pos = EventManager.getInstance()
						.getCurrentLocationObject();
				String text = "latitude=" + pos.getLatitude() + ", longitude="
						+ pos.getLongitude();
				CommandShowToast.show(myTargetActivity, text);
			}
		}, "Show real GPS pos");

		guiSetup.addButtonToBottomView(new CommandInUiThread() {

			@Override
			public void executeInUiThread() {
				GeoObj pos = EventManager.getInstance()
						.getZeroPositionLocationObject();
				String text = "latitude=" + pos.getLatitude() + ", longitude="
						+ pos.getLongitude();
				CommandShowToast.show(myTargetActivity, text);
			}
		}, "Show zero GPS pos");
	}

	private void addSpawnButtonToUI(final GeoObj pos, String buttonText,
			GuiSetup guiSetup) {
		guiSetup.addButtonToTopView(new Command() {
			@Override
			public boolean execute() {

				MeshComponent mesh = GLFactory.getInstance().newArrow();
				spawnObj(pos, mesh);
				return true;
			}

		}, buttonText);
	}

	private void spawnObj(final GeoObj pos, MeshComponent mesh) {
		GeoObj x = new GeoObj(pos);

		mesh.setPosition(Vec.getNewRandomPosInXYPlane(new Vec(), 0.1f, 1f));
		x.setComp(mesh);
		CommandShowToast.show(myTargetActivity, "Object spawned at "
				+ x.getMySurroundGroup().getPosition());
		world.add(x);
	}
}
