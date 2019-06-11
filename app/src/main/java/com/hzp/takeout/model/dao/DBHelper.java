package com.hzp.takeout.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.hzp.takeout.model.dao.bean.ReceiptAddressBean;
import com.hzp.takeout.model.dao.bean.UserInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by HASEE on 2017/1/12.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    //创建一个hashmap存储多个Dao对象
    private HashMap<String,Dao> hashMap = new HashMap<>();
    private DBHelper(Context context) {
        super(context, "takeout.db", null, 1);
    }
    private static DBHelper dbHelper = null;
    //线程安全问题
    /*public static DBHelper getInstance(Context ctx){
        if (dbHelper == null){
            synchronized (DBHelper.class){
                if (dbHelper == null){
                    dbHelper = new DBHelper(ctx);
                }
            }
        }
        return dbHelper;
    }*/

    public synchronized static DBHelper getInstance(Context ctx){
        if (dbHelper == null){
            dbHelper = new DBHelper(ctx);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //原生创建表做法,编写sql语句
        //使用ormlite以后只需要调用以下方法
        //传递进来的javabean,以及由注解替代掉了原有的sql语句
        try {
            TableUtils.createTable(connectionSource, UserInfo.class);//userInfodao
            TableUtils.createTable(connectionSource, ReceiptAddressBean.class);//创建地址表  addressDao
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    //获取操作UserInfo表的dao对象,多个,项目中有多张表,每一个dao对象对应一张表进行操作
    public Dao getDao(Class clazz){
        Dao dao = null;
        String clazzName = clazz.getSimpleName();
        dao = hashMap.get(clazzName);
        if (dao==null){
            //获取clazz对应表的dao操作对象
            try {
                dao = super.getDao(clazz);
                hashMap.put(clazzName,dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }
    //销毁所有dao
    @Override
    public void close() {
        //遍历hashMap清空dao对象
        for (String key: hashMap.keySet()) {
            Dao dao = hashMap.get(key);
            dao = null;
        }
        super.close();
    }
}
