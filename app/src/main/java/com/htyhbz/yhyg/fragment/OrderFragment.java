package com.htyhbz.yhyg.fragment;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.view.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/6.
 */
public class OrderFragment extends Fragment {
    private View currentView = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public List<OrderTypeFragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_order, container, false);
        initView();
        return currentView;
    }



    private void initView() {
        tabLayout = (TabLayout) currentView.findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        tabLayout.setTabTextColors(Color.parseColor("#2e2e2e"), Color.parseColor("#0398ff"));//设置tab上文字的颜色，第一个参数表示没有选中状态下的文字颜色，第二个参数表示选中后的文字颜色
        //tabLayout.setSelectedIndicatorColor(Color.parseColor("#0ddcff"));//设置tab选中的底部的指示条的颜色
        viewPager = (ViewPager) currentView.findViewById(R.id.paraViewPager);
        //viewPager.setOffscreenPageLimit(4);

//        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
//                R.drawable.layout_divider_vertical));


        fragments = new ArrayList<OrderTypeFragment>();
        fragments.add(OrderTypeFragment.newInstance(0));
        fragments.add(OrderTypeFragment.newInstance(1));
        fragments.add(OrderTypeFragment.newInstance(2));
        fragments.add(OrderTypeFragment.newInstance(3));

        //给viewPager设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "待支付";
                    case 1:
                        return "已支付";
                    case 2:
                        return "配送中";
                    case 3:
                        return "已完成";

                }
                return null;
            }

        });


        //然后让TabLayout和ViewPager关联，只需要一句话，简直也是没谁了.
        tabLayout.setupWithViewPager(viewPager);
    }

    public List<OrderTypeFragment> getFragments() {
        return fragments;
    }
}
