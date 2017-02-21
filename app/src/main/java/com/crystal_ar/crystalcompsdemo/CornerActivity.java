package com.crystal_ar.crystalcompsdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crystal_ar.crystal_ar.CrystalAR;
import com.crystal_ar.crystal_ar.IntPair;

import java.util.TreeSet;

/**
 * Created by Frederik on 2/19/17.
 */

public class CornerActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMAGE = 1;

    private CrystalAR crystalAR;
    private ImageView imageView;
    private Bitmap photo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corner);

        crystalAR = new CrystalAR(getApplicationContext());
        imageView = (ImageView) this.findViewById(R.id.cornerImageView);

        Button takePhotoButton = (Button) this.findViewById(R.id.BtnTakePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        Button loadPhotoButton = (Button) this.findViewById(R.id.BtnLoadPhoto);
        loadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                photo = BitmapFactory.decodeFile(picturePath);
            }

            initiateCornerHandler(imageView);
        }
    }

    public void initiateCornerHandler(ImageView imageView) {
        Thread thread = new Thread(crystalAR.findCornersRunnable(new cornerHandler(imageView), photo));
        thread.setDaemon(true);
        thread.start();
    }

    private class cornerHandler extends Handler {
        ImageView imageView;

        public cornerHandler(ImageView imageView) {
            this.imageView = imageView;
        }
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case CrystalAR.CORNERS_FOUND:
                    // Make bitmap mutable.
                    Bitmap workingBitmap = Bitmap.createBitmap(photo);
                    Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

                    // Draw the image bitmap into the canvas.
                    Canvas c = new Canvas(mutableBitmap);
                    c.drawBitmap(mutableBitmap, 0, 0, null);
                    Paint p = new Paint();
                    p.setARGB(255,0,0,255);
                    p.setStyle(Paint.Style.STROKE);
                    p.setStrokeWidth(3);

                    // Draw a blue dot at each corner.
                    IntPair[] corners = (IntPair[]) message.obj;
                    for (IntPair coordinate : corners) {
                        c.drawPoint(coordinate.x, coordinate.y, p);
                    }

                    imageView.setImageBitmap(mutableBitmap);
                    break;
            }
        }
    }
}
