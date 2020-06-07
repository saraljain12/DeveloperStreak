package com.dotquestionmark.developerstreak;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ThirdFragment extends Fragment {

    private Button mprofilebtn;

    public static ThirdFragment newInstance(){
        ThirdFragment fragment=new ThirdFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view =inflater.inflate(R.layout.fragment_third, container,  false);

        mprofilebtn = view.findViewById(R.id.myprofilebtn);

        mprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent profileintent = new Intent(getActivity(),MyProfileActivity.class);
                startActivity(profileintent);
            }
        });



        return view;
    }
}
