package com.droidar2.gl.scenegraph;

import com.droidar2.gl.GLUtilityClass;
import com.droidar2.gl.Renderable;
import com.droidar2.oobjloader.builder.Build;
import com.droidar2.oobjloader.builder.FaceVertex;
import com.droidar2.oobjloader.builder.Material;
import com.droidar2.oobjloader.builder.VertexGeometric;
import com.droidar2.oobjloader.builder.VertexNormal;
import com.droidar2.worldData.RenderableEntity;
import com.droidar2.worldData.Updateable;
import com.droidar2.worldData.Visitor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by pallavahooja on 16/08/16.
 */
public class CustomObj implements RenderableEntity {
    FloatBuffer normalsBuffer,vertexBuffer;
    IntBuffer shapeBuffer;
    ArrayList<int[]> shapeList;

    public int drawMode = GL10.GL_TRIANGLES;
    private int verticesCount;
    public Build mBuilder;
    static float mRotationAngle = 0;


    public CustomObj(Build builder) {
       // this,vertexBuffer =
        this.mBuilder = builder;
        ArrayList<FaceVertex> faceVerticeList = builder.faceVerticeList;
        this.vertexBuffer = GLUtilityClass.createAndInitFloatBuffer(
               turnShapeToFloatArray(faceVerticeList));
        this.normalsBuffer =GLUtilityClass.createAndInitFloatBuffer(
                turnNormalsToFloatArray(faceVerticeList));
//        this.normalsBuffer = RenderData.getNormalsBuffer(normalsBuffer);
//
//
//        ByteBuffer b = ByteBuffer.allocateDirect(shapeList.size()*12);
//        b.order(ByteOrder.nativeOrder());
//        IntBuffer a = b.asIntBuffer();
//
//        for (int[] f : shapeList) {
//            a.put(f);
//
//        }
//        a.position(0);
//        this.shapeBuffer =a;
//
//
//        this.shapeList = shapeList;

    }

    @Override
    public void render(GL10 gl, Renderable parent) {

        gl.glPushMatrix();
        gl.glRotatef(mRotationAngle, 0, 0, 1);

        Material material = mBuilder.materialLib.get("Material__25");

        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT, material.ka.toFloat(), 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, material.kd.toFloat(), 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, material.ks.toFloat(), 0);
        gl.glMaterialf(gl.GL_FRONT_AND_BACK, gl.GL_SHININESS,
                Math.min(Math.max((float)material.nsExponent, 0), 128));


        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        if (normalsBuffer != null) {
            // Enable normals array (for lightning):
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBuffer);
        }
        gl.glDrawArrays(drawMode, 0, verticesCount);

//        gl.glDrawElements(drawMode, shapeList.size() , GL10.GL_UNSIGNED_BYTE, shapeBuffer);


        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        // Disable normals array (for lightning):
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

        mRotationAngle += 5;


        gl.glPopMatrix();


    }

    @Override
    public Updateable getMyParent() {
        return null;
    }

    @Override
    public void setMyParent(Updateable parent) {

    }

    @Override
    public boolean update(float timeDelta, Updateable parent) {
        return true;
    }

    @Override
    public boolean accept(Visitor visitor) {
        return false;
    }

    public float[] turnShapeToFloatArray( ArrayList<FaceVertex> faceVerticeList) {

        float[] vertices = new float[faceVerticeList.size() * 3];
        verticesCount = faceVerticeList.size();
        int i = 0;
        for (FaceVertex vertex : faceVerticeList) {
            i = addFaceVertex(vertices, i, vertex.v);
        }
        return vertices;
    }
    public float[] turnNormalsToFloatArray( ArrayList<FaceVertex> faceVerticeList) {

        float[] vertices = new float[faceVerticeList.size() * 3];
        verticesCount = faceVerticeList.size();
        int i = 0;
        for (FaceVertex vertex : faceVerticeList) {
            i = addFaceNormal(vertices, i, vertex.n);
        }
        return vertices;
    }
    public int addFaceVertex(float[] normalsArray, int j,
                             VertexGeometric vertexGeometric) {
        normalsArray[j] = vertexGeometric.x;
        j++;
        normalsArray[j] = vertexGeometric.y;
        j++;
        normalsArray[j] = vertexGeometric.z;
        j++;
        return j;
    }
    public int addFaceNormal(float[] normalsArray, int j,
                             VertexNormal vertexNormal) {
        normalsArray[j] = vertexNormal.x;
        j++;
        normalsArray[j] = vertexNormal.y;
        j++;
        normalsArray[j] = vertexNormal.z;
        j++;
        return j;
    }

}