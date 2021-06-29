package com.example.daoyun09.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoyun09.R;
import com.example.daoyun09.httpBean.StudentsListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentListAdapter2 extends RecyclerView.Adapter<StudentListAdapter2.ViewHolder> {

    private List<StudentsListBean> data;
    private Context context;

    public StudentListAdapter2(List<StudentsListBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.student_list_item1, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StudentsListBean stu = data.get(i);
        viewHolder.studentName.setText(stu.getName());
        viewHolder.studentSno.setText(stu.getStu_code());
        viewHolder.studentCheck.setText(stu.getCheck_count());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stu_name_TV)
        TextView studentName;
        @BindView(R.id.stu_exp)
        TextView studentCheck;
        @BindView(R.id.stu_sno_TV)
        TextView studentSno;
        @BindView(R.id.stu_list_item)
        RelativeLayout stu_list_item;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
