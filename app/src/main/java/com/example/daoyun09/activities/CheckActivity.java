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

import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckActivity extends AppCompatActivity {

    private static final int WHAT_SEND_EMAIL = 1001;
    private static final int WHAT_NETWORK_ERROR = 1002;
    private static final int WHAT_CAN_RESEND = 1003;
    private static final int WHAT_SET_TIME = 1004;
    private static final int WHAT_OPERATION_SUCCESS = 1005;
    private static final int WHAT_OPERATION_FAIL = 1006;
    private static final int WHAT_BACK = 1008;

    int count = 60;
    Thread sendCodeThread;

    @BindView(R.id.check_sec)
    TextView check_sec;
    @BindView(R.id.check_button)
    Button check_button;

    String type;
    String courseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_teacher);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        courseId = getIntent().getStringExtra("courseId");
        if(type.equals("1"))
        {
            mHandler.sendEmptyMessage(WHAT_SEND_EMAIL);
        }
        else
        {
            check_sec.setText("--");
        }
    }


    @OnClick(R.id.check_button)
    public void stop_check()
    {
        HttpUtil.stopCheck(courseId, new BaseObserver<String>() {
            @Override
            protected void onSuccess(String response) {
                JSONObject res =JSONObject.parseObject(response);
                String resCode = ((String)res.get("respCode")).trim();
                if(resCode.equals("1"))
                {
                    finish();
                }
                else
                {
                    ToastUtil.showMessage(CheckActivity.this, "连接出错");
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {

            }
        });

        check_button.setClickable(false);
    }



    //具体处理事务
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_SEND_EMAIL:
                    sendCodeThread = new Thread(canSendCode);
                    sendCodeThread.start();
                    break;
                //时间到自动结束
                case WHAT_CAN_RESEND:
                    stop_check();
                    check_sec.setText("--");
                    check_button.setClickable(false);
                    break;
                //设置倒数时间
                case WHAT_SET_TIME:
                    String s = count+"";
                    check_sec.setText(s);
                    count--;
                    break;
                case WHAT_OPERATION_SUCCESS:
                    mHandler.sendEmptyMessageDelayed(WHAT_BACK, 500);
                    break;
                case WHAT_OPERATION_FAIL:
                    break;
                case WHAT_BACK:
                    break;
            }
        }
    };

    Runnable canSendCode = () -> {
        while (true) {
            if (count != 0) {
                mHandler.sendEmptyMessage(WHAT_SET_TIME);
            } else {
                mHandler.sendEmptyMessage(WHAT_CAN_RESEND);
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
