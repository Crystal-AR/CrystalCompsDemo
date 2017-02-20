package com.simonorlovsky.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crystal_ar.crystal_ar.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainLibrary lib = new mainLibrary();
        lib.hello();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
