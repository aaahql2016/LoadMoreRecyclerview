package com.wckj.myrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {


    private MyRecyclerView myRecyclerView;
    private ArrayList<String> data = new ArrayList<>();
    private int j = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        myRecyclerView = (MyRecyclerView) findViewById(R.id.my_recyclerview);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        MyRecyViewAdapter adapter = new MyRecyViewAdapter(this);
        adapter.setData(data);
        adapter.setOnItemClickListener(this);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.setOnLoadListener(new OnloadListener() {
            @Override
            public void OnLoadData() {
                if (j==5) {
                    //数据加载完成调用loadComplete，改变foot状态
                    myRecyclerView.loadComplete(false);
                    return;
                }
                initData();
            }
        });
    }

    private void initData() {
        j++;

        for (int i=0;i<20;i++){
            data.add("我是一个粉刷匠，代号"+i);
        }
    }

    @Override
    public void onItemClick(int position, View view) {

    }
}
