package com.crystal_ar.crystalcompsdemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Camera;
import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderAWD;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.security.InvalidParameterException;
import java.util.Vector;

/**
 * Created by Frederik on 2/16/17.
 */

public class ModelRenderer extends RajawaliRenderer implements SensorEventListener {

    private DirectionalLight directionalLight;
    private Object3D model;
    private double rotX, rotY,rotZ, camX, camY, camZ;
    private Vector3 pos;
    private Vector3 mAccVals = new Vector3();

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    public ModelRenderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    public void initScene(){
        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        Camera mCamera = new Camera();
        mCamera.setPosition(0, 0, 0);
//        getCurrentCamera().setZ(15f);
        getCurrentScene().addCamera(mCamera);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void setRotations(Vector3 angles, Vector3 trans, Vector3 actual){
        this.rotX = angles.x;
        this.rotY = angles.y;
        this.rotZ = angles.z;
        this.camX = trans.x;
        this.camY = trans.y;
        this.camZ = trans.z;
        this.pos = actual;

        Log.e("ROTATION x" , String.valueOf(rotX));
        Log.e("ROT  Y" , String.valueOf(rotY));
        Log.e("ROT  Z" , String.valueOf(rotZ));


    }

    public void renderModel(Integer modelID, Integer texture, String type) {
        // Clear lights and models.
        getCurrentScene().clearChildren();
        getCurrentScene().clearLights();
        getCurrentScene().addLight(directionalLight);

        // Create material.
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccVals = new Number3d();

        // Only add texture if the model has one.
        if (texture != null) {
            Texture objTexture = new Texture("objTexture", texture);
            try {
                material.addTexture(objTexture);
            } catch (ATexture.TextureException error) {
                Log.d("DEBUG", "TEXTURE ERROR");
            }
        }

        if (type.equals("obj")) {
            // OBJ loader/parser.
            LoaderOBJ parser = new LoaderOBJ(getContext().getResources(), mTextureManager, modelID);

            try {
                parser.parse();
            } catch (ParsingException e) {
                e.printStackTrace();
            }
            model = parser.getParsedObject();
        } else if (type.equals("awd")) {
            // AWD loader/parser.
            LoaderAWD parser = new LoaderAWD(getContext().getResources(), mTextureManager, modelID);
            try {
                parser.parse();
            } catch (ParsingException e) {
                e.printStackTrace();
            }
            model = parser.getParsedObject();
        } else {
            Log.d("DEBUG", "UNSUPPORTED FILE TYPE ERROR");
            throw new InvalidParameterException("renderModel, UNSUPPORTED FILE TYPE: " + type);
        }

        model.setMaterial(material);

        getCurrentScene().addChild(model);
    }

    public void setPosition(Double x, Double y, Double z) {
        // These are coordinates for the virtual world.
        model.setPosition(x, y, z);

//        // These are coordinates for the "real" world.
//        model.setScreenCoordinates(1.0, 1.0, getViewportWidth(), getViewportHeight(), 1.0);
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        // Rotate object.
        if (model != null) {
            //model.rotate(Vector3.Axis.Y, 1.0);

//            Log.e("COODINATE X" , String.valueOf(getCurrentCamera().getX()));
//            Log.e("COODINATE  Y" , String.valueOf(getCurrentCamera().getY()));
//            Log.e("COODINATE  Z" , String.valueOf(getCurrentCamera().getZ()));

//            pos.rotateX(Math.toRadians(rotX));
//            pos.rotateY(Math.toRadians(rotY));
//            pos.rotateZ(Math.toRadians(rotZ));

//            getCurrentCamera().setZ(pos.z);
//            getCurrentCamera().setX(pos.x);
//            getCurrentCamera().setY(pos.y);
//

//            model.setRotX(Math.toRadians(rotX));
//            model.setRotY(Math.toRadians(rotY));
//            model.setRotZ(Math.toRadians(rotZ));



            double X = getCurrentCamera().getX() + camX/10;
            double Y = getCurrentCamera().getY() + camY/10;
            double Z = getCurrentCamera().getZ() + camZ/10;

//

            getCurrentCamera().setX(X);
            getCurrentCamera().setY(Y);
            getCurrentCamera().setZ(Z);

            getCurrentCamera().enableLookAt();
            //getCurrentCamera().setLookAt(0.0,0.0,0.0);
        }
    }

    public void onTouchEvent(MotionEvent event){
    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){
    }
}
