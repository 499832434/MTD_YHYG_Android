package com.htyhbz.yhyg.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class ResetPasswordActivity extends BaseActivity{

    private EditText usernameET,passwordET;
    private Button commitB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        commitB= (Button) findViewById(R.id.commitB);
        commitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usernameET.getText().toString().equals(passwordET.getText().toString())){
                    toast(ResetPasswordActivity.this,"两次密码不一致");
                    return;
                }
                hiddenSoftInput();
                request();
            }
        });
        usernameET= (EditText) findViewById(R.id.usernameET);
        passwordET= (EditText) findViewById(R.id.passwordET);
        usernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(usernameET.getText().toString())) {
                    if (!TextUtils.isEmpty(passwordET.getText().toString())) {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button);
                        commitB.setClickable(true);
                    } else {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                        commitB.setClickable(false);
                    }
                } else {
                    commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                    commitB.setClickable(false);
                }
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(passwordET.getText().toString())) {
                    if (!TextUtils.isEmpty(usernameET.getText().toString())) {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button);
                        commitB.setClickable(true);
                    } else {
                        commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                        commitB.setClickable(false);
                    }
                } else {
                    commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                    commitB.setClickable(false);
                }
            }
        });
    }


    
    private void request() {
        if (!NetworkUtils.isNetworkAvailable(ResetPasswordActivity.this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("userPhoneNumber", getIntent().getStringExtra("userPhoneNumber"));
        params.put("newPassword", InitApp.getSHA256StrJava(usernameET.getText().toString()) );
        String url= InitApp.getUrlByParameter(ApiConstants.FORGOT_PASSWORD_API, params, true);
        Log.e("ModifyPasswordURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ModifyPasswordRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                toast(ResetPasswordActivity.this,"成功");
                                finish();
                            }else{
                                toast(ResetPasswordActivity.this,jsonObject.getString("msg"));
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
