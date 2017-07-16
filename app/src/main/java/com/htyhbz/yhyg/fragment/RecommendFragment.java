package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.shoppingcat.ShoppingCatActivity;
import com.htyhbz.yhyg.adapter.HomeProductAdapter;
import com.htyhbz.yhyg.service.LocationService;
import com.htyhbz.yhyg.utils.DensityUtil;
import com.htyhbz.yhyg.view.ClearEditText;
import com.htyhbz.yhyg.view.MyGridView;
import com.htyhbz.yhyg.view.MyScrollView;

import java.lang.reflect.Field;

/**
 * Created by zongshuo on 2017/7/5.
 */
public class RecommendFragment extends Fragment implements OnRefreshListener,MyScrollView.OnScrollListener,View.OnClickListener{
    private View currentView = null;
    private SwipeToLoadLayout swipeToLoadLayout;
    private MyScrollView scrollView;
    private TextView  tv2;
    private int topHeight, tv2Height;
    private ClearEditText searchET;
    private LocationService locationService;
    private RelativeLayout RL1,RL2;
    private TextView searchTV;
    private MainActivity mActivity;
    private MyGridView homeGV;
    private HomeProductAdapter adapter;
    private LinearLayout smallFireworksLL,comboClassLL,firecrackersLL,fireworksLL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_recommend, container, false);
        initView();
        getPositon();
        return currentView;
    }

    private void initView() {
        topHeight = getStatusBarHeight();

        smallFireworksLL= (LinearLayout) currentView.findViewById(R.id.smallFireworksLL);
        comboClassLL= (LinearLayout) currentView.findViewById(R.id.comboClassLL);
        firecrackersLL= (LinearLayout) currentView.findViewById(R.id.firecrackersLL);
        fireworksLL= (LinearLayout) currentView.findViewById(R.id.fireworksLL);
        smallFireworksLL.setOnClickListener(this);
        comboClassLL.setOnClickListener(this);
        firecrackersLL.setOnClickListener(this);
        fireworksLL.setOnClickListener(this);
        searchET = (ClearEditText) currentView.findViewById(R.id.searchET);
        tv2 = (TextView) currentView.findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getPositon();// 定位SDK
            }
        });
        RL1= (RelativeLayout) currentView.findViewById(R.id.RL1);
        RL2= (RelativeLayout) currentView.findViewById(R.id.RL2);
        tv2Height = DensityUtil.dip2px(getActivity(), 50);
        searchTV= (TextView) currentView.findViewById(R.id.searchTV);

        scrollView = (MyScrollView) currentView.findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);

        swipeToLoadLayout.setOnRefreshListener(this);

        scrollView.setOnScrollListener(this);
        //当布局的状态或者控件的可见性发生改变回调的接口
        currentView.findViewById(R.id.swipeToLoadLayout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //这一步很重要，使得上面的购买布局和下面的购买布局重合
                onScroll(scrollView.getScrollY());
            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);


        homeGV= (MyGridView) currentView.findViewById(R.id.homeGV);
        adapter=new HomeProductAdapter(mActivity,null);
        homeGV.setAdapter(adapter);
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        }, 100);

    }

    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {

        @Override
        public void onScrollChanged() {
            if (scrollView.getChildAt(0).getHeight() < scrollView.getScrollY() + scrollView.getHeight() && !ViewCompat.canScrollVertically(scrollView, 1)) {
                swipeToLoadLayout.setLoadingMore(true);
            }
        }
    };

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onScroll(int scrollY) {
        int v12ParentTop = Math.max(scrollY, RL1.getTop());
        RL2.layout(0, v12ParentTop, RL2.getWidth(), v12ParentTop + RL2.getHeight());
        getss();
    }

    private void getss() {

        int[] location = new int[2];
        currentView.findViewById(R.id.searchLL).getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        if (y < topHeight) {
            searchTV.setVisibility(View.VISIBLE);
        } else {
            searchTV.setVisibility(View.GONE);
        }

    }

    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }



    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    Toast.makeText(getActivity(),"gps定位成功",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    Toast.makeText(getActivity(),"网络定位成功",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    Toast.makeText(getActivity(),"离线定位成功，离线定位结果也是有效的",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(getActivity(),"服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(getActivity(),"网络不同导致定位失败，请检查网络是否通畅",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(getActivity(),"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机",Toast.LENGTH_SHORT).show();
                }
                logMsg(location.getAddrStr());
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };


    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (tv2 != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tv2.post(new Runnable() {
                            @Override
                            public void run() {
                                tv2.setText(s);
                            }
                        });

                    }
                }).start();
            }
            //LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity=(MainActivity)context;
    }

    private void getPositon(){
        // -----------location config ------------
        locationService = InitApp.initApp.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.smallFireworksLL:
                startActivity(new Intent(mActivity, ShoppingCatActivity.class));
                break;
            case R.id.comboClassLL:
                startActivity(new Intent(mActivity, ShoppingCatActivity.class));
                break;
            case R.id.firecrackersLL:
                startActivity(new Intent(mActivity, ShoppingCatActivity.class));
                break;
            case R.id.fireworksLL:
                startActivity(new Intent(mActivity, ShoppingCatActivity.class));
                break;
        }
    }
}