package com.htyhbz.yhyg.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
public class OrderQueryFragment extends Fragment {
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
        footView=LayoutInflater.from(mActivity).inflate(R.layout.footview_order_query,null);
        orderLV.addFooterView(footView);
        startTimeTV= (TextView) currentView.findViewById(R.id.startTimeTV);
        endTimeTV= (TextView) currentView.findViewById(R.id.endTimeTV);
        areaTV= (TextView) currentView.findViewById(R.id.areaTV);
        areaTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setIcon(R.drawable.icon);
                builder.setTitle("请选择乡镇");
                final String[] sex = {"乡镇1", "乡镇2", "乡镇3","乡镇4", "乡镇5", "乡镇6"};
                builder.setSingleChoiceItems(sex, flagNum, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        flagNum=which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        areaTV.setText(sex[flagNum]);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
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
                setDate(0);
            }
        });
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay(0);
        updateDisplay(1);
    }

    private void updateDisplay(int flag) {
        if(0==flag){
            startTimeTV.setText(new StringBuilder().append("开始时间:").append(mYear).append("-").append(
                    (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append(
                    (mDay < 10) ? "0" + mDay : mDay));
        }else{
            endTimeTV.setText(new StringBuilder().append("结束时间:").append(mYear).append("-").append(
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


//    /**
//     * 网络请求
//     */
//    private void request() {
//        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
//            return;
//        }
//
//        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
//        params.put("userID", mActivity.getUserInfo(0));
//        params.put("orderType", orderType+"");
//        params.put("pageIndex", pageIndex+"");
//        params.put("pageSize", InitApp.PAGESIZE);
//        String url=InitApp.getUrlByParameter(ApiConstants.ORDER_LIST_API,params,true);
//        Log.e("queryListURl", url);
//
//        HighRequest request = new HighRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("queryListRe", response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getString("code").equals("0")) {
//                                JSONArray infoArr=jsonObject.getJSONArray("info");
//                                for(int i=0;i<infoArr.length();i++){
//                                    JSONObject obj1= (JSONObject) infoArr.get(i);
//                                    JSONObject obj=obj1.getJSONObject("orderInfo");
//                                    OrderInfo info=new OrderInfo();
//                                    info.setOrderID(obj.getString("orderID"));
//                                    info.setOrderSendTime(obj.getString("orderTime"));
//                                    info.setUseIntegralCount(obj.getString("useIntegralCount"));
//                                    info.setOrderAllPrice(obj.getString("orderAllPrice"));
//                                    info.setOrderType(obj.getString("orderType"));
//                                    info.setActualPayPrice(obj.getString("actualPayPrice"));
//                                    JSONArray productArr=obj.getJSONArray("orderProductions");
//                                    List<Product> productList=new ArrayList<Product>();
//                                    for(int j=0;j<productArr.length();j++){
//                                        JSONObject obj3=productArr.getJSONObject(j);
//                                        Product product=new Product();
//                                        product.setproductId(obj3.getInt("productId"));
//                                        product.setproductName(obj3.getString("productName"));
//                                        product.setproductPrice(obj3.getDouble("productPrice"));
//                                        product.setproductPictureUrl(ApiConstants.BASE_URL+obj3.getString("productPictureUrl"));
//                                        product.setorderProductCount(obj3.getInt("orderProductionsCount"));
//                                        productList.add(product);
//                                    }
//                                    info.setList(productList);
//                                    orderInfoList.add(info);
//
//                                    JSONObject obj2=obj1.getJSONObject("userInfo");
//                                    UserInfo userInfo=new UserInfo();
//                                    userInfo.setTownId(obj2.getString("townID"));
//                                    userInfo.setOrderAddress(obj2.getString("orderAddress"));
//                                    userInfo.setOrderDetailAddress(obj2.getString("orderDetailAddress"));
//                                    userInfo.setOrderSendTime(obj2.getString("orderSendTime"));
//                                    userInfo.setReceiverPhone(obj2.getString("receiverPhone"));
//                                    userInfo.setReceiverName(obj2.getString("receiverName"));
//                                    userInfo.setMark(obj2.getString("mark"));
//                                    userInfoList.add(userInfo);
//                                }
//                                adapter.notifyDataSetChanged();
//                            }else{
//                                mActivity.toast(mActivity, jsonObject.getString("msg"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }
//        );
//        InitApp.initApp.addToRequestQueue(request);
//    }

}
