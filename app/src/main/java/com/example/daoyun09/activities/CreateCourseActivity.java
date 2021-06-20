package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.dto.CourseDto;
import com.example.daoyun09.httpBean.DefaultResultBean;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.GPSUtils;
import com.example.daoyun09.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateCourseActivity extends AppCompatActivity {
    private static final int WHAT_CREATE_SUCCESS = 1001;
    private static final int WHAT_CREATE_FAILED = 1002;
    private static final int WHAT_FINISH_ACTIVITY = 1003;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.course_name)
    EditText courseName;
    @BindView(R.id.class_name)
    EditText className;
    @BindView(R.id.term)
    EditText term;
    @BindView(R.id.course_loc)
    EditText courseLoc;
//    @BindView(R.id.course_teacher)
//    EditText courseTeacher;
    @BindView(R.id.course_tel)
    EditText tel;
    @BindView(R.id.loading_button)
    Button loadingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        term.setText("2020-2021-2");
        //courseTeacher.setText(SessionKeeper.getUserInfo(this).getName());
    }

    @OnClick(R.id.loading_button)
    public void onViewClicked() {
        loadingButton.setClickable(false);
        loadingButton.setText("提交中...");

        HttpUtil.createCourse(className.getText().toString(),"s",0, courseName.getText().toString(),"s",
                "s","s",tel.getText().toString(),term.getText().toString(),new BaseObserver<String>() {
          //HttpUtil.createCourse(cd,new BaseObserver<String>() {
            @Override
            protected void onSuccess(String number) {
                ToastUtil.showMessage(getApplicationContext(), "创建成果："+number, ToastUtil.LENGTH_LONG);
                Intent intent=new Intent(CreateCourseActivity.this,QRCode.class);
                intent.putExtra("qrNum", number);//参数：num、value
                CreateCourseActivity.this.startActivity(intent);
//                if (objectDefaultResultBean.getResult_code().equals("200")) {
//                    mHandler.sendEmptyMessage(WHAT_CREATE_SUCCESS);
//                } else {
//                    ToastUtil.showMessage(getApplicationContext(), objectDefaultResultBean.getResult_desc());
//                    mHandler.sendEmptyMessage(WHAT_CREATE_FAILED);
//                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(getApplicationContext(), "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(getApplicationContext(), e.getMessage(), ToastUtil.LENGTH_LONG);

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CREATE_SUCCESS:
                    ToastUtil.showMessage(getApplicationContext(), "创建成功");
                    mHandler.sendEmptyMessageDelayed(WHAT_FINISH_ACTIVITY, 1000);
                    break;
                case WHAT_CREATE_FAILED:
                    loadingButton.setClickable(true);
                    loadingButton.setText("提交");
                    break;
                case WHAT_FINISH_ACTIVITY:
                    finish();
                    break;
            }
        }
    };
}
