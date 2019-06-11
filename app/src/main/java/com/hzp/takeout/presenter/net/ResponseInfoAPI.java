package com.hzp.takeout.presenter.net;


import com.hzp.takeout.presenter.net.bean.ResponseInfo;
import com.hzp.takeout.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HASEE on 2017/1/9.
 */
public interface ResponseInfoAPI {
    //指定请求方式
    //请求完整链接地址
    //请求方法,请求参数
    //请求的响应结果
    //因为整个项目中请求json的结果,结构都是
    /*{
        "code": "0",
        "data": "{……}"
    }
    * 创建ResponseInfo 存储服务器返回的json
    */

    //http://10.0.2.2:8080/TakeoutServiceVersion2/home?latitude=调用方法时传递的latitude&longitude=调用方法时传递的longitude
    @GET(Constant.HOME_URL)
    Call<ResponseInfo> getHomeInfo(@Query("latitude") String latitude, @Query("longitude") String longitude);

    @GET(Constant.BUSINESS)
    Call<ResponseInfo> getBusinessInfo(@Query("sellerId") long sellerId);

    @GET(Constant.LOGIN)
    Call<ResponseInfo> getLoginInfo(@Query("username") String username, @Query("password") String password,
                                    @Query("phone") String phone, @Query("type") int type);
    @GET(Constant.ORDER)
    Call<ResponseInfo> getOrderInfo(@Query("userId") int userId);
}
