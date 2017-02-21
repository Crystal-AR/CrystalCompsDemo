package com.crystal_ar.crystalcompsdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.crystal_ar.crystal_ar.CrystalAR;
import com.crystal_ar.crystal_ar.Word;

import java.util.List;

import static android.graphics.Color.BLUE;

public class TextActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMAGE = 1;

    Bitmap tempPhoto;
    Bitmap photo;

    Boolean urlChecked = false;
    Boolean phoneChecked = false;
    Boolean replaceImageChecked = false;
    Boolean emailChecked = false;


    private ImageView imageView;
    private CrystalAR crystalAR;
    private CheckBox emailCheckBox;
    private CheckBox urlCheckBox;
    private CheckBox phoneNumbersCheckBox;
    private CheckBox replaceImageCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        photo = BitmapFactory.decodeResource(getResources(), R.drawable.replace, opt);


        this.imageView = (ImageView)this.findViewById(R.id.textImageView);
        emailCheckBox = (CheckBox) findViewById(R.id.emailCheckBox);
        urlCheckBox = (CheckBox) findViewById(R.id.urlCheckBox);
        phoneNumbersCheckBox = (CheckBox) findViewById(R.id.phoneNumberCheckBox);
        replaceImageCheckBox = (CheckBox) findViewById(R.id.replaceImageCheckBox);
        Context context = getApplicationContext();
        crystalAR = new CrystalAR(context);
        crystalAR.setLanguage("eng");

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

        emailCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(emailChecked == false) {
                    emailChecked = true;
                }
                else{
                    emailChecked = false;
                }
            }
        });

        phoneNumbersCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(phoneChecked == false) {
                    phoneChecked = true;
                }
                else{
                    phoneChecked = false;
                }
            }
        });

        replaceImageCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(replaceImageChecked == false) {
                    replaceImageChecked = true;
                }
                else{
                    replaceImageChecked = false;
                }
            }
        });

        urlCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(urlChecked == false) {
                    urlChecked = true;
                }
                else{
                    urlChecked = false;
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            crystalAR.processImage(photo);

            tempPhoto = Bitmap.createBitmap(photo, 0,0,photo.getWidth(), photo.getHeight());
            if(urlChecked == true)
                createURLRect(crystalAR.getURLs());
            if(phoneChecked == true)
                createPhoneNosRect(crystalAR.getPhoneNumbers());
            if(emailChecked == true)
                createEmailsRect(crystalAR.getEmails());
            if(replaceImageChecked == true)
                replaceWithImg();

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

    protected void createURLRect(List<Word> urls)
    {
        Canvas c = new Canvas(tempPhoto);
        //Draw the image bitmap into the cavas
        c.drawBitmap(tempPhoto, 0, 0, null);
        Paint p=new Paint();
        p.setARGB(255,0,0,255);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth (3);

        for(Word url : urls) {
            c.drawRect(new Rect(url.x-5, url.y-5, url.x + url.width + 5, url.y + url.height), p);
        }
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });
    }

    protected void createEmailsRect(List<Word> emails)
    {
        Bitmap mutableBitmap = tempPhoto.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Canvas c = new Canvas(tempPhoto);
        //Draw the image bitmap into the cavas
        c.drawBitmap(tempPhoto, 0, 0, null);
        Paint p=new Paint();
        p.setARGB(255,255,0,0);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth (3);

        for(Word email : emails) {
            c.drawRect(new Rect(email.x-5, email.y-5, email.x + email.width + 5, email.y + email.height), p);
        }
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });
    }

    protected void createPhoneNosRect(List<Word> phoneNos)
    {
        Canvas c = new Canvas(tempPhoto);
        //Draw the image bitmap into the cavas
        c.drawBitmap(tempPhoto, 0, 0, null);
        Paint p=new Paint();
        p.setARGB(255,0,255,0);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth (3);

        for(Word no : phoneNos) {
            c.drawRect(new Rect(no.x-5, no.y-5, no.x + no.width + 5, no.y + no.height), p);
        }
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });
    }

    protected void replaceWithImg()
    {
        String[] replace= {"Email", "Contact"};

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.email);//assign your bitmap;
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.phone);//assign your bitmap;
        Bitmap[] arrayOfBitmap = {bitmap1, bitmap2};
        tempPhoto = crystalAR.replaceWithImage(tempPhoto, replace, arrayOfBitmap);
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });

    }
}

