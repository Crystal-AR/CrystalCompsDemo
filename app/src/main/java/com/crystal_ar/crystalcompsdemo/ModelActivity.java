package com.crystal_ar.crystalcompsdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crystal_ar.crystal_ar.CrystalAR;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

/**
 * Created by Frederik on 2/16/17.
 */

public class ModelActivity extends AppCompatActivity {

    private Context context = this;
    private TextView clickText;
    private Float clickX;
    private Float clickY;
    private CrystalAR crystalAR;
    private ImageView imageView;
    private ListView modelListView;
    private ModelRenderer modelRenderer;
    private RajawaliSurfaceView rajSurface;

    // Filenames for obj/awd files.
    // Do not include file extensions for awd files.
    // OBJ files do not have a file extension (convert .obj to _obj).
    String[] modelFileList = new String[] {
            "multiobjects_obj",
            "bumpsphere_obj",
            "bumptorus_obj",
            "dark_fighter", // awd
            "space_cruiser", // awd
            "teapot_obj"
    };

    // The strings that appear in our list.
    String[] modelNameList = new String[] {
            "Multiple objects",
            "Earth",
            "Torus",
            "Dark fighter",
            "Space cruiser",
            "Teapot"
    };

    // Filenames for textures.
    // Do not include file extensions.
    String[] modelTextureList = new String[] {
            null, // no texture.
            "earthtruecolor_nasa_big",
            "torus_texture",
            "dark_fighter_6_color",
            "space_cruiser_4_color_1",
            null // no texture.
    };

    String[] modelTypeList = new String[]{
            "obj",
            "obj",
            "obj",
            "awd",
            "awd",
            "obj"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        // Store cornerOverlayText TextView.
        this.clickText = (TextView) findViewById(R.id.cornerOverlayText);

        // Setup onTouchListener for imageView to register click coordinates.
        this.imageView = (ImageView) findViewById(R.id.cornerImageView);
        this.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clickX = event.getX();
                    clickY = event.getY();

                    // This is where we would use clickX and clickY to calculate where the model
                    // should be.

                    // Swap views.
                    swapViews();
                }
                return true;
            }
        });

        // Setup RajawaliSurfaceView and create ModelRenderer.
        rajSurface = new RajawaliSurfaceView(this);
        rajSurface.setFrameRate(60.0);
        rajSurface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
        rajSurface.setTransparent(true);
        rajSurface.setZOrderOnTop(true);
        addContentView(rajSurface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        this.modelRenderer = new ModelRenderer(this);
        rajSurface.setSurfaceRenderer(modelRenderer);

        creatListView();
    }

    private void creatListView() {
        modelListView = (ListView) findViewById(R.id.modelList);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, modelNameList);
        modelListView.setAdapter(adapter);

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
                modelRenderer.renderModel(model, texture, modelTypeList[position]);
                // TODO[@stensaethf]
                // Change coordinates of where the model is displayed.
                modelRenderer.setPosition(1.0, 1.0, 1.0);

                // Swap views.
                swapViews();
            }
        };

        modelListView.setOnItemClickListener(itemClickListener);
    }

    private void swapViews() {
        if (modelListView.getVisibility() == View.INVISIBLE) {
            // Show list, hide rajSurface, hide click text.
            clickText.setVisibility(View.INVISIBLE);
            modelListView.setVisibility(View.VISIBLE);
            rajSurface.setVisibility(View.INVISIBLE);
        } else {
            // Hide list, show rajSurface, show click text.
            rajSurface.setVisibility(View.VISIBLE);
            rajSurface.setTransparent(true);
            clickText.setVisibility(View.VISIBLE);
            modelListView.setVisibility(View.INVISIBLE);
        }
    }
}
