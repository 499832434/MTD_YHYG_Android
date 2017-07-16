package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.adapter.OrderTypeAdapter;
import com.htyhbz.yhyg.vo.Dish;
import com.htyhbz.yhyg.vo.OrderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/6.
 */
public class OrderTypeFragment extends Fragment {
    private View currentView = null;
    private ListView orderTypeLV;
    private OrderTypeAdapter adapter;
    private List<OrderInfo>  orderInfoList=new ArrayList<OrderInfo>();
    private MainActivity mActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_order_type, container, false);
        initView();
        return currentView;
    }


    private void initView() {
        orderTypeLV= (ListView) currentView.findViewById(R.id.orderTypeLV);
        adapter=new OrderTypeAdapter(mActivity,null);
        orderTypeLV.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }
}
