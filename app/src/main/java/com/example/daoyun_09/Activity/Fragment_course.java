package com.example.daoyun_09.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daoyun_09.Adapter.courseList_Adapter;
import com.example.daoyun_09.Entity.courseInfo;
import com.example.daoyun_09.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_course extends Fragment {

//    TextView createClass;
//    TextView joinClass;
      ImageView createCourse;
      FrameLayout frameLayout;
      FragmentManager mFragmentManager =null ;
      FragmentTransaction transaction =null ;
      List<courseInfo> courselist = new ArrayList<>();
      RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View course = inflater.inflate(R.layout.activity_course,container,false);
        if(courselist.size()==0) init();
        createCourse = (ImageView)course.findViewById(R.id.addClassView);
        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(),CreateActivity.class);
                //startActivity(it);
                startActivityForResult(it,0x001);

            }
        });

        recyclerView = (RecyclerView) course.findViewById(R.id.recycle_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        courseList_Adapter adapter = new courseList_Adapter(courselist);
        recyclerView.setAdapter(adapter);
        return course;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==0x001&&resultCode==0x002){
            courseInfo c = new courseInfo();
            c.setCourseName(data.getStringExtra("courseName"));
            c.setTeacherName(data.getStringExtra("teacherName")+"   "+data.getStringExtra("time"));
            c.setCourseNum(data.getStringExtra("tcn"));
            courselist.add(c);
            Toast.makeText(getActivity(),"1231321132",Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init()
    {
        courseInfo c1 = new courseInfo();
        c1.setCourseName("工程训练");
        c1.setTeacherName("池芝标");
        c1.setCourseNum("电子信息2班");
        courselist.add(c1);
        courseInfo c2 = new courseInfo();
        c2.setCourseName("人工智能");
        c2.setTeacherName("余春艳");
        c1.setCourseNum("电子信息1班");
        courselist.add(c2);
    }

    public void setRecycleList()
    {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        courseList_Adapter adapter = new courseList_Adapter(courselist);
        recyclerView.setAdapter(adapter);
    }
}
