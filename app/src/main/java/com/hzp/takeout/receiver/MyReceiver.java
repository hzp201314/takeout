package com.hzp.takeout.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.hzp.takeout.observer.OrderObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by HASEE on 2017/1/16.
 */

public class MyReceiver extends BroadcastReceiver{
    private HashMap<String,String> hashMap = new HashMap<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("", "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // 在这里可以做些统计，或者做些其他工作
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.i("",json);
            //json的解析,和解析结果的展示
            try {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.has("orderId")){
                    String orderId = jsonObject.getString("orderId");
                    hashMap.put("orderId",orderId);
                }
                if (jsonObject.has("type")){
                    String type = jsonObject.getString("type");
                    hashMap.put("type",type);
                }
                if (jsonObject.has("lat")){
                    String lat = jsonObject.getString("lat");
                    hashMap.put("lat",lat);
                }
                if (jsonObject.has("lng")){
                    String lng = jsonObject.getString("lng");
                    hashMap.put("lng",lng);
                }

                OrderObserver.getIntance().ChangeUI(hashMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //观察者模式 jdk1.0
        //触发OrderObserver中集合循环遍历,调用update方法过程
    }
}
