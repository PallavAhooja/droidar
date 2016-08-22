package com.droidar2.geo;

import com.droidar2.gl.GLCamera;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.scenegraph.MeshComponent;

public class DefaultNodeEdgeListener extends SimpleNodeEdgeListener {

	public DefaultNodeEdgeListener(GLCamera glCamera) {
		super(glCamera);
	}

	@Override
	public MeshComponent getEdgeMesh(GeoGraph targetGraph, GeoObj startPoint,
			GeoObj endPoint) {
		return Edge.getDefaultMesh(targetGraph, startPoint, endPoint, null);
	}

	@Override
	public MeshComponent getNodeMesh() {
		return GLFactory.getInstance().newDiamond(null);
	}

}