package com.dotquestionmark.developerstreak;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
        byte[] imageByte=CameraFragment.a;

        if(imageByte!=null)
        {
            mImageView=(ImageView) findViewById(R.id.imageView);
            Bitmap bitmap= BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

            Bitmap rotateBitmap= rotate(bitmap);
            mImageView.setImageBitmap(rotateBitmap);

        }
    }

    private Bitmap rotate(Bitmap bitmap)
    {
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();

        Matrix matrix=new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0 , width, height, matrix, true);
    }
}
