package de.rwth.setups;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.system.DefaultARSetup;
import com.droidar2.util.Vec;
import com.droidar2.worldData.World;

public class GeoPosTestSetup extends DefaultARSetup {

	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {
		GeoObj o = new GeoObj(50.779058, 6.060429);
		o.setComp(GLFactory.getInstance().newSolarSystem(new Vec()));
		world.add(o);
	}

}
