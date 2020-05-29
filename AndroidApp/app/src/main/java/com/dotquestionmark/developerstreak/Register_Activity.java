package com.dotquestionmark.developerstreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Register_Activity extends AppCompatActivity {


    private EditText mEmail;
    private EditText mPass;
    private EditText mName;

    private Button mLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mFireBaseAuthSateListener;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mFireBaseAuthSateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mFireBaseAuthSateListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);


        mAuth=FirebaseAuth.getInstance();

        mName=(EditText) findViewById(R.id.nameid);
        mEmail=(EditText) findViewById(R.id.emailid);
        mPass=(EditText) findViewById(R.id.passid);
        mLogin=(Button) findViewById(R.id.registerid);


        mFireBaseAuthSateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                if(user!=null)
                {
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=mName.getText().toString();
                final String email=mEmail.getText().toString();
                final String pas=mPass.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, pas).addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Error in LOGIN", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String userId=mAuth.getCurrentUser().getUid();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                            Map userData=new HashMap<>();
                            userData.put("email", email);
                            userData.put("name", name);
                            userData.put("image", "default");

                            reference.updateChildren(userData);
                        }
                    }
                });



            }
        });


    }
}
