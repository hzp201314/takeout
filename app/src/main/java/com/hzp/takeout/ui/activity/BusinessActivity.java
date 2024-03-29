package com.hzp.takeout.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.hzp.takeout.R;
import com.hzp.takeout.presenter.BusinessPresenter;
import com.hzp.takeout.presenter.net.bean.GoodsInfo;
import com.hzp.takeout.presenter.net.bean.Seller;
import com.hzp.takeout.ui.adapter.BusinessFragmentPagerAdapter;
import com.hzp.takeout.ui.adapter.ShopCartAdapter;
import com.hzp.takeout.ui.fragment.GoodsFragment;
import com.hzp.takeout.utils.CountPriceFormater;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/10.
 */
public class BusinessActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_menu)
    ImageButton ibMenu;
    @InjectView(R.id.tabs)
    TabLayout tabs;
    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.bottomSheetLayout)
    BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.imgCart)
    ImageView imgCart;
    @InjectView(R.id.tvSelectNum)
    TextView tvSelectNum;
    @InjectView(R.id.tvCountPrice)
    TextView tvCountPrice;
    @InjectView(R.id.tvDeliveryFee)
    TextView tvDeliveryFee;
    @InjectView(R.id.tvSendPrice)
    TextView tvSendPrice;
    @InjectView(R.id.tvSubmit)
    TextView tvSubmit;
    @InjectView(R.id.bottom)
    LinearLayout bottom;
    @InjectView(R.id.fl_Container)
    FrameLayout flContainer;

    private String[] stringArry = new String[]{"商品","评价","商家"};
    private Seller seller;
    private BusinessFragmentPagerAdapter businessFragmentPagerAdapter;
    public BusinessPresenter businessPresenter;
    private View shopCartListView;
    private ShopCartAdapter shopCartAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_bussiness);
        businessPresenter = new BusinessPresenter(this);

        seller = (Seller) getIntent().getSerializableExtra("seller");

        ButterKnife.inject(this);
        //顶部的tabLayout+viewPager联动效果
        initTab();
        //填充viewPager
        initViewPager();
        //选项卡和viewpager绑定
        tabs.setupWithViewPager(vp);

        String deliveryFee = seller.getDeliveryFee();
        //将金额转换成float类型然后在转换成String类型,在前面加上¥符号
        String strDeliveryFee = CountPriceFormater.format(Float.parseFloat(deliveryFee));
        //更改运费
        tvDeliveryFee.setText("运费:"+strDeliveryFee);
        //更改起送价格
        String sendPrice = seller.getSendPrice();
        String strSendPrice = CountPriceFormater.format(Float.parseFloat(sendPrice));
        tvSendPrice.setText("起送价格:"+strSendPrice);
    }

    private void initViewPager() {
        //PagerAdapger----->viewpager中直接指定添加的view对象
        //FragmentPagerAdapter----->viewpager中添加的是fragment oncreateView方法中返回的view对象
        businessFragmentPagerAdapter = new BusinessFragmentPagerAdapter(getSupportFragmentManager(),stringArry,seller);
        vp.setAdapter(businessFragmentPagerAdapter);
    }

    private void initTab() {
        for (int i = 0; i < stringArry.length; i++) {
            tabs.addTab(tabs.newTab().setText(stringArry[i]));
        }
    }

    /**
     * @param imageView 添加在帧布局中的图片,添加位置已经通过setX和setY指定过了
     * @param width 添加控件宽度
     * @param height    添加控件的高度
     */
    public void addImageView(ImageView imageView, int width, int height) {
        flContainer.addView(imageView,width,height);
    }

    /**
     * @return  返回购物图片所在屏幕中的x,y的坐标
     */
    public int[] getShopCartLocation(){
        int[] shopCart = new int[2];
        imgCart.getLocationInWindow(shopCart);
        return shopCart;
    }

    /**
     * @param imageView 动画结束以后,移除图片
     */
    public void removeImageView(ImageView imageView) {
        if (imageView!=null){
            flContainer.removeView(imageView);
        }
    }

    //获取GoodsFragment方法
    public GoodsFragment getGoodsFragment(){
        ArrayList<Fragment> fragmentList = businessFragmentPagerAdapter.getFragmentList();
        if (fragmentList!=null && fragmentList.size()>0){
            GoodsFragment goodsFragment = (GoodsFragment) fragmentList.get(0);
            return goodsFragment;
        }
        return null;
    }

    /**
     * @param totalCount    购物车中数量
     * @param totalPrice    购物车中金额
     */
    public void updateShopCartCount(int totalCount, float totalPrice) {
        if (totalCount == 0){
            tvSelectNum.setVisibility(View.GONE);
            tvCountPrice.setText(CountPriceFormater.format(0.0f));
        }else{
            tvSelectNum.setVisibility(View.VISIBLE);
            tvSelectNum.setText(totalCount+"");
            tvCountPrice.setText(CountPriceFormater.format(totalPrice));
        }
        //判断购买的商品的总金额是否大于起送金额,大于隐藏起送价,显示去下单UI效果
        float sendPrice = Float.parseFloat(seller.getSendPrice());
        if (totalPrice>sendPrice){
            //显示去下单按钮,隐藏起送价格
            tvSubmit.setVisibility(View.VISIBLE);
            tvSendPrice.setVisibility(View.GONE);
        }else{
            tvSubmit.setVisibility(View.GONE);
            tvSendPrice.setVisibility(View.VISIBLE);
        }
    }

    //bottom是底部的灰色条目
    @OnClick({R.id.bottom,R.id.tvSubmit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bottom:
                //如果对话框的view是空的,则创建一个view
                if (shopCartListView == null){
                    shopCartListView = onCreateShopCartListView();
                }
                if (bottomSheetLayout.isSheetShowing()){
                    //如果对话框是显示的点击后就隐藏
                    bottomSheetLayout.dismissSheet();
                }else{
                    //如果对话框是隐藏的点击后就显示
                    bottomSheetLayout.showWithSheetView(shopCartListView);
                    //将购物车中的数据进行从新获取,并且告知数据适配器刷新
                    List<GoodsInfo> shopCartList = businessPresenter.getShopCartList();

                    shopCartAdapter.setData(shopCartList);
                }
                break;
            case R.id.tvSubmit:
                Intent intent = new Intent(BusinessActivity.this, ConfirmOrderActivity.class);
                //1.获取购买商品的集合,传递到下一个界面
                List<GoodsInfo> shopCartList = businessPresenter.getShopCartList();
                intent.putExtra("shopCartList", (Serializable) shopCartList);
                //2.运费传递
                intent.putExtra("deliveryFee",seller.getDeliveryFee());
                startActivity(intent);
                break;
        }
    }

    private View onCreateShopCartListView() {
        View view = View.inflate(this,R.layout.cart_list,null);
        RecyclerView rvCart = (RecyclerView) view.findViewById(R.id.rvCart);
        TextView tvClear = (TextView) view.findViewById(R.id.tvClear);
        //获取购物车中商品数量的集合
        List<GoodsInfo> shopCartList =  businessPresenter.getShopCartList();

        shopCartAdapter = new ShopCartAdapter(this,shopCartList);
        rvCart.setAdapter(shopCartAdapter);
        rvCart.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框,提示用户清空购物车
                AlertDialog.Builder builder = new AlertDialog.Builder(BusinessActivity.this);
                builder.setTitle("是否要清空购物车?");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //清空购物车,在businessPresenter类中提供清空购物车方法
                        businessPresenter.clearShopCart();
                    }
                });
                builder.show();
            }
        });
        return view;
    }

    public void dismissBottomSheetLayout() {
        //隐藏弹出购买商品的对话框
        if (bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }
    }
}
