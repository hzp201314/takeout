package com.hzp.takeout.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.hzp.takeout.R;
import com.hzp.takeout.global.MyApplication;
import com.hzp.takeout.model.dao.ReceiptAddresDao;
import com.hzp.takeout.model.dao.bean.ReceiptAddressBean;
import com.hzp.takeout.ui.adapter.MyAddressListAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/15.
 */
public class AddressListActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rv_receipt_address)
    RecyclerView rvReceiptAddress;
    @InjectView(R.id.tv_add_address)
    TextView tvAddAddress;
    private ReceiptAddresDao receiptAddresDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_receipt_address);
        ButterKnife.inject(this);

        receiptAddresDao = new ReceiptAddresDao(this);
    }

    @Override
    protected void onResume() {
        //给recyclerView设置显示地址的列表数据
        //1.查询此用户的数据库列表,查询所有的地址
        List<ReceiptAddressBean> receiptAddressBeanList
                = receiptAddresDao.queryAllAddress( MyApplication.userId);
        MyAddressListAdapter myAddressListAdapter = new MyAddressListAdapter(this,receiptAddressBeanList);
        rvReceiptAddress.setAdapter(myAddressListAdapter);
        rvReceiptAddress.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        super.onResume();
    }

    @OnClick({R.id.tv_add_address})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_add_address:
                Intent intentAddressList = new Intent(this, AddAddressActivity.class);
                startActivity(intentAddressList);
                break;
        }
    }
}
