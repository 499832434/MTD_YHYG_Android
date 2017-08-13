package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.search.ProductSearchActivity;
import com.htyhbz.yhyg.activity.shoppingcat.ShoppingCatActivity;
import com.htyhbz.yhyg.adapter.HomeProductAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.service.LocationService;
import com.htyhbz.yhyg.utils.DensityUtil;
import com.htyhbz.yhyg.view.ClearEditText;
import com.htyhbz.yhyg.view.MyGridView;
import com.htyhbz.yhyg.view.MyScrollView;
import com.htyhbz.yhyg.vo.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ImageView searchIV;
    private MainActivity mActivity;
    private MyGridView homeGV;
    private HomeProductAdapter adapter;
    private LinearLayout xiaoyanhuaLL,taocanLL,baozhuLL,yanhuaLL;
    private List<Product> productList=new ArrayList<Product>();
    private TextView yanhuaTV,baozhuTV,taocanTV,xiaoyanhuaTV;
    private ImageView yanhuaIV,baozhuIV,taocanIV,xiaoyanhuaIV;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_recommend, container, false);
        initView();
        //getPositon();

        return currentView;
    }

    private void initView() {
        topHeight = getStatusBarHeight();

        yanhuaTV= (TextView) currentView.findViewById(R.id.yanhuaTV);
        baozhuTV= (TextView) currentView.findViewById(R.id.baozhuTV);
        taocanTV= (TextView) currentView.findViewById(R.id.taocanTV);
        xiaoyanhuaTV= (TextView) currentView.findViewById(R.id.xiaoyanhuaTV);

        yanhuaIV= (ImageView) currentView.findViewById(R.id.yanhuaIV);
        baozhuIV= (ImageView) currentView.findViewById(R.id.baozhuIV);
        taocanIV= (ImageView) currentView.findViewById(R.id.taocanIV);
        xiaoyanhuaIV= (ImageView) currentView.findViewById(R.id.xiaoyanhuaIV);
        xiaoyanhuaLL= (LinearLayout) currentView.findViewById(R.id.xiaoyanhuaLL);
        taocanLL= (LinearLayout) currentView.findViewById(R.id.taocanLL);
        baozhuLL= (LinearLayout) currentView.findViewById(R.id.baozhuLL);
        yanhuaLL= (LinearLayout) currentView.findViewById(R.id.yanhuaLL);
        xiaoyanhuaLL.setOnClickListener(this);
        taocanLL.setOnClickListener(this);
        baozhuLL.setOnClickListener(this);
        yanhuaLL.setOnClickListener(this);
        searchET = (ClearEditText) currentView.findViewById(R.id.searchET);
        searchET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    startActivity(new Intent(mActivity, ProductSearchActivity.class));
                }
            }
        });
        searchET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, ProductSearchActivity.class));
            }
        });
        tv2 = (TextView) currentView.findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getPositon();// 定位SDK
            }
        });
        RL1= (RelativeLayout) currentView.findViewById(R.id.RL1);
        RL2= (RelativeLayout) currentView.findViewById(R.id.RL2);
//        tv2Height = DensityUtil.dip2px(getActivity(), 50);
        searchIV= (ImageView) currentView.findViewById(R.id.searchIV);
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, ProductSearchActivity.class));
            }
        });
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
        adapter=new HomeProductAdapter(mActivity,productList);
        homeGV.setAdapter(adapter);
        homeGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1=new Intent(mActivity, ShoppingCatActivity.class);
                intent1.putExtra("productId",productList.get(i).getproductId());
                intent1.putExtra("productType",productList.get(i).getproductType());
                startActivity(intent1);
            }
        });
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });

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
//        swipeToLoadLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeToLoadLayout.setRefreshing(false);
//            }
//        }, 3000);

        getCategory();
        getHotSaleProduction();
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
            searchIV.setVisibility(View.VISIBLE);
        } else {
            searchIV.setVisibility(View.GONE);
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

//    /***
//     * Stop location service
//     */
//    @Override
//    public void onStop() {
//        // TODO Auto-generated method stub
//        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
//        super.onStop();
//    }


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
            case R.id.xiaoyanhuaLL:
                Intent intent1=new Intent(mActivity, ShoppingCatActivity.class);
                intent1.putExtra("categoryId",(Integer) xiaoyanhuaLL.getTag());
                startActivity(intent1);
                break;
            case R.id.taocanLL:
                Intent intent2=new Intent(mActivity, ShoppingCatActivity.class);
                intent2.putExtra("categoryId",(Integer) taocanLL.getTag());
                startActivity(intent2);
                break;
            case R.id.yanhuaLL:
                Intent intent3=new Intent(mActivity, ShoppingCatActivity.class);
                intent3.putExtra("categoryId", (Integer) yanhuaLL.getTag());
                startActivity(intent3);
                break;
            case R.id.baozhuLL:
                Intent intent4=new Intent(mActivity, ShoppingCatActivity.class);
                intent4.putExtra("categoryId", (Integer) baozhuLL.getTag());
                startActivity(intent4);
                break;
        }
    }

    /**
     * 网络请求
     */
    private void getCategory() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        params.put("userID", mActivity.getUserInfo(0));
        params.put("areaID", mActivity.getUserInfo(1));
        String url=InitApp.getUrlByParameter(ApiConstants.GET_CATEGPRY_API,params,true);
        Log.e("getCategoryURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getCategoryRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray array=jsonObject.getJSONArray("info");
                                for(int i=0;i<4;i++){
                                    JSONObject obj=array.getJSONObject(i);
                                    switch (i){
                                        case 0:
                                            yanhuaTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), yanhuaIV);
                                            yanhuaLL.setTag(obj.getInt("categoryId"));
                                            break;
                                        case 1:
                                            baozhuTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), baozhuIV);
                                            baozhuLL.setTag(obj.getInt("categoryId"));
                                            break;
                                        case 2:
                                            taocanTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), taocanIV);
                                            taocanLL.setTag(obj.getInt("categoryId"));
                                            break;
                                        case 3:
                                            xiaoyanhuaTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), xiaoyanhuaIV);
                                            xiaoyanhuaLL.setTag(obj.getInt("categoryId"));
                                            break;
                                    }
                                }
                            }else{
                                mActivity.toast(mActivity, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) ;
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 网络请求
     */
    private void getHotSaleProduction() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        params.put("areaID", mActivity.getUserInfo(1));
        String url=InitApp.getUrlByParameter(ApiConstants.HOT_SALE_PRODUCTION_API,params,true);
        Log.e("HotSaleProductionURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HotSaleProductionRe", response);
                        try {
                            productList.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj= (JSONObject) infoArr.get(i);
                                    Product product=new Product();
                                    product.setproductId(obj.getInt("productId"));
                                    product.setproductName(obj.getString("productName"));
                                    product.setproductDetail(obj.getString("productDetail"));
                                    product.setproductPictureUrl(ApiConstants.BASE_URL+obj.getString("productPictureUrl"));
                                    product.setproductType(obj.getInt("productType"));
                                    productList.add(product);
                                }
                                if(productList.size()>0){
                                    adapter.notifyDataSetChanged();
                                }
                            }else{
                                mActivity.toast(mActivity, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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