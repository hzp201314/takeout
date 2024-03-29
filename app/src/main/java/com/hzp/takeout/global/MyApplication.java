package com.hzp.takeout.global;

import android.app.Application;

import com.hzp.takeout.model.dao.DBHelper;
import com.hzp.takeout.model.dao.bean.UserInfo;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

/**
 * Created by HASEE on 2017/1/10.
 */
public class MyApplication extends Application {
    public static int statusBarHeight;
    public static int userId = -1;

    @Override
    public void onCreate() {
        super.onCreate();

        SMSSDK.initSDK(this, "1ab0922b18f6f", "c3f657c30a47c531d46d94730e7dc05c");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        //在此处添加根据isLogin为1数据库查询逻辑,查询已登录的用户
        Dao<UserInfo,Integer> dao = DBHelper.getInstance(this).getDao(UserInfo.class);
        QueryBuilder queryBuilder = dao.queryBuilder();
        try {
            List<UserInfo> userInfoList = queryBuilder.where().eq("isLogin", 1).query();
            if (userInfoList!=null && userInfoList.size()>0){
                UserInfo userInfo = userInfoList.get(0);
                userId = userInfo.get_id();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
