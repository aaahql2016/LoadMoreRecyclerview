package com.wckj.myrecyclerview;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HQL
 * on 2018/5/22 0022.
 */

public class MyRecyViewAdapter extends BaseRecycleAdapter<String> {
    public MyRecyViewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId(int type) {
        return R.layout.normal_item;
    }

    @Override
    public void onBindData(BaseHolder holder, int position, List<String> data) {

        TextView view = (TextView) holder.getView(R.id.tv_normal_item);
        view.setText(data.get(position));
    }

}
