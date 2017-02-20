package com.crystal_ar.crystalcompsdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnTextActivity;
    private Button btnCornerActivity;
    private Button btnModelActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTextActivity = (Button) findViewById(R.id.BtnTextActivity);
        btnCornerActivity = (Button) findViewById(R.id.BtnCornerActivity);
        btnModelActivity = (Button) findViewById(R.id.BtnModelActivity);

        btnTextActivity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextActivity.class);
                MainActivity.this.startActivity(intent);
            }


        });

        btnCornerActivity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CornerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        btnModelActivity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ModelActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
