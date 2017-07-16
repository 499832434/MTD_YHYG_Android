package com.htyhbz.yhyg.activity.order;

import android.os.Bundle;
import android.view.View;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.adapter.OrderTypeContentAdapter;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/13.
 */
public class OrderDetailActivity extends BaseActivity{
    private MyListView orderMLV;
    private OrderTypeContentAdapter adapter;
    private List<Dish> list =new ArrayList<Dish>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orderMLV= (MyListView) findViewById(R.id.orderMLV);
        adapter=new OrderTypeContentAdapter(OrderDetailActivity.this,list);
        orderMLV.setAdapter(adapter);
    }
}
