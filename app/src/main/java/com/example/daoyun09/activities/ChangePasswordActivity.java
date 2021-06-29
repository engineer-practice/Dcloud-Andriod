package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.droidbond.loadingbutton.LoadingButton;
import com.example.daoyun09.httpBean.DefaultResultBean;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;
import com.example.daoyun09.utils.Util;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final int WHAT_SEND_EMAIL = 1001;
    private static final int WHAT_NETWORK_ERROR = 1002;
    private static final int WHAT_CAN_RESEND = 1003;
    private static final int WHAT_SET_TIME = 1004;
    private static final int WHAT_OPERATION_SUCCESS = 1005;
    private static final int WHAT_OPERATION_FAIL = 1006;
    private static final int WHAT_HIDE_LOADING = 1007;
    private static final int WHAT_BACK = 1008;

    @BindView(R.id.updatepwd_toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_repeat_password)
    EditText etRepeatPassword;
    @BindView(R.id.loading_button)
    LoadingButton loadingButton;

    Thread sendCodeThread;
    private String resCode="";
    int count = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        etPhone.setText(SessionKeeper.getUserInfo(ChangePasswordActivity.this).getPhone());
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); //设置返回按键
        setListener();
    }

    @OnClick(R.id.loading_button)
    public void onLoadingButtonClicked() {
        HttpUtil.updatePwd(etNewPwd.getText().toString(),etOldPwd.getText().toString(),etRepeatPassword.getText().toString(),etPhone.getText().toString(), new BaseObserver<String>() {
            @Override
            protected void onSuccess(String response) {
                JSONObject res =JSONObject.parseObject(response);
                String resCode = ((String)res.get("respCode")).trim();
                if(resCode.equals("1"))
                {
                    ToastUtil.showMessage(ChangePasswordActivity.this, "密码修改成功");
                    Intent intent=new Intent(ChangePasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    ToastUtil.showMessage(ChangePasswordActivity.this, resCode);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    Message msg = new Message();
                    msg.what = WHAT_OPERATION_FAIL;
                    msg.obj = e.getMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    private void sendEmail(String phone) {

        HttpUtil.sendTelCode(phone, new BaseObserver<String>() {
            @Override
            protected void onSuccess(String response) {
                //ToastUtil.showMessage(LoginActivity.this, response, ToastUtil.LENGTH_LONG);
                JSONObject res =JSONObject.parseObject(response);
                resCode = ((String)res.get("respCode")).trim(); //将获取回来的验证码值存到resCode
                //ToastUtil.showMessage(LoginActivity.this, resCode, ToastUtil.LENGTH_LONG);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {

            }
        });

    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //点击发送验证码后 按钮文字改为提示多久可再次发送
                case WHAT_SEND_EMAIL:
                    //sendVerifyCode.setClickable(false);
                    sendCodeThread = new Thread(canSendCode);
                    sendCodeThread.start();
                    break;
                //设置为可再发送
                case WHAT_CAN_RESEND:
                    count = 60;
                    //sendVerifyCode.setText("发送验证码");
                    //sendVerifyCode.setClickable(true);
                    break;
                //设置倒数时间
                case WHAT_SET_TIME:
                    String s = count + "秒";
                    //sendVerifyCode.setText(s);
                    count--;
                    break;
                case WHAT_OPERATION_SUCCESS:
                    ToastUtil.showMessage(ChangePasswordActivity.this, "操作成功", ToastUtil.LENGTH_LONG);
                    loadingButton.showSuccess();
                    mHandler.sendEmptyMessageDelayed(WHAT_BACK, 500);
                    break;
                case WHAT_OPERATION_FAIL:
                    ToastUtil.showMessage(ChangePasswordActivity.this, (String) msg.obj, ToastUtil.LENGTH_LONG);
                    loadingButton.showError();
                    mHandler.sendEmptyMessageDelayed(WHAT_HIDE_LOADING, 1000);
                    break;
                case WHAT_HIDE_LOADING:
                    loadingButton.hideLoading();
                    break;
                case WHAT_BACK:
                    ChangePasswordActivity.this.onBackPressed();
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

    @Override
    protected void onStop() {
        super.onStop();
        if (sendCodeThread != null && !sendCodeThread.isInterrupted())
            sendCodeThread.interrupt();
    }

    private void setListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {

                }
            }
        });
        etNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {

                }
            }
        });

        etRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
