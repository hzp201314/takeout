package com.hzp.takeout.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hzp.takeout.R;
import com.hzp.takeout.global.MyApplication;
import com.hzp.takeout.model.dao.ReceiptAddresDao;
import com.hzp.takeout.model.dao.bean.ReceiptAddressBean;
import com.hzp.takeout.presenter.net.bean.GoodsInfo;
import com.hzp.takeout.utils.CountPriceFormater;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/13.
 */
public class ConfirmOrderActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_login)
    TextView tvLogin;
    @InjectView(R.id.iv_location)
    ImageView ivLocation;
    @InjectView(R.id.tv_hint_select_receipt_address)
    TextView tvHintSelectReceiptAddress;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_sex)
    TextView tvSex;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_label)
    TextView tvLabel;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.ll_receipt_address)
    LinearLayout llReceiptAddress;
    @InjectView(R.id.rl_location)
    RelativeLayout rlLocation;
    @InjectView(R.id.iv_arrow)
    ImageView ivArrow;
    @InjectView(R.id.iv_icon)
    ImageView ivIcon;
    @InjectView(R.id.tv_seller_name)
    TextView tvSellerName;
    @InjectView(R.id.ll_select_goods)
    LinearLayout llSelectGoods;
    @InjectView(R.id.tv_deliveryFee)
    TextView tvDeliveryFee;
    @InjectView(R.id.tv_CountPrice)
    TextView tvCountPrice;
    @InjectView(R.id.tvSubmit)
    TextView tvSubmit;
    private List<GoodsInfo> shopCartList;
    private String deliveryFee;
    private String[] addressLabels;
    private int[] bgLabels;
    private ReceiptAddressBean receiptAddressBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_confirm_order);
        ButterKnife.inject(this);
        //获取由购物车界面传递过来的购买商品集合
        shopCartList = (List<GoodsInfo>) getIntent().getSerializableExtra("shopCartList");
        //获取由购物车界面传递过来的商品运费
        deliveryFee = getIntent().getStringExtra("deliveryFee");
        String strDeliveryFee = CountPriceFormater.format(Float.parseFloat(deliveryFee));
        //设置运费
        tvDeliveryFee.setText(strDeliveryFee);

        //初始化购买的商品列表
        initShopCartList();

        float totalPrice = 0.0f;
        //计算待支付的总金额 = 商品总金额+运费
        for (int i = 0; i < shopCartList.size(); i++) {
            GoodsInfo goodsInfo = shopCartList.get(i);
            totalPrice += goodsInfo.getCount()*goodsInfo.getNewPrice();
        }

        totalPrice += Float.parseFloat(deliveryFee);
        tvCountPrice.setText("待支付:"+CountPriceFormater.format(totalPrice));

        addressLabels = new String[]{"家", "公司", "学校"};
        //家  橙色
        //公司 蓝色
        //学校   绿色
        bgLabels = new int[]{
                Color.parseColor("#fc7251"),//家  橙色
                Color.parseColor("#468ade"),//公司 蓝色
                Color.parseColor("#02c14b"),//学校   绿色
        };
    }

    @Override
    protected void onResume() {
        ReceiptAddresDao receiptAddresDao = new ReceiptAddresDao(this);
        //查询数据库对应当前登录用户,已选中的默认地址
        List<ReceiptAddressBean> receiptAddressBeanList = receiptAddresDao.querySelectAddress( MyApplication.userId);
        if (receiptAddressBeanList!=null && receiptAddressBeanList.size()>0){
            receiptAddressBean = receiptAddressBeanList.get(0);
            showReceiptAddress(receiptAddressBean);
        }
        super.onResume();
    }

    @OnClick({R.id.tvSubmit,R.id.rl_location})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvSubmit:
                Intent intent = new Intent(this, PayOnlineActivity.class);
                intent.putExtra("data", (Serializable) shopCartList);
                intent.putExtra("deliveryFee", deliveryFee);
                startActivity(intent);
                break;
            case R.id.rl_location:
                Intent intentAddressList = new Intent(this, AddressListActivity.class);
                startActivityForResult(intentAddressList,100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 101 && data!=null){
            ReceiptAddressBean receiptAddressBean = (ReceiptAddressBean) data.getSerializableExtra("receiptAddress");
            showReceiptAddress(receiptAddressBean);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showReceiptAddress(ReceiptAddressBean receiptAddressBean) {

        tvName.setText(receiptAddressBean.getName());
        tvSex.setText(receiptAddressBean.getSex());

        if (!TextUtils.isEmpty(receiptAddressBean.getPhone())
                && !TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            tvPhone.setText(receiptAddressBean.getPhone() + "," + receiptAddressBean.getPhoneOther());
        }
        if (!TextUtils.isEmpty(receiptAddressBean.getPhone())
                && TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            tvPhone.setText(receiptAddressBean.getPhone());
        }
        tvAddress.setText(receiptAddressBean.getReceiptAddress() + receiptAddressBean.getDetailAddress());

        if (!TextUtils.isEmpty(receiptAddressBean.getLabel())) {
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.setText(receiptAddressBean.getLabel());
            //设置tvLabel背景颜色,根据label中的字符串,获取索引值,根据索引值去指定背景颜色
            int index = getIndex(receiptAddressBean.getLabel());
            tvLabel.setBackgroundColor(bgLabels[index]);
        } else {
            tvLabel.setVisibility(View.GONE);
        }
    }

    private int getIndex(String label) {
        int index = 0;
        for (int i = 0; i < addressLabels.length; i++) {
            if (label.equals(addressLabels[i])){
                index = i;
                return index;
            }
        }
        return 0;
    }

    private void initShopCartList() {
        llSelectGoods.removeAllViews();
        for (int i = 0; i < shopCartList.size(); i++) {
            View view = View.inflate(this,R.layout.item_confirm_order_goods,null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCount = (TextView)view.findViewById(R.id.tv_count);
            TextView tvPrice = (TextView)view.findViewById(R.id.tv_price);

            GoodsInfo goodsInfo = shopCartList.get(i);
            if (goodsInfo.getCount()>0){
                //购买的此商品的数量
                int count = goodsInfo.getCount();
                //购买此商品的金额
                float totalPrice = goodsInfo.getCount() * goodsInfo.getNewPrice();

                tvName.setText(goodsInfo.getName());
                tvCount.setText(count+"");
                tvPrice.setText(CountPriceFormater.format(totalPrice));
            }

            llSelectGoods.addView(view);
        }
    }
}
