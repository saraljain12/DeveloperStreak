package com.dotquestionmark.developerstreak;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagePreviewActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);



//        Bundle extras=getIntent().getExtras();
//        byte[] imageByte=extras.getByteArray("abcd");
//
//        if(imageByte!=null)
//        {
//            mImageView=(ImageView) findViewById(R.id.imageView);
//            Bitmap bitmap= BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
//            mImageView.setImageBitmap(bitmap);
//
//        }
    }
}
