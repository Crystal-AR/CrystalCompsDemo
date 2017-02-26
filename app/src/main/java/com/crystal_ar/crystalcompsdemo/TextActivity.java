package com.crystal_ar.crystalcompsdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    float origBitmapWidth;
    float origBitmapHeight;
    float scaledBitmapWidth;
    float scaledBitmapHeight;
    float origBitmapAspectRatio;
    float scaleFactor;

    private ImageView imageView;
    private CrystalAR crystalAR;
    private CheckBox emailCheckBox;
    private CheckBox urlCheckBox;
    private CheckBox phoneNumbersCheckBox;
    private CheckBox replaceImageCheckBox;

    private List<Word> urls;
    private List<Word> phoneNumbers;
    private List<Word> emails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        photo = BitmapFactory.decodeResource(getResources(), R.drawable.test_url, opt);
        origBitmapHeight = photo.getHeight();
        origBitmapWidth = photo.getWidth();

        imageView = (ImageView) this.findViewById(R.id.textImageView);
        imageView.setImageBitmap(photo);



//        imageView.getLayoutParams().width = Math.round(scaledBitmapWidth);
//        imageView.setMaxHeight(Math.round(scaledBitmapHeight));
//            imageView.setMaxHeight(Math.round(scaledBitmapHeight));





//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);


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
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (emailChecked == false) {
                    emailChecked = true;
                } else {
                    emailChecked = false;
                }
            }
        });

        phoneNumbersCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (phoneChecked == false) {
                    phoneChecked = true;
                } else {
                    phoneChecked = false;
                }
            }
        });

        replaceImageCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (replaceImageChecked == false) {
                    replaceImageChecked = true;
                } else {
                    replaceImageChecked = false;
                }
            }
        });

        urlCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (urlChecked == false) {
                    urlChecked = true;
                } else {
                    urlChecked = false;
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
            origBitmapAspectRatio = origBitmapHeight/origBitmapWidth;
            Log.d("up here", String.valueOf(origBitmapAspectRatio));



            scaledBitmapWidth= imageView.getWidth();
            scaledBitmapHeight = imageView.getHeight();
            Log.d("Orig", String.valueOf(origBitmapHeight));

            Log.d("Scaled", String.valueOf(scaledBitmapHeight));

            scaleFactor = origBitmapHeight / scaledBitmapHeight;

            Log.d("IMPORTANT RATIO", String.valueOf(origBitmapAspectRatio));
            Log.d("IMPORTANT WIDTH", String.valueOf(imageView.getWidth()));

            Log.d("IMPORTANT HEIGHT", String.valueOf(origBitmapAspectRatio*imageView.getWidth()));
            Log.d("Shitty HEIGHT", String.valueOf(scaledBitmapHeight));



            crystalAR.processImage(photo);

            tempPhoto = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());
            if (urlChecked == true) {
                urls = crystalAR.getURLs();
                createURLRect(urls);
            }

            if (phoneChecked == true) {
                phoneNumbers = crystalAR.getPhoneNumbers();
                createPhoneNosRect(phoneNumbers);
            }

            if (emailChecked == true) {
                emails = crystalAR.getEmails();
                createEmailsRect(emails);
            }

        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            tempPhoto = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());
            if (urlChecked == true)
                createURLRect(crystalAR.getURLs());
            if (phoneChecked == true)
                createPhoneNosRect(crystalAR.getPhoneNumbers());
            if (emailChecked == true)
                createEmailsRect(crystalAR.getEmails());

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        float xRelativeDiff = imageView.getLeft();
        float yRelativeDiff = imageView.getTop();
//        float ximageViewDiff = (imageView.getHeight() - photo.getHeight()) /2;
//        float yimageViewDiff =(imageView.getWidth() - photo.getWidth()) /2;
//        x = x +xRelativeDiff ;
//        y = y +yRelativeDiff;
//        y = y - 100;
        imageView.setBackgroundColor(Color.GREEN);
        Log.d("imageView Height", String.valueOf(imageView.getHeight()));
        Log.d("scaled Height", String.valueOf(scaledBitmapHeight));
        Log.d("Imageviewtop", Float.toString(imageView.getTop()));
        Log.d("Imageviewleft", Float.toString(imageView.getLeft()));
        Log.d("imageviewbottom", Float.toString(imageView.getBottom()));
        Log.d("imageviewright", Float.toString(imageView.getRight()));
//        Log.d("screenTop", );
//        Log.d("screenleft", Float.toString(imageView.getRight()));



        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float top = imageView.getTop();

                if(emails != null) {
                    for (int i = 0; i < emails.size(); i++) {
                        float leftUrlX = emails.get(i).x/scaleFactor;
                        float rightUrlX = emails.get(i).x /scaleFactor + emails.get(i).width/scaleFactor;
                        float topUrlY = emails.get(i).y/scaleFactor+top;
                        float bottomUrlY = emails.get(i).y/scaleFactor + emails.get(i).height+top;


                        //Check if the x and y position of the touch is inside the bitmap
                        if ((x  > leftUrlX) && (x  < rightUrlX) && (y < bottomUrlY) && (y  > topUrlY)) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                            intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                            intent.setData(Uri.parse("mailto:" + Uri.parse(emails.get(i).str)));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                }

                if(urls!= null) {
                    for (int i = 0; i < urls.size(); i++) {
                        Log.d("Scale factor", String.valueOf(scaleFactor));
                        float leftUrlX = urls.get(i).x/scaleFactor;
                        float rightUrlX = urls.get(i).x /scaleFactor + urls.get(i).width/scaleFactor;
                        float topUrlY = urls.get(i).y/scaleFactor+top;
                        float bottomUrlY = urls.get(i).y/scaleFactor + urls.get(i).height+top;
//
//
//                        Log.d("left url X", Float.toString(leftUrlX));
//                        Log.d("Touch x", Float.toString(x));
//                        Log.d("        right url X", Float.toString(rightUrlX));
//                        Log.d("top url Y", Float.toString(topUrlY));
//                        Log.d("Touch y", Float.toString(y));
//                        Log.d("bottom url Y", Float.toString(bottomUrlY));
//                        Log.d(" url ", urls.get(i).str);


                        //Check if the x and y position of the touch is inside the bitmap
                        if ((x  > leftUrlX) && (x  < rightUrlX) && (y < bottomUrlY) && (y  > topUrlY)) {
                            Log.d(" url ", urls.get(i).str);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls.get(i).str));
                            startActivity(browserIntent);
                        }

                    }
                }
                if(phoneNumbers != null) {
                    for (int i = 0; i < phoneNumbers.size(); i++) {
                        float leftUrlX = phoneNumbers.get(i).x/scaleFactor;
                        float rightUrlX = phoneNumbers.get(i).x /scaleFactor + phoneNumbers.get(i).width/scaleFactor;
                        float topUrlY = phoneNumbers.get(i).y/scaleFactor+top;
                        float bottomUrlY = phoneNumbers.get(i).y/scaleFactor + phoneNumbers.get(i).height+top;

                        //Check if the x and y position of the touch is inside the bitmap
                        if ((x  > leftUrlX) && (x  < rightUrlX) && (y < bottomUrlY) && (y  > topUrlY)) {
                            Log.d("awesome", "phone");

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNumbers.get(i).str));
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                //return TODO;
                            }
                            startActivity(intent);
                        }

                    }
                }


                return true;
        }

/*
        array = getEmails
        loop through all rectangles
        if touch event is within/ close rectangle
        Make urk intent using corresponding word
        */


//        float x = event.getX();
//        float y = event.getY();
//        switch(event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                //Check if the x and y position of the touch is inside the bitmap
//                if( x > bitmapXPosition && x < bitmapXPosition + bitmapWidth && y > bitmapYPosition && y < bitmapYPosition + bitmapHeight )
//                {
//                    //Bitmap touched
//                }
//                return true;
//        }
        return false;
    }

    protected void createURLRect(List<Word> words) {
        urls = words;
        Canvas c = new Canvas(tempPhoto);
        //Draw the image bitmap into the cavas
        c.drawBitmap(tempPhoto, 0, 0, null);
        Paint p = new Paint();
        p.setARGB(255, 0, 0, 255);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);

        for (Word url : urls) {
            c.drawRect(new Rect(url.x - 5, url.y - 5, url.x + url.width + 5, url.y + url.height), p);
        }
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });
    }

    protected void createEmailsRect(List<Word> emails) {
        Bitmap mutableBitmap = tempPhoto.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Canvas c = new Canvas(tempPhoto);
        //Draw the image bitmap into the cavas
        c.drawBitmap(tempPhoto, 0, 0, null);
        Paint p = new Paint();
        p.setARGB(255, 255, 0, 0);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);

        for (Word email : emails) {
            c.drawRect(new Rect(email.x - 5, email.y - 5, email.x + email.width + 5, email.y + email.height), p);
        }
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });
    }

    protected void createPhoneNosRect(List<Word> phoneNos) {
        Canvas c = new Canvas(tempPhoto);
        //Draw the image bitmap into the cavas
        c.drawBitmap(tempPhoto, 0, 0, null);
        Paint p = new Paint();
        p.setARGB(255, 0, 255, 0);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);

        for (Word no : phoneNos) {
            c.drawRect(new Rect(no.x - 5, no.y - 5, no.x + no.width + 5, no.y + no.height), p);
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

