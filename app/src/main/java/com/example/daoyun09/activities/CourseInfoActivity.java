package com.example.daoyun09.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.httpBean.StudentsListBean;
import com.example.daoyun09.R;
import com.example.daoyun09.adapters.StudentListAdapter;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.GPSUtils;
import com.example.daoyun09.utils.LogUtil;
import com.example.daoyun09.utils.ToastUtil;
import com.lxj.xpopup.XPopup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseInfoActivity extends AppCompatActivity {
    private static final int WHAT_REQUEST_FAILED = 1;
    private static final int WHAT_CHECK_SUCCESS = 2;
    private static final int WHAT_CHECK_FAILED = 3;
    private static final int WHAT_NETWORK_ERROR = 4;
    private static final int WHAT_GET_STUDENTS_SUCCESS = 5;
    private static final int WHAT_START_CHECK_SUCCESS = 6;
    private static final int WHAT_START_CHECK_FAILED = 7;
    private static final int WHAT_GET_CAN_CHECK_SUCCESS = 8;
    private static final int WHAT_GET_CAN_CHECK_FAILED = 9;
    private static final int REQUEST_TAKE_MEDIA = 102;
    private static final int WHAT_STOP_CHECK_SUCCESS = 10;
    private static final int WHAT_STOP_CHECK_FAILED = 11;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bigTime)
    TextView courseName;
    @BindView(R.id.teacher_name)
    TextView teacherName;
    @BindView(R.id.course_code)
    TextView courseCode;
    @BindView(R.id.stu_count)
    TextView stuCount;
    @BindView(R.id.check)
    Button check;
    @BindView(R.id.stop_check)
    Button setting_course;
    @BindView(R.id.atten_recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.place)
    TextView place;
    @BindView(R.id.refresh_students_list)
    SwipeRefreshLayout refresh_students_list;
    @BindView(R.id.isJoin)
    TextView isJoin;
    @BindView(R.id.Join_checkBox)
    CheckBox  Join_checkBox;


    String courseId;
    String userType;
    String teacher;

    private int stuSize;
    Button stop_check;
    List<StudentsListBean> students = new ArrayList<>();
    StudentListAdapter mAdapter;
    long duration_time = 365 * 24 * 60 * 1000 * 60;

    ProgressDialog progressDialog;
    JSONObject tmp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ButterKnife.bind(this);
        userType = getIntent().getStringExtra("type");  // 1表示教师 0表示学生
        courseId = getIntent().getStringExtra("course_id");
        teacher = getIntent().getStringExtra("teacher");
        initView();
        initData();
    }

    private void initView() {
        //setButtonState();
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); //设置返回按键
        mAdapter = new StudentListAdapter(students, this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(mAdapter);
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //refresh_students_list.setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        //refresh_students_list.setOnRefreshListener(this::initData);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在签到.....");
        progressDialog.setCancelable(false);

        if(userType.equals("1")) //教师
        {
            check.setText("发起签到");

        }
        else //学生
        {
            check.setText("点击签到");
            Join_checkBox.setClickable(false);
        }

    }

    @OnClick(R.id.Join_checkBox) //是否允许加入
    public void settingJoin()
    {
        if(Join_checkBox.isChecked()) //允许加入
        {
            HttpUtil.updateCourse(tmp.getString("className"),courseId,"",tmp.getIntValue("flag"),1, tmp.getString("name"),
                    "",tmp.getString("learnRequire"),tmp.getString("schoolCode"),tmp.getString("semester"),new BaseObserver<String>() {
                        //HttpUtil.createCourse(cd,new BaseObserver<String>() {
                        @Override
                        protected void onSuccess(String res) {
                            ToastUtil.showMessage(getApplicationContext(), "设置成功，允许加入班课", ToastUtil.LENGTH_LONG);
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
        else
        {
            HttpUtil.updateCourse(tmp.getString("className"),courseId,"",tmp.getIntValue("flag"),0, tmp.getString("name"),
                    "",tmp.getString("learnRequire"),tmp.getString("schoolCode"),tmp.getString("semester"),new BaseObserver<String>() {
                        //HttpUtil.createCourse(cd,new BaseObserver<String>() {
                        @Override
                        protected void onSuccess(String res) {
                            ToastUtil.showMessage(getApplicationContext(), "设置成功，禁止加入班课", ToastUtil.LENGTH_LONG);
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
    }


    private void initData() {
        students.clear();

        //获取课程学生
        HttpUtil.getStudentsList(courseId, new BaseObserver<JSONObject>() {
            @Override
            protected void onSuccess(JSONObject res) {
                stuSize = res.getIntValue("total");
                stuCount.setText("班课人数："+stuSize);
                JSONArray info = res.getJSONArray("dataList");
                for (int i = 0; i < stuSize; i++) {
                    JSONObject tmp = info.getJSONObject(i);
                    StudentsListBean stu = new StudentsListBean();
                    stu.setName(tmp.getString("name"));
                    stu.setStu_code(tmp.getString("sno"));
                    stu.setCheck_count(tmp.getString("exp"));
                    students.add(stu);
                }
                mHandler.sendEmptyMessage(WHAT_GET_STUDENTS_SUCCESS);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    LogUtil.e("get student list info", e.getMessage());
                    mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
                }
            }
        });

        //获取课程相关信息
        HttpUtil.getCourseInfo(courseId, new BaseObserver<JSONObject>() {
            @Override
            protected void onSuccess(JSONObject res) {
                String schoolcode;
                JSONArray info = res.getJSONArray("dataList");
                tmp = info.getJSONObject(0);
                courseName.setText("课程名称: "+tmp.getString("name"));
                courseCode.setText("课程代码: "+tmp.getString("code"));
                teacherName.setText("任课教师: "+teacher);
                place.setText("上课地点: " + tmp.getString("learnRequire"));
                if(tmp.getIntValue("isJoin") == 1)
                {
                    Join_checkBox.setChecked(true);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                check.setClickable(false);
                check.setVisibility(View.GONE);
                stop_check.setVisibility(View.GONE);
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    LogUtil.e("get course info", e.getMessage());
                    mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
                }
            }
        });


    }


    public void showSignInPopupMenu(Context context) {
        //
        new XPopup.Builder(context)
                //.isDarkTheme(false)
                .hasShadowBg(true)
                .asBottomList("签到类型", new String[]{"一键签到", "一分钟限时签到"},
                        (position, text) -> {
                                //TODO 这里接入转换接口
                                switch (position) {
                                    case 0:
                                        //发起一键签到
                                        beginSignRequest(0,0);

                                        break;
                                    case 1:
                                        //发起限时签到
                                        beginSignRequest(1,60);
                                    default:
                                        break;
                                }
                        }).show();
    }



    public void showSetting_teacher(Context context) {
        //
        new XPopup.Builder(context)
                //.isDarkTheme(false)
                .hasShadowBg(true)
                .asBottomList("设置", new String[]{"查看班课二维码", "查看签到记录" ,"解散班课"},
                        (position, text) -> {
                            //TODO 这里接入转换接口
                            switch (position) {
                                case 0:
                                    Intent intent=new Intent(CourseInfoActivity.this,QRCode.class);
                                    intent.putExtra("qrNum", courseId);//参数：num、value
                                    startActivity(intent);
                                    break;
                                case 1:
                                    Intent intent2 = new Intent(CourseInfoActivity.this, AttendActivity.class);
                                    intent2.putExtra("courseId", courseId);
                                    startActivity(intent2);
                                    break;
                                case 2:
                                    HttpUtil.Teacher_delCourse(courseId, new BaseObserver<String>() {
                                        @Override
                                        protected void onSuccess(String response) {
                                            JSONObject res =JSONObject.parseObject(response);
                                            String resCode = ((String)res.get("respCode")).trim();
                                            if(resCode.equals("1"))
                                            {
                                                Intent intent=new Intent(CourseInfoActivity.this,MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                ToastUtil.showMessage(CourseInfoActivity.this, "连接出错");
                                            }
                                        }

                                        @Override
                                        protected void onFailure(Throwable e, boolean isNetWorkError) {

                                        }
                                    });

                                default:
                                    break;
                            }
                        }).show();
    }

    public void showSetting_stu(Context context) {
        //
        new XPopup.Builder(context)
                //.isDarkTheme(false)
                .hasShadowBg(true)
                .asBottomList("设置", new String[]{"查看班课二维码", "退出班课"},
                        (position, text) -> {
                            //TODO 这里接入转换接口
                            switch (position) {
                                case 0:
                                    Intent intent=new Intent(CourseInfoActivity.this,QRCode.class);
                                    intent.putExtra("qrNum", courseId);//参数：num、value
                                    startActivity(intent);
                                    break;
                                case 1:
                                    HttpUtil.Stu_delCourse(courseId,SessionKeeper.getUserInfo(CourseInfoActivity.this).getPhone(), new BaseObserver<String>() {
                                        @Override
                                        protected void onSuccess(String response) {
                                            JSONObject res =JSONObject.parseObject(response);
                                            String resCode = ((String)res.get("respCode")).trim();
                                            if(resCode.equals("1"))
                                            {
                                                Intent intent=new Intent(getActivityContext(),MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                ToastUtil.showMessage(CourseInfoActivity.this, "连接出错");
                                            }
                                        }

                                        @Override
                                        protected void onFailure(Throwable e, boolean isNetWorkError) {

                                        }
                                    });

                                default:
                                    break;
                            }
                        }).show();
    }

    @OnClick(R.id.check)
    public void onCheckClicked() {
        if (userType.equals("1")) {
            showSignInPopupMenu(CourseInfoActivity.this);
        } else {
            Intent intent = new Intent(CourseInfoActivity.this, Check_Stu_Activity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        }
    }

    @OnClick(R.id.stop_check)
    public void setting()
    {
        if (userType.equals("1")) {
            showSetting_teacher(CourseInfoActivity.this);
        } else {
            showSetting_stu(CourseInfoActivity.this);
        }
    }

    /**
     * 发起签到
     */

    private void beginSignRequest(int attendance_type,int count) {
        //progressDialog.show();
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        Date turn = new Date(time);
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:m:ss");
        String start_time = fm.format(turn);
        //ToastUtil.showMessage(getActivityContext(), "开始时间："+start_time, Toast.LENGTH_SHORT);
        HttpUtil.check(attendance_type,courseId,count,"",GPSUtils.getInstance(this).getLatitude(),GPSUtils.getInstance(this).getLongitude(),
                start_time,SessionKeeper.getUserInfo(CourseInfoActivity.this).getPhone(), new BaseObserver<Integer>() {
            @Override
            protected void onSuccess(Integer res) {
                if (res != 0) {
                    if (attendance_type == 0) {
                        Intent intent = new Intent(CourseInfoActivity.this, CheckActivity.class);
                        intent.putExtra("type", "0");
                        intent.putExtra("courseId", courseId);
                        startActivity(intent);
                    } else
                    {
                        Intent intent2 = new Intent(CourseInfoActivity.this, CheckActivity.class);
                        intent2.putExtra("type", "1");
                        intent2.putExtra("courseId", courseId);
                        startActivity(intent2);
                    }
                } else {
                    ToastUtil.showMessage(getActivityContext(), "该课程还在签到中");
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    LogUtil.e("check", e.getMessage());
                    mHandler.sendEmptyMessage(WHAT_CHECK_FAILED);
                    ToastUtil.showMessage(getActivityContext(), "签到失败");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    Context getActivityContext() {
        return this;
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "请求失败，请重试");
                    break;
                case WHAT_CHECK_SUCCESS:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    check.setText("已签到");
                    check.setClickable(false);
                    break;
                case WHAT_CHECK_FAILED:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    break;
                case WHAT_NETWORK_ERROR:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    ToastUtil.showNetworkErrorMsg(getActivityContext());
                    break;
                case WHAT_GET_STUDENTS_SUCCESS:
                    mAdapter.notifyDataSetChanged();
                    break;
                case WHAT_START_CHECK_SUCCESS:
                    ToastUtil.showMessage(getActivityContext(), "开始签到成功");
                    break;
                case WHAT_START_CHECK_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "开始签到失败,请重试");
                    break;
                case WHAT_STOP_CHECK_SUCCESS:
                    ToastUtil.showMessage(getActivityContext(), "停止签到成功");
                    break;
                case WHAT_STOP_CHECK_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "停止签到失败,请重试");
                    break;
                case WHAT_GET_CAN_CHECK_SUCCESS:
                    int canCheck = msg.arg1;
                    if (userType.equals("3")) {
                        if (canCheck == 0) {
                            check.setText("无签到");
                            check.setClickable(false);
                        }
                    } else {
                        if (canCheck == 1) {
                            check.setText("签到中");
                            check.setClickable(false);
                            stop_check.setVisibility(View.VISIBLE);
                        } else {
                            check.setText("发起签到");
                            check.setClickable(true);
                            stop_check.setVisibility(View.GONE);
                        }
                    }
                    break;
                case WHAT_GET_CAN_CHECK_FAILED:
                    check.setText("无法操作");
                    check.setClickable(false);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_MEDIA:
                if (resultCode == RESULT_OK) {
                    //beginSignRequest();
                } else {
                    ToastUtil.showMessage(CourseInfoActivity.this, "拍照失败");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
