package com.htyhbz.yhyg.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.order.OrderQueryActivity;
import com.htyhbz.yhyg.adapter.OrderTypeAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.vo.OrderInfo;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/14.
 */
public class OrderQueryFragment extends ErrorsFragment {
    private View currentView = null;
    private final static String FRAGMENT_TYPE = "fragmentType";
    private OrderQueryActivity parentActivity = null;
    private int fragmentType = 0;
    private TextView startTimeTV,endTimeTV,areaTV;
    private int mYear;
    private int mMonth;
    private int mDay;
    private OrderQueryActivity mActivity;
    private ListView orderLV;
    private OrderTypeAdapter adapter;
    private View footView;
    private LinearLayout timeLL,areaLL;
    private int flagNum=0;
    private List<OrderInfo>  orderInfoList=new ArrayList<OrderInfo>();
    private List<UserInfo>  userInfoList=new ArrayList<UserInfo>();
    private int townPosition=0;
    private List<String> townList=new ArrayList<String>();

    public static OrderQueryFragment newInstance(OrderQueryActivity parentActivity,  int fragmentType) {
        OrderQueryFragment fr = new OrderQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, fragmentType);
        fr.setArguments(bundle);
        fr.parentActivity = parentActivity;
        return fr;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_order_query, container, false);
        initView();
        return currentView;
    }
    private void initView(){
        fragmentType = getArguments().getInt(FRAGMENT_TYPE);
        timeLL= (LinearLayout) currentView.findViewById(R.id.timeLL);
        areaLL= (LinearLayout) currentView.findViewById(R.id.areaLL);
        if(0==fragmentType){
            timeLL.setVisibility(View.VISIBLE);
            areaLL.setVisibility(View.GONE);
        }else{
            timeLL.setVisibility(View.GONE);
            areaLL.setVisibility(View.VISIBLE);
        }
        orderLV= (ListView) currentView.findViewById(R.id.orderLV);
        adapter=new OrderTypeAdapter(mActivity,orderInfoList,userInfoList);
        orderLV.setAdapter(adapter);
        startTimeTV= (TextView) currentView.findViewById(R.id.startTimeTV);
        endTimeTV= (TextView) currentView.findViewById(R.id.endTimeTV);
        areaTV= (TextView) currentView.findViewById(R.id.areaTV);
        areaTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getTown();
            }
        });
        startTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(0);
            }
        });
        endTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(1);
            }
        });
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
//        updateDisplay(0);
//        updateDisplay(1);
        currentView.findViewById(R.id.queryTimeB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
        currentView.findViewById(R.id.querytownB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
    }

    private void updateDisplay(int flag) {
        if(0==flag){
            startTimeTV.setText(new StringBuilder().append(mYear).append("-").append(
                    (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append(
                    (mDay < 10) ? "0" + mDay : mDay));
        }else{
            endTimeTV.setText(new StringBuilder().append(mYear).append("-").append(
                    (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append(
                    (mDay < 10) ? "0" + mDay : mDay));
        }
    }

    private void setDate(final int flag){
        new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                updateDisplay(flag);
            }
        }, mYear, mMonth, mDay).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity=(OrderQueryActivity)context;
    }


    /**
     * 网络请求
     */
    private void request() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }
        if(footView!=null){
            orderLV.removeFooterView(footView);
        }

        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        if(0==fragmentType){
            if(TextUtils.isEmpty(startTimeTV.getText().toString())||TextUtils.isEmpty(endTimeTV.getText().toString())){
                ((BaseActivity)mActivity).toast(mActivity,"时间不能为空");
                return;
            }
            params.put("startTime",startTimeTV.getText().toString());
            params.put("endTime",endTimeTV.getText().toString());
            params.put("townID", "");
        }else{
            if(areaTV.getText().toString().indexOf("选择乡镇")!=-1){
                ((BaseActivity)mActivity).toast(mActivity,"请选择乡镇");
                return;
            }
            params.put("townID", townList.get(townPosition).split("====")[1]);
            params.put("startTime","");
            params.put("endTime","");
        }
        String url=InitApp.getUrlByParameter(ApiConstants.QUERY_ORDER_API,params,true);
        Log.e("queryListURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("queryListRe", response);
                        try {
                            orderInfoList.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                int priceCount = 0;
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj1= (JSONObject) infoArr.get(i);
                                    JSONObject obj=obj1.getJSONObject("orderInfo");
                                    OrderInfo info=new OrderInfo();
                                    info.setOrderID(obj.getString("orderID"));
                                    info.setOrderSendTime(obj.getString("orderTime"));
                                    info.setUseIntegralCount(obj.getString("useIntegralCount"));
                                    info.setOrderAllPrice(obj.getInt("orderAllPrice")+"");
                                    priceCount+=obj.getInt("orderAllPrice");
                                    info.setOrderType(obj.getString("orderType"));
                                    info.setActualPayPrice(obj.getInt("actualPayPrice")+"");
                                    JSONArray productArr=obj.getJSONArray("orderProductions");
                                    List<Product> productList=new ArrayList<Product>();
                                    for(int j=0;j<productArr.length();j++){
                                        JSONObject obj3=productArr.getJSONObject(j);
                                        Product product=new Product();
                                        product.setproductId(obj3.getInt("productId"));
                                        product.setproductName(obj3.getString("productName"));
                                        product.setproductPrice(obj3.getInt("productPrice"));
                                        product.setproductPictureUrl(ApiConstants.BASE_URL+obj3.getString("productPictureUrl"));
                                        product.setorderProductCount(obj3.getInt("orderProductionsCount"));
                                        productList.add(product);
                                    }
                                    info.setList(productList);
                                    orderInfoList.add(info);

                                    JSONObject obj2=obj1.getJSONObject("userInfo");
                                    UserInfo userInfo=new UserInfo();
                                    userInfo.setTownId(obj2.getString("townID"));
                                    userInfo.setOrderAddress(obj2.getString("orderAddress"));
                                    userInfo.setOrderDetailAddress(obj2.getString("orderDetailAddress"));
                                    userInfo.setOrderSendTime(obj2.getString("orderSendTime"));
                                    userInfo.setReceiverPhone(obj2.getString("receiverPhone"));
                                    userInfo.setReceiverName(obj2.getString("receiverName"));
                                    userInfo.setMark(obj2.getString("mark"));
                                    userInfoList.add(userInfo);
                                }
                                if(orderInfoList.size()>0){
                                    footView=LayoutInflater.from(mActivity).inflate(R.layout.footview_order_query,null);
                                    ((TextView)footView.findViewById(R.id.orderNumTV)).setText("订单:"+orderInfoList.size()+"件");
                                    ((TextView)footView.findViewById(R.id.orderPriceTV)).setText(Html.fromHtml("金额:<font color='#f25424'>"+priceCount+"</font>元"));
                                    orderLV.addFooterView(footView);
                                    orderLV.setVisibility(View.VISIBLE);
                                    currentView.findViewById(R.id.layoutError).setVisibility(View.GONE);
                                }else{
                                    ((BaseActivity)mActivity).toast(mActivity,"无订单");
                                    orderLV.setVisibility(View.GONE);
                                    currentView.findViewById(R.id.layoutError).setVisibility(View.VISIBLE);
                                    showErrorLayout(currentView.findViewById(R.id.layoutError), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            request();
                                        }
                                    },4);
                                }
                                adapter.notifyDataSetChanged();
                            }else{
                                mActivity.toast(mActivity, jsonObject.getString("msg"));
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


    private void getTown() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        params.put("areaID", mActivity.getUserInfo(1));
        String url= InitApp.getUrlByParameter(ApiConstants.GET_TOWN_API, params, true);
        Log.e("getTownURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getTownRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            List<String> list=new ArrayList<String>();
                            JSONArray infoArr=jsonObject.getJSONArray("info");
                            for(int i=0;i<infoArr.length();i++){
                                JSONObject obj=infoArr.getJSONObject(i);
                                list.add(obj.getString("townName"));
                                townList.add(obj.getString("townName")+"===="+obj.getString("townID"));
                            }
                            if(list.size()>0){
                                showTownDialog(list);
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


    private void showTownDialog(final List<String> list){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("请选择城镇");
        int size = list.size();
        final String[] arr = (String[])list.toArray(new String[size]);//
        builder.setSingleChoiceItems(arr, 0, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                townPosition=which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                areaTV.setText(list.get(townPosition));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
