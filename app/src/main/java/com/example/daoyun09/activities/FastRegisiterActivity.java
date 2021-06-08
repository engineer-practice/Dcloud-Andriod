package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.daoyun09.utils.isPhoneNumber.isMobileNO;

public class FastRegisiterActivity extends AppCompatActivity {

    @BindView(R.id.re_username)
    TextView username;
    @BindView(R.id.re_code)
    TextView code;
    @BindView(R.id.back_img)
    ImageView backLogin;
    @BindView(R.id.getCode_r)
    Button getCoder;
    @BindView(R.id.bt_register)
    Button bt_register;
    Thread sendCodeThread;

    //发送验证码
    private static final int WHAT_SEND_EMAIL = 1001;
    //验证码正确
    private static final int WHAT_NETWORK_ERROR = 1002;
    //可重新发送短信
    private static final int WHAT_CAN_RESEND = 1003;
    //设置倒数时间
    private static final int WHAT_SET_TIME = 1004;
    //操作正确
    private static final int WHAT_OPERATION_SUCCESS = 1005;
    //操作失败
    private static final int WHAT_OPERATION_FAIL = 1006;
    //注册成功 返回
    private static final int WHAT_BACK = 1008;

    private String resCode="";
    int count = 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fast_register);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.back_img)
    public void back()
    {
        ToastUtil.showMessage(FastRegisiterActivity.this,"返回", ToastUtil.LENGTH_LONG);
        Intent intent = new Intent(FastRegisiterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.getCode_r)
    public void getCode()
    {
        mHandler.sendEmptyMessage(WHAT_SEND_EMAIL); //设置倒计时
        if(isMobileNO(username.getText().toString())) //判断输入的手机号是否合法
        {
            HttpUtil.sendTelCode(username.getText().toString(), new BaseObserver<String>() {
                @Override
                protected void onSuccess(String response) {
                    //ToastUtil.showMessage(FastRegisiterActivity.this,"msg:"+ s, ToastUtil.LENGTH_LONG);
                    JSONObject res =JSONObject.parseObject(response);
                    resCode = ((String)res.get("respCode")).trim();
                }
                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) {

                }
            });
        }


    }

    @OnClick(R.id.bt_register)
    public void register()
    {
        if(code.getText().toString().trim().equals(resCode)) //验证码验证
        {
            HttpUtil.FastregisterUser(username.getText().toString(), new BaseObserver<String>() {
                @Override
                protected void onSuccess(String s) {
                    //ToastUtil.showMessage(FastRegisiterActivity.this,"test:"+ s, ToastUtil.LENGTH_LONG);
                    Intent i2 = new Intent(FastRegisiterActivity.this, LoginActivity.class);
                    startActivity(i2);
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) {

                }
            });
        }

    }

    //具体处理事务
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //点击发送验证码后 按钮文字改为提示多久可再次发送
                case WHAT_SEND_EMAIL:
                    getCoder.setClickable(false);
                    sendCodeThread = new Thread(canSendCode);
                    sendCodeThread.start();
                    break;
                //设置为可再发送
                case WHAT_CAN_RESEND:
                    count = 60;
                    getCoder.setText("发送验证码");
                    getCoder.setClickable(true);
                    break;
                //设置倒数时间
                case WHAT_SET_TIME:
                    String s = count + "秒";
                    getCoder.setText(s);
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

    /**
     * 发送验证码后倒数的线程
     */
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
