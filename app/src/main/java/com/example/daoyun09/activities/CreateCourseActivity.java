package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;

import java.util.ArrayList;
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
    @BindView(R.id.bigTime)
    EditText courseName;
    @BindView(R.id.class_name)
    EditText className;
    @BindView(R.id.term)
    EditText term;
    @BindView(R.id.course_loc)
    EditText courseLoc;

    @BindView(R.id.spinner_school)
    Spinner spinner_school;
    @BindView(R.id.spinner_depart)
    Spinner spinner_depart;

//    @BindView(R.id.course_teacher)
//    EditText courseTeacher;
    @BindView(R.id.loading_button)
    Button loadingButton;

    String tel="";
    String selectedInfo = "";
    Map<String,String> schools;
    ArrayList<String> schoolItem = new ArrayList<String>();
    ArrayList<String> schoolCode = new ArrayList<String>();
    ArrayList<String> departItem = new ArrayList<String>();
    ArrayList<String> departCode = new ArrayList<String>();

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
        tel = SessionKeeper.getUserInfo(CreateCourseActivity.this).getPhone();
        //ToastUtil.showMessage(getApplicationContext(), "手机："+tel, ToastUtil.LENGTH_LONG);

        schoolItem.add("学校");
        schoolCode.add("学校");
        departItem.add("院系");
        departCode.add("院系");

        getSchools(); //获取学校
        spinner_school.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,schoolItem));

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
                    ToastUtil.showMessage(CreateCourseActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(CreateCourseActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
    }

    public void getDepartment(int pos)
    {
        //ToastUtil.showMessage(CreateCourseActivity.this, "school:"+schoolCode.get(pos), ToastUtil.LENGTH_LONG);
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
                    ToastUtil.showMessage(CreateCourseActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(CreateCourseActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
        spinner_depart.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,departItem));
    }


    @OnClick(R.id.loading_button)
    public void onViewClicked() {
        loadingButton.setClickable(false);
        loadingButton.setText("提交中...");

        HttpUtil.createCourse(className.getText().toString(),"",0, courseName.getText().toString(),"",
                courseLoc.getText().toString(),selectedInfo,tel,term.getText().toString(),new BaseObserver<String>() {
          //HttpUtil.createCourse(cd,new BaseObserver<String>() {
            @Override
            protected void onSuccess(String number) {
                Intent intent=new Intent(CreateCourseActivity.this,QRCode.class);
                intent.putExtra("qrNum", number);//参数：num、value
                CreateCourseActivity.this.startActivity(intent);
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
                    //ToastUtil.showMessage(getApplicationContext(), "创建成功");
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
