package com.wckj.myrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HQL
 * on 2018/5/22 0022.
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseHolder> {

    public List<T> mDatas;

    private final Context mContext;

    private static final int TYPE_ITEM = 0;       //普通Item View
    private static final int TYPE_FOOTER = 1;    //底部FootView

    public static final int PULL_UP_LOAD_MORE = 0;  //上拉加载
    public static final int LOADING_MORE = 1;      //正在加载中
    public static final int NO_MORE_DATA = 2;     //加载完成没有数据

    private int load_more_status = 0;               //上拉加载状态默认0
    private OnItemClickListener onItemClickListener;

    public BaseRecycleAdapter(Context context) {
        mContext = context;
    }

    /**
     * 设置数据源 泛型T代表list的泛型
     * @param datas
     */
    public void setData(List<T> datas) {
        mDatas = datas;
    }

    /**
     * item点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener  onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 根据不同的viewType创建不同的holder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder = null;
        View view = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(mContext).inflate(getLayoutId(viewType), parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.pull_load_more, parent, false);
        }
        holder = new BaseHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(BaseHolder holder, final int position) {

        //底部Item 根据传入的状态更改布局(load_more_status)

        if (holder.getItemViewType() == BaseRecycleAdapter.TYPE_FOOTER) {
            ProgressBar progressBar = (ProgressBar) holder.getView(R.id.progressBar);
            TextView tv_progress_text = (TextView) holder.getView(R.id.tv_progress_text);
            switch (load_more_status){
                case LOADING_MORE:
                    tv_progress_text.setText("正在加载数据");
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case PULL_UP_LOAD_MORE:
                    if (getItemCount()<10){
                        progressBar.setVisibility(View.INVISIBLE);
                        tv_progress_text.setText("");
                    }else {
                        tv_progress_text.setText("上拉加载更多");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    break;
                case NO_MORE_DATA:
                    tv_progress_text.setText("没有更多数据了");
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        //普通Item 绑定数据
        if (holder.getItemViewType() == BaseRecycleAdapter.TYPE_ITEM) {
            onBindData(holder, position, mDatas);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(position,v);
                    }
                }
            });
        }
    }

    /**
     * 获取item数量
     * @return
     */
    @Override
    public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size() + 1;
    }

    /**
     * 获取item类型（普通item 、底部item）
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mDatas == null)
            return TYPE_ITEM;
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * 根据传入status改变foot状态并刷新数据
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    /**
     * 根据type 返回不同的布局
     *
     * @param type
     * @return
     */
    public abstract int getLayoutId(int type);

    /**
     * 绑定数据（做成抽象 在子类实现）
     * @param holder
     * @param position
     * @param mDataList
     */
    public abstract void onBindData(BaseHolder holder, int position, List<T> mDataList);

    public static class BaseHolder extends RecyclerView.ViewHolder {
        private Map<Integer, View> mViewMap;
        public View mView;

        public BaseHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mViewMap = new HashMap<>();
        }

        public View getView(int id) {
            View view = mViewMap.get(id);
            if (view == null) {
                view = mView.findViewById(id);
                mViewMap.put(id, view);
            }
            return view;
        }
    }


}
