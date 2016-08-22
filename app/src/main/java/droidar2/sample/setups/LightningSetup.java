package droidar2.sample.setups;

import com.droidar2.gl.Color;
import com.droidar2.gl.CustomGLSurfaceView;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.LightSource;
import com.droidar2.gl.animations.AnimationRotate;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gl.scenegraph.Shape;
import com.droidar2.gui.GuiSetup;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.droidar2.system.DefaultARSetup;
import com.droidar2.system.EventManager;
import com.droidar2.util.Vec;
import com.droidar2.util.Wrapper;
import com.droidar2.worldData.Entity;
import com.droidar2.worldData.MoveComp;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;
import com.droidar2.actions.ActionMoveObject;
import android.app.Activity;
import android.util.Log;

import com.droidar2.commands.Command;

public class LightningSetup extends DefaultARSetup {

	protected static final String LOG_TAG = "LightningSetup";

	private static float zMoveFactor = 1f;

	private final Wrapper targetMoveWrapper;

	private LightSource spotLight;

	private final Obj lightObject;

	public LightningSetup() {
		super();
		lightObject = new Obj();
		targetMoveWrapper = new Wrapper(lightObject);
	}

	@Override
	public boolean _a2_initLightning(ArrayList<LightSource> lights) {
		lights.add(LightSource.newDefaultAmbientLight(GL10.GL_LIGHT0));
		spotLight = LightSource.newDefaultDefuseLight(GL10.GL_LIGHT1, new Vec(
				0, 0, 0));
		lights.add(spotLight);
		return true;
	}

	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {

		addNewObjToWorld(world, objectFactory);
		MeshComponent innerGroup = new Shape();
		innerGroup.addChild(spotLight);
		innerGroup.addChild(objectFactory.newCircle(Color.red()));
		innerGroup.setPosition(new Vec(0, 3, 0));

		MeshComponent outerGroup = new Shape();
		outerGroup.addAnimation(new AnimationRotate(30, new Vec(0, 0, 1)));
		outerGroup.addChild(innerGroup);
		outerGroup.addChild(objectFactory.newCircle(Color.blue()));

		spotLight.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				targetMoveWrapper.setTo(lightObject);
				return true;
			}
		});

		lightObject.setComp(outerGroup);
		lightObject.setComp(new MoveComp(1));
		world.add(lightObject);

	}

	private void addNewObjToWorld(World world, GLFactory objectFactory) {
		final Obj o = new Obj();

		MeshComponent mesh = objectFactory.newCube();
		// mesh = newCube();
		mesh = objectFactory.newDiamond(Color.red());
		mesh.setScale(new Vec(2, 3, 1));
		mesh.addChild(new AnimationRotate(30, new Vec(0, 0, -1)));

		o.setComp(mesh);
		o.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				targetMoveWrapper.setTo(o);
				return true;
			}
		});
		o.setComp(new MoveComp(1));
		world.add(o);

		world.add(o);
	}

	private Entity newCube() {
		Shape s = new Shape();
		s.add(new Vec());
		s.add(new Vec(2, 2, 0));
		s.add(new Vec(2, 4, 0));

		s.add(new Vec());
		s.add(new Vec(2, 4, 0));
		s.add(new Vec(2, 10, 0));

		return s;
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		super._c_addActionsToEvents(eventManager, arView, updater);

		// clear some inputs set in default methods
		eventManager.getOnLocationChangedAction().clear();
		eventManager.getOnTrackballEventAction().clear();

		eventManager.addOnTrackballAction(new ActionMoveObject(
				targetMoveWrapper, getCamera(), 10, 100));
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				if (targetMoveWrapper.getObject() instanceof Obj) {
					MoveComp mover = ((Obj) targetMoveWrapper.getObject())
							.getComp(MoveComp.class);
					if (mover != null) {
						mover.myTargetPos.z -= zMoveFactor;
					} else {
						Vec pos = ((Obj) targetMoveWrapper.getObject())
								.getPosition();
						if (pos != null) {
							pos.z -= zMoveFactor;
						} else {
							Log.e(LOG_TAG, "Cant move object, has no position!");
						}
					}
					return true;
				}
				return false;
			}
		}, "Obj Down");
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				if (targetMoveWrapper.getObject() instanceof Obj) {
					MoveComp mover = ((Obj) targetMoveWrapper.getObject())
							.getComp(MoveComp.class);
					if (mover != null) {
						mover.myTargetPos.z += zMoveFactor;
					} else {
						Vec pos = ((Obj) targetMoveWrapper.getObject())
								.getPosition();
						if (pos != null) {
							pos.z += zMoveFactor;
						} else {
							Log.e(LOG_TAG, "Cant move object, has no position!");
						}
					}
					return true;
				}
				return false;
			}
		}, "Obj up");
	}

}
