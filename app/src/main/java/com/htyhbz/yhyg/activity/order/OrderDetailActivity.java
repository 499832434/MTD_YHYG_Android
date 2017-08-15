package com.htyhbz.yhyg.activity.order;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.adapter.OrderTypeContentAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/13.
 */
public class OrderDetailActivity extends BaseActivity{
    private MyListView orderMLV;
    private OrderTypeContentAdapter adapter;
    private List<Product> productList =new ArrayList<Product>();
    private String flag="",useIntegralCount="",actualPayPrice="";
    private UserInfo userInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initData();
        initView();
        getTown();
    }

    private void initData(){
        try{
            flag=getIntent().getStringExtra("flag");
            useIntegralCount=getIntent().getStringExtra("useIntegralCount");
            actualPayPrice=getIntent().getStringExtra("actualPayPrice");

            String map=getIntent().getStringExtra("map");
            Log.e("map", map);
            JSONObject jsonObject=new JSONObject(map);
            JSONArray array=jsonObject.getJSONArray("shoppinglist");
            for(int i=0;i<array.length();i++){
                JSONObject obj=array.getJSONObject(i);
                Product product=new Product();
                product.setproductId(obj.getInt("productId"));
                product.setproductName(obj.getString("productName"));
                product.setproductPrice(obj.getDouble("productPrice"));
                product.setproductPictureUrl(obj.getString("productPictureUrl"));
                product.setorderProductCount(obj.getInt("shoppingsingleTotal"));
                productList.add(product);
            }
            userInfo=getIntent().getParcelableExtra("userinfo");
        }catch (Exception e){

        }

    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.orderAddressTV)).setText(userInfo.getOrderAddress()+userInfo.getTownId());
        ((TextView)findViewById(R.id.orderDetailAddressTV)).setText(userInfo.getOrderDetailAddress());
        ((TextView)findViewById(R.id.orderSendTimeTV)).setText(userInfo.getOrderSendTime());
        ((TextView)findViewById(R.id.receiverPhoneTV)).setText(userInfo.getReceiverPhone());
        ((TextView)findViewById(R.id.receiverNameTV)).setText(userInfo.getReceiverName());
        ((TextView)findViewById(R.id.markTV)).setText(userInfo.getMark());
        ((TextView)findViewById(R.id.useIntegralCountTV)).setText("使用"+useIntegralCount+"积分");
        ((TextView) findViewById(R.id.countTV)).setText(actualPayPrice);
        String str="";
        if("1".equals(flag)){
            str="已支付,请等待配送";
        }else if("2".equals(flag)){
            str="订单配送中";
        }else {
            str="订单已完成";
        }
        ((TextView)findViewById(R.id.detailTV)).setText(str);

        orderMLV= (MyListView) findViewById(R.id.orderMLV);
        adapter=new OrderTypeContentAdapter(OrderDetailActivity.this,productList);
        orderMLV.setAdapter(adapter);
        ((ScrollView)findViewById(R.id.SV)).smoothScrollTo(0, 20);
    }

    /**
     * 网络请求
     */
    private void getTown() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("areaID", getUserInfo(1));
        String url= InitApp.getUrlByParameter(ApiConstants.GET_TOWN_API, params, true);
        Log.e("getTownURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getTownRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    if(userInfo.getTownId().equals(obj.getString("townID"))){
                                        ((TextView)findViewById(R.id.orderAddressTV)).setText(getUserInfo(5)+obj.getString("townName"));
                                        break;
                                    }
                                }


                            }else{
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
