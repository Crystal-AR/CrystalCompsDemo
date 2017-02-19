package com.crystal_ar.crystalcompsdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.crystal_ar.crystal_ar.CrystalAR;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frederik on 2/16/17.
 */

public class CornerActivity extends AppCompatActivity {

    private ImageView imageView;
    private CrystalAR crystalAR;
    private ModelRenderer objRenderer;
    private RajawaliSurfaceView rajSurface;
    private ListView objListView;
    private Float clickX;
    private Float clickY;
    private Context context = this;

    // Filenames for obj/awd files.
    // Do not include file extensions for awd files.
    // OBJ files do not have a file extension (convert .obj to _obj).
    String[] modelFileList = new String[] {
            "multiobjects_obj",
            "bumpsphere_obj",
            "bumptorus_obj",
            "dark_fighter", // awd
            "space_cruiser" // awd
    };

    // Filenames for textures.
    // Do not include file extensions.
    String[] modelTextureList = new String[] {
            null, // no texture
            "earthtruecolor_nasa_big",
            "torus_texture",
            "dark_fighter_6_color",
            "space_cruiser_4_color_1"
    };

    // The strings that appear in our list.
    String[] modelNameList = new String[] {
            "Multiple objects",
            "Earth",
            "Torus",
            "Dark fighter",
            "Space cruiser"
    };

    boolean[] modelOBJList = new boolean[]{
            true,
            true,
            true,
            false,
            false,
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corner);

        // Setup onTouchListener for imageView to register click coordinates.
        this.imageView = (ImageView) findViewById(R.id.cornerImageView);
        this.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clickX = event.getX();
                    clickY = event.getY();

                    // This is where we would use clickX and clickY to calculate where the model
                    // should be in the next frame.

                    // Swap views.
                    objListView.setVisibility(View.VISIBLE);
                    rajSurface.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        // Setup RajawaliSurfaceView and create ModelRenderer.
        rajSurface = new RajawaliSurfaceView(this);
        rajSurface.setFrameRate(60.0);
        rajSurface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
        addContentView(rajSurface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        this.objRenderer = new ModelRenderer(this);
        rajSurface.setSurfaceRenderer(objRenderer);

        creatListView();
    }

    private void creatListView() {
        objListView = (ListView) findViewById(R.id.modelList);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, modelNameList);
        objListView.setAdapter(adapter);

        // Create onclick functionality for each list item.
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                // Get the model from the resource folder.
                String modelFile = modelFileList[position];
                int model = context.getResources().getIdentifier(modelFile, "raw", context.getPackageName());

                // Get the texture from the resource folder.
                String textureFile = modelTextureList[position];
                Integer texture = textureFile == null
                    ? null
                    : context.getResources().getIdentifier(textureFile, "drawable", context.getPackageName());

                // Render model.
                objRenderer.renderModel(model, texture, modelOBJList[position]);
                // TODO[@stensaethf]
                // Change coordinates of where the model is displayed.
                objRenderer.setPosition(1.0, 1.0, 1.0);

                // Swap views.
                rajSurface.setVisibility(View.VISIBLE);
                objListView.setVisibility(View.INVISIBLE);
            }
        };

        objListView.setOnItemClickListener(itemClickListener);
    }
}
