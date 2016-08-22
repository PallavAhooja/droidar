package com.droidar2.gl.textures;

import com.droidar2.gl.scenegraph.Shape;

import java.util.ArrayList;

import com.droidar2.util.Log;
import com.droidar2.util.Vec;
import android.graphics.Bitmap;

public class TexturedShape extends Shape {

	/**
	 * this values are corresponding to the shape edges
	 */
	ArrayList<Vec> myTexturePositions = new ArrayList<Vec>();
	String mTextureName;
	Bitmap mTexture;

	/**
	 * Please read
	 * {@link TextureManager#addTexture(TexturedRenderData, Bitmap, String)} for
	 * information about the parameters
	 * 
	 * @param textureName
	 * @param texture
	 */
	public TexturedShape(String textureName, Bitmap texture) {
		super(null);
		myRenderData = new TexturedRenderData();
		mTexture = texture;
		mTextureName = textureName;
		/*
		 * TODO redesign this so that the input texture is projected on the mesh
		 * correctly
		 */
		if (texture != null) {
			texture = TextureManager.getInstance().resizeBitmapIfNecessary(
					texture);
			TextureManager.getInstance().addTexture(
					(TexturedRenderData) myRenderData, texture, textureName);
		} else {
			Log.e("TexturedShape",
					"got null-bitmap! check bitmap creation process");
		}
	}

	public void add(Vec vec, int x, int y) {
		getMyShapeArray().add(vec);
		// z coordinate not needed for 2d textures:
		myTexturePositions.add(new Vec(x, y, 0));
		myRenderData.updateShape(getMyShapeArray());
		((TexturedRenderData) myRenderData)
				.updateTextureBuffer(myTexturePositions);
	}

	public void add(Vec vec, Vec vn, float x, float y) {
		getMyShapeArray().add(vec);
		getMyNormalArray().add(vn);
		// z coordinate not needed for 2d textures:
		myTexturePositions.add(new Vec(x, y, 0));

	}
	public void updateRest(){

		myRenderData.updateShape(getMyShapeArray(), getMyNormalArray());
		((TexturedRenderData) myRenderData)
				.updateTextureBuffer(myTexturePositions);
		if (mTexture != null) {
			mTexture = TextureManager.getInstance().resizeBitmapIfNecessary(
					mTexture);
			TextureManager.getInstance().addTexture(
					(TexturedRenderData) myRenderData, mTexture, mTextureName);
		} else {
			Log.e("TexturedShape",
					"got null-bitmap! check bitmap creation process");
		}
	}

	@Override
	public void add(Vec v) {
		// TODO throw error
		super.add(v);
	}

}
