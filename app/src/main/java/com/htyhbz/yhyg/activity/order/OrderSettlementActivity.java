package com.htyhbz.yhyg.activity.order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.htyhbz.yhyg.utils.PrefUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.Product;
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
public class OrderSettlementActivity extends BaseActivity{
    private MyListView orderMLV;
    private OrderTypeContentAdapter adapter;
    private List<Product> list =new ArrayList<Product>();
    private EditText orderSendTimeET,townET;
    private int mYear;
    private int mMonth;
    private int mDay;
    private List<String> townList=new ArrayList<String>();
    private int townPosition=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_settlement);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orderMLV= (MyListView) findViewById(R.id.orderMLV);
        adapter=new OrderTypeContentAdapter(OrderSettlementActivity.this,list);
        orderMLV.setAdapter(adapter);

        townET= (EditText) findViewById(R.id.townET);
        townET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    getTown();
                }
            }
        });
        townET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTown();
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
        updateDisplay();
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
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                townET.setText(list.get(townPosition));
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
}
