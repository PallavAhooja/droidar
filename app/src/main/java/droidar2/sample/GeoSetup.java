package droidar2.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.droidar2.geo.GeoObj;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.scenegraph.CustomObj;
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

    private Context context;

    public GeoSetup(Context context, double mLat, double mLng, String modelName) {
        this.mLat = mLat;
        this.mLng = mLng;
        this.context = context;
        this.mModelName = modelName;


/*
        try {
            mModel = new MyObjModel(ObjModel.loadFromStream(context.getResources().openRawResource(R.raw.axis_die), "mat1_dice.jpg"), context);
        } catch (java.io.IOException e) {
            Log.v("DemoRendererView", "loading model: " + e);
        }
*/

    }

    @Override
    public void addObjectsTo(GL1Renderer renderer, World world,
                             GLFactory objectFactory) {
//
//    world.add(objectFactory.newTextObject("DroidAR", new Vec(10, 1, 1),
//          getActivity(), camera));

//    GeoObj o = GeoObj.rwthI9;

        GeoObj o = new GeoObj(mLat, mLng, 100);
        o.setMaxVectorLength(1);

        Obj textObj =objectFactory.newTextObject("Pick Up Point", o.getVirtualPosition(),
                getActivity(), camera, o);

        o.getMeshComp().setScale(new Vec(1, 1, 1));

//
//		world.add(objectFactory.newSolarSystem(new Vec(-10, 1, 1), o));

//    o.setComp(objectFactory.newTextObject("pp",new Vec(10,1,1),getActivity(),camera));
//    world.add(o);
//    o.refreshVirtualPosition();


        Build builder = new Build();
        Parse obj = null;
        try {
            obj = new Parse(builder, this.context, this.mModelName);
        } catch (Exception E){

        }

        if( builder.groups.isEmpty()) {
            CustomObj customObj = new CustomObj(builder);
            //o.setComp(customObj);
            o.getGraphicsComponent().addChild(customObj);
        }
        else {
            Iterator it = builder.groups.entrySet().iterator();
            Iterator itMaterials = builder.materialLib.entrySet().iterator();
            boolean add = true;
            HashMap<String, TexturedShape> Shapes = new HashMap<String, TexturedShape>();
            int index = 0;

            while (itMaterials.hasNext()) {
                Map.Entry pair = (Map.Entry) itMaterials.next();

                String key = (String) pair.getKey();
                Material material = (Material) pair.getValue();

                String filename = "/AR/" + this.mModelName + "/Texture/" + material.mapKdFilename;
                File file = new File(Environment.getExternalStorageDirectory() + filename);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                TexturedShape shape = new TexturedShape( material.name, bitmap);
                Shapes.put( material.name, shape);
                index++;
            }


            for( Face face: builder.faces){
                if( face.material != null){
                    TexturedShape shape = Shapes.get(face.material.name);
                    for (FaceVertex vertex : face.vertices) {
                        shape.add(new Vec(vertex.v.x, vertex.v.y, vertex.v.z),
                                new Vec(vertex.n.x, vertex.n.y, vertex.n.z), vertex.t.u, vertex.t.v);
                    }

                }
            }

            Iterator itShapes = Shapes.entrySet().iterator();
            while (itShapes.hasNext()) {
                Map.Entry pair = (Map.Entry) itShapes.next();

                String key = (String) pair.getKey();
                TexturedShape shape = (TexturedShape) pair.getValue();
                shape.updateRest();
                o.getGraphicsComponent().addChild(shape);

            }


//			while (it.hasNext()) {
//				if( !add )
//					break;
//				Map.Entry pair = (Map.Entry)it.next();
//
//				String key = (String)pair.getKey();
//				ArrayList<Face> faces =  (ArrayList<Face>)pair.getValue();
//
//				for( Face face : faces) {
//					String filename = "/Work/" + this.mModelName + "/Texture/" + face.material.mapKdFilename;
//					try {
//						File file = new File(Environment.getExternalStorageDirectory() + filename);
//						BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//						Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
//						TexturedShape shape = new TexturedShape(face.material.mapKdFilename, bitmap);
//						for (FaceVertex vertex : face.vertices) {
//							shape.add(new Vec(vertex.v.x, vertex.v.y, vertex.v.z), vertex.t.u, vertex.t.v);
//						}
//						o.getGraphicsComponent().addChild(shape);
//					} catch (Exception E){
//						int i = 0;
//					}
//				}
//
//			}
        }


        o.refreshVirtualPosition();
        world.add(o);

    }


}
