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
import android.graphics.Matrix;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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

    private List<Word> urls;
    private List<Word> phoneNumbers;
    private List<Word> emails;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        photo = BitmapFactory.decodeResource(getResources(), R.drawable.everything, opt);

        this.imageView = (ImageView) this.findViewById(R.id.textImageView);
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
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        Button loadPhotoButton = (Button) this.findViewById(R.id.BtnLoadPhoto);
        loadPhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // Rotate image.
            //photo = (Bitmap) data.getExtras().get("data");
            //Matrix matrix = new Matrix();
            //matrix.postRotate(90);
            //photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);

            imageView.setImageBitmap(photo);

            crystalAR.processImage(photo);

            Word[] w = crystalAR.getWords();
            for (Word i : w) {
                Log.d("Word: ", i.str);
            }
            tempPhoto = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());
        }

         else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
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
        }
    }

    public void onCheckboxClicked(View view) {
        if(crystalAR.getWords()!=null) {
            if (replaceImageCheckBox.isChecked())
                replaceWithImg();
            if (emailCheckBox.isChecked()) {
                emails = crystalAR.getEmails();
                createEmailsRect(emails);
            }

            if (phoneNumbersCheckBox.isChecked()) {
                phoneNumbers = crystalAR.getPhoneNumbers();
                createPhoneNosRect(phoneNumbers);
            }

            if (urlCheckBox.isChecked()) {
                urls = crystalAR.getURLs();
                createURLRect(urls);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        y = y - 100;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (emails != null) {
                    for (int j = 0; j < emails.size(); j++) {
//                        Log.d("left email X", Integer.toString(emails.get(j).x));
//                        Log.d("Touch x", Float.toString(x));
//                        Log.d("        right email X", Integer.toString(emails.get(j).x + emails.get(j).width));
//                        Log.d("top email Y", Integer.toString(emails.get(j).y));
//                        Log.d("Touch y", Float.toString(y));
//                        Log.d("bottom email Y", Integer.toString(emails.get(j).y + emails.get(j).height));

                        if ((x + 170 > emails.get(j).x && x + 170 < emails.get(j).x + emails.get(j).width) && (y + 140 > emails.get(j).y && y + 140 < emails.get(j).y + emails.get(j).height)) {
                            Log.d("awesome", "pants");
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                            intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                            intent.setData(Uri.parse("mailto:" + Uri.parse(emails.get(j).str)));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                }
                if (urls != null) {
                    for (int i = 0; i < urls.size(); i++) {
                        int xOffset = 170;
                        int yOffset = 130 - 120;
                        Log.d("left url X", Integer.toString(urls.get(i).x));
                        Log.d("Touch x", Float.toString(x + xOffset));
                        Log.d("        right url X", Integer.toString(urls.get(i).x + urls.get(i).width));
                        Log.d("top url Y", Integer.toString(urls.get(i).y));
                        Log.d("Touch y", Float.toString(y + yOffset));
                        Log.d("bottom url Y", Integer.toString(urls.get(i).y + urls.get(i).height));

                        //Check if the x and y position of the touch is inside the bitmap
                        if (x + xOffset > urls.get(i).x && x + xOffset < urls.get(i).x + urls.get(i).width && y + yOffset > urls.get(i).y && y + yOffset < urls.get(i).y + urls.get(i).height) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls.get(i).str));
                            startActivity(browserIntent);
                        }

                    }
                }
                if (phoneNumbers != null) {
                    for (int k = 0; k < phoneNumbers.size(); k++) {
                        int xOffset = 50 + 220;
                        int yOffset = 150 - 80;
//                        Log.d("left phone X", Integer.toString(phoneNumbers.get(k).x));
//                        Log.d("Touch x", Float.toString(x + xOffset));
//                        Log.d("        right phone X", Integer.toString(phoneNumbers.get(k).x + phoneNumbers.get(k).width));
//                        Log.d("top phone Y", Integer.toString(phoneNumbers.get(k).y));
//                        Log.d("Touch y", Float.toString(y + yOffset));
//                        Log.d("bottom phone Y", Integer.toString(phoneNumbers.get(k).y + phoneNumbers.get(k).height));


                        if (x + xOffset > phoneNumbers.get(k).x && x + xOffset < phoneNumbers.get(k).x + phoneNumbers.get(k).width && y + yOffset > phoneNumbers.get(k).y && y + yOffset < phoneNumbers.get(k).y + phoneNumbers.get(k).height) {

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNumbers.get(k).str));
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
            c.drawRect(new Rect(no.x, no.y - 5, no.x + no.width + 5, no.y + no.height), p);
        }
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });
    }

    protected void replaceWithImg() {
        String[] replace = {"Email", "Contact"};

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.email);//assign your bitmap;
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.phone);//assign your bitmap;
        Bitmap[] arrayOfBitmap = {bitmap1, bitmap2};
        tempPhoto = crystalAR.replaceWithImage(tempPhoto, replace, arrayOfBitmap);
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Text Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
