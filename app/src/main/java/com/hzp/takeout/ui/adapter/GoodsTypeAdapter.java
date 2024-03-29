package com.hzp.takeout.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hzp.takeout.R;
import com.hzp.takeout.presenter.net.bean.GoodsTypeInfo;
import com.hzp.takeout.ui.fragment.GoodsFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by HASEE on 2017/1/10.
 */
public class GoodsTypeAdapter extends RecyclerView.Adapter {
    private GoodsFragment goodsFragment;
    private Context ctx;
    private List<GoodsTypeInfo> data;
    //定义一个记录现在选中条目的索引值
    public int currentPosition = 0;

    public GoodsTypeAdapter(Context ctx, GoodsFragment goodsFragment) {
        this.ctx = ctx;
        this.goodsFragment = goodsFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_type, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).type.setText(data.get(position).getName());
        if (position == currentPosition) {
            //将此条目的背景变白,文字变红
            ((ViewHolder) holder).type.setTextColor(Color.RED);
            ((ViewHolder) holder).itemView.setBackgroundColor(Color.WHITE);
        } else {
            ((ViewHolder) holder).type.setTextColor(Color.BLACK);
            ((ViewHolder) holder).itemView.setBackgroundColor(Color.LTGRAY);
        }
        ((ViewHolder) holder).setPosition(position);

        //根据此分类是否有选中商品更新UI
        if (data.get(position).getCount() == 0){
            //此分类没有选中商品
            ((ViewHolder) holder).tvCount.setVisibility(View.GONE);
        }else{
            ((ViewHolder) holder).tvCount.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).tvCount.setText(data.get(position).getCount()+"");
        }
    }

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    public void setData(List<GoodsTypeInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<GoodsTypeInfo> getData() {
        return data;
    }

    /**
     * @param typeId    更新分类id
     * @param operate   更新方式(加一件,减一件)
     */
    public void refreshGoodsType(int typeId, int operate) {
        //在此处根据传递的typeId和操作类型更新data(左侧列表的数据集合)中的数据
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == typeId){
                //找到了需要更新分类数量的条目
                switch (operate){
                    case GoodsAdapter.ADD:
                        //对typeId一致的分类添加一件商品
                        int countAdd = data.get(i).getCount()+1;
                        data.get(i).setCount(countAdd);
                        break;
                    case GoodsAdapter.DELETE:
                        //对typeId一致的分类删除一件商品,商品的分类数量>0
                        if (data.get(i).getCount()>0){
                            int countDelete = data.get(i).getCount()-1;
                            data.get(i).setCount(countDelete);
                        }
                        break;
                }
                notifyDataSetChanged();
                break;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tvCount)
        TextView tvCount;
        @InjectView(R.id.type)
        TextView type;
        private int position;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    notifyDataSetChanged();

                    //点中某一个分类条目,需要让右侧的列表指向的商品也停留在此分类
                    //分类  id(分类id)
                    //商品  typeId就是分类的id

                    //获取左侧条目的分类id
                    int typeId = data.get(position).getId();
                    //传递给GoodsFragment
                    goodsFragment.setOnClickTypeId(typeId);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
