package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.enterprise.EnterpriseDetailActivity;
import com.htyhbz.yhyg.activity.video.VideoActivity;
import com.htyhbz.yhyg.adapter.EnterpriseAdapter;
import com.htyhbz.yhyg.adapter.EnterpriseProductAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.vo.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/6.
 */
public class EnProductFragment extends ErrorsFragment implements OnRefreshListener, OnLoadMoreListener {
    private View currentView = null;
    private EnterpriseDetailActivity mActivity;
    private ListView productLV;
    private EnterpriseProductAdapter adapter;
    private List<Product> productList=new ArrayList<Product>();
    private SwipeToLoadLayout swipeToLoadLayout;
    private int pageIndex=1;
    private final static String ENTERPRISEID = "enterpriseID";
    private final static String FLAG = "FLAG";
    private String enterpriseID,flag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_en_product, container, false);
        initView();
        return currentView;
    }

    public static EnProductFragment newInstance(String param1,String param2) {
        EnProductFragment fragment = new EnProductFragment();
        Bundle args = new Bundle();
        args.putString(ENTERPRISEID,param1);
        args.putString(FLAG,param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        enterpriseID = getArguments().getString(ENTERPRISEID);
        flag = getArguments().getString(FLAG);
        productLV= (ListView) currentView.findViewById(R.id.listView);
        adapter=new EnterpriseProductAdapter(mActivity,productList);
        productLV.setAdapter(adapter);
        productLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!TextUtils.isEmpty(productList.get(i).getProductVideoUrl())){
                    Intent intent=new Intent(mActivity, VideoActivity.class);
                    intent.putExtra("url",productList.get(i).getProductVideoUrl());
                    startActivity(intent);
                }else{
                    ((BaseActivity)mActivity).toast(mActivity,"暂无此视频");
                }
            }
        });

        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (EnterpriseDetailActivity)context;
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        request();
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        request();
    }

    /**
     * 网络请求
     */
    private void request() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        params.put("enterpriseID", enterpriseID);
        params.put("pageIndex", pageIndex+"");
        params.put("pageSize", InitApp.PAGESIZE);
        String url;
        if("2".equals(flag)){
            url=InitApp.getUrlByParameter(ApiConstants.ENTERPRISE_PRODUCT_LIST_API,params,true);
        }else{
            url=InitApp.getUrlByParameter(ApiConstants.FACTORY_PRODUCT_LIST_API,params,true);
        }

        Log.e("enterpriseProductsURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("enterpriseProductsRe", response);
                        try {
                            if(1==pageIndex){
                                productList.clear();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj= (JSONObject) infoArr.get(i);
                                    Product product=new Product();
                                    product.setproductName(obj.getString("productName"));
                                    product.setproductDetail(obj.getString("productDetail"));
                                    if(TextUtils.isEmpty(obj.getString("productPictureUrl"))||"null".equals(obj.getString("productPictureUrl"))){
                                        product.setproductPictureUrl("");
                                    }else{
                                        product.setproductPictureUrl(ApiConstants.BASE_URL+obj.getString("productPictureUrl"));
                                    }
                                    if(TextUtils.isEmpty(obj.getString("productVideoUrl"))||"null".equals(obj.getString("productVideoUrl"))){
                                        product.setProductVideoUrl("");
                                    }else{
                                        product.setProductVideoUrl(ApiConstants.BASE_URL + obj.getString("productVideoUrl"));
                                    }
                                    productList.add(product);
                                }
//                                if(productList.size()>0){
//                                    adapter.notifyDataSetChanged();
//                                }
                                if(productList.size()>0){
                                    productLV.setVisibility(View.VISIBLE);
                                    currentView.findViewById(R.id.layoutError).setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }else{
                                    productLV.setVisibility(View.GONE);
                                    currentView.findViewById(R.id.layoutError).setVisibility(View.VISIBLE);
                                    showErrorLayout(currentView.findViewById(R.id.layoutError), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            swipeToLoadLayout.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    swipeToLoadLayout.setRefreshing(true);
                                                }
                                            });
                                        }
                                    },4);
                                }
                            }else{
                                mActivity.toast(mActivity, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
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
