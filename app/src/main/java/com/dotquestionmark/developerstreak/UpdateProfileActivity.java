package com.dotquestionmark.developerstreak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
public class UpdateProfileActivity extends AppCompatActivity {

    public DatabaseReference muserdatabase;
    private FirebaseUser mcurrent_user;


    //android layout
    private CircleImageView mdisplayimage;
    private TextInputEditText mtextinputlayout;
    private Button mimgbtn,msavebtn;

    //storage firebase
    private StorageReference mimagestorage;
    private StorageReference mstoragereferance;
    private ProgressDialog mprogdialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateprofile);

        mdisplayimage = (CircleImageView)findViewById(R.id.profile_imageview);
        mtextinputlayout= findViewById(R.id.updateprofile_displayname);
        mimgbtn = (Button) findViewById(R.id.updateprofile_imagebtn);
        msavebtn = findViewById(R.id.savebtn);




        mcurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mcurrent_user.getUid();
        muserdatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);
        muserdatabase.keepSynced(true);


        mimagestorage = FirebaseStorage.getInstance().getReference();


        muserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mtextinputlayout.setText(name);


                if(!image.equals("default")) {

                    Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.avatar).into(mdisplayimage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.avatar).into(mdisplayimage);

                        }


                    });
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        msavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newname =  mtextinputlayout.getText().toString();
                muserdatabase.child("name").setValue(newname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent profileintent = new Intent(UpdateProfileActivity.this,MyProfileActivity.class);
                            startActivity(profileintent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"You got some error in saving profile",Toast.LENGTH_LONG).show();
                        }


                    }
                });

            }
        });


        mimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(500,500)
                        .start(UpdateProfileActivity.this);

            }
        });



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mprogdialog = new ProgressDialog(UpdateProfileActivity.this);
                mprogdialog.setTitle("Uploading Image");
                mprogdialog.setMessage("please wait while we process and upload the image...");
                mprogdialog.setCanceledOnTouchOutside(false);
                mprogdialog.show();


                Uri resultUri = result.getUri();

                File Thumb_path = new File(resultUri.getPath());

                String current_user_id = mcurrent_user.getUid();

                Bitmap Thumb_bitmap = null;
                try {


                    Thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(100)
                            .compressToBitmap(Thumb_path);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath = mimagestorage.child("profile_images").child(current_user_id + ".jpg");

                final StorageReference thumb_filepath = mimagestorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");

                mstoragereferance = FirebaseStorage.getInstance().getReference();
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String download_url = uri.toString();
                                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                                        thumb_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String thumb_download_url = uri.toString();
                                                Map updateHashmap = new HashMap();
                                                updateHashmap.put("image", download_url);
                                                updateHashmap.put("thumb_image", thumb_download_url);


                                                muserdatabase.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mprogdialog.dismiss();
                                                            Toast.makeText(getApplicationContext(), "profile uploaded successfully", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "error in uploading thumbnail", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                });
                                            }

                                        });
                                    }
                                });
                            }
                        });
                    }


                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }}

