package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.adapters.StudentListAdapter;
import com.example.daoyun09.adapters.StudentListAdapter2;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.httpBean.StudentsListBean;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.GPSUtils;
import com.example.daoyun09.utils.LogUtil;
import com.example.daoyun09.utils.ToastUtil;
import com.lxj.xpopup.XPopup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendActivity extends AppCompatActivity {
    private static final int WHAT_REQUEST_FAILED = 1;
    private static final int WHAT_CHECK_SUCCESS = 2;
    private static final int WHAT_CHECK_FAILED = 3;
    private static final int WHAT_NETWORK_ERROR = 4;
    private static final int WHAT_GET_STUDENTS_SUCCESS = 5;
    private static final int WHAT_START_CHECK_SUCCESS = 6;
    private static final int WHAT_START_CHECK_FAILED = 7;
    private static final int WHAT_GET_CAN_CHECK_SUCCESS = 8;
    private static final int WHAT_GET_CAN_CHECK_FAILED = 9;
    private static final int REQUEST_TAKE_MEDIA = 102;
    private static final int WHAT_STOP_CHECK_SUCCESS = 10;
    private static final int WHAT_STOP_CHECK_FAILED = 11;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.atten_recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.refresh_students_list)
    SwipeRefreshLayout refresh_students_list;



    String courseId;
    String userType;
    String teacher;

    private int stuSize;
    Button stop_check;
    List<StudentsListBean> students = new ArrayList<>();
    StudentListAdapter2 mAdapter;


    ProgressDialog progressDialog;
    JSONObject tmp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info2);
        ButterKnife.bind(this);
        courseId = getIntent().getStringExtra("courseId");
        initView();
        initData();
    }

    private void initView() {
        //setButtonState();
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); //设置返回按键
        mAdapter = new StudentListAdapter2(students, this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(mAdapter);
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    private void initData() {
        students.clear();

        HttpUtil.getHistoryInfo(courseId, new BaseObserver<JSONObject>() {
            @Override
            protected void onSuccess(JSONObject res) throws ParseException {

                JSONArray info = res.getJSONArray("dataList");
                stuSize = info.size();
                for (int i = 0; i < stuSize; i++) {
                    JSONObject tmp = info.getJSONObject(i);
                    StudentsListBean stu = new StudentsListBean();
                    String t = tmp.getString("startTime");
                    String bt = t.substring(0,10);
                    String st = t.substring(11,19);
                    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:m:ss ");
                    Date d=fm.parse(bt+" "+st+" ");
                    d.setTime(d.getTime()+1000*60*60*8);
                    t=fm.format(d);
                    bt = t.substring(0,10);
                    st = t.substring(11,19);
                    String ct = tmp.getIntValue("count")+"/"+tmp.getIntValue("total");
                    String type;
                    if(tmp.getIntValue("attendanceType") == 1)
                    {
                        type = "限时签到";
                    }
                    else
                    {
                        type = "一键签到";
                    }
                    stu.setName(bt+"     "+type);
                    stu.setStu_code(st);
                    stu.setCheck_count("     "+ct);
                    students.add(stu);
                }
                mHandler.sendEmptyMessage(WHAT_GET_STUDENTS_SUCCESS);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                ToastUtil.showMessage(getActivityContext(), "ddd连接失败", Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    Context getActivityContext() {
        return this;
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "请求失败，请重试");
                    break;
                case WHAT_CHECK_SUCCESS:
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    break;
                case WHAT_CHECK_FAILED:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    break;
                case WHAT_NETWORK_ERROR:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    ToastUtil.showNetworkErrorMsg(getActivityContext());
                    break;
                case WHAT_GET_STUDENTS_SUCCESS:
                    mAdapter.notifyDataSetChanged();
                    break;
                case WHAT_START_CHECK_SUCCESS:
                    ToastUtil.showMessage(getActivityContext(), "开始签到成功");
                    break;
                case WHAT_START_CHECK_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "开始签到失败,请重试");
                    break;
                case WHAT_STOP_CHECK_SUCCESS:
                    ToastUtil.showMessage(getActivityContext(), "停止签到成功");
                    break;
                case WHAT_STOP_CHECK_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "停止签到失败,请重试");
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_MEDIA:
                if (resultCode == RESULT_OK) {
                    //beginSignRequest();
                } else {
                    ToastUtil.showMessage(AttendActivity.this, "拍照失败");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
