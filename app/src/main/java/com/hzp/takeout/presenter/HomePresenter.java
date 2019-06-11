package com.hzp.takeout.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.hzp.takeout.presenter.net.bean.HomeInfo;
import com.hzp.takeout.presenter.net.bean.ResponseInfo;
import com.hzp.takeout.ui.adapter.HomeRecyclerViewAdapter;


import retrofit2.Call;
/**
 * Created by HASEE on 2017/1/9.
 */
public class HomePresenter extends BasePresenter {
    private HomeRecyclerViewAdapter adapter;

    public HomePresenter(HomeRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void showError(String message) {
        //由开发者自己决定如何处理message
    }

    @Override
    protected void parseJson(String json) {
        //在此处解析json
        Log.i("",json);
        Gson gson = new Gson();
        //获取首页的数据
        HomeInfo homeInfo = gson.fromJson(json, HomeInfo.class);

        adapter.setData(homeInfo);

//        homeInfo.getBody();底部列表需要用到的数据
//        homeInfo.getHead();顶部轮播图需要用到的数据
    }

    //触发网络请求的方法
    public void getHomeData(String lat,String lon){
        Call<ResponseInfo> homeInfo = responseInfoAPI.getHomeInfo(lat, lon);
        //触发callback中的成功或者失败的回调方法
        homeInfo.enqueue(new CallBackAdapter());
    }
}
