package com.htyhbz.yhyg.activity.integral;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.vo.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zongshuo on 2017/7/17.
 */
public class IntegralLiftingActivity extends BaseActivity{
    private EditText moneyET;
    private int more;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_lifting);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        more=Integer.parseInt(getUserInfo(2))/Integer.parseInt(getUserInfo(3));
        ((TextView)findViewById(R.id.moreTV)).setText("最多可提现:  "+more);
        moneyET=((EditText) findViewById(R.id.moneyET));
        findViewById(R.id.commitB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(moneyET.getText().toString().trim())) {
                    toast(IntegralLiftingActivity.this, "金额不能为空");
                } else {
                    int money = Integer.parseInt(moneyET.getText().toString().trim());
                    if (money > more) {
                        toast(IntegralLiftingActivity.this, "金额超过上限");
                    } else {
                        request(money);
                    }
                }
            }
        });
    }

    /**
     * 网络请求
     */
    private void request(int money) {
        if (!NetworkUtils.isNetworkAvailable(IntegralLiftingActivity.this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("userID", getUserInfo(0));
        params.put("withdrawalAmount", money+"");
        String url=InitApp.getUrlByParameter(ApiConstants.WITH_DRAW_API,params,true);
        Log.e("LiftingURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("LiftingRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                toast(IntegralLiftingActivity.this,"成功");
                                finish();
                            }else{
//                                toast(IntegralLiftingActivity.this,jsonObject.getString("info"));
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
