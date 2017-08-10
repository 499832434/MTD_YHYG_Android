package com.htyhbz.yhyg.activity.enterprise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.order.OrderQueryActivity;
import com.htyhbz.yhyg.fragment.EnAddressFragment;
import com.htyhbz.yhyg.fragment.EnIntroductFragment;
import com.htyhbz.yhyg.fragment.EnProductFragment;
import com.htyhbz.yhyg.fragment.OrderQueryFragment;
import com.htyhbz.yhyg.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/17.
 */
public class EnterpriseDetailActivity extends BaseActivity{
    private ViewPager currentViewPager = null;
    private MyViewPaperAdapter adapter = null;
    private RadioButton introductRB = null, addressRB = null,productRB=null;
    private RadioGroup navRadioGroup = null;
    private String flag="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_detail);
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
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        currentViewPager = (ViewPager) findViewById(R.id.currentViewPager);
        navRadioGroup = (RadioGroup) findViewById(R.id.navRadioGroup);


        introductRB = (RadioButton) findViewById(R.id.introductRB);
        addressRB = (RadioButton) findViewById(R.id.addressRB);
        productRB = (RadioButton) findViewById(R.id.productRB);


        adapter = new MyViewPaperAdapter(getSupportFragmentManager());

        if("0".equals(flag)){
            productRB.setVisibility(View.GONE);
            introductRB.setText("批发商介绍");
            addressRB.setText("批发商地址");
            adapter.addFragment(new EnIntroductFragment());
            adapter.addFragment(new EnAddressFragment());
        }else{
            adapter.addFragment(new EnIntroductFragment());
            adapter.addFragment(new EnProductFragment());
            adapter.addFragment(new EnAddressFragment());
        }
        currentViewPager.setAdapter(adapter);
        currentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        introductRB.setChecked(true);
                        break;
                    case 1:
                        productRB.setChecked(true);
                        break;
                    case 2:
                        addressRB.setChecked(true);
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
                    case R.id.introductRB:
                        currentViewPager.setCurrentItem(0);
                        break;
                    case R.id.productRB:
                        currentViewPager.setCurrentItem(1);
                        break;
                    case R.id.addressRB:
                        currentViewPager.setCurrentItem(2);
                        break;
                }
            }
        });
        introductRB.setChecked(true);
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
