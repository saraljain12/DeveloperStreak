package com.dotquestionmark.developerstreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private Button mupdatebtn;
    private CircleImageView mimageview;
    private TextView mnameview;

    public DatabaseReference muserdatabase;
    private FirebaseUser mcurrent_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mupdatebtn = findViewById(R.id.updateprofile_btn);
        mimageview = findViewById(R.id.myprofile_imageview);
        mnameview = findViewById(R.id.myprofile_displayname);

        mcurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mcurrent_user.getUid();
        muserdatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);
        muserdatabase.keepSynced(true);




        muserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("name").getValue().toString();
                mnameview.setText(name);
                final String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                if(!image.equals("default")&& image !=null && !image.isEmpty()) {
                    Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.avatar).into(mimageview, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.avatar).into(mimageview);
                        }


                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateintent = new Intent(MyProfileActivity.this,UpdateProfileActivity.class);
                startActivity(updateintent);
            }
        });



    }
}
