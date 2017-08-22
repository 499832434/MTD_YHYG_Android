package com.htyhbz.yhyg.activity.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.fragment.OrderQueryFragment;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/14.
 */
public class OrderQueryActivity extends BaseActivity{
    public final static int TYPE_TIME= 0;
    public final static int TYPE_AREA = 1;
    private ViewPager currentViewPager = null;
    private MyViewPaperAdapter adapter = null;
    private OrderQueryFragment timeFragment = null, areaFragment = null;
    private RadioButton timeRadioButton = null, areaRadioButton = null;
    private RadioGroup navRadioGroup = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query);
        initView();
    }


    private void initView() {
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        currentViewPager = (ViewPager) findViewById(R.id.currentViewPager);
        navRadioGroup = (RadioGroup) findViewById(R.id.navRadioGroup);

        timeRadioButton = (RadioButton) findViewById(R.id.timeRadioButton);
        areaRadioButton = (RadioButton) findViewById(R.id.areaRadioButton);

        timeFragment = OrderQueryFragment.newInstance(this,TYPE_TIME);
        areaFragment = OrderQueryFragment.newInstance(this,TYPE_AREA);
        adapter = new MyViewPaperAdapter(getSupportFragmentManager());
        adapter.addFragment(timeFragment);
        adapter.addFragment(areaFragment);
        currentViewPager.setAdapter(adapter);
        currentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        timeRadioButton.setChecked(true);
                        break;
                    case 1:
                        areaRadioButton.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.timeRadioButton:
                        currentViewPager.setCurrentItem(0);
                        break;
                    case R.id.areaRadioButton:
                        currentViewPager.setCurrentItem(1);
                        break;
                }
            }
        });
        timeRadioButton.setChecked(true);
    }
    

    public class MyViewPaperAdapter extends FragmentStatePagerAdapter {

        public final List<Fragment> mFragments = new ArrayList<Fragment>();

        public void addFragment(Fragment fr) {
            mFragments.add(fr);
        }

        public void removeFragment() {
            mFragments.clear();
        }

        public MyViewPaperAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
