package droidar2.sample.setups;

import android.app.Activity;
import android.content.Context;

import com.droidar2.commands.Command;
import com.droidar2.components.DirectionComp;
import com.droidar2.geo.GeoObj;
import com.droidar2.gl.Color;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.animations.AnimationFaceToCamera;
import com.droidar2.gl.animations.AnimationFaceToObject;
import com.droidar2.gl.animations.AnimationMove;
import com.droidar2.gl.animations.AnimationStickToCameraCenter;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gl.scenegraph.Shape;
import com.droidar2.gui.GuiSetup;
import com.droidar2.gui.RadarView;
import com.droidar2.system.DefaultARSetup;
import com.droidar2.util.IO;
import com.droidar2.util.Log;
import com.droidar2.util.Vec;
import com.droidar2.worldData.MoveComp;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;

import droidar2.sample.R;

public class TestSetup extends DefaultARSetup {



	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {

		MeshComponent hippo = GLFactory.getInstance()
				.newTexturedSquare(
						"hippoId",
						IO.loadBitmapFromId(getActivity(),
								R.drawable.hippopotamus64));
		hippo.addChild(new AnimationFaceToCamera(camera, 0.5f));
		hippo.setScale(new Vec(5, 5, 5));
		GeoObj hippoObj = GeoObj.newRandomGeoObjAroundCamera(camera,10f,75f);
		hippoObj.setComp(hippo);
		world.add(hippoObj);
		world.add(newObject2(hippoObj));
		Log.e("World Setup: ", "init");

	}


	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		super._d_addElementsToUpdateThread(updater);

//		updater.addObjectToUpdateCycle();

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);

	}

	private Obj newObject2(Obj targetObj) {
		final Obj obj = new Obj();
		MeshComponent diamond = GLFactory.getInstance().newCuror();
		obj.setComp(diamond);
		obj.getMeshComp().addAnimation(new AnimationFaceToObject(targetObj, false));
		obj.getMeshComp().addAnimation(new AnimationStickToCameraCenter(camera, 0.1f));
		return obj;
	}


}
