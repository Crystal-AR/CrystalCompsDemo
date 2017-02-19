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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.crystal_ar.crystal_ar.CrystalAR;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

/**
 * Created by Frederik on 2/16/17.
 */

public class CornerActivity extends AppCompatActivity {

    private ImageView imageView;
    private CrystalAR crystalAR;
    private OBJRenderer objRenderer;
    private RajawaliSurfaceView rajSurface;
    private ListView objListView;
    private Float clickX;
    private Float clickY;
    private Context context = this;

    String[] modelFileList = new String[]{
            "multiobjects_obj",
            "bumpsphere_obj",
            "bumptorus_obj",
            "dark_fighter",
            "space_cruiser",
            null
    };

    String[] modelTextureList = new String[]{
            null,
            "earthtruecolor_nasa_big",
            "torus_texture",
            "dark_fighter_6_color",
            "space_cruiser_4_color_1",
            null
    };

    String[] modelNameList = new String[]{
            "Multiple objects",
            "Earth",
            "Torus",
            "Dark fighter",
            "Space cruiser",
            "EMPTY"
    };

    boolean[] modelOBJList = new boolean[]{
            true,
            true,
            true,
            false,
            false,
            true // set to correct value
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corner);

        this.imageView = (ImageView) findViewById(R.id.cornerImageView);
        this.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clickX = event.getX();
                    clickY = event.getY();
                    objListView.setVisibility(View.VISIBLE);
                    rajSurface.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        rajSurface = new RajawaliSurfaceView(this);
        rajSurface.setFrameRate(60.0);
        rajSurface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
        addContentView(rajSurface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        this.objRenderer = new OBJRenderer(this);
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
                // Get the model from the resource folder and render it.
                String modelFile = modelFileList[position];
                int model = context.getResources().getIdentifier(modelFile, "raw", context.getPackageName());
                // this is where we would use clickX and clickY.

                Log.d("HERE", "HERE");
                String textureFile = modelTextureList[position];
                Log.d("HERE", "HERE2");
                Integer texture = textureFile == null ? null : context.getResources().getIdentifier(textureFile, "drawable", context.getPackageName());
//                if ()
//                int texture = context.getResources().getIdentifier(textureFile, "drawable", context.getPackageName());
                Log.d("HERE", "HER3");

                objRenderer.renderModel(model, 1.0, 1.0, 1.0, texture, modelOBJList[position]);
                rajSurface.setVisibility(View.VISIBLE);
                objListView.setVisibility(View.INVISIBLE);
            }
        };

        // Setting the item click listener for the listview
        objListView.setOnItemClickListener(itemClickListener);
    }
}
