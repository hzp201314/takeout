package com.hzp.takeout.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hzp.takeout.R;
import com.hzp.takeout.global.MyApplication;
import com.hzp.takeout.model.dao.DBHelper;
import com.hzp.takeout.model.dao.bean.UserInfo;
import com.hzp.takeout.ui.activity.LoginActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/9.
 */
public class UserFragment extends BaseFragment {
    @InjectView(R.id.tv_user_setting)
    ImageView tvUserSetting;
    @InjectView(R.id.iv_user_notice)
    ImageView ivUserNotice;
    @InjectView(R.id.login)
    ImageView login;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.phone)
    TextView phone;
    @InjectView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @InjectView(R.id.iv_address)
    ImageView ivAddress;
    private Dao<UserInfo, Integer> dao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //1.DBHelper--->Dao
        DBHelper dbHelper = DBHelper.getInstance(getActivity());
        dao = dbHelper.getDao(UserInfo.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_user, null);
        ButterKnife.inject(this, view);
        return view;
    }

/*  此方法在再次回到Fragment的时候,不会触发,所以不能在其内部编写显示电话号码的逻辑,需要放置到Fragment中
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }*/

    @Override
    public void onResume() {
        if (MyApplication.userId!=-1) {
            try {
                login.setVisibility(View.GONE);
                llUserinfo.setVisibility(View.VISIBLE);
                UserInfo userInfo = dao.queryForId(MyApplication.userId);
                if (userInfo!=null){
                    username.setText(userInfo.getName());//从数据库中查询出来用户名称放在控件中显示
                    phone.setText(userInfo.getPhone());//从数据库中查询出来用户名称放在控件中显示
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            llUserinfo.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @OnClick({R.id.login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
