package com.hzp.takeout.observer;

import java.util.Observable;

/**
 * 抽象主题角色的子类
 */
public class OrderObserver extends Observable{
   /* 订单状态  极光推送
     * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送 达 5已送达 6 取消的订单*/
    public static final String ORDERTYPE_UNPAYMENT = "10";
    public static final String ORDERTYPE_SUBMIT = "20";
    public static final String ORDERTYPE_RECEIVEORDER = "30";
    public static final String ORDERTYPE_DISTRIBUTION = "40";//如果接受到的推送消息的状态是配送中,显示地图,并且显示买卖双方
    // 骑手状态：接单、取餐、送餐
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE = "43";
    //骑手一旦接单--->要将经纬度发送给公司的服务器,公司服务器就会将经纬度推送给外卖的客户端,客户端代码根据经纬度在地图上显示骑手的图片
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL = "46";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL = "48";

    public static final String ORDERTYPE_SERVED = "50";
    public static final String ORDERTYPE_CANCELLEDORDER = "60";

    //提供此类获取对象方法
   private OrderObserver(){}
   public static OrderObserver orderObserver = new OrderObserver();
   public static OrderObserver getIntance(){
     return orderObserver;
   }

   public void ChangeUI(Object args){
    //将源码中的changed改成true,让其可以通过notifyObservers中的hasChange()方法
        setChanged();
        notifyObservers(args);
   }

}
