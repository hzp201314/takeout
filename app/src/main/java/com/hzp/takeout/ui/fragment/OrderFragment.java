package com.hzp.takeout.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hzp.takeout.R;
import com.hzp.takeout.global.MyApplication;
import com.hzp.takeout.presenter.OrderPresenter;
import com.hzp.takeout.ui.adapter.OrderListAdapter;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by HASEE on 2017/1/9.
 */
public class OrderFragment extends BaseFragment{
    @InjectView(R.id.rv_order_list)
    RecyclerView rvOrderList;
    @InjectView(R.id.srl_order)
    SwipeRefreshLayout srlOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_order, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        OrderListAdapter orderListAdapter = new OrderListAdapter(getActivity());
        rvOrderList.setAdapter(orderListAdapter);
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        OrderPresenter orderPresenter = new OrderPresenter(orderListAdapter);
        orderPresenter.getOrderData( MyApplication.userId);
        //订单列表的链接地址 http://localhost:8080/TakeoutServiceVersion2/order?userId=1
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
