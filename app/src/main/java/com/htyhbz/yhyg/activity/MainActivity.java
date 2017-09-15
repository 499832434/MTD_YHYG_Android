package com.htyhbz.yhyg.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.*;
import com.gyf.barlibrary.ImmersionBar;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.fragment.AccountFragment;
import com.htyhbz.yhyg.fragment.OrderFragment;
import com.htyhbz.yhyg.fragment.RecommendFragment;
import com.htyhbz.yhyg.view.CustomViewPager;
import com.htyhbz.yhyg.view.StatusBarUtil;

import java.util.Random;

/**
 * Created by zongshuo on 2017/7/3.
 */
public class MainActivity extends BaseActivity {

    public MyFragAdapter mAdapter;
    private FragmentManager mFragmentManager;
    public CustomViewPager masterViewPager;
    public  OrderFragment orderFragment;
    private AccountFragment accountFragment;
    private RecommendFragment recommendFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabViewPager();

    }


    private void initTabViewPager() {
        masterViewPager = (CustomViewPager) findViewById(R.id.masterViewPager);
        mFragmentManager = getSupportFragmentManager();
        mAdapter = new MyFragAdapter(mFragmentManager);
        masterViewPager.setOffscreenPageLimit(5);
        masterViewPager.setAdapter(mAdapter);


        findViewById(R.id.recommendRB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masterViewPager.setCurrentItem(0);
            }
        });
        findViewById(R.id.orderRB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterViewPager.setCurrentItem(1);
            }
        });

        findViewById(R.id.accountRB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterViewPager.setCurrentItem(2);
                accountFragment.getUserIntegral();
            }
        });
    }

    public class MyFragAdapter extends SmartFragmentStatePagerAdapter {

        public MyFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return recommendFragment=new RecommendFragment();
            } else if (position == 1) {
                orderFragment=new OrderFragment();
                return orderFragment;
            } else if (position == 2) {
                return accountFragment=new AccountFragment();
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        // Sparse array to keep track of registered fragments in memory
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Register the fragment when the item is instantiated
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        // Unregister when the item is inactive
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the fragment for the position (if instantiated)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    public OrderFragment getOrderFragment() {
        return orderFragment;
    }


}
