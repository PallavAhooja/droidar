package de.rwth.setups;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.scenegraph.Shape;
import com.droidar2.system.DefaultARSetup;
import com.droidar2.util.Vec;
import com.droidar2.worldData.World;

public class TestSetup extends DefaultARSetup {

	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {
		GeoObj o = new GeoObj(50.77854197, 6.06048614);
		Shape s = objectFactory.newSquare(null);
		s.setScale(new Vec(0.5f, 0.5f, 0.5f));
		o.setComp(s);
		world.add(o);
	}

}
