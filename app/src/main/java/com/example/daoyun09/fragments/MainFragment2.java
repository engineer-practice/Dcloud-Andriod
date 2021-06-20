package com.example.daoyun09.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.R;
import com.example.daoyun09.activities.CourseInfoActivity;
import com.example.daoyun09.activities.LoginActivity;
import com.example.daoyun09.adapters.CoursesListAdapter;
import com.example.daoyun09.http.BaseObserver;
import com.example.daoyun09.http.HttpUtil;
import com.example.daoyun09.httpBean.CoursesListBean;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link MainFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment2 extends Fragment {
    private static final int WHAT_GET_DATA_SUCCESS = 1;
    private static final int WHAT_GET_DATA_FAILED = 2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.refresh_course_list)
    SwipeRefreshLayout mRefreshCourseList;
    @BindView(R.id.recycle_main)
    RecyclerView mRecycleView;
    @BindView(R.id.no_class_notify)
    TextView noClassNotify;
    @BindView(R.id.add_xcourse)
    ImageView add_xcourse;

    private String mParam1;
    private String mParam2;
    private int size;

    Unbinder unbinder;
    CoursesListAdapter mAdapter;

    //课程列表
    List<CoursesListBean> data = new ArrayList<>();

    public static MainFragment2 newInstance(String param1, String param2) {
        MainFragment2 fragment = new MainFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    @OnClick(R.id.add_xcourse)
//    public void add_Course()
//    {
//        mFragmentManager.beginTransaction().replace(R.id.frame_xmain,mAddCourseFragment).commit();
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main2, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        if (data.size() == 0)
            getData();
        return view;
    }

    private void getData() {
        mRefreshCourseList.setRefreshing(true);
        data.clear();

        HttpUtil.hasJoinCourse("18760372609", new BaseObserver<JSONObject>() {
            @Override
            protected void onSuccess(JSONObject res) {
                size=res.getIntValue("total");
                JSONArray info = res.getJSONArray("dataList");
                for(int i=0;i<size;i++)
                {
                    JSONObject tmp = info.getJSONObject(i);
                    CoursesListBean c =new CoursesListBean();
                    c.setCourse_id(tmp.getString("code"));
                    c.setCourse_name(tmp.getString("name"));
                    c.setTeacher(tmp.getString("teacherName"));
                    c.setTime("2020-2");
                    data.add(c);
                }
                mHandler.sendEmptyMessage(WHAT_GET_DATA_SUCCESS);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(getActivity(), "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(getActivity(), e.getMessage(), ToastUtil.LENGTH_LONG);

            }
        });
    }

    private void initView() {
        mAdapter = new CoursesListAdapter(data, getActivity());
        mAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(getActivity(), CourseInfoActivity.class);
            intent.putExtra("course_id", String.valueOf(data.get(position).getCourse_id()));
            startActivity(intent);
        });
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(mAdapter);
        mRefreshCourseList.setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        mRefreshCourseList.setOnRefreshListener(this::getData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_GET_DATA_SUCCESS:
                    mAdapter.notifyDataSetChanged();
                    if (mRefreshCourseList != null && mRefreshCourseList.isRefreshing())
                        mRefreshCourseList.setRefreshing(false);
                    noClassNotify.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    break;
                case WHAT_GET_DATA_FAILED:
                    if (mRefreshCourseList != null && mRefreshCourseList.isRefreshing())
                        mRefreshCourseList.setRefreshing(false);
                    ToastUtil.showMessage(getActivity(), "如是QQ登录失败，请换普通登录", ToastUtil.LENGTH_LONG);
                    //网络失败的话返回登录界面
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
