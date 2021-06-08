package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.entity.ServerResponse;
import com.example.daoyun09.entity.loginByPasswordVo;
import com.example.daoyun09.httpBean.LoginBean;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.qqdemo.BaseUiListener;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.LogUtil;
import com.example.daoyun09.utils.ToastUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.daoyun09.utils.Util.md5;
import static com.example.daoyun09.utils.isPhoneNumber.isMobileNO;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.register_go)
    TextView tvForgotPassword;
    @BindView(R.id.getCode_x)
    Button getCode;
    @BindView(R.id.qq_login)
    ImageView qq_img;
    @BindView(R.id.go_password)
    TextView go_password;
    int count = 60;

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

    ProgressDialog progressDialog;
    //第三方登录
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private static final String APP_ID = "101949518";
    private UserInfo mUserInfo;

    boolean typeStateStu = true;//是否学生号qq登录
    private int requestCode;
    private int resultCode;
    private Intent data;
    Thread sendCodeThread;

    private boolean flag;
    private String resCode="";
    private static final String TAG = "qq";

    //    @BindView(R.id.bt_switch_type)
//    Button btSwitchType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        flag = true;
        initView();
        //initData();
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, LoginActivity.this.getApplicationContext());
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登录中....");
        progressDialog.setCancelable(false);
    }

    private void initData() {
        verifyStoragePermissions(this);
        String email=SessionKeeper.getUserEmail(LoginActivity.this);
        if(email!=""){
            //etUsername.setText(email);
            //etPassword.setText(SessionKeeper.getUserPassword(LoginActivity.this));
            if (SessionKeeper.getAutoLogin(this)) {
                onBtGoClicked();
            }
        }
    }

    public LoginBean getUserInfo(String account)
    {
        LoginBean user = new LoginBean();
        HttpUtil.getUserInfo(account,new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                JSONObject res = JSONObject.parseObject(s);
                user.setToken(res.getString("token"));
                user.setUid(res.getInteger("id"));
                user.setEmail(res.getString("email"));
                user.setName(res.getString("name"));
                user.setNick_name(res.getString("Nickname"));
                user.setGender(res.getString("sex"));
                user.setStu_code(res.getString("sno"));
                user.setPhone(res.getString("telphone"));

                user.setType(res.getInteger("type"));
                user.setSchool(res.getString("university"));
                user.setDepartment(res.getString("telphone"));
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                ToastUtil.showMessage(LoginActivity.this, "获取信息失败", ToastUtil.LENGTH_LONG);
            }
        });

        return user;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.bt_go)
    public void onBtGoClicked() {
        if(flag) //flag==true: 表示手机短信登陆
        {
            if(!etUsername.getText().toString().isEmpty() && isMobileNO(etUsername.getText().toString())) //账号非空且合法
            {
                if(etPassword.getText().toString().trim().equals(resCode)) { //验证码正确
                    saveData(getUserInfo(etUsername.getText().toString())); //获取用户信息
                    loginSuccess();
                }
                else
                {
                    ToastUtil.showMessage(LoginActivity.this, "验证码错误，请重新输入", ToastUtil.LENGTH_LONG);
                }
            }
        }
        else //表示账号密码登陆
        {
            if (!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty() && isMobileNO(etUsername.getText().toString()))
            {
                HttpUtil.login(etUsername.getText().toString(), etPassword.getText().toString(),new BaseObserver<ServerResponse<loginByPasswordVo>>() {

                    @Override
                    protected void onSuccess(ServerResponse<loginByPasswordVo> response) {
                        ToastUtil.showMessage(LoginActivity.this, "result:"+response.getResult(), ToastUtil.LENGTH_LONG);
                        if(response.getResult()) //如果返回值为true
                        {
                            saveData(getUserInfo(etUsername.getText().toString())); //获取用户信息
                            loginSuccess();
                        }
                        else
                        {
                            ToastUtil.showMessage(LoginActivity.this, "密码错误，请重新输入", ToastUtil.LENGTH_LONG);
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) {

                    }
                });
            }
        }

    }

    @OnClick(R.id.getCode_x)
    public void getCode()
    {
        if(isMobileNO(etUsername.getText().toString())) //验证手机号码是否非法
        {
            mHandler.sendEmptyMessage(WHAT_SEND_EMAIL); //设计倒计时
            HttpUtil.sendTelCode(etUsername.getText().toString(), new BaseObserver<String>() {
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
    }

    @OnClick(R.id.go_password)
    public void go_login_password() //动态布局
    {
        flag = !flag;
        if(!flag)
        {
            getCode.setVisibility(View.GONE);
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) etPassword.getLayoutParams();
            linearParams.width = 650;
            etPassword.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            SpannableString s = new SpannableString("   请输入密码");//这里输入自己想要的提示文字
            etPassword.setHint(s);
        }
        else
        {
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) etPassword.getLayoutParams();
            linearParams.width = 350;// 控件的宽强制设成350
            etPassword.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            getCode.setVisibility(View.VISIBLE);
            SpannableString s = new SpannableString("   验证码");//这里输入自己想要的提示文字
            etPassword.setHint(s);
        }
    }

    @OnClick(R.id.qq_login)
    public void qqLogin()
    {
        mIUiListener = new BaseUiListener(LoginActivity.this,mTencent,2);
        //all表示获取所有权限
        mTencent.login(LoginActivity.this,"all", mIUiListener);
    }


    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    getCode.setClickable(false);
                    sendCodeThread = new Thread(canSendCode);
                    sendCodeThread.start();
                    break;
                //设置为可再发送
                case WHAT_CAN_RESEND:
                    count = 60;
                    getCode.setText("发送验证码");
                    getCode.setClickable(true);
                    break;
                //设置倒数时间
                case WHAT_SET_TIME:
                    String s = count + "秒";
                    getCode.setText(s);
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

    private static final int REQUEST_EXTERNAL_STORAGE = 10001;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static boolean verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                LogUtil.d("权限获取", "未获得权限");
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                return false;
            } else {
                LogUtil.d("权限获取", "已有权限");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveData(LoginBean loginBean) {
        String pwd = etPassword.getText().toString();
        SessionKeeper.loginSave(LoginActivity.this, loginBean);
        SessionKeeper.keepUserPassword(LoginActivity.this, pwd.length() > 20 ? pwd : md5(pwd));
    }

    @OnClick(R.id.register_go)
    public void onFabClicked() {
        startActivity(new Intent(LoginActivity.this, FastRegisiterActivity.class));
    }

    private void loginSuccess() {
        btGo.setClickable(true);
        SessionKeeper.keepAutoLogin(this, true);
        Explode explode = new Explode();
        explode.setDuration(500);

        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
        Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i2, oc2.toBundle());
        finish();
    }

}
