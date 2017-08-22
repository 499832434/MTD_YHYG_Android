package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.collect.CollectActivity;
import com.htyhbz.yhyg.activity.integral.IntegralLiftingActivity;
import com.htyhbz.yhyg.activity.integral.IntegralRecordActivity;
import com.htyhbz.yhyg.activity.login.LoginActivity;
import com.htyhbz.yhyg.activity.login.ModifyPasswordActivity;
import com.htyhbz.yhyg.activity.login.ResetPasswordActivity;
import com.htyhbz.yhyg.activity.order.OrderQueryActivity;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.utils.PrefUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zongshuo on 2017/7/3.
 */
public class AccountFragment extends Fragment implements View.OnClickListener{
    private View currentView = null;
    private TextView unLoginTV,integralTV;
    protected MainActivity mActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        getUserIntegral();
        return currentView;
    }

    private void initView(){
        unLoginTV= (TextView) currentView.findViewById(R.id.unLoginTV);
        integralTV= (TextView) currentView.findViewById(R.id.integralTV);
        unLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.clearLoginInfo();
                mActivity.toast(mActivity,"已退出账号");
                startActivity(new Intent(mActivity,LoginActivity.class));
                mActivity.finish();
            }
        });
        ((TextView) currentView.findViewById(R.id.accountTV)).setText("账号:"+mActivity.getUserInfo(8));
        currentView.findViewById(R.id.integralLiftingRL).setOnClickListener(this);
        currentView.findViewById(R.id.orderSummaryRL).setOnClickListener(this);
        currentView.findViewById(R.id.currentRecordRL).setOnClickListener(this);
        currentView.findViewById(R.id.modifyPasswordRL).setOnClickListener(this);
        currentView.findViewById(R.id.myCollectionRL).setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.integralLiftingRL:
                startActivity(new Intent(mActivity,IntegralLiftingActivity.class));
                break;
            case R.id.orderSummaryRL:
                startActivity(new Intent(mActivity, OrderQueryActivity.class));
                break;
            case R.id.currentRecordRL:
                startActivity(new Intent(mActivity, IntegralRecordActivity.class));
                break;
            case R.id.modifyPasswordRL:
                startActivity(new Intent(mActivity, ModifyPasswordActivity.class));
                break;
            case R.id.myCollectionRL:
                startActivity(new Intent(mActivity, CollectActivity.class));
                break;
        }
    }


    /**
     * 网络请求
     */
    private void getUserIntegral() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        params.put("userID", mActivity.getUserInfo(0));
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
                                integralTV.setText("积分:"+info.getString("userIntegral"));
                                PrefUtils.putString(mActivity, InitApp.USER_PRIVATE_DATA, InitApp.USER_INTEGRAL_KEY, info.getString("userIntegral"));
                                PrefUtils.putString(mActivity, InitApp.USER_PRIVATE_DATA, InitApp.SCORE_SCALE_KEY, info.getString("scoreScale"));
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
}
