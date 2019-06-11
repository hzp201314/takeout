package com.hzp.takeout.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.hzp.takeout.R;
import com.hzp.takeout.ui.fragment.HomeFragment;
import com.hzp.takeout.ui.fragment.MoreFragment;
import com.hzp.takeout.ui.fragment.OrderFragment;
import com.hzp.takeout.ui.fragment.UserFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.main_fragment_container)
    FrameLayout mainFragmentContainer;

    @InjectView(R.id.main_bottome_switcher_container)
    LinearLayout mainBottomeSwitcherContainer;

    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        ButterKnife.inject(this);

        initFragment();
        initClick();
        //默认选中第0个索引位置的界面
        //1.获取索引位置为0的view对象
        View childView = mainBottomeSwitcherContainer.getChildAt(0);
        //2.手动调用onclick方法
        onClick(childView);
    }

    private void initClick() {
        //通过获取mainBottomeSwitcherContainer容器中的每一个孩子节点,即选项卡那个FrameLayout,注册点击事件
        int childCount = mainBottomeSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            FrameLayout frameLayout = (FrameLayout) mainBottomeSwitcherContainer.getChildAt(i);
            frameLayout.setOnClickListener(this);
        }
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();

        fragmentList.add(new HomeFragment());
        fragmentList.add(new OrderFragment());
        fragmentList.add(new UserFragment());
        fragmentList.add(new MoreFragment());
    }

    @Override
    public void onClick(View v) {
        //v到底是那个孩子节点
        int index = mainBottomeSwitcherContainer.indexOfChild(v);
        //注册点击事件要做的业务
        //通过递归FrameLayout,循环便利其所有的孩子节点,将是否可用的状态,都设置为否false
        changeUI(index);
        changeFragment(index);
    }

    private void changeFragment(int index) {
        //获取fragment的管理者对象
        getSupportFragmentManager().beginTransaction().replace
                (R.id.main_fragment_container,fragmentList.get(index)).commit();
    }

    /**
     * @param index 指定此索引位置的控件,以及其孩子节点上的控件都不可用,不可用(蓝色)
     */
    private void changeUI(int index) {
        for (int i = 0; i < mainBottomeSwitcherContainer.getChildCount(); i++) {
            View view = mainBottomeSwitcherContainer.getChildAt(i);
            if (i == index){
                //循环遍历到的i,选中条目
                setEnable(view,false);
            }else{
                setEnable(view,true);
            }
        }
    }

    /**
     * @param view  将view设置为b(true 可用,false 不可用),将view中所有的孩子设置为b(true 可用,false 不可用)
     * @param b
     */
    private void setEnable(View view, boolean b) {
        //1.将view设置为不可用
        view.setEnabled(b);
        //2.处理view的孩子结点状态,ViewGroup容器,只有容器才有孩子结点
        if (view instanceof ViewGroup){
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = ((ViewGroup) view).getChildAt(i);
                setEnable(childView,b);
            }
        }
    }
}
