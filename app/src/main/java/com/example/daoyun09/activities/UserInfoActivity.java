package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.droidbond.loadingbutton.LoadingButton;
import com.example.daoyun09.httpBean.DictInfoListBean;
import com.example.daoyun09.httpBean.LoginBean;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.InputDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends AppCompatActivity {
    private static final int WHAT_SAVE_SUCCESS = 1;
    private static final int WHAT_SAVE_FAILED = 2;
    private static final int WHAT_QUIT = 3;
    @BindView(R.id.nick_name)
    EditText nickName;
    @BindView(R.id.up_name)
    EditText up_name;
    @BindView(R.id.stu_code)
    EditText stuCode;
    @BindView(R.id.birth)
    EditText birth;
    @BindView(R.id.save)
    LoadingButton save;
    @BindView(R.id.department_input)
    TextInputLayout departmentInput;
    @BindView(R.id.spinner_school)
    Spinner spinner_school;
    @BindView(R.id.spinner_depart)
    Spinner spinner_depart;
    @BindView(R.id.spinner_sex)
    Spinner spinner_sex;
    @BindView(R.id.spinner_type)
    Spinner spinner_type;


    String selectedInfo = "";
    ArrayList<String> schoolItem = new ArrayList<String>();
    ArrayList<String> schoolCode = new ArrayList<String>();
    ArrayList<String> departItem = new ArrayList<String>();
    ArrayList<String> departCode = new ArrayList<String>();
    ArrayList<String> sexItem = new ArrayList<String>();
    ArrayList<Integer> sexCode = new ArrayList<Integer>();
    ArrayList<String> typeItem = new ArrayList<String>();
    ArrayList<Integer> typeCode = new ArrayList<Integer>();

    String uid = "";
    LoginBean user;
    int sex = -1;
    int type = -1;
    int schoolId = 0;
    int departmentId = 0;
    int professionId = 0;
    DataForm schoolForm = new DataForm();
    DataForm departmentForm = new DataForm();
    DataForm professionForm = new DataForm();
    List<List<DictInfoListBean>> data = new ArrayList<>();
    @BindView(R.id.userinfo_back)
    Button userinfodBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        uid = getIntent().getStringExtra("uid");
        schoolItem.add("学校");
        schoolCode.add("学校");
        departItem.add("院系");
        departCode.add("院系");
        sexItem.add("性别");
        sexCode.add(-1);
        sexItem.add("男");
        sexCode.add(0);
        sexItem.add("女");
        sexCode.add(1);

        typeItem.add("身份");
        typeCode.add(-1);
        typeItem.add("学生");
        typeCode.add(0);
        typeItem.add("教师");
        typeCode.add(1);

        initData();
        initView();
        getSchools(); //获取学校
        spinner_school.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,schoolItem));
        spinner_sex.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,sexItem));
        spinner_type.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,typeItem));
        spinner_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                departItem.clear();
                departCode.clear();
                departItem.add("院系");
                departCode.add("院系");
                getDepartment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_depart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedInfo = departCode.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                sex = sexCode.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type = typeCode.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void getSchools()
    {
        HttpUtil.getSchools(new BaseObserver<JSONObject>() {
            @Override
            protected void onSuccess(JSONObject res) {
                //ToastUtil.showMessage(getApplicationContext(), res.toJSONString(), ToastUtil.LENGTH_LONG);
                int size=res.getIntValue("total");
                JSONArray info = res.getJSONArray("dataList");
                for(int i=0;i<size;i++)
                {
                    JSONObject tmp = info.getJSONObject(i);
                    schoolItem.add(tmp.getString("name"));
                    schoolCode.add(tmp.getString("code"));
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                        ToastUtil.showMessage(UserInfoActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(UserInfoActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
    }

    public void getDepartment(int pos)
    {
        HttpUtil.getDepartment(schoolCode.get(pos),new BaseObserver<JSONObject>() {
            @Override
            protected void onSuccess(JSONObject res) {
                //ToastUtil.showMessage(getApplicationContext(), res.toJSONString(), ToastUtil.LENGTH_LONG);
                int size=res.getIntValue("total");
                JSONArray info = res.getJSONArray("dataList");
                for(int i=0;i<size;i++)
                {
                    JSONObject tmp = info.getJSONObject(i);
                    departItem.add(tmp.getString("name"));
                    departCode.add(tmp.getString("code"));
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(UserInfoActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(UserInfoActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
        spinner_depart.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,departItem));
    }

    private void initData() {
        data.add(new ArrayList<>());
        data.add(new ArrayList<>());
        data.add(new ArrayList<>());
        HttpUtil.getDictInfo(SessionKeeper.getToken(this), getResources().getString(R.string.http_get_school_info), new BaseObserver<DictInfoListBean>() {
            @Override
            protected void onSuccess(DictInfoListBean dictInfoListBean) {
                if (dictInfoListBean.getResult_code().equals("200")) {
                    if (dictInfoListBean.getData() != null)
                        for (DictInfoListBean info : dictInfoListBean.getData())
                            data.get(info.getType_level() - 1).add(info);
                } else
                    ToastUtil.showMessage(UserInfoActivity.this, dictInfoListBean.getResult_desc(), ToastUtil.LENGTH_SHORT);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(UserInfoActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(UserInfoActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        user = SessionKeeper.getUserInfo(this);
        up_name.setText(user.getName());
        nickName.setText(user.getNick_name());
        //gender.setText(user.getGender());
        birth.setText(user.getDepartment());
        stuCode.setText(user.getStu_code());
    }
    @OnClick(R.id.userinfo_back)
    public void onUserinfodBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.save)
    public void onSaveClicked() {
        save.setClickable(false);
        HttpUtil.UpdateUserInfo(selectedInfo,birth.getText().toString(),"",up_name.getText().toString(), nickName.getText().toString(),type,
                sex,stuCode.getText().toString(),SessionKeeper.getUserInfo(UserInfoActivity.this).getPhone(),new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String response) {
                        JSONObject res =JSONObject.parseObject(response);
                        String resCode = ((String)res.get("respCode")).trim();
                        if(resCode.equals("1"))
                        {
                            Intent intent=new Intent(UserInfoActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            ToastUtil.showMessage(UserInfoActivity.this, "修改失败");
                        }
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

    public boolean onActUserDepartmentTouched(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (schoolId == -1) return showInputDepartment();
            departmentForm.clear();
            departmentForm.add("手动输入", -1);
            if (data != null && data.get(1) != null)
                for (DictInfoListBean info : data.get(1))
                    if (info.getType_belong() == schoolId)
                        departmentForm.add(info.getInfo(), info.getId());
            BottomMenu.show(UserInfoActivity.this, departmentForm.getInfo(), (text, position) -> {
                departmentId = departmentForm.getIds().get(position);
            });
        }
        return true;
    }

    public boolean onActUserProfessionTouched(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (departmentId == -1) return showInputProfession();
            professionForm.clear();
            professionForm.add("手动输入", -1);
            if (data != null && data.get(2) != null)
                for (DictInfoListBean info : data.get(2))
                    if (info.getType_belong() == departmentId)
                        professionForm.add(info.getInfo(), info.getId());
            BottomMenu.show(UserInfoActivity.this, professionForm.getInfo(), (text, position) -> {
                professionId = professionForm.getIds().get(position);

            });
        }
        return true;
    }

    private boolean showInputProfession() {
        InputDialog.show(UserInfoActivity.this,
                "专业名称",
                "输入专业名称",
                "确定", (dialog, inputText) -> {
                    dialog.dismiss();
                }, "取消", (dialog, which) -> {
                });
        return true;
    }

    private boolean showInputDepartment() {
        InputDialog.show(UserInfoActivity.this,
                "学院名称",
                "输入学院名称",
                "确定", (dialog, inputText) -> {
                    dialog.dismiss();
                }, "取消", (dialog, which) -> {
                });
        return true;
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SAVE_SUCCESS:
                    save.setClickable(true);
                    ToastUtil.showMessage(UserInfoActivity.this, "保存成功");
                    saveUserInfo();
                    break;
                case WHAT_SAVE_FAILED:
                    save.setClickable(true);
                    break;
                case WHAT_QUIT:
                    finish();
                    break;
            }
        }
    };

    private void saveUserInfo() {
        SessionKeeper.keepUserNickName(this,nickName.getText().toString());
        LoginBean bean = SessionKeeper.getUserInfo(this);
        bean.setNick_name(nickName.getText().toString());
        bean.setPhone(up_name.getText().toString());
        //bean.setGender(gender.getText().toString());
        bean.setStu_code(stuCode.getText().toString());
        //bean.setSchool(school.getText().toString());
        bean.setDepartment(birth.getText().toString());
        SessionKeeper.keepUserInfo(this,bean);
        mHandler.sendEmptyMessageDelayed(WHAT_QUIT,1000);
    }
}
