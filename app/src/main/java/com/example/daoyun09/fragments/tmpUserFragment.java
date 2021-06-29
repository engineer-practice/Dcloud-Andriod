package com.example.daoyun09.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.activities.ChangePasswordActivity;
import com.example.daoyun09.activities.LoginActivity;
import com.example.daoyun09.activities.UserInfoActivity;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.httpBean.UploadAvatarBean;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;
import com.google.gson.JsonObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class tmpUserFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.info_name)
    TextView info_name;
    @BindView(R.id.info_tel)
    TextView info_tel;
    @BindView(R.id.info_exp)
    TextView info_exp;
    @BindView(R.id.info_sno)
    TextView info_sno;
    @BindView(R.id.info_nickname)
    TextView info_nickname;
    @BindView(R.id.info_school)
    TextView info_school;
    @BindView(R.id.info_depart)
    TextView info_depart;
    @BindView(R.id.info_birth)
    TextView info_birth;
    @BindView(R.id.info_sex)
    TextView info_sex;
    @BindView(R.id.info_type)
    TextView info_type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tmframe_userinfo, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init()
    {
        if(SessionKeeper.getUserInfo(getActivity()).getName().equals("0")) //第一次登錄
        {
            info_name.setText("游客2333");
            info_tel.setText(SessionKeeper.getUserInfo(getActivity()).getPhone());
            info_exp.setText(SessionKeeper.getUserInfo(getActivity()).getAvatar());
            info_nickname.setText("昵称：  "+"请设置");
            info_sno.setText("学号：  "+"请设置");
            info_school.setText("学校：  "+"请设置");
            info_depart.setText("学校：  "+"请设置");
            info_birth.setText("生日：  "+"请设置");
            info_sex.setText("性别：  "+"请设置");
            info_type.setText("身份：  "+"请设置");
        }
        else if(SessionKeeper.getUserInfo(getActivity()).getPhone().equals("待关联"))
        {
            info_name.setText(SessionKeeper.getUserInfo(getActivity()).getName());
            info_tel.setText(SessionKeeper.getUserInfo(getActivity()).getPhone());
            info_exp.setText(SessionKeeper.getUserInfo(getActivity()).getAvatar());
            info_nickname.setText("昵称：  "+"请设置");
            info_type.setText("身份：  "+"请设置");
            info_sno.setText("学号：  "+"请设置");
            info_school.setText("学校：  "+"请设置");
            info_depart.setText("学校：  "+"请设置");
            info_birth.setText("生日：  "+"请设置");
            info_sex.setText("性别：  "+"请设置");
        }
        else
        {
            info_name.setText(SessionKeeper.getUserInfo(getActivity()).getName());
            info_tel.setText(SessionKeeper.getUserInfo(getActivity()).getPhone());
            info_exp.setText(SessionKeeper.getUserInfo(getActivity()).getAvatar());
            info_nickname.setText("昵称：  "+SessionKeeper.getUserInfo(getActivity()).getNick_name());
            if(SessionKeeper.getUserInfo(getActivity()).getType() == 0)
            {
                info_type.setText("身份：  "+"学生");
                info_sno.setText("学号：  "+SessionKeeper.getUserInfo(getActivity()).getStu_code());
            }
            else
            {
                info_type.setText("身份：  "+"教师");
                info_sno.setText("工号：  "+SessionKeeper.getUserInfo(getActivity()).getStu_code());
            }

            info_birth.setText("生日：  "+SessionKeeper.getUserInfo(getActivity()).getDepartment());
            if(SessionKeeper.getUserInfo(getActivity()).getGender().equals("1"))
            {
                info_sex.setText("性别：  "+"女");
            }
            else info_sex.setText("性别：  "+"男");
            HttpUtil.getSchoolInfo(SessionKeeper.getUserInfo(getActivity()).getSchool(), new BaseObserver<JSONObject>() {
                @Override
                protected void onSuccess(JSONObject res) {
                    JSONArray info = res.getJSONArray("dataList");
                    JSONObject tmp = info.getJSONObject(0);
                    info_school.setText("学校：  "+tmp.getString("university"));
                    info_depart.setText("院系：  "+tmp.getString("department"));
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) {
                    if (isNetWorkError)
                        ToastUtil.showMessage(getActivity(), "网络错误", ToastUtil.LENGTH_LONG);
                    else
                        ToastUtil.showMessage(getActivity(), e.getMessage(), ToastUtil.LENGTH_SHORT);
                }
            });
        }
    }

    @OnClick(R.id.info_updateinfo)
    public void updateInfo()
    {
        Intent userIntent = new Intent(getActivity(), UserInfoActivity.class);
        userIntent.putExtra("uid", SessionKeeper.getUserId(Objects.requireNonNull(getActivity())));
        startActivity(userIntent);
    }

    @OnClick(R.id.info_updatepswd)
    public void changePswd()
    {
        Intent userIntent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(userIntent);
    }

    @OnClick(R.id.info_relogin)
    public void reLogin()
    {
        SessionKeeper.keepAutoLogin(getContext(), false);
        startActivity(new Intent(getActivity(), LoginActivity.class));
        Objects.requireNonNull(getActivity()).finish();
    }

}
