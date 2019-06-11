package com.hzp.takeout.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hzp.takeout.R;
import com.hzp.takeout.presenter.net.bean.GoodsInfo;
import com.hzp.takeout.utils.CountPriceFormater;
import com.hzp.takeout.utils.OrderInfoUtil2_0;
import com.hzp.takeout.utils.PayResult;


import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/13.
 */
public class PayOnlineActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_residualTime)
    TextView tvResidualTime;
    @InjectView(R.id.tv_order_name)
    TextView tvOrderName;
    @InjectView(R.id.tv)
    TextView tv;
    @InjectView(R.id.tv_order_detail)
    TextView tvOrderDetail;
    @InjectView(R.id.iv_triangle)
    ImageView ivTriangle;
    @InjectView(R.id.ll_order_toggle)
    RelativeLayout llOrderToggle;
    @InjectView(R.id.tv_receipt_connect_info)
    TextView tvReceiptConnectInfo;
    @InjectView(R.id.tv_receipt_address_info)
    TextView tvReceiptAddressInfo;
    @InjectView(R.id.ll_goods)
    LinearLayout llGoods;
    @InjectView(R.id.ll_order_detail)
    LinearLayout llOrderDetail;
    @InjectView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @InjectView(R.id.iv_pay_alipay)
    ImageView ivPayAlipay;
    @InjectView(R.id.cb_pay_alipay)
    CheckBox cbPayAlipay;
    @InjectView(R.id.tv_selector_other_payment)
    TextView tvSelectorOtherPayment;
    @InjectView(R.id.ll_hint_info)
    LinearLayout llHintInfo;
    @InjectView(R.id.iv_pay_wechat)
    ImageView ivPayWechat;
    @InjectView(R.id.cb_pay_wechat)
    CheckBox cbPayWechat;
    @InjectView(R.id.iv_pay_qq)
    ImageView ivPayQq;
    @InjectView(R.id.cb_pay_qq)
    CheckBox cbPayQq;
    @InjectView(R.id.iv_pay_fenqile)
    ImageView ivPayFenqile;
    @InjectView(R.id.cb_pay_fenqile)
    CheckBox cbPayFenqile;
    @InjectView(R.id.ll_other_payment)
    LinearLayout llOtherPayment;
    @InjectView(R.id.bt_confirm_pay)
    Button btConfirmPay;
    private List<GoodsInfo> shopCartList;
    private String deliveryFee;
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016112003012990";
    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMPjSeEXkAqakBi2enQ1Chi+PDWC5hyKZs4tNwoeCrwJncoIerYSNW7IMYPmbiZ0bQwlZNx9EhYA3Bbm0mA23vUIJNm5EUfchr4AM4DFPqA40UZb6Mz5pGzyA4Rb+bTwO4VrAmf8iOFq26eBZWpr85jiVk/7B0dW/HGddxy8c/FZAgMBAAECgYBl7HmgeVYlbk7TzP7iQEbEoRdK8JUy/ICJftVImmETfh1v5gGTgt3yio/ZBakCsUmcLEjSwPEMKd5avDdygJp5EnD+D1Nuow6YxUQfExVji4ZocIvRivZj3QZo4b7Xt06oWPh0OwYJJ0UAVC8CdnyTxzfuWcoYWDAKFwEZIAxykQJBAPY6JSkbaE+xcTQU8kai/lxAmRe4qiF/RCvd/hZzd+IPLq/hJcUqNsWkT9a5NDOuMKg1vEuoHyko23OqajiFjI8CQQDLqah1SH/MRqHIB1dlj1xOirOFMxfkjDqORmRtZ8cdMzfVZkl8wCP0PLiAm0kRJm4N5W/3nnFX5QvqWo4NF+eXAkEAlx/Y7wIDY+ZktLKmgPRJahW74PNWeHjEPqhh6yWzzuvCm/B0Xi8qruPKnN/PSmj/ND7G8yic94Y8KyHNUCOnwQJBAJvNdqoChIHppuo3c4ymV59eTGeh5q1Y+ZLOFxX7Rj/4ZsZCMgXVl6vIp/z6zrLoC1lmJHnyJBNxjeQC0pkBqJECQC3mfJ+1XXfkv7Zj7zmuywuLNAB0VkCOsDpywmHWN9ZZ2z97wSjHBfz2xmkrM+T3gMXWHbj22cBIPeKLvuREI+Y=";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayOnlineActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        //发送一个请求给服务器,让服务器返回真实的支付结果

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayOnlineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        };
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_online_payment);
        ButterKnife.inject(this);
        //购买的具体商品的集合
        shopCartList = (List<GoodsInfo>) getIntent().getSerializableExtra("data");
        //运费字符串
        deliveryFee = getIntent().getStringExtra("deliveryFee");

        float totalPrice = 0.0f;
        //计算待支付的总金额 = 商品总金额+运费
        for (int i = 0; i < shopCartList.size(); i++) {
            GoodsInfo goodsInfo = shopCartList.get(i);
            totalPrice += goodsInfo.getCount()*goodsInfo.getNewPrice();
        }
        //购买商品的总金额
        totalPrice += Float.parseFloat(deliveryFee);
        tvPayMoney.setText( CountPriceFormater.format(totalPrice));
    }

    @OnClick({R.id.iv_triangle,R.id.bt_confirm_pay})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_triangle:
                //展示购买的具体商品
                showOrderDetail();
                break;
            case R.id.bt_confirm_pay:
                pay(view);
                break;
        }
    }

    private void pay(View view) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //加签
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        //订单信息
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayOnlineActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void showOrderDetail() {
        //判断llOrderDetail显示和隐藏的状态
        int visibility = llOrderDetail.getVisibility();
        if (View.VISIBLE == visibility){
            //点之前是可见的,点完后隐藏
            llOrderDetail.setVisibility(View.GONE);
        }else{
            llOrderDetail.setVisibility(View.VISIBLE);
            //llGoods线性布局,用于放置购买商品的列表
            showGoodsList();

        }
    }

    private void showGoodsList() {
        llGoods.removeAllViews();
        for (int i = 0; i < shopCartList.size(); i++) {
            GoodsInfo goodsInfo = shopCartList.get(i);
            //购买此件的商品数量
            int count = goodsInfo.getCount();
            //购买此件商品的总金额
            float goodsTotalPrice = goodsInfo.getCount() * goodsInfo.getNewPrice();
            View view = View.inflate(this,R.layout.item_confirm_order_goods,null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCount = (TextView)view.findViewById(R.id.tv_count);
            TextView tvPrice = (TextView)view.findViewById(R.id.tv_price);

            tvName.setText(goodsInfo.getName());
            tvCount.setText(count+"");
            tvPrice.setText(CountPriceFormater.format(goodsTotalPrice));

            llGoods.addView(view);
        }
    }
}
