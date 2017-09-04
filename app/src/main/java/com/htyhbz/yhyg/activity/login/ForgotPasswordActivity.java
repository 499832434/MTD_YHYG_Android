package com.htyhbz.yhyg.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.htyhbz.yhyg.utils.TimesUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class ForgotPasswordActivity extends BaseActivity{

    private EditText telephoneET;
    private EditText verificationCodeET;
    private Button commitB;
    private TimesUtils timesUtils=null;
    private TextView verificationCodeTV;
    private String code="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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
                if(!isMobileNO(telephoneET.getText().toString())){
                    toast(ForgotPasswordActivity.this,"请输入正确的联系方式");
                    return;
                }
                if(TextUtils.isEmpty(verificationCodeET.getText().toString())||!code.equals(verificationCodeET.getText().toString())){
                    toast(ForgotPasswordActivity.this,"请输入正确的验证码");
                    return;
                }
                hiddenSoftInput();
                Intent intent=new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class);
                intent.putExtra("userPhoneNumber",telephoneET.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        telephoneET= (EditText) findViewById(R.id.telephoneET);
        verificationCodeET= (EditText) findViewById(R.id.verificationCodeET);
        telephoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(telephoneET.getText().toString())) {
                    commitB.setBackgroundResource(R.drawable.ic_bg_login_button_un);
                    commitB.setClickable(false);
                }
            }
        });


        verificationCodeTV= (TextView) findViewById(R.id.verificationCodeTV);
        verificationCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                request(telephoneET.getText().toString());
                timesUtils=new TimesUtils(60000,1000,verificationCodeTV,ForgotPasswordActivity.this);
                timesUtils.start();
            }
        });
    }


    /**
     * 网络请求
     */
    private void request(String phone) {
        if (!NetworkUtils.isNetworkAvailable(ForgotPasswordActivity.this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("userPhoneNumber", phone);
        String url= InitApp.getUrlByParameter(ApiConstants.GET_VERIFY_API, params, true);
        Log.e("ForgotPasswordURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ForgotPasswordRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                code=jsonObject.getJSONObject("info").getString("verificationcode");
                                commitB.setBackgroundResource(R.drawable.ic_bg_login_button);
                                commitB.setClickable(true);
                            }else{
                                toast(ForgotPasswordActivity.this,jsonObject.getString("msg"));
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
