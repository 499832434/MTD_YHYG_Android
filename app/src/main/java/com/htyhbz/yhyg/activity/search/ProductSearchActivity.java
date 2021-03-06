package com.htyhbz.yhyg.activity.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.shoppingcat.ShoppingCatActivity;
import com.htyhbz.yhyg.adapter.HomeProductAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.view.ClearEditText;
import com.htyhbz.yhyg.vo.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/11.
 */
public class ProductSearchActivity extends BaseActivity{
    private ClearEditText searchET;
    private TextView searchTV;
    private GridView searchGV;
    private HomeProductAdapter adapter;
    private List<Product> productList=new ArrayList<Product>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        initView();
    }
    private void initView(){
        searchET= (ClearEditText) findViewById(R.id.searchET);
        searchET.setFocusable(true);
        searchET.setFocusableInTouchMode(true);
        searchET.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        searchET.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    if(TextUtils.isEmpty(searchET.getText().toString().trim())){
                        toast(ProductSearchActivity.this,"请输入关键字");
                    }else{
                        getSearchProduction(searchET.getText().toString().trim());
                    }
                }
                return false;
            }
        });



        searchTV= (TextView) findViewById(R.id.searchTV);
        searchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(searchET.getText().toString().trim())){
                    toast(ProductSearchActivity.this,"请输入关键字");
                }else{
                    hiddenSoftInput();
                    getSearchProduction(searchET.getText().toString().trim());
                }
            }
        });
        searchGV= (GridView) findViewById(R.id.searchGV);
        adapter=new HomeProductAdapter(this,productList);
        searchGV.setAdapter(adapter);
        searchGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(ProductSearchActivity.this, ShoppingCatActivity.class);
                intent1.putExtra("productId", productList.get(i).getproductId());
                intent1.putExtra("productType", productList.get(i).getproductType());
                startActivity(intent1);
            }
        });
    }

    /**
     * 网络请求
     */
    private void getSearchProduction(String keyword) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("areaID", getUserInfo(1));
        params.put("keyword", keyword);
        params.put("userID", getUserInfo(0));
        String url= InitApp.getUrlByParameter(ApiConstants.SEARCH_PRODUCTION_API, params, true);
        Log.e("getSearchProductionURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSearchProductionRe", response);
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
                                    findViewById(R.id.LL).setVisibility(View.VISIBLE);
                                }else{
                                    findViewById(R.id.LL).setVisibility(View.GONE);
                                    toast(ProductSearchActivity.this,"无相关搜索结果");
                                }
                                adapter.notifyDataSetChanged();
                            }else{
                                toast(ProductSearchActivity.this, jsonObject.getString("msg"));
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
