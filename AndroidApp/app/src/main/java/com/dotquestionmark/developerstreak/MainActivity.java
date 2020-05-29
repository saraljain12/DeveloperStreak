package com.dotquestionmark.developerstreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Camera;
import android.os.Bundle;
import android.view.ViewParent;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager=(ViewPager) findViewById(R.id.viewpagerid);
        mFragmentPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.setCurrentItem(1);


    }

    public static class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return UserFragment.newInstance();
                case 1:
                    return CameraFragment.newInstance();
                case 2:
                    return ThirdFragment.newInstance();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
