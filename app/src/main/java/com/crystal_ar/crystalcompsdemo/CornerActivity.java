package com.crystal_ar.crystalcompsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

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
    }
}
