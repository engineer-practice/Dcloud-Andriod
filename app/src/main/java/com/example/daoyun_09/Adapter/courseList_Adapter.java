package com.example.daoyun_09.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.daoyun_09.Entity.courseInfo;
import com.example.daoyun_09.R;

import java.util.List;

public class courseList_Adapter extends RecyclerView.Adapter {

    private List<courseInfo> courseList;

    //内部类
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView courseImage;
        TextView courseName;
        TextView tName,tClass;

        public ViewHolder (View view)
        {
            super(view);
            courseImage = (ImageView) view.findViewById(R.id.courseImg);
            courseName = (TextView) view.findViewById(R.id.courseName);
            tName = (TextView) view.findViewById(R.id.tName);
        }
    }

    //构造函数
    public courseList_Adapter (List <courseInfo> List){
        courseList = List;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        courseInfo tmp = courseList.get(position);
        TextView cn = holder.itemView.findViewById(R.id.courseName);
        cn.setText(tmp.getCourseName());
        TextView tn = holder.itemView.findViewById(R.id.tName);
        tn.setText(tmp.getTeacherName());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
