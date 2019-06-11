package com.hzp.takeout.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzp.takeout.presenter.net.bean.Order;
import com.hzp.takeout.presenter.net.bean.ResponseInfo;
import com.hzp.takeout.ui.adapter.OrderListAdapter;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by HASEE on 2017/1/16.
 */

public class OrderPresenter extends BasePresenter {
    private OrderListAdapter orderListAdapter;

    public OrderPresenter(OrderListAdapter orderListAdapter) {
        this.orderListAdapter = orderListAdapter;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void parseJson(String json) {
        Gson gson = new Gson();
        ArrayList<Order> orderArrayList = gson.fromJson(json,new TypeToken<ArrayList<Order>>(){}.getType());

        orderListAdapter.setData(orderArrayList);
    }
    public void getOrderData(int userId){
        Call<ResponseInfo> orderInfo = responseInfoAPI.getOrderInfo(userId);
        orderInfo.enqueue(new CallBackAdapter());
    }
}
