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

    String[] objModelFileList = new String[]{
            "multiobjects_obj",
            "model1",
            "model2",
            "model3",
            "model4",
            "model5",
    };

    String[] objModelNameList = new String[]{
            "multiobjects",
            "model1",
            "model2",
            "model3",
            "model4",
            "model5",
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corner);

        this.imageView = (ImageView) findViewById(R.id.cornerImageView);
        this.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // What do we want?
                // on first click --> show list that the user can choose from.
                // when user chooses a model, display it at the click location.
                // do we want users to be able to drag the object around or adjust the size?
                // make it so that the background picture is visible.

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // logs the x,y coordinates in the monitor.
                    Log.d("COORDINATE", String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
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
        objListView = (ListView) findViewById(R.id.objModelsList);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, objModelNameList);
        objListView.setAdapter(adapter);

        // Create onclick functionality for each list item.
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                // Getting the Container Layout of the ListView
                Log.d("LIST CLICK", objModelNameList[position]);

                String modelStr = objModelNameList[position] + "_obj";
                int model = context.getResources().getIdentifier(modelStr, "raw", context.getPackageName());
                // this is where we would use clickX and clickY.
                objRenderer.renderModel(model, 1.0, 1.0, 1.0);
                rajSurface.setVisibility(View.VISIBLE);
                objListView.setVisibility(View.INVISIBLE);
            }
        };

        // Setting the item click listener for the listview
        objListView.setOnItemClickListener(itemClickListener);
    }
}
