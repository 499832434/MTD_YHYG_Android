package com.htyhbz.yhyg.activity.enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.adapter.EnterpriseAdapter;
import com.htyhbz.yhyg.adapter.RecordAdapter;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.vo.Enterprise;
import com.htyhbz.yhyg.vo.WithDrawal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/17.
 */
public class EnterpriseMainActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private ListView enterpriseLV;
    private List<Enterprise> list=new ArrayList<Enterprise>();
    private EnterpriseAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private String flag="";
    private TextView titleTV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_main);
        initData();
        initView();
    }

    private void initData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            flag=bundle.getString("flag");
        }
    }
    private void initView(){
        titleTV= (TextView) findViewById(R.id.titleTextView);
        if("0".equals(flag)){
            titleTV.setText("批发企业");
        }else {
            titleTV.setText("厂家");
        }
        findViewById(R.id.unLoginTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        enterpriseLV= (ListView) findViewById(R.id.swipe_target);
        enterpriseLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(EnterpriseMainActivity.this,EnterpriseDetailActivity.class);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
        adapter=new EnterpriseAdapter(EnterpriseMainActivity.this,list);
        enterpriseLV.setAdapter(adapter);
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
