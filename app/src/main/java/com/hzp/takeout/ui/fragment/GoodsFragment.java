package com.hzp.takeout.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;


import com.hzp.takeout.R;
import com.hzp.takeout.presenter.GoodsPresenter;
import com.hzp.takeout.presenter.net.bean.GoodsInfo;
import com.hzp.takeout.presenter.net.bean.GoodsTypeInfo;
import com.hzp.takeout.presenter.net.bean.Seller;
import com.hzp.takeout.ui.adapter.GoodsAdapter;
import com.hzp.takeout.ui.adapter.GoodsTypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by HASEE on 2017/1/10.
 */
public class GoodsFragment extends BaseFragment {
    @InjectView(R.id.rv_goods_type)
    RecyclerView rvGoodsType;
    @InjectView(R.id.slhlv)
    StickyListHeadersListView slhlv;
    private Seller seller;
    public GoodsAdapter goodsAdapter;
    private GoodsTypeAdapter goodsTypeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        seller = (Seller) bundle.getSerializable("seller");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_goods, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //左侧分类的数据适配器
        goodsTypeAdapter = new GoodsTypeAdapter(getActivity(),this);
        rvGoodsType.setAdapter(goodsTypeAdapter);
        rvGoodsType.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //右侧列表的数据适配器

        goodsAdapter = new GoodsAdapter(getActivity());
        slhlv.setAdapter(goodsAdapter);

        slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
                List<GoodsTypeInfo> goodsTypeInfoList = goodsTypeAdapter.getData();

                if (goodsInfoList!=null && goodsInfoList.size()>0
                        && goodsTypeInfoList!=null && goodsTypeInfoList.size()>0){
                    //1.获取右侧列表滚动到的第一个条目的分类typeid,如何获取右侧ListView中的列表数据

                    GoodsInfo goodsInfo = goodsInfoList.get(firstVisibleItem);
                    //2.获取滚动到的第一个条目目的分类id
                    int typeId = goodsInfo.getTypeId();
                    //3.获取左侧选中条目的分类id
                    GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(goodsTypeAdapter.currentPosition);
                    int id = goodsTypeInfo.getId();
                    //在滚动的过程中,右侧列表滚动到的分类和左侧列表指向的分类不一致
                    if (typeId!=id){
                        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
                            if (typeId == goodsTypeInfoList.get(i).getId()){
                                //找到了分类id一致的条目,i就是左侧列表需要选中的索引位置
                                goodsTypeAdapter.currentPosition = i;
                                goodsTypeAdapter.notifyDataSetChanged();
                                //当recyclerView条目不可见的时候,会向上滚动
                                rvGoodsType.smoothScrollToPosition(i);
                                break;
                            }
                        }
                    }
                }
            }
        });

        //网络请求
        GoodsPresenter goodsPresenter = new GoodsPresenter(goodsTypeAdapter, goodsAdapter,seller);
        goodsPresenter.getBusinessData(seller.getId());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * @param typeId    左侧列表点中条目的分类id
     */
    public void setOnClickTypeId(int typeId) {
        //此分类id,需要管理右侧的列表滚动
        ArrayList<GoodsInfo> goodsList = goodsAdapter.getData();
        if (goodsList!=null && goodsList.size()>0){
            for (int i = 0; i < goodsList.size(); i++) {
                //如果左侧选中的分类id,在遍历过程中找到了右侧typeId一致条目,右侧的ListView就需要滚动到索引i的位置
                if (typeId == goodsList.get(i).getTypeId()){
                    slhlv.setSelection(i); //1 2 3 4 5 6 7 8
                    Log.i("","滚动到的位置 i = "+i);
                    break;
                }
            }
        }
    }

    /**
     * @return  返回左侧的数据适配器对象
     */
    public GoodsTypeAdapter getGoodsTypeAdapter() {
        return goodsTypeAdapter;
    }
}
