package com.htyhbz.yhyg.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.activity.enterprise.EnterpriseMainActivity;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.utils.PrefUtils;
import com.htyhbz.yhyg.view.ClearEditText;
import com.htyhbz.yhyg.vo.Product;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class LoginActivity extends BaseActivity{
    private ClearEditText phoneNumberET;
    private ClearEditText passwordET;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getPositon();
        initView();

    }


    private void initView(){
        phoneNumberET= (ClearEditText) findViewById(R.id.phoneNumberET);
        phoneNumberET.setText(getUserInfo(8));
        passwordET= (ClearEditText) findViewById(R.id.passwordET);
        findViewById(R.id.forgotTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(phoneNumberET.getText().toString())&&!TextUtils.isEmpty(passwordET.getText().toString())){
                    request(phoneNumberET.getText().toString(),passwordET.getText().toString());
                }else{
                    toast(LoginActivity.this,"手机和密码不能为空");
                }
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }


    /**
     * 网络请求
     */
    private void request(final String phoneNumber,final String password) {
        if (!NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
            return;
        }

        HashMap<String, String> params =getNetworkRequestHashMap();
        params.put("phoneNumber", phoneNumber);
        params.put("password", password);
        String url=InitApp.getUrlByParameter(ApiConstants.PHONE_LOGIN_API,params,true);
        Log.e("loginUrl",url);
        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("LoginRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONObject info=jsonObject.getJSONObject("info");
                                PrefUtils.putString(LoginActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_ID_KEY, info.getString("userID"));
                                PrefUtils.putString(LoginActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.AREA_ID_KEY, info.getString("areaID"));
                                PrefUtils.putString(LoginActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_AREA_KEY, info.getString("userArea"));
                                PrefUtils.putString(LoginActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_NAME_KEY, phoneNumberET.getText().toString());
                                String userPermission=info.getString("userPermission");
                                if("6".equals(userPermission)||"7".equals(userPermission)){
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }else if("3".equals(userPermission)){
                                    Intent intent = new Intent(LoginActivity.this, EnterpriseMainActivity.class);
                                    intent.putExtra("flag", "3");
                                    startActivity(intent);
                                }else if("2".equals(userPermission)){
                                    Intent intent = new Intent(LoginActivity.this, EnterpriseMainActivity.class);
                                    intent.putExtra("flag", "2");
                                    startActivity(intent);
                                }
                                finish();
                            }else{
                                toast(LoginActivity.this,jsonObject.getString("info"));
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("phoneNumber", phoneNumber);
                params.put("password", password);
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }
}
