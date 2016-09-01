package droidar2.sample.setups;

import com.droidar2.commands.Command;
import com.droidar2.components.DirectionComp;
import com.droidar2.geo.GeoObj;
import com.droidar2.gl.Color;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.animations.AnimationFaceToCamera;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gl.scenegraph.Shape;
import com.droidar2.system.DefaultARSetup;
import com.droidar2.util.IO;
import com.droidar2.util.Vec;
import com.droidar2.worldData.MoveComp;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;

import droidar2.sample.R;

public class TestSetup extends DefaultARSetup {

	private DirectionComp directionComp;
	private MoveComp moveComp;
	private Obj selectedObj;


	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {
//		GeoObj o = new GeoObj(28.411882, 77.041739);
//
//		Obj textObj =objectFactory.newTextObject("Pick Up Point", o.getVirtualPosition(),
//				getActivity(), camera, o);
//
//		world.add(textObj);


//		Obj o = objectFactory.newTextObject("PPP",new Vec(1,1,1),getActivity(), camera);
//		o.setPosition(new Vec(-5,2,2));
//
//		world.add(o);


//		Shape s = objectFactory.newSquare(null);
//		s.setScale(new Vec(0.5f, 0.5f, 0.5f));
//		o.setComp(objectFactory.newSolarSystem(new Vec(1,1,1),o));
//		world.add(o);


		MeshComponent triangleMesh = GLFactory.getInstance()
				.newTexturedSquare(
						"hippoId",
						IO.loadBitmapFromId(getActivity(),
								R.drawable.hippopotamus64));
		triangleMesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
		triangleMesh.setScale(new Vec(5, 5, 5));
		GeoObj triangleGeo = new GeoObj(GeoObj.newRandomGeoObjAroundCamera(
				camera, 25f, 75f), triangleMesh);
		world.add(triangleGeo);

		directionComp = new DirectionComp(camera,0.1f,triangleGeo);
		moveComp = new MoveComp(4);
		world.add(newObject());

	}


	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		super._d_addElementsToUpdateThread(updater);

//		updater.addObjectToUpdateCycle();

	}

	private Obj newObject() {
		final Obj obj = new Obj();
		Color c = Color.getRandomRGBColor();
		c.alpha = 0.7f;
		MeshComponent diamond = GLFactory.getInstance().newDiamond(c);
		obj.setComp(diamond);
		setComps(obj);

//		diamond.setOnClickCommand(new Command() {
//			@Override
//			public boolean execute() {
//				setComps(obj);
//				return true;
//			}
//
//		});
		return obj;
	}

	private void setComps(Obj obj) {
		if (selectedObj != null) {
			selectedObj.remove(directionComp);
			selectedObj.remove(moveComp);
		}
		obj.setComp(directionComp);
		obj.setComp(moveComp);
		selectedObj = obj;
	}


}
