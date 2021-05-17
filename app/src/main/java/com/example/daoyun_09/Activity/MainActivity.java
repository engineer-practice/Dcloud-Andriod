package com.example.daoyun_09.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.daoyun_09.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView navigation;
    FragmentManager mFragmentManager =null ;
    FragmentTransaction transaction =null ;

    Fragment course = new Fragment_course();
    Fragment finds = new Fragment_find();
    Fragment user = new Fragment_user();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frame_layout_main);
        navigation = findViewById(R.id.navigation_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mFragmentManager = getSupportFragmentManager();
        startFragment();
    }

    int position=1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_my_course:
                    if(position==0)return false;
                    position = 0;
                    mFragmentManager.beginTransaction().replace(frameLayout.getId(),course).commit();
                    return true;
                case R.id.find:
                    if(position==1)return false;
                    position = 1;
                    mFragmentManager.beginTransaction().replace(frameLayout.getId(),finds).commit();
                    return true;
                case R.id.navigation_my_info:
                    if(position==2)return false;
                    position = 2;
                    mFragmentManager.beginTransaction().replace(frameLayout.getId(),user).commit();

                    return true;
            }
            return false;
        }
    };

    public void startFragment(){
        mFragmentManager.beginTransaction().replace(frameLayout.getId(),course).commit();
    }

}
