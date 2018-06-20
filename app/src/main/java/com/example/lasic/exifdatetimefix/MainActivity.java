package com.example.lasic.exifdatetimefix;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PICK_PHOTO = 10001;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.display_image);

        Button btnSelectPicture = (Button) findViewById(R.id.btn_select_picture);
        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show();
                return;
            }

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }).start();

            initDateTimeFix(data);
        }
    }
    private void initDateTimeFix(Intent data){
        //number of images
        int amount = data.getClipData().getItemCount();
//        Log.d(TAG, "initDateTimeFix: " + amount);

        Uri imgUri;
        File file;

//        Log.d(TAG, "onActivityResult: URI:" + imgUri + " " + imgUri.getPath());
//        Log.d(TAG, "onActivityResult: PATH: " + file.getAbsolutePath());
//        Log.d(TAG, "onActivityResult: FILENAME: " + file.getName());

        for(int curr=0; curr<amount; curr++){
            imgUri = data.getClipData().getItemAt(curr).getUri();
            file = new File(imgUri.getPath());
            setDateTime(file);
        }
    }
    private void setDateTime(File file){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "DateTime: \n"
//                + exif.getAttribute("DateTimeOriginal") + "\n"
//                + exif.getAttribute("DateTime") + "\n");
        String dateTimeOriginal = exif.getAttribute("DateTimeOriginal");
        String dateTime = exif.getAttribute("DateTime");

        if (dateTime != null && dateTimeOriginal != null)
            return;

        String trueDateTime = DateParser.parse(file.getName());
        if (trueDateTime == null)
            return;

        exif.setAttribute("DateTimeOriginal", trueDateTime);
        exif.setAttribute("DateTime", trueDateTime);
        try {
            exif.saveAttributes();
//            Toast.makeText(this, "DateTime fix Successful", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(this, "DateTime fix FAILED", Toast.LENGTH_SHORT).show();
        }
    }
}
