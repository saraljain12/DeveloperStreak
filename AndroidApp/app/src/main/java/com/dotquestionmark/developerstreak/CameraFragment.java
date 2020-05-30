package com.dotquestionmark.developerstreak;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback{

    Camera mCamera;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    Camera.PictureCallback mPictureCallback;

    private Button captureButton;

    final int CAMERA_CODE=1;
    byte[] a;

    public static CameraFragment newInstance(){
        CameraFragment fragment=new CameraFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_camera, container,  false);

        mSurfaceView=view.findViewById(R.id.surfaceView);
        mSurfaceHolder=mSurfaceView.getHolder();



        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
               ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_CODE);
        }

        else {
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        captureButton=(Button) view.findViewById(R.id.capturebuttonid);



        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageCapture();
            }
        });

        mPictureCallback=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                ;

//                if(data!=null)
//                {
//
//                    Bitmap bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
//                    mImageView.setImageBitmap(bitmap);
//
//                }

                 a=data;

//                Intent intent=new Intent(getActivity(), ImagePreviewActivity.class);
//                intent.putExtra("abcd", a);
//                startActivity(intent);


                return;
            }
        };



        return view;
    }

    void imageCapture()
    {
        mCamera.takePicture(null, null, mPictureCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            mCamera= Camera.open();
        Camera.Parameters parameters;
        parameters=mCamera.getParameters();
        mCamera.setDisplayOrientation(90);

        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case CAMERA_CODE:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }

                else {
                    Toast.makeText(getContext(), "Accept the permission", Toast.LENGTH_SHORT).show();
                }
                break;


            }
        }

    }
}
