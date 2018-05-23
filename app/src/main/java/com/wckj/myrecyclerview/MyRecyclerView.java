package com.wckj.myrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by HQL
 * on 2018/5/21 0021.
 */

public class MyRecyclerView extends RecyclerView {

    private BaseRecycleAdapter myLoadMoreAdapter;
    OnloadListener mOnLoadListener = null;
    private boolean haveData = true;

    public MyRecyclerView(Context context) {
        this(context,null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnScrollListener(null);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        LoadMoreDataListener loadMoreListener = new LoadMoreDataListener(listener);
        super.setOnScrollListener(loadMoreListener);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!( adapter instanceof BaseRecycleAdapter)){
            return;
        }
        myLoadMoreAdapter = (BaseRecycleAdapter) adapter;
        super.setAdapter(myLoadMoreAdapter);

    }
    //自定义的接口方便回调
    public void setOnLoadListener(OnloadListener listener) {
        mOnLoadListener = listener;
    }

    //重写OnScrollListener
    class LoadMoreDataListener extends OnScrollListener{
        private OnScrollListener listener;
        private int lastVisibleItem;


        public LoadMoreDataListener(OnScrollListener listener) {
            this.listener = listener;
        }

        /**
         * 当目前显示是最后一个条目的时候
         * @param recyclerView
         * @param newState
         */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //当目前显示是最后一个条目的时候继续向下执行
            if (newState == RecyclerView.SCROLL_STATE_IDLE &&lastVisibleItem + 1 == getAdapter().getItemCount()){
                if (mOnLoadListener != null && haveData) {
                    //改变底部foot状态
                    myLoadMoreAdapter.changeMoreStatus(BaseRecycleAdapter.LOADING_MORE);
                    //通过接口回调回去
                    mOnLoadListener.OnLoadData();
                }
            }
            if (listener != null) {
                listener.onScrollStateChanged(recyclerView, newState);
            }
        }

        /**
         * 计算最后一个可见条目（lastVisibleItem）
         * 目前只支持 LinearLayoutManager和GridLayoutManager两种类型
         * @param recyclerView
         * @param dx
         * @param dy
         */
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager lm = (LinearLayoutManager) getLayoutManager();
                lastVisibleItem = lm.findLastVisibleItemPosition();
            } else if (getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager gm = (GridLayoutManager) getLayoutManager();
                 lastVisibleItem = gm.findLastVisibleItemPosition();
            }
            if (listener != null) {
                listener.onScrolled(recyclerView, dx, dy);
            }

        }

    }

    /**
     * 数据加载完成后调用
     * @param haveData
     */
    public void loadComplete(final boolean haveData) {
        //判断时候还有数据
        this.haveData = haveData;

        //改变底部foot状态
        if (!haveData) {
            myLoadMoreAdapter.changeMoreStatus(BaseRecycleAdapter.NO_MORE_DATA);
        } else {
            myLoadMoreAdapter.changeMoreStatus(BaseRecycleAdapter.PULL_UP_LOAD_MORE);
        }

    }

}
