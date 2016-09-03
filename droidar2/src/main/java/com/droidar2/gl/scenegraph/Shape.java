package com.droidar2.gl.scenegraph;

import com.droidar2.gl.Color;
import com.droidar2.gl.Renderable;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Visitor;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Shape extends MeshComponent {

	private ArrayList<Vec> myShapeArray;
	private ArrayList<Vec> myNormalArray;
	protected RenderData myRenderData;
	private boolean singeSide = false;

	public float[] mKa = {(float)0.0, (float)0.0, (float)0.0, (float)0.0};
	public float[] mKd = {(float)0.0, (float)0.0, (float)0.0, (float)0.0};
	public float[] mKs = {(float)0.0, (float)0.0, (float)0.0, (float)0.0};
	public float mNs = 0;
	public float mNi = 0;
	public float mD = 0;
	public float mIllum = 0;

	public void setKa(float[] mKa) {
		this.mKa = mKa.clone();

	}

	public void setKd(float[] mKd) {
		this.mKd = mKd.clone();
	}

	public void setKs(float[] mKs) {
		this.mKs = mKs.clone();
	}

	public void setNs(float mNs) {
		this.mNs = mNs;
	}

	public void setNi(float mNi) {
		this.mNi = mNi;
	}

	public void setD(float mD) {
		this.mD = mD;
	}

	public void setIllum(float mIllum) {
		this.mIllum = mIllum;
	}

	public Shape() {
		this(null);
	}

	public Shape(Color color) {
		super(color);
	}

	public Shape(Color color, Vec pos) {
		this(color);
		myPosition = pos;
	}


	public ArrayList<Vec> getMyShapeArray() {
		if (myShapeArray == null)
			myShapeArray = new ArrayList<Vec>();
		return myShapeArray;
	}

	public ArrayList<Vec> getMyNormalArray() {
		if (myNormalArray == null)
			myNormalArray = new ArrayList<Vec>();
		return myNormalArray;
	}

	public void add(Vec v) {
		if (myShapeArray == null)
			myShapeArray = new ArrayList<Vec>();
		myShapeArray.add(v.copy());

		if (myRenderData == null)
			myRenderData = new RenderData();
		myRenderData.updateShape(myShapeArray);
	}

	public void add(Vec v, Vec vn) {
		if (myShapeArray == null)
			myShapeArray = new ArrayList<Vec>();
		myShapeArray.add(v.copy());

		if (myNormalArray == null)
			myNormalArray = new ArrayList<Vec>();
		myNormalArray.add(vn.copy());

		if (myRenderData == null)
			myRenderData = new RenderData();
		myRenderData.updateShape(myShapeArray);
	}

	public void updateRest(){
		if (myShapeArray != null) {
			if (myRenderData == null)
				myRenderData = new RenderData();
			myRenderData.updateShape(myShapeArray, getMyNormalArray());
		}
		this.setColor(new Color(mKd[0], mKd[1], mKd[2], mD));
	}

	/**
	 * use this to add multiple vectors at once and call
	 * {@link Shape#updateRenderDataManually()} after all vectors are added!
	 * 
	 * @param v
	 */
	public void addFast(Vec v) {
		if (myShapeArray == null)
			myShapeArray = new ArrayList<Vec>();
		myShapeArray.add(v.copy());
	}

	/**
	 * Use this in combination with {@link Shape#addFast(Vec)}
	 */
	public void updateRenderDataManually() {
		if (myShapeArray != null) {
			if (myRenderData == null)
				myRenderData = new RenderData();
			myRenderData.updateShape(myShapeArray);
		}
	}

	@Override
	public void draw(GL10 gl, Renderable parent) {
		if (myRenderData != null) {
			if (singeSide) {
				// which is the front? the one which is drawn counter clockwise
				gl.glFrontFace(GL10.GL_CCW);
				// enable the differentiation of which side may be visible
				gl.glEnable(GL10.GL_CULL_FACE);
				// which one should NOT be drawn
				gl.glCullFace(GL10.GL_BACK);
				gl.glLightModelf(GL10.GL_LIGHT_MODEL_TWO_SIDE, 0);

				mKa[3] = mKs[3] = mKs[3] = mD;
				gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT, mKa, 0);
				gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, mKd, 0);
				gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, mKs, 0);
				gl.glMaterialf(gl.GL_FRONT_AND_BACK, gl.GL_SHININESS,
						Math.min(Math.max((float) mNs, 0), 128));

				myRenderData.draw(gl);

				// Disable face culling.
				gl.glDisable(GL10.GL_CULL_FACE);
			} else {
				/*
				 * The GL_LIGHT_MODEL_TWO_SIDE can be used to use the same
				 * normal vector and light for both sides of the mesh
				 */
				mKa[3] = mKs[3] = mKs[3] = mD;
				gl.glLightModelf(GL10.GL_LIGHT_MODEL_TWO_SIDE, 1);
				gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT, mKa, 0);
				gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, mKd, 0);
				gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, mKs, 0);
				gl.glMaterialf(gl.GL_FRONT_AND_BACK, gl.GL_SHININESS,
						Math.min(Math.max((float) mNs, 0), 128));
				myRenderData.draw(gl);
			}
		}
	}

	public void setMyRenderData(RenderData myRenderData) {
		this.myRenderData = myRenderData;
	}

	public void setTriangleDrawing() {
		if (myRenderData == null)
			myRenderData = new RenderData();
		myRenderData.drawMode = GL10.GL_TRIANGLES;
	}

	public void setLineDrawing() {
		if (myRenderData == null)
			myRenderData = new RenderData();
		myRenderData.drawMode = GL10.GL_LINES;
	}

	/*
	 * also possible: GL_POINTS GL_LINE_STRIP GL_TRIANGLE_STRIP GL_TRIANGLE_FAN
	 */

	public void setLineLoopDrawing() {
		myRenderData.drawMode = GL10.GL_LINE_LOOP;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	@Override
	public String toString() {
		return "Shape " + super.toString();
	}

	public void clearShape() {
		setMyRenderData(null);
	}

	public RenderData getMyRenderData() {
		return myRenderData;
	}

}
