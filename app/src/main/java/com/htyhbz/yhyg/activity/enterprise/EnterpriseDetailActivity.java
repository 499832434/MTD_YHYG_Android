package com.htyhbz.yhyg.activity.enterprise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.order.OrderQueryActivity;
import com.htyhbz.yhyg.fragment.EnAddressFragment;
import com.htyhbz.yhyg.fragment.EnIntroductFragment;
import com.htyhbz.yhyg.fragment.EnProductFragment;
import com.htyhbz.yhyg.fragment.OrderQueryFragment;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    private String title="";
    private int enterpriseID;
    private TextView nameTV,addressTV,phoneTV,personTV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_detail);
        initData();
        initView();

        getEnterpriseDetail();
    }


    private void initData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            flag=bundle.getString("flag");
            enterpriseID=bundle.getInt("enterpriseID");
            title=bundle.getString("title");
        }
    }


    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setTitleTextView(title);
        nameTV= (TextView) findViewById(R.id.nameTV);
        addressTV= (TextView) findViewById(R.id.addressTV);
        phoneTV= (TextView) findViewById(R.id.phoneTV);
        personTV= (TextView) findViewById(R.id.personTV);

        currentViewPager = (ViewPager) findViewById(R.id.currentViewPager);
        navRadioGroup = (RadioGroup) findViewById(R.id.navRadioGroup);


        introductRB = (RadioButton) findViewById(R.id.introductRB);
        addressRB = (RadioButton) findViewById(R.id.addressRB);
        productRB = (RadioButton) findViewById(R.id.productRB);


        adapter = new MyViewPaperAdapter(getSupportFragmentManager());

        if("2".equals(flag)){
            introductRB.setText("批发商介绍");
            productRB.setText("批发商产品");
            addressRB.setText("批发商地址");
        }

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


    /**
     * 网络请求
     */
    private void getEnterpriseDetail() {
        if (!NetworkUtils.isNetworkAvailable(EnterpriseDetailActivity.this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("enterpriseID", enterpriseID+"");
        String url;
        if("2".equals(flag)){
            url= InitApp.getUrlByParameter(ApiConstants.ENTERPRISE_DETAIL_API, params, true);
        }else{
            url= InitApp.getUrlByParameter(ApiConstants.FACTORY_DETAIL_API, params, true);
        }
        Log.e("getEnterpriseDetailURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getEnterpriseDetailRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONObject obj=jsonObject.getJSONObject("info");
                                nameTV.setText(obj.getString("enterpriseName"));
                                addressTV.setText("地址:"+obj.getString("enterpriseAddress"));
                                phoneTV.setText("电话:"+obj.getString("enterprisePhone"));
                                personTV.setText("联系人:"+obj.getString("enterpriseContactPerson"));

                                adapter.addFragment(EnIntroductFragment.newInstance(obj.getString("enterpriseIntroduction")));
                                adapter.addFragment(EnProductFragment.newInstance(obj.getString("enterpriseID"),flag));
                                adapter.addFragment(EnAddressFragment.newInstance(obj.getString("enterpriseLongitude"),obj.getString("enterpriseLatitude")));
                                currentViewPager.setAdapter(adapter);
                            }else{
                                toast(EnterpriseDetailActivity.this,jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        InitApp.initApp.addToRequestQueue(request);
    }
}
