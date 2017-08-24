package com.htyhbz.yhyg.activity.order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.adapter.OrderTypeContentAdapter;
import com.htyhbz.yhyg.event.ShoppingcatRefreshEvent;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.utils.PrefUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.UserInfo;
import de.greenrobot.event.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/14.
 */
public class OrderSettlementActivity extends BaseActivity{
    private MyListView orderMLV;
    private OrderTypeContentAdapter adapter;
    private List<Product> productList =new ArrayList<Product>();
    private EditText orderSendTimeET,townET,phoneET,receiverNameET,markET;
    private int mYear;
    private int mMonth;
    private int mDay;
    private List<String> townList=new ArrayList<String>();
    private int townPosition=0;
    private double shoppingTotalPrice;
    private String flag="";
    private TextView gprsTV,scoreTV,countTV;
    private ImageView scoreIV;
    private int payType=1;
    private RadioButton alipayRadioBtn, wechatRadioBtn;
    private RelativeLayout alipayRadioBtnContainer, wechatRadioContainer;
    private StringBuffer orderProductionsID,orderProductionsCount;
    private UserInfo userInfo;
    private String orderId="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_settlement);

        initData();
        initView();
        if("order".equals(flag)){
            getTown(0);
        }
        getUserIntegral();
    }


    private void initData(){
        try{
            flag=getIntent().getStringExtra("flag");
            orderProductionsID=new StringBuffer();
            orderProductionsCount=new StringBuffer();

            String map=getIntent().getStringExtra("map");
            Log.e("map",map);
            JSONObject jsonObject=new JSONObject(map);
            shoppingTotalPrice=jsonObject.getDouble("shoppingTotalPrice");
            JSONArray array=jsonObject.getJSONArray("shoppinglist");
            for(int i=0;i<array.length();i++){
                JSONObject obj=array.getJSONObject(i);
                Product product=new Product();
                product.setproductId(obj.getInt("productId"));
                product.setproductName(obj.getString("productName"));
                product.setProductPrice(obj.getDouble("productPrice"));
                product.setproductPictureUrl(obj.getString("productPictureUrl"));
                product.setorderProductCount(obj.getInt("shoppingsingleTotal"));
                orderProductionsID.append(obj.getInt("productId") + ",");
                orderProductionsCount.append(obj.getInt("shoppingsingleTotal")+",");
                productList.add(product);
            }
            if("order".equals(flag)){
                userInfo=getIntent().getParcelableExtra("userinfo");
                orderId=getIntent().getStringExtra("orderId");
            }
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

        alipayRadioBtn = (RadioButton) findViewById(R.id.alipayRadioBtn);
        wechatRadioBtn = (RadioButton) findViewById(R.id.wechatRadioBtn);
        alipayRadioBtnContainer = (RelativeLayout) findViewById(R.id.alipayRadioBtnContainer);
        wechatRadioContainer = (RelativeLayout) findViewById(R.id.wechatRadioContainer);

        alipayRadioBtnContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alipayRadioBtn.setChecked(true);
            }
        });
        wechatRadioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechatRadioBtn.setChecked(true);
            }
        });

        gprsTV= ((TextView)findViewById(R.id.gprsTV));
        gprsTV.setText(Html.fromHtml(getUserInfo(5) + "<u>" + "请选择乡镇" + "</u>"));
        gprsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTown(1);
            }
        });
        phoneET= (EditText) findViewById(R.id.phoneET);
        receiverNameET= (EditText) findViewById(R.id.receiverNameET);
        markET= (EditText) findViewById(R.id.markET);
        scoreTV=(TextView)findViewById(R.id.scoreTV);
        countTV= (TextView) findViewById(R.id.countTV);
        countTV.setText("¥"+shoppingTotalPrice+"");
        scoreIV= (ImageView) findViewById(R.id.scoreIV);
        scoreIV.setTag("0");
        findViewById(R.id.scoreRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("0".equals(scoreIV.getTag())) {
                    scoreIV.setTag("1");
                    scoreIV.setBackgroundResource(R.drawable.icon_xuanzhong);
                    double count=(new BigDecimal(Double.toString(shoppingTotalPrice))).subtract(new BigDecimal(Double.toString((Double.parseDouble(getUserInfo(2)) / Double.parseDouble(getUserInfo(3)))))).doubleValue();
                    //double count = shoppingTotalPrice - (Double.parseDouble(getUserInfo(2)) / Double.parseDouble(getUserInfo(3)));
                    if (count < 0) {
                        count = 0;
                    }
                    countTV.setText("¥" + count + "");
                } else {
                    scoreIV.setTag("0");
                    scoreIV.setBackgroundResource(R.drawable.icon_weixuanzhong);
                    countTV.setText("¥" + shoppingTotalPrice + "");
                }
            }
        });
        orderMLV= (MyListView) findViewById(R.id.orderMLV);
        adapter=new OrderTypeContentAdapter(OrderSettlementActivity.this,productList);
        orderMLV.setAdapter(adapter);

        townET= (EditText) findViewById(R.id.townET);
        findViewById(R.id.commitTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });
        orderSendTimeET= (EditText) findViewById(R.id.orderSendTimeET);
        orderSendTimeET.setInputType(InputType.TYPE_NULL);
        orderSendTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(OrderSettlementActivity.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            updateDisplay();
                        }
                    }, mYear, mMonth, mDay).show();
                }
            }
        });
        orderSendTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OrderSettlementActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDisplay();
                    }
                }, mYear, mMonth, mDay).show();
            }
        });
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        if("order".equals(flag)){
            //gprsTV.setText(getUserInfo(5)+userInfo.getTownId());
            orderSendTimeET.setText(userInfo.getOrderSendTime());
            townET.setText(userInfo.getOrderDetailAddress());
            phoneET.setText(userInfo.getReceiverPhone());
            receiverNameET.setText(userInfo.getReceiverName());
            markET.setText(userInfo.getMark());
        }

//        updateDisplay();
    }



    /**

     * 更新日期

     */

    private void updateDisplay() {
        orderSendTimeET.setText(new StringBuilder().append(mYear).append("-").append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append(
                (mDay < 10) ? "0" + mDay : mDay));
    }

    /**
     * 网络请求
     */
    private void getTown(final int flagInt) {
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
                                List<String> list=new ArrayList<String>();
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    list.add(obj.getString("townName"));
                                    townList.add(obj.getString("townName")+"===="+obj.getString("townID"));
                                    if("order".equals(flag)&&userInfo.getTownId().equals(obj.getString("townID"))){
                                        gprsTV.setText(Html.fromHtml(getUserInfo(5) + "<u>" +obj.getString("townName") + "</u>"));
                                    }
                                }
                                if("shoppingcat".equals(flag)||flagInt==1){
                                    if(list.size()>0){
                                        showTownDialog(list);
                                    }
                                }

                            }else{
                                toast(OrderSettlementActivity.this, jsonObject.getString("msg"));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSettlementActivity.this);
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
                gprsTV.setText(Html.fromHtml(getUserInfo(5) + "<u>" +list.get(townPosition)+ "</u>"));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    /**
     * 网络请求
     */
    public void getUserIntegral() {
        if (!NetworkUtils.isNetworkAvailable(OrderSettlementActivity.this)) {
            return;
        }

        final HashMap<String,String> params=OrderSettlementActivity.this.getNetworkRequestHashMap();
        params.put("userID", OrderSettlementActivity.this.getUserInfo(0));
        String url= InitApp.getUrlByParameter(ApiConstants.GET_USER_INTEGRAL_API, params, true);
        Log.e("getUserIntegralURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getUserIntegralRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONObject info=jsonObject.getJSONObject("info");
                                scoreTV.setText("使用积分  (可用"+info.getString("userIntegral")+"积分,"+info.getString("scoreScale")+"积分=1元)");
                                PrefUtils.putString(OrderSettlementActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_INTEGRAL_KEY, info.getString("userIntegral"));
                                PrefUtils.putString(OrderSettlementActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.SCORE_SCALE_KEY, info.getString("scoreScale"));
                            }else{
                                OrderSettlementActivity.this.toast(OrderSettlementActivity.this, jsonObject.getString("msg"));
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


    public void commit() {
        if (!NetworkUtils.isNetworkAvailable(OrderSettlementActivity.this)) {
            return;
        }
        if(gprsTV.getText().toString().indexOf("请选择乡镇")!=-1){
            toast(OrderSettlementActivity.this,"请选择乡镇");
            return;
        }
        if(TextUtils.isEmpty(townET.getText().toString())){
            toast(OrderSettlementActivity.this,"请输入详细地址");
            return;
        }
        if(TextUtils.isEmpty(orderSendTimeET.getText().toString())){
            toast(OrderSettlementActivity.this,"请选择配送时间");
            return;
        }
        if(TextUtils.isEmpty(phoneET.getText().toString())){
            toast(OrderSettlementActivity.this,"请输入您的联系方式");
            return;
        }
        if(!isMobileNO(phoneET.getText().toString())){
            toast(OrderSettlementActivity.this,"请输入正确的联系方式");
            return;
        }
        if(TextUtils.isEmpty(receiverNameET.getText().toString())){
            toast(OrderSettlementActivity.this,"请输入收货人姓名");
            return;
        }

        final HashMap<String,String> params=OrderSettlementActivity.this.getNetworkRequestHashMap();
        params.put("userID", OrderSettlementActivity.this.getUserInfo(0));
        params.put("orderAddress", gprsTV.getText().toString());
        if("order".equals(flag)){
            params.put("townID", userInfo.getTownId());
            params.put("orderID", orderId);
        }else{
            params.put("townID", townList.get(townPosition).split("====")[1]);
            params.put("orderID", "");
        }
        params.put("orderDetailAddress", townET.getText().toString());
        params.put("orderSendTime",orderSendTimeET.getText().toString());
        params.put("receiverPhone", phoneET.getText().toString());
        params.put("receiverName", receiverNameET.getText().toString());
        params.put("mark", markET.getText().toString());
        int count1;
        if("0".equals(scoreIV.getTag())){
            count1=0;
        }else{
            double cc=shoppingTotalPrice*Double.parseDouble(getUserInfo(3));
            String str=Double.toString(cc).substring(0,Double.toString(cc).indexOf("."));
            if(cc%1==0){
                count1=Integer.valueOf(str);
            }else{
                count1=Integer.valueOf(str)+1;
            }
        }
        Log.e("useIntegralCount",count1+"");
        params.put("useIntegralCount", count1+"");
        boolean b=alipayRadioBtn.isChecked();
        if(b){
            payType=1;
        }else {
            payType=2;
        }
        params.put("payType", "1");
        double count;
        if("0".equals(scoreIV.getTag())){
            count=shoppingTotalPrice;
        }else{
            count=(new BigDecimal(Double.toString(shoppingTotalPrice))).subtract(new BigDecimal(Double.toString((Double.parseDouble(getUserInfo(2)) / Double.parseDouble(getUserInfo(3)))))).doubleValue();
//            count = shoppingTotalPrice - (Double.parseDouble(getUserInfo(2)) / Double.parseDouble(getUserInfo(3)));
            if (count < 0) {
                count = 0;
            }
        }
        params.put("actualPayPrice", count+"");
        params.put("orderAllPrice", shoppingTotalPrice+"");
        Log.e("orderProductionsID",orderProductionsID.toString());
        params.put("orderProductionsID", orderProductionsID.substring(0,orderProductionsID.length()-1).toString());
        params.put("orderProductionsCount", orderProductionsCount.substring(0, orderProductionsCount.length() - 1).toString());


        String url= InitApp.getUrlByParameter(ApiConstants.COMMIT_ORDER_API, params, true);
        Log.e("commitOrderURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("commitOrderRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                toast(OrderSettlementActivity.this, "下单成功");
                                if("shoppingcat".equals(flag)){
                                    EventBus.getDefault().post(new ShoppingcatRefreshEvent());
                                }
                                finish();
                            }else{
                                OrderSettlementActivity.this.toast(OrderSettlementActivity.this, jsonObject.getString("msg"));
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
