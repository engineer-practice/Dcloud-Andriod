package com.example.daoyun09.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daoyun09.activities.UserInfoActivity;
import com.example.daoyun09.httpBean.CoursesListBean;
import com.example.daoyun09.R;
import com.example.daoyun09.activities.CourseInfoActivity;
import com.example.daoyun09.activities.LoginActivity;
import com.example.daoyun09.adapters.CoursesListAdapter;
import com.example.daoyun09.session.SessionKeeper;
import com.example.daoyun09.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private static final int WHAT_GET_DATA_SUCCESS = 1;
    private static final int WHAT_GET_DATA_FAILED = 2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.tabLayout1)
    TabLayout tb;
    @BindView(R.id.viewpager1)
    ViewPager vb;

    private String mParam1;
    private String mParam2;

    Unbinder unbinder;
    CoursesListAdapter mAdapter;

    //课程列表
    List<CoursesListBean> data = new ArrayList<>();
    List<Fragment> fragments;
    FragmentManager mFragmentManager;
    String[] title= {"我创建的","我加入的"};

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragments = new ArrayList<>();
        fragments.add(new MainPage1());
        fragments.add(new MainPage2());
        adaper myadapter = new adaper(getChildFragmentManager(),fragments);
        vb.setAdapter(myadapter);
        tb.setupWithViewPager(vb);
        return view;
    }

    //翻页适配器
    private class adaper extends FragmentPagerAdapter
    {
        private List<Fragment> list;
        public adaper(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
