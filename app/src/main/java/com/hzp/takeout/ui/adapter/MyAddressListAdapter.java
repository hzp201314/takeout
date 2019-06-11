package com.hzp.takeout.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.InputDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.hzp.takeout.R;
import com.hzp.takeout.model.dao.ReceiptAddresDao;
import com.hzp.takeout.model.dao.bean.ReceiptAddressBean;
import com.hzp.takeout.ui.activity.AddAddressActivity;
import com.hzp.takeout.ui.activity.AddressListActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/15.
 */
public class MyAddressListAdapter extends RecyclerView.Adapter {

    private int[] bgLabels;
    private String[] addressLabels;
    private Context ctx;
    private List<ReceiptAddressBean> data;
    private final ReceiptAddresDao receiptAddresDao;

    public MyAddressListAdapter(Context ctx, List<ReceiptAddressBean> receiptAddressBeanList) {
        this.ctx = ctx;
        this.data = receiptAddressBeanList;

        addressLabels = new String[]{"家", "公司", "学校"};
        //家  橙色
        //公司 蓝色
        //学校   绿色
        bgLabels = new int[]{
                Color.parseColor("#fc7251"),//家  橙色
                Color.parseColor("#468ade"),//公司 蓝色
                Color.parseColor("#02c14b"),//学校   绿色
        };

        receiptAddresDao = new ReceiptAddresDao(ctx);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_receipt_address, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReceiptAddressBean receiptAddressBean = data.get(position);
        ((ViewHolder) holder).tvName.setText(receiptAddressBean.getName());
        ((ViewHolder) holder).tvSex.setText(receiptAddressBean.getSex());

        if (!TextUtils.isEmpty(receiptAddressBean.getPhone())
                && !TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            ((ViewHolder) holder).tvPhone.setText(receiptAddressBean.getPhone() + "," + receiptAddressBean.getPhoneOther());
        }
        if (!TextUtils.isEmpty(receiptAddressBean.getPhone())
                && TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            ((ViewHolder) holder).tvPhone.setText(receiptAddressBean.getPhone());
        }
        ((ViewHolder) holder).tvAddress.setText(receiptAddressBean.getReceiptAddress() + receiptAddressBean.getDetailAddress());

        if (!TextUtils.isEmpty(receiptAddressBean.getLabel())) {
            ((ViewHolder) holder).tvLabel.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).tvLabel.setText(receiptAddressBean.getLabel());
            //设置tvLabel背景颜色,根据label中的字符串,获取索引值,根据索引值去指定背景颜色
            int index = getIndex(receiptAddressBean.getLabel());
            ((ViewHolder) holder).tvLabel.setBackgroundColor(bgLabels[index]);
        } else {
            ((ViewHolder) holder).tvLabel.setVisibility(View.GONE);
        }

        if (data.get(position).isSelect() == 1){
            //此条目被选中
            ((ViewHolder) holder).cb.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).cb.setChecked(true);
        }else{
            //此条目未被选中
            ((ViewHolder) holder).cb.setVisibility(View.GONE);
            ((ViewHolder) holder).cb.setChecked(false);
        }
        ((ViewHolder) holder).setPosition(position);

        ((ViewHolder) holder).ivEdit.setVisibility(View.VISIBLE);
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

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.cb)
        CheckBox cb;
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
        @InjectView(R.id.iv_edit)
        ImageView ivEdit;
        private int position;

        @OnClick({R.id.iv_edit})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.iv_edit:
                    ReceiptAddressBean receiptAddressBean = data.get(position);
                    Intent intent = new Intent(ctx, AddAddressActivity.class);
                    intent.putExtra("address",receiptAddressBean);
                    ctx.startActivity(intent);
                    break;
            }
        }

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < data.size(); i++) {
                        //点中view对象后,获取点中条目的在集合中的对象
                        ReceiptAddressBean receiptAddressBean = data.get(i);
                        if (i == position){
                            //更新data集合中的isSelect字段的值为1
                            receiptAddressBean.setSelect(1);
                        }else{
                            //更新data集合中的isSelect字段的值为1
                            receiptAddressBean.setSelect(0);
                        }
                        //更新数据库表中的isSelect字段的值为1
                        receiptAddresDao.update(receiptAddressBean);
                    }
                    notifyDataSetChanged();
                    //点击后需要结束此界面,将数据传递给前一个界面
                    Intent intent = new Intent();
                    intent.putExtra("receiptAddress",data.get(position));
                    ((AddressListActivity)ctx).setResult(101,intent);
                    ((AddressListActivity)ctx).finish();
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
