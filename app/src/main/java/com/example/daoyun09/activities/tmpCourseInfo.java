package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.adapters.CoursesListAdapter;
import com.example.daoyun09.adapters.StudentListAdapter;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.httpBean.CoursesListBean;
import com.example.daoyun09.httpBean.StudentsListBean;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class tmpCourseInfo extends AppCompatActivity {
    private static final int WHAT_GET_DATA_SUCCESS = 1;
    private static final int WHAT_GET_DATA_FAILED = 2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.refresh_students_list)
    SwipeRefreshLayout mRefreshStudentList;
    @BindView(R.id.recycle_main)
    RecyclerView mRecycleView;
    @BindView(R.id.no_class_notify)
    TextView noClassNotify;


    private String mParam1;
    private String mParam2;
    private int size;

    String courseId;
    String userType;
    String teacher;

    Unbinder unbinder;
    StudentListAdapter mAdapter;

    //课程列表
    List<StudentsListBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ButterKnife.bind(this);
        userType = SessionKeeper.getUserType(this);
        courseId = getIntent().getStringExtra("course_id");
        teacher = getIntent().getStringExtra("teacher");
        initView();
        if (data.size() == 0)
            getData();
    }

    private void getData() {
        mRefreshStudentList.setRefreshing(true);
        data.clear();

        HttpUtil.getStudentsList(courseId, new BaseObserver<JSONObject>() {
            @Override
            protected void onSuccess(JSONObject res) {
                size=res.getIntValue("total");
                JSONArray info = res.getJSONArray("dataList");
                for(int i=0;i<size;i++)
                {
                    JSONObject tmp = info.getJSONObject(i);
                    StudentsListBean c =new StudentsListBean();
                    c.setName(tmp.getString("name"));
                    c.setStu_code(tmp.getString("sno"));
                    c.setCheck_count(tmp.getString("exp"));
                    data.add(c);
                }
                mHandler.sendEmptyMessage(WHAT_GET_DATA_SUCCESS);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(tmpCourseInfo.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(tmpCourseInfo.this, e.getMessage(), ToastUtil.LENGTH_LONG);

            }
        });


    }

    private void initView() {
        mAdapter = new StudentListAdapter(data, tmpCourseInfo.this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(tmpCourseInfo.this));
        mRecycleView.setAdapter(mAdapter);
        mRefreshStudentList.setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        mRefreshStudentList.setOnRefreshListener(this::getData);
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_GET_DATA_SUCCESS:
                    mAdapter.notifyDataSetChanged();
                    if (mRefreshStudentList != null && mRefreshStudentList.isRefreshing())
                        mRefreshStudentList.setRefreshing(false);
                    noClassNotify.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    break;
                case WHAT_GET_DATA_FAILED:
                    if (mRefreshStudentList != null && mRefreshStudentList.isRefreshing())
                        mRefreshStudentList.setRefreshing(false);
                    ToastUtil.showMessage(tmpCourseInfo.this, "如是QQ登录失败，请换普通登录", ToastUtil.LENGTH_LONG);
                    //网络失败的话返回登录界面
                    Intent intent = new Intent(tmpCourseInfo.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
