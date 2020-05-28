package com.cmpestudents.seslikitap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class Pop extends Activity {
    Button camera,gallery;
    ImageView image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);
        camera = (Button)findViewById(R.id.camera);
        gallery = (Button) findViewById(R.id.gallery);
        image = (ImageView)findViewById(R.id.image);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Intent intent = getIntent();
        if(intent.hasExtra("bytearray")){
            camera.setVisibility(View.INVISIBLE);
            gallery.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    intent.getByteArrayExtra("bytearray"),0,intent.getByteArrayExtra("bytearray").length);
            image.setImageBitmap(bitmap);
            getWindow().setLayout((int) (width ), (int) (height * 0.6));
            //getWindow().setLayout(image.getWidth(), image.getHeight());
        }else {
            getWindow().setLayout((int) (width * 0.6), (int) (height * 0.2));
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Adminpanelduzenle.cameragallery = 1;
                    finish();
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Adminpanelduzenle.cameragallery = 2;
                    finish();
                }
            });
        }
    }
}
