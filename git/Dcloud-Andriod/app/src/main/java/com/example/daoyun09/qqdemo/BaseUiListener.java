package com.example.daoyun09.qqdemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.daoyun09.activities.FastRegisiterActivity;
import com.example.daoyun09.activities.tmpMainActivity;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.httpBean.LoginBean;
import com.example.daoyun09.activities.MainActivity;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseUiListener implements IUiListener {
    private static final String TAG = "qq";
    private Context context;
    private Tencent mTencent;
    private UserInfo mUserInfo;
    private int type;
    public BaseUiListener(Context context, Tencent mTencent, int type){
        this.context=context;
        this.mTencent=mTencent;
        this.type=type;
    }
    @Override
    public void onComplete(Object response) {
        Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "response:" + response);
        JSONObject obj = (JSONObject) response;
        try {
            String openID = obj.getString("openid");
            String accessToken = obj.getString("access_token");
            String expires = obj.getString("expires_in");
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(accessToken, expires);
            QQToken qqToken = mTencent.getQQToken();
            mUserInfo = new UserInfo(context.getApplicationContext(), qqToken);
            mUserInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    //是一个json串response.tostring，直接使用gson解析就好
                    Log.e(TAG, "登录成功" + response.toString());
                    //登录成功后进行Gson解析即可获得你需要的QQ头像和昵称
                    // Nickname  昵称
                    //Figureurl_qq_1 //头像
                    try {
                        String avatar = ((JSONObject) response).getString("figureurl_2");
                        String nickName = ((JSONObject) response).getString("nickname");
                        String url ="https://graph.qq.com/oauth2.0/me?access_token="+accessToken+"&unionid=1";
                        HttpUtil.getUid(url, new BaseObserver<String>() { //获取uid
                            @Override
                            protected void onSuccess(String s) {
                                String resddd = s.substring(9,s.length()-3);
                                Toast.makeText(context,resddd , Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject juid = new JSONObject(resddd);
                                    String unionid = juid.getString("unionid");
                                    Toast.makeText(context,unionid, Toast.LENGTH_SHORT).show();
                                    //如果第一次三方登陆，则插入数据库
                                    HttpUtil.QQregisterUser(nickName, avatar,new BaseObserver<String>() {
                                        @Override
                                        protected void onSuccess(String s) {

                                        }

                                        @Override
                                        protected void onFailure(Throwable e, boolean isNetWorkError) {

                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) {

                            }
                        });
                        loginSuccess();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Intent intent = new Intent(context, tmpMainActivity.class);
//                    context.startActivity(intent);
                }

                @Override
                public void onError(UiError uiError) {
                    Log.e(TAG, "登录失败" + uiError.toString());
                }

                @Override
                public void onCancel() {
                    Log.e(TAG, "登录取消");

                }

                @Override
                public void onWarning(int i) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onError(UiError uiError) {
        Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancel() {
        Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onWarning(int i) {

    }

    private void saveData(LoginBean loginBean) {
        SessionKeeper.loginSave(context, loginBean);
    }
    private void loginSuccess() {
       // SessionKeeper.keepAutoLogin(this, true);

        Intent i2 = new Intent(context, MainActivity.class);
        context.startActivity(i2);
    }
}
