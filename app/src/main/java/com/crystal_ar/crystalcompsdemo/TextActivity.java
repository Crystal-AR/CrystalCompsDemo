package com.crystal_ar.crystalcompsdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    Boolean imageSet = false;
    Boolean imageTapped = false;
    Boolean firstLoad = true;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);


        imageView = (ImageView) this.findViewById(R.id.textImageView);
        emailCheckBox = (CheckBox) findViewById(R.id.emailCheckBox);
        urlCheckBox = (CheckBox) findViewById(R.id.urlCheckBox);
        phoneNumbersCheckBox = (CheckBox) findViewById(R.id.phoneNumberCheckBox);
        replaceImageCheckBox = (CheckBox) findViewById(R.id.replaceImageCheckBox);
        Context context = getApplicationContext();
        crystalAR = new CrystalAR(context);
        crystalAR.setLanguage("eng");

        // TODO: Add camera feature
//        Button takePhotoButton = (Button) this.findViewById(R.id.BtnTakePhoto);
//        takePhotoButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
//        });

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

          // TODO: Handle Camera

//            BitmapFactory.Options opt = new BitmapFactory.Options();
//            opt.inMutable = true;
//
//            photo = (Bitmap) data.getExtras().get("data");
//
//            Bitmap copiedPhoto = photo.copy(Bitmap.Config.ARGB_8888, true);
//
//            origBitmapHeight = photo.getHeight();
//            origBitmapWidth = photo.getWidth();
//
//            origBitmapAspectRatio = origBitmapHeight/origBitmapWidth;
//
//            Log.d("Width", String.valueOf(imageView.getWidth()));
//            Log.d("Height", String.valueOf(imageView.getHeight()));
//
//            scaledBitmapWidth= imageView.getWidth();
//            scaledBitmapHeight = imageView.getHeight();
//            Log.d("imageview height", String.valueOf(imageView.getHeight()));
//            scaleFactor = origBitmapHeight / scaledBitmapHeight;
//
//            imageView.setImageBitmap(copiedPhoto);
//            crystalAR.processImage(copiedPhoto);
//
//            Word[] w = crystalAR.getWords();
//            tempPhoto = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());



        }

         else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


            if (firstLoad == true) {
                AlertDialog alertDialog = new AlertDialog.Builder(TextActivity.this).create();
                alertDialog.setTitle("Please Refresh Image");
                alertDialog.setMessage("Please load a new photo");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                firstLoad = false;
            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inMutable = true;

            photo = BitmapFactory.decodeFile(picturePath, opt);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            imageView.invalidate();

            origBitmapHeight = photo.getHeight();
            origBitmapWidth = photo.getWidth();

            origBitmapAspectRatio = origBitmapHeight / origBitmapWidth;

            scaledBitmapWidth = imageView.getWidth();
            scaledBitmapHeight = imageView.getHeight();
            scaleFactor = origBitmapHeight / scaledBitmapHeight;

            imageView.setImageBitmap(photo);
            crystalAR.processImage(photo);

            Word[] w = crystalAR.getWords();

            resetCheckboxes();

            tempPhoto = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());


            imageTapped = false;
            imageSet = true;

            if (firstLoad){
                reloadImage();
            }
        }
    }

    public void reloadImage(){
        imageView.invalidate();
    }

    public void resetCheckboxes(){
        emailCheckBox.setChecked(false);
        emailChecked = false;
        phoneNumbersCheckBox.setChecked(false);
        phoneChecked = false;
        urlCheckBox.setChecked(false);
        urlChecked = false;
    }

    public void onCheckboxClicked(View view) {
        if(crystalAR.getWords()!=null) {
            tempPhoto = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());
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
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(tempPhoto);
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (imageTapped) {
            AlertDialog alertDialog = new AlertDialog.Builder(TextActivity.this).create();
            alertDialog.setTitle("Please Refresh Image");
            alertDialog.setMessage("Please load a new photo");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        if (imageSet){
            float x = event.getX();
            float y = event.getY();

            imageView.setBackgroundColor(Color.GREEN);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float top = imageView.getTop();
                    float left = imageView.getLeft();

                    if(emails != null) {
                        for (int i = 0; i < emails.size(); i++) {
                            float leftUrlX = emails.get(i).x/scaleFactor;
                            float rightUrlX = emails.get(i).x/scaleFactor + emails.get(i).width/scaleFactor;
                            float topUrlY = emails.get(i).y/scaleFactor+top+top;
                            float bottomUrlY = emails.get(i).y/scaleFactor + emails.get(i).height+top+top;


                            //Check if the x and y position of the touch is inside the bitmap
                            if ((x  > leftUrlX) && (x  < rightUrlX) && (y < bottomUrlY) && (y  > topUrlY)) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                                intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                                intent.setData(Uri.parse("mailto:" + Uri.parse(emails.get(i).str)));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                imageTapped = true;
                                startActivity(intent);
                            }

                        }
                    }
                    if(urls!= null) {
                        for (int i = 0; i < urls.size(); i++) {
                            float leftUrlX = urls.get(i).x/scaleFactor+left;
                            float rightUrlX = urls.get(i).x/scaleFactor + urls.get(i).width/scaleFactor+left;
                            float topUrlY = urls.get(i).y/scaleFactor+top+92;
                            float bottomUrlY = urls.get(i).y/scaleFactor + urls.get(i).height+top+92;


                            if(!urls.get(i).str.substring(0,3).equals("ht")) {
                                Rect r = new Rect(urls.get(i).x, urls.get(i).y, urls.get(i).width, urls.get(i).height);
                                String str = "http://" + urls.get(i).str;
                                Word w = new Word(str, r);
                                urls.set(i, w);
                            }

                            //Check if the x and y position of the touch is inside the bitmap
                            if ((x  > leftUrlX) && (x  < rightUrlX) && (y < bottomUrlY) && (y  > topUrlY)) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls.get(i).str));
                                imageTapped = true;
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
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneNumbers.get(i).str));
                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    //TODO[@abha, @josh, @simon] -- Ask the user for permission here.
                                }
                                imageTapped = true;
                                startActivity(intent);
                            }

                        }
                    }


                    return true;
            }
        }

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
