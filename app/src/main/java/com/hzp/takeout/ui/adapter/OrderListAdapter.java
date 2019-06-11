package com.hzp.takeout.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hzp.takeout.R;
import com.hzp.takeout.observer.OrderObserver;
import com.hzp.takeout.presenter.net.bean.Order;
import com.hzp.takeout.ui.activity.OrderDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by HASEE on 2017/1/16.
 */
public class OrderListAdapter extends RecyclerView.Adapter  implements Observer {
    private Context ctx;
    private ArrayList<Order> data;

    public OrderListAdapter(Context ctx) {
        this.ctx = ctx;
        //注册观察者对象
        OrderObserver.getIntance().addObserver(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_order_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).tvOrderItemSellerName.setText(data.get(position).getSeller().getName());
        String state = getIndex(data.get(position).getType());
        ((ViewHolder) holder).tvOrderItemType.setText(state);

        ((ViewHolder) holder).setPosition(position);
    }

    /* 订单状态
    * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送 达 5已送达 6 取消的订单*/

    private String getIndex(String type) {
        String curretState ="";
        switch (type){
            case OrderObserver.ORDERTYPE_UNPAYMENT:
                curretState = "未支付";
                break;
            case OrderObserver.ORDERTYPE_SUBMIT:
                curretState = "已提交订单";
                break;
            case OrderObserver.ORDERTYPE_RECEIVEORDER:
                curretState = "商家接单";
                break;
            case OrderObserver.ORDERTYPE_DISTRIBUTION:
                curretState = "配送中";
                break;
            case OrderObserver.ORDERTYPE_SERVED:
                curretState = "已送达";
                break;
            case OrderObserver.ORDERTYPE_CANCELLEDORDER:
                curretState = "取消的订单";
                break;
        }
        return curretState;
    }

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg) {
        int position = -1;
        //arg---->就是MyReceiver中获取的hashMap集合(其中包含了  orderId和type)
        HashMap<String, String> hashMap = (HashMap<String, String>) arg;
        String oId = hashMap.get("orderId");
        String type = hashMap.get("type");
        for (int i = 0; i < data.size(); i++) {
            Order order = data.get(i);
            if (order.getId().equals(oId)){
                order.setType(type);
                position = i;
                break;
            }
        }
        notifyItemChanged(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.iv_order_item_seller_logo)
        ImageView ivOrderItemSellerLogo;
        @InjectView(R.id.tv_order_item_seller_name)
        TextView tvOrderItemSellerName;
        @InjectView(R.id.tv_order_item_type)
        TextView tvOrderItemType;
        @InjectView(R.id.tv_order_item_time)
        TextView tvOrderItemTime;
        @InjectView(R.id.tv_order_item_foods)
        TextView tvOrderItemFoods;
        @InjectView(R.id.tv_order_item_money)
        TextView tvOrderItemMoney;
        @InjectView(R.id.tv_order_item_multi_function)
        TextView tvOrderItemMultiFunction;
         private int position;

         ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, OrderDetailActivity.class);
                    Order order = data.get(position);
                    intent.putExtra("orderId",order.getId());
                    intent.putExtra("type",order.getType());
                    ctx.startActivity(intent);
                }
            });
        }

         public void setPosition(int position) {
             this.position = position;
         }
     }
}
