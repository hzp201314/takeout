package com.hzp.takeout.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hzp.takeout.R;
import com.hzp.takeout.global.MyApplication;
import com.hzp.takeout.model.dao.ReceiptAddresDao;
import com.hzp.takeout.model.dao.bean.ReceiptAddressBean;
import com.hzp.takeout.utils.SMSUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/15.
 */
public class AddAddressActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_delete)
    ImageButton ibDelete;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.rb_man)
    RadioButton rbMan;
    @InjectView(R.id.rb_women)
    RadioButton rbWomen;
    @InjectView(R.id.rg_sex)
    RadioGroup rgSex;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.ib_delete_phone)
    ImageButton ibDeletePhone;
    @InjectView(R.id.ib_add_phone_other)
    ImageButton ibAddPhoneOther;
    @InjectView(R.id.et_phone_other)
    EditText etPhoneOther;
    @InjectView(R.id.ib_delete_phone_other)
    ImageButton ibDeletePhoneOther;
    @InjectView(R.id.rl_phone_other)
    RelativeLayout rlPhoneOther;
    @InjectView(R.id.tv_receipt_address)
    TextView tvReceiptAddress;
    @InjectView(R.id.et_detail_address)
    EditText etDetailAddress;
    @InjectView(R.id.tv_label)
    TextView tvLabel;
    @InjectView(R.id.ib_select_label)
    ImageView ibSelectLabel;
    @InjectView(R.id.bt_ok)
    Button btOk;
    private String[] addressLabels;
    private int[] bgLabels;
    private ReceiptAddresDao receiptAddresDao;
    private ReceiptAddressBean receiptAddressBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_add_edit_receipt_address);
        ButterKnife.inject(this);
        receiptAddresDao = new ReceiptAddresDao(this);

        addressLabels = new String[]{ "家", "公司", "学校"};
        //家  橙色
        //公司 蓝色
        //学校   绿色
        bgLabels = new int[]{
                Color.parseColor("#fc7251"),//家  橙色
                Color.parseColor("#468ade"),//公司 蓝色
                Color.parseColor("#02c14b"),//学校   绿色
        };



        //给edtiText注册事件监听,让其文本变化的时候,显示x图片
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //当文本有变化后,如果文本内容不是空的,可以显示x号让,用户可以点击删除
                String phone = etPhone.getText().toString();
                if (!TextUtils.isEmpty(phone)){
                    //显示x,提示用户可以删除
                    ibDeletePhone.setVisibility(View.VISIBLE);
                }else{
                    ibDeletePhone.setVisibility(View.GONE);
                }
            }
        });

        etPhoneOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //当文本有变化后,如果文本内容不是空的,可以显示x号让,用户可以点击删除
                String phone = etPhoneOther.getText().toString();
                if (!TextUtils.isEmpty(phone)){
                    //显示x,提示用户可以删除
                    ibDeletePhoneOther.setVisibility(View.VISIBLE);
                }else{
                    ibDeletePhoneOther.setVisibility(View.GONE);
                }
            }
        });

        //给editText注册焦点变化的事件监听(有焦点+有文本内容的时候,x符号才会出现)
        MyOnFocusChangeListener myOnFocusChangeListener = new MyOnFocusChangeListener();
        etPhone.setOnFocusChangeListener(myOnFocusChangeListener);
        etPhoneOther.setOnFocusChangeListener(myOnFocusChangeListener);

        receiptAddressBean = (ReceiptAddressBean) getIntent().getSerializableExtra("address");
        if (receiptAddressBean!=null){
            //将对象中的数据,回显到页面中
            String name = receiptAddressBean.getName();
            String sex = receiptAddressBean.getSex();
            String phone = receiptAddressBean.getPhone();
            String phoneOther = receiptAddressBean.getPhoneOther();
            String receiptAddress = receiptAddressBean.getReceiptAddress();
            String detailAddress = receiptAddressBean.getDetailAddress();
            String label = receiptAddressBean.getLabel();

            etName.setText(name);
            etPhone.setText(phone);
            etPhoneOther.setText(phoneOther);
            tvReceiptAddress.setText(receiptAddress);
            etDetailAddress.setText(detailAddress);
            tvLabel.setText(label);
            int index = getIndex(label);
            tvLabel.setBackgroundColor(bgLabels[index]);

            if (sex.equals("先生")){
                //第一个单选项选中
//                rbMan.setChecked(true);
                rgSex.check(R.id.rb_man);
            }else{
                //第二个单选项选中
                rgSex.check(R.id.rb_women);
            }
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

    class MyOnFocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.et_phone){
                //v就是注册此焦点变化监听的控件
                //hasFocus 在回调此方法的时候,是否获取了焦点  true 有焦点  false没有焦点
                String phone = etPhone.getText().toString();
                if (!TextUtils.isEmpty(phone) && hasFocus){
                    ibDeletePhone.setVisibility(View.VISIBLE);
                }else{
                    ibDeletePhone.setVisibility(View.GONE);
                }
            }else if (v.getId() == R.id.et_phone_other){
                String phoneOther = etPhoneOther.getText().toString();
                if (!TextUtils.isEmpty(phoneOther) && hasFocus){
                    ibDeletePhoneOther.setVisibility(View.VISIBLE);
                }else{
                    ibDeletePhoneOther.setVisibility(View.GONE);
                }
            }

        }
    }
    @OnClick({R.id.ib_delete_phone,R.id.ib_add_phone_other,R.id.ib_delete_phone_other,R.id.ib_select_label
    ,R.id.bt_ok,R.id.ib_delete,R.id.tv_receipt_address})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ib_delete_phone:
                etPhone.setText("");
                break;
            case R.id.ib_delete_phone_other:
                etPhoneOther.setText("");
                break;
            case R.id.ib_add_phone_other:
                //备用电话的显示隐藏
                int visibility = rlPhoneOther.getVisibility();
                if (visibility == View.GONE){
                    rlPhoneOther.setVisibility(View.VISIBLE);
                }else{
                    rlPhoneOther.setVisibility(View.GONE);
                }
                break;
            case R.id.ib_select_label:
                showLableDialog();
                break;
            case R.id.bt_ok:
                if (checkData()){
                    if (receiptAddressBean!=null){
                        //更新
                        updateAddress();
                    }else{
                        //增加
                        addAddress();
                    }
                }
                break;
            case R.id.ib_delete:
                if (receiptAddressBean!=null){
                    receiptAddresDao.delete(receiptAddressBean);
                    finish();
                }
                break;
            case R.id.tv_receipt_address:
                Intent intent = new Intent(this, AddressLocationActivity.class);
                startActivityForResult(intent,100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 101 && data!=null){
            String title = data.getStringExtra("title");
            String snippet = data.getStringExtra("snippet");

            tvReceiptAddress.setText(title);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateAddress() {
        //此数据可以插入数据库
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String phoneOther = etPhoneOther.getText().toString();
        String receiptAddress = tvReceiptAddress.getText().toString();
        String detailAddress = etDetailAddress.getText().toString();
        String label = tvLabel.getText().toString();

        String sex = "";
        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_man){
            sex = "先生";
        }else{
            sex = "女士";
        }

        receiptAddressBean.setName(name);
        receiptAddressBean.setPhone(phone);
        receiptAddressBean.setPhoneOther(phoneOther);
        receiptAddressBean.setReceiptAddress(receiptAddress);
        receiptAddressBean.setDetailAddress(detailAddress);
        receiptAddressBean.setLabel(label);
        receiptAddressBean.setSex(sex);
        //数据库更新
        receiptAddresDao.update(receiptAddressBean);

        finish();
    }

    private void addAddress() {
        //此数据可以插入数据库
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String phoneOther = etPhoneOther.getText().toString();
        String receiptAddress = tvReceiptAddress.getText().toString();
        String detailAddress = etDetailAddress.getText().toString();
        String label = tvLabel.getText().toString();

        String sex = "";
        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_man){
            sex = "先生";
        }else{
            sex = "女士";
        }
        ReceiptAddressBean receiptAddressBean = new ReceiptAddressBean( MyApplication.userId, name, sex,
                phone, phoneOther, receiptAddress, detailAddress, label, 0);
        receiptAddresDao.insert(receiptAddressBean);
        //结束此界面,在列表中显示插入的数据
        finish();
    }

    private void showLableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择标签");
        builder.setItems(addressLabels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //which就是选中条目的索引值
                tvLabel.setText(addressLabels[which]);
                tvLabel.setBackgroundColor(bgLabels[which]);

            }
        });
        builder.show();
    }

    private boolean checkData() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!SMSUtil.isMobileNO(phone)) {
            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*String receiptAddress = tvReceiptAddress.getText().toString().trim();
        if (TextUtils.isEmpty(receiptAddress)) {
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        String address = etDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
        if (checkedRadioButtonId != R.id.rb_man && checkedRadioButtonId != R.id.rb_women) {
            //2个不相等，则说明没有选中任意一个
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }

        String tvLableString = tvLabel.getText().toString();
        if (TextUtils.isEmpty(tvLableString)) {
            Toast.makeText(this, "请输入标签信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
