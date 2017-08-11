package com.htyhbz.yhyg.activity.collect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
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
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.enterprise.EnterpriseMainActivity;
import com.htyhbz.yhyg.adapter.HomeProductAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.utils.PrefUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.vo.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zongshuo on 2017/7/17.
 */
public class CollectActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private GridView collectGV;
    private HomeProductAdapter adapter;
    private List<Product> productList=new ArrayList<Product>();
    private SwipeToLoadLayout swipeToLoadLayout;
    private int pageIndex=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        collectGV= (GridView) findViewById(R.id.swipe_target);
        adapter=new HomeProductAdapter(CollectActivity.this,productList);
        collectGV.setAdapter(adapter);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
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
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
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
        if (!NetworkUtils.isNetworkAvailable(CollectActivity.this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("userID", getUserInfo(0));
        params.put("pageIndex", pageIndex+"");
        params.put("pageSize", InitApp.PAGESIZE);
        String url=InitApp.getUrlByParameter(ApiConstants.MY_COLLECTION_LIST_API,params,true);
        Log.e("collectURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("collectRe", response);
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
                                toast(CollectActivity.this,jsonObject.getString("msg"));
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
