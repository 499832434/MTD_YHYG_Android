package com.htyhbz.yhyg.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
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
import com.htyhbz.yhyg.view.StatusBarCompat;
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
    private RelativeLayout RL1,RL2;
    private ImageView searchIV;
    private MainActivity mActivity;
    private MyGridView homeGV;
    private HomeProductAdapter adapter;
    private LinearLayout xiaoyanhuaLL,taocanLL,baozhuLL,yanhuaLL;
    private List<Product> productList=new ArrayList<Product>();
    private TextView yanhuaTV,baozhuTV,taocanTV,xiaoyanhuaTV,gprsTV;
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

        gprsTV= (TextView) currentView.findViewById(R.id.gprsTV);
        gprsTV.setText(mActivity.getUserInfo(5));
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
        currentView.findViewById(R.id.searchLL).setOnClickListener(this);
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







    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity=(MainActivity)context;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.xiaoyanhuaLL:
                if(TextUtils.isEmpty(xiaoyanhuaTV.getText().toString())){
                    return;
                }
                Intent intent1=new Intent(mActivity, ShoppingCatActivity.class);
                intent1.putExtra("categoryId",(Integer) xiaoyanhuaLL.getTag());
                startActivity(intent1);
                break;
            case R.id.taocanLL:
                if(TextUtils.isEmpty(taocanTV.getText().toString())){
                    return;
                }
                Intent intent2=new Intent(mActivity, ShoppingCatActivity.class);
                intent2.putExtra("categoryId",(Integer) taocanLL.getTag());
                startActivity(intent2);
                break;
            case R.id.yanhuaLL:
                if(TextUtils.isEmpty(yanhuaTV.getText().toString())){
                    return;
                }
                Intent intent3=new Intent(mActivity, ShoppingCatActivity.class);
                intent3.putExtra("categoryId", (Integer) yanhuaLL.getTag());
                startActivity(intent3);
                break;
            case R.id.baozhuLL:
                if(TextUtils.isEmpty(baozhuTV.getText().toString())){
                    return;
                }
                Intent intent4=new Intent(mActivity, ShoppingCatActivity.class);
                intent4.putExtra("categoryId", (Integer) baozhuLL.getTag());
                startActivity(intent4);
                break;
            case R.id.searchLL:
                startActivity(new Intent(mActivity, ProductSearchActivity.class));
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
        yanhuaLL.setVisibility(View.INVISIBLE);
        baozhuLL.setVisibility(View.INVISIBLE);
        taocanLL.setVisibility(View.INVISIBLE);
        xiaoyanhuaLL.setVisibility(View.INVISIBLE);
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
                                JSONArray array1=new JSONArray();
                                for(int i=0;i<array.length();i++){
                                    JSONObject obj=array.getJSONObject(i);
                                    if(1==obj.getInt("showInHomepage")){
                                        array1.put(obj);
                                    }
                                }
                                int num=4;
                                if(array1.length()<4){
                                    num=array1.length();
                                }
                                for(int i=0;i<num;i++){
                                    JSONObject obj=array1.getJSONObject(i);
                                    switch (i){
                                        case 0:
                                            yanhuaTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), yanhuaIV);
                                            yanhuaLL.setTag(obj.getInt("categoryId"));
                                            yanhuaLL.setVisibility(View.VISIBLE);
                                            break;
                                        case 1:
                                            baozhuTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), baozhuIV);
                                            baozhuLL.setTag(obj.getInt("categoryId"));
                                            baozhuLL.setVisibility(View.VISIBLE);
                                            break;
                                        case 2:
                                            taocanTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), taocanIV);
                                            taocanLL.setTag(obj.getInt("categoryId"));
                                            taocanLL.setVisibility(View.VISIBLE);
                                            break;
                                        case 3:
                                            xiaoyanhuaTV.setText(obj.getString("catalog"));
                                            mActivity.getNetWorkPicture(ApiConstants.BASE_URL + obj.getString("categoryImageUrl"), xiaoyanhuaIV);
                                            xiaoyanhuaLL.setTag(obj.getInt("categoryId"));
                                            xiaoyanhuaLL.setVisibility(View.VISIBLE);
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
        String url=InitApp.getUrlByParameter(ApiConstants.HOT_SALE_PRODUCTION_API, params, true);
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
                                    if(TextUtils.isEmpty(obj.getString("productPictureUrl"))||"null".equals(obj.getString("productPictureUrl"))){
                                        product.setproductPictureUrl("");
                                    }else{
                                        product.setproductPictureUrl(ApiConstants.BASE_URL+obj.getString("productPictureUrl"));
                                    }
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