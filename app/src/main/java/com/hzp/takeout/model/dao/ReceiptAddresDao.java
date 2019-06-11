package com.hzp.takeout.model.dao;

import android.content.Context;


import com.hzp.takeout.model.dao.bean.ReceiptAddressBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by HASEE on 2017/1/15.
 */

public class ReceiptAddresDao {
    private Dao<ReceiptAddressBean,Integer> dao;
    public ReceiptAddresDao(Context ctx){
        if (dao == null){
            //1.获取操作ReceiptAddressBean的dao对象
            dao = DBHelper.getInstance(ctx).getDao(ReceiptAddressBean.class);
        }
    }
    //插入一个地址方法
    public void insert(ReceiptAddressBean receiptAddressBean){
        try {
            dao.create(receiptAddressBean);//insert into
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //删除一个地址方法
    public void delete(ReceiptAddressBean receiptAddressBean){
        try {
            dao.delete(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //修改一个地址方法
    public void update(ReceiptAddressBean receiptAddressBean){
        try {
            dao.update(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //查询所有地址的方法
    public List<ReceiptAddressBean> queryAllAddress(int userId){
        //select * from t_receiptaddress where uid = userId;
        try {
            QueryBuilder<ReceiptAddressBean, Integer> queryBuilder = dao.queryBuilder();
            List<ReceiptAddressBean> userAddressList = queryBuilder.where().eq("uid", userId).query();
            return userAddressList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //查询选中默认送货地址方法
    public List<ReceiptAddressBean> querySelectAddress(int userId){
        //select * from t_receiptaddress where isSelect = 1 and uid = 10001;
        try {
            QueryBuilder<ReceiptAddressBean, Integer> queryBuilder = dao.queryBuilder();
            List<ReceiptAddressBean> isSelectList = queryBuilder.where().eq("isSelect", 1).and().eq("uid",userId).query();
            return isSelectList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
