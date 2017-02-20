package com.crystal_ar.crystalcompsdemo;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

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

/**
 * Created by Frederik on 2/16/17.
 */

public class ModelRenderer extends RajawaliRenderer {

    private DirectionalLight directionalLight;
    private Object3D model;

    public ModelRenderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    public void initScene(){
        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        getCurrentCamera().setZ(15f);
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
            model.rotate(Vector3.Axis.Y, 1.0);
        }
    }

    public void onTouchEvent(MotionEvent event){
    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){
    }
}
