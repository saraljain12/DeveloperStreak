package com.dotquestionmark.developerstreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImagePreviewActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button addToStoryButton;
    private String userid;
    private Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);


        addToStoryButton=(Button) findViewById(R.id.addtostoryid);
        userid= FirebaseAuth.getInstance().getUid();



//        Bundle extras=getIntent().getExtras();
//        byte[] imageByte=extras.getByteArray("abcd");
//
        byte[] imageByte=CameraFragment.a;

        if(imageByte!=null)
        {
            mImageView=(ImageView) findViewById(R.id.imageView);
            Bitmap bitmap= BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

            rotateBitmap= rotate(bitmap);
            mImageView.setImageBitmap(rotateBitmap);

        }

        addToStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToStory();
            }
        });
    }

    private void saveToStory() {


        final DatabaseReference storyDB= FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("user_story");

        final String key=storyDB.push().getKey();

        StorageReference imagePath= FirebaseStorage.getInstance().getReference().child("imagecapture").child(key);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] imageUploadByte=byteArrayOutputStream.toByteArray();

        UploadTask uploadTask=imagePath.putBytes(imageUploadByte);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                String imageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                Task<Uri> a =taskSnapshot.getMetadata().getReference().getDownloadUrl();

                Log.d("url111", a.toString());

                Long currentTimeStamp=System.currentTimeMillis();

                Long endTimeStamp=System.currentTimeMillis()+(24*60*60*1000);

                Map<String, Object> uploadMap=new HashMap<>();
                uploadMap.put("imageURL", imageUrl);
                uploadMap.put("storyStartTime", currentTimeStamp);
                uploadMap.put("storyEndTime", endTimeStamp);

                storyDB.child(key).setValue(uploadMap);

                finish();
                return;
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                return;
            }
        });


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
