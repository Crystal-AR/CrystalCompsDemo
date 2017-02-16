package com.crystal_ar.crystalcompsdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crystal_ar.crystal_ar.CrystalAR;

public class TextActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMAGE = 1;


    private ImageView imageView;
    private CrystalAR crystalAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        this.imageView = (ImageView)this.findViewById(R.id.textImageView);
        Button takePhotoButton = (Button) this.findViewById(R.id.BtnTakePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        Button loadPhotoButton = (Button) this.findViewById(R.id.BtnLoadPhoto);
        loadPhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Context context = getApplicationContext();
            crystalAR = new CrystalAR(context);
            crystalAR.setLanguage("eng");
            crystalAR.processImage(photo);
            crystalAR.getPhoneNumbers();
            Log.d(crystalAR.getPhoneNumbers().get(0), "getPhoneNumbers: ");

            imageView.setImageBitmap(photo);
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}
