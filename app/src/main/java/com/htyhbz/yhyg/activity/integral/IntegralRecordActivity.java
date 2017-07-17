package com.htyhbz.yhyg.activity.integral;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.adapter.RecordAdapter;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.vo.WithDrawal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/17.
 */
public class IntegralRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private ListView recordLV;
    private List<WithDrawal> list=new ArrayList<WithDrawal>();
    private RecordAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_record);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        recordLV= (ListView) findViewById(R.id.swipe_target);
        adapter=new RecordAdapter(IntegralRecordActivity.this,list);
        recordLV.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.setRefreshing(false);
    }
}
