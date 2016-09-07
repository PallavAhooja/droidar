package droidar2.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.droidar2.components.DistUpdateComp;
import com.droidar2.geo.GeoObj;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.animations.AnimationFaceToCamera;
import com.droidar2.gl.animations.AnimationFaceToObject;
import com.droidar2.gl.animations.AnimationRotate;
import com.droidar2.gl.animations.AnimationStickToCameraCenter;
import com.droidar2.gl.scenegraph.Shape;
import com.droidar2.gl.textures.TexturedShape;
import com.droidar2.oobjloader.builder.Build;
import com.droidar2.oobjloader.builder.Face;
import com.droidar2.oobjloader.builder.FaceVertex;
import com.droidar2.oobjloader.builder.Material;
import com.droidar2.oobjloader.parser.Parse;
import com.droidar2.system.DefaultARSetup;
import com.droidar2.util.Vec;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.World;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pallavahooja on 22/08/16.
 */
public class GeoSetup extends DefaultARSetup {

    private double mLat, mLng;
    private String mModelName;
    private File mDir;

    private Context context;

    public GeoSetup(Context context, double mLat, double mLng, File dir, String modelName) {
        this.mLat = mLat;
        this.mLng = mLng;
        this.context = context;
        this.mModelName = modelName;
        this.mDir = dir;


    }

    @Override
    public void addObjectsTo(GL1Renderer renderer, World world,
                             GLFactory objectFactory) {


        GeoObj o = new GeoObj(mLat, mLng, 0);
        o.setMaxVectorLength(100f);
        o.setMyMinVectorLength(10f);
        o.setComp(new Shape());


        Build builder = new Build();
        Parse obj = null;
        try {
            obj = new Parse(builder, this.context, mDir, this.mModelName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (builder.materialLib.isEmpty()) {
            //		CustomObj customObj = new CustomObj(builder);
            //o.setComp(customObj);
            //		o.getGraphicsComponent().addChild(customObj);
        } else {
            Iterator it = builder.groups.entrySet().iterator();
            Iterator itMaterials = builder.materialLib.entrySet().iterator();
            boolean add = true;
            HashMap<String, TexturedShape> TShapes = new HashMap<String, TexturedShape>();
            HashMap<String, Shape> Shapes = new HashMap<String, Shape>();
            int index = 0;


            while (itMaterials.hasNext()) {
                Map.Entry pair = (Map.Entry) itMaterials.next();

                String key = (String) pair.getKey();
                Material material = (Material) pair.getValue();
                if (material.mapKdFilename != null) {
                    String filename = this.mModelName + "/Texture/" + material.mapKdFilename;
                    File file = new File(mDir, filename);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                    TexturedShape shape = new TexturedShape(material.name, bitmap);
                    shape.setKa(material.ka.toFloat());
                    shape.setKd(material.kd.toFloat());
                    shape.setKs(material.ks.toFloat());
                    shape.setD((float) material.dFactor);
                    shape.setIllum((float) material.illumModel);
                    shape.setNs((float) material.nsExponent);
                    shape.setNi((float) material.niOpticalDensity);
                    TShapes.put(material.name, shape);
                    index++;
                } else {

                    Shape shape = new Shape();
                    shape.setKa(material.ka.toFloat());
                    shape.setKd(material.kd.toFloat());
                    shape.setKs(material.ks.toFloat());
                    shape.setD((float) material.dFactor);
                    shape.setIllum((float) material.illumModel);
                    shape.setNs((float) material.nsExponent);
                    shape.setNi((float) material.niOpticalDensity);
                    Shapes.put(material.name, shape);
                    index++;

                }

            }


            for (Face face : builder.faces) {
                if (face.material != null) {
                    if (!TShapes.isEmpty()) {
                        TexturedShape shape = TShapes.get(face.material.name);
                        if (shape != null)
                            for (FaceVertex vertex : face.vertices) {
                                shape.add(new Vec(vertex.v.x, vertex.v.y, vertex.v.z),
                                        new Vec(vertex.n.x, vertex.n.y, vertex.n.z), vertex.t.u, vertex.t.v);
                            }
                    }
                    if (!Shapes.isEmpty()) {
                        Shape shape = Shapes.get(face.material.name);
                        if (shape != null)
                            for (FaceVertex vertex : face.vertices) {
                                shape.add(new Vec(vertex.v.x, vertex.v.y, vertex.v.z),
                                        new Vec(vertex.n.x, vertex.n.y, vertex.n.z));
                            }
                    }

                } else {
                    int i = 0;
                }
            }

            if (!TShapes.isEmpty()) {
                Iterator itShapes = TShapes.entrySet().iterator();
                while (itShapes.hasNext()) {
                    Map.Entry pair = (Map.Entry) itShapes.next();
                    String key = (String) pair.getKey();
                    TexturedShape shape = (TexturedShape) pair.getValue();
                    shape.updateRest();
                    o.getGraphicsComponent().addChild(shape);

                }
            }

            if (!Shapes.isEmpty()) {
                Iterator itShapes = Shapes.entrySet().iterator();
                while (itShapes.hasNext()) {
                    Map.Entry pair = (Map.Entry) itShapes.next();
                    String key = (String) pair.getKey();
                    Shape shape = (Shape) pair.getValue();
                    shape.updateRest();
                    o.getGraphicsComponent().addChild(shape);

                }
            }


            o.refreshVirtualPosition();
            o.getGraphicsComponent().addAnimation(new AnimationRotate(30, new Vec(0, 0, 1)));
//            o.getGraphicsComponent().setRotation(new Vec(0,-90,0));
//            o.getGraphicsComponent().addAnimation(new AnimationFaceToCamera(camera));
            world.add(o);

            world.add(newArrow(o));

            world.add(newTextObject(o));

        }
    }


    private Obj newArrow(Obj targetObj) {
        final Obj obj = new Obj();
//        MeshComponent diamond = GLFactory.getInstance().newCuror();
//        obj.setComp(diamond);
        obj.setComp(new Shape());
        parseArrow(obj);
        obj.getMeshComp().addAnimation(new AnimationFaceToObject(targetObj, false));
        obj.getMeshComp().addAnimation(new AnimationStickToCameraCenter(camera, 0.1f));
        return obj;
    }

    private Obj newTextObject(GeoObj geoObj) {
        Obj o = new Obj();
        o.setComp(new Shape());
        o.setComp(new DistUpdateComp(camera, 1f, context, geoObj,0.5f));
        o.getGraphicsComponent().addAnimation(new AnimationFaceToCamera(camera,0.5f,false));
        o.getGraphicsComponent().addAnimation(new AnimationStickToCameraCenter(camera, 0.1f, new Vec(0, 0, 0.5f)));
        return o;
    }


    private void parseArrow(Obj o) {
        Build builder = new Build();
        Parse obj = null;
        try {
            obj = new Parse(builder, this.context, R.raw.arrow7_obj, R.raw.arrow7_mtl);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (builder.materialLib.isEmpty()) {

        } else {
            Iterator it = builder.groups.entrySet().iterator();
            Iterator itMaterials = builder.materialLib.entrySet().iterator();
            boolean add = true;
            HashMap<String, TexturedShape> TShapes = new HashMap<String, TexturedShape>();
            HashMap<String, Shape> Shapes = new HashMap<String, Shape>();
            int index = 0;


            while (itMaterials.hasNext()) {
                Map.Entry pair = (Map.Entry) itMaterials.next();

                String key = (String) pair.getKey();
                Material material = (Material) pair.getValue();
                if (material.mapKdFilename != null) {
                    String filename = "arrow3" + "/Texture/" + material.mapKdFilename;
                    File file = new File(mDir + filename);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                    TexturedShape shape = new TexturedShape(material.name, bitmap);
                    shape.setKa(material.ka.toFloat());
                    shape.setKd(material.kd.toFloat());
                    shape.setKs(material.ks.toFloat());
                    shape.setD((float) material.dFactor);
                    shape.setIllum((float) material.illumModel);
                    shape.setNs((float) material.nsExponent);
                    shape.setNi((float) material.niOpticalDensity);
                    TShapes.put(material.name, shape);
                    index++;
                } else {

                    Shape shape = new Shape();
                    shape.setKa(material.ka.toFloat());
                    shape.setKd(material.kd.toFloat());
                    shape.setKs(material.ks.toFloat());
                    shape.setD((float) material.dFactor);
                    shape.setIllum((float) material.illumModel);
                    shape.setNs((float) material.nsExponent);
                    shape.setNi((float) material.niOpticalDensity);
                    Shapes.put(material.name, shape);
                    index++;

                }

            }


            for (Face face : builder.faces) {
                if (face.material != null) {
                    if (!TShapes.isEmpty()) {
                        TexturedShape shape = TShapes.get(face.material.name);
                        for (FaceVertex vertex : face.vertices) {
                            shape.add(new Vec(vertex.v.x, vertex.v.y, vertex.v.z),
                                    new Vec(vertex.n.x, vertex.n.y, vertex.n.z), vertex.t.u, vertex.t.v);
                        }
                    }
                    if (!Shapes.isEmpty()) {
                        Shape shape = Shapes.get(face.material.name);
                        for (FaceVertex vertex : face.vertices) {
                            shape.add(new Vec(vertex.v.x, vertex.v.y, vertex.v.z),
                                    new Vec(vertex.n.x, vertex.n.y, vertex.n.z));
                        }
                    }

                } else {
                    int i = 0;
                }
            }

            if (!TShapes.isEmpty()) {
                Iterator itShapes = TShapes.entrySet().iterator();
                while (itShapes.hasNext()) {
                    Map.Entry pair = (Map.Entry) itShapes.next();
                    String key = (String) pair.getKey();
                    TexturedShape shape = (TexturedShape) pair.getValue();
                    shape.updateRest();
                    o.getGraphicsComponent().addChild(shape);

                }
            }

            if (!Shapes.isEmpty()) {
                Iterator itShapes = Shapes.entrySet().iterator();
                while (itShapes.hasNext()) {
                    Map.Entry pair = (Map.Entry) itShapes.next();
                    String key = (String) pair.getKey();
                    Shape shape = (Shape) pair.getValue();
                    shape.updateRest();
                    o.getGraphicsComponent().addChild(shape);

                }
            }

        }
    }

}
