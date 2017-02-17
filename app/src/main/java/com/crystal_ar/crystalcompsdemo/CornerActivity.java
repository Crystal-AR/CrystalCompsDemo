package com.crystal_ar.crystalcompsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crystal_ar.crystal_ar.CrystalAR;

/**
 * Created by Frederik on 2/16/17.
 */

public class CornerActivity extends AppCompatActivity {

    private ImageView imageView;
    private CrystalAR crystalAR;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corner);

        this.imageView = (ImageView) findViewById(R.id.cornerImageView);
        this.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    // logs the x,y coordinates in the monitor.
                    Log.d("COORDINATE", String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
                }
                return true;
            }
        });
    }
}
