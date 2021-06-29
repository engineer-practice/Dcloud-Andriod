package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.GPSUtils;
import com.example.daoyun09.utils.LogUtil;
import com.example.daoyun09.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Check_Stu_Activity extends AppCompatActivity {

    private static final int WHAT_SEND_EMAIL = 1001;
    private static final int WHAT_NETWORK_ERROR = 1002;
    private static final int WHAT_CAN_RESEND = 1003;
    private static final int WHAT_SET_TIME = 1004;
    private static final int WHAT_OPERATION_SUCCESS = 1005;
    private static final int WHAT_OPERATION_FAIL = 1006;
    private static final int WHAT_BACK = 1008;

    int count = 60;
    Thread sendCodeThread;

    @BindView(R.id.check_toolbar)
    Toolbar toolbar;
    @BindView(R.id.stu_button)
    Button stu_button;

    String courseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_stu);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); //设置返回按键
        courseId = getIntent().getStringExtra("courseId");
    }

    @OnClick(R.id.stu_button)
    public void stu_check()
    {
        HttpUtil.canCheck(courseId, new BaseObserver<String>() {
            @Override
            protected void onSuccess(String response) {
                JSONObject res =JSONObject.parseObject(response);
                String resCode = ((String)res.get("respCode")).trim();
                if(resCode.equals("签到未结束！"))
                {
                    Calendar calendar = Calendar.getInstance();
                    long time = calendar.getTimeInMillis();
                    Date turn = new Date(time);
                    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:m:ss");
                    String attend_time = fm.format(turn);
                    //ToastUtil.showMessage(getActivityContext(), "开始时间："+start_time,Toast.LENGTH_SHORT);
                    HttpUtil.stuCheck(attend_time,courseId, GPSUtils.getInstance(Check_Stu_Activity.this).getLatitude(),GPSUtils.getInstance(Check_Stu_Activity.this).getLongitude(),
                            SessionKeeper.getUserInfo(Check_Stu_Activity.this).getPhone(), new BaseObserver<String>() {
                                @Override
                                protected void onSuccess(String response) {
                                    JSONObject res =JSONObject.parseObject(response);
                                    String resCode = ((String)res.get("respCode")).trim();
                                    if(resCode.equals("1")) //签到成功
                                    {
                                        ToastUtil.showMessage(Check_Stu_Activity.this, "签到成功", Toast.LENGTH_LONG);
                                    }
                                    else if(resCode.equals("签到距离过远，无法签到！")) //不在签到范围
                                    {
                                        ToastUtil.showMessage(Check_Stu_Activity.this, "签到距离过远，无法签到",Toast.LENGTH_LONG);
                                    }
                                    else if(resCode.equals("已签到，请勿重复签到！")) //请勿重复签到
                                    {
                                        ToastUtil.showMessage(Check_Stu_Activity.this, "已签到，请勿重复签到",Toast.LENGTH_LONG);
                                    }
                                    else //不是班课学生
                                    {
                                        ToastUtil.showMessage(Check_Stu_Activity.this, "签到出错",Toast.LENGTH_LONG);
                                    }
                                    finish();
                                }
                                @Override
                                protected void onFailure(Throwable e, boolean isNetWorkError) {
                                    if (isNetWorkError) {

                                    } else {

                                        ToastUtil.showMessage(Check_Stu_Activity.this, "签到失败");
                                    }
                                }
                            });
                }
                else
                {
                    ToastUtil.showMessage(Check_Stu_Activity.this, "签到已经结束",Toast.LENGTH_LONG);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {

            }
        });

    }

}
