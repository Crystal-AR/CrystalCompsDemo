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

/**
 * Created by Frederik on 2/16/17.
 */

public class OBJRenderer extends RajawaliRenderer {

    public Context context;
    private DirectionalLight directionalLight;
    private Object3D obj;

    public OBJRenderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    public void initScene(){
        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        getCurrentCamera().setZ(15f);
    }

    public void renderModel(Integer model, Double x, Double y, Double z) {
        renderModel(model, x, y, z, null, true);
    }

    public void renderModel(Integer model, Double x, Double y, Double z, Integer texture, boolean isOBJ) {
        // Clear lights and models.
        getCurrentScene().clearChildren();
        getCurrentScene().clearLights();
        getCurrentScene().addLight(directionalLight);

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        if (texture != null) {
            Texture objTexture = new Texture("objTexture", texture);
            try {
                material.addTexture(objTexture);
            } catch (ATexture.TextureException error) {
                Log.d("DEBUG", "TEXTURE ERROR");
            }
        }

        if (!isOBJ) {
            LoaderAWD parser = new LoaderAWD(getContext().getResources(), mTextureManager,
                model);
            try {
                parser.parse();
            } catch (ParsingException e) {
                e.printStackTrace();
            }
            obj = parser.getParsedObject();
        } else {
            LoaderOBJ parser = new LoaderOBJ(getContext().getResources(), mTextureManager,
                model);

            try {
                parser.parse();
            } catch (ParsingException e) {
                e.printStackTrace();
            }
            obj = parser.getParsedObject();
        }
//          obj = parser.getParsedObject();
        obj.setMaterial(material);
        obj.setX(x);
        obj.setY(y);
        obj.setZ(z);
        getCurrentScene().addChild(obj);
//      getCurrentScene().addLight(directionalLight); // this works, but a scene can only have 1 light, so an error is thrown for the next item.
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        if (obj != null) {
            obj.rotate(Vector3.Axis.Y, 1.0);
        }
    }

    public void onTouchEvent(MotionEvent event){
    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){
    }
}
