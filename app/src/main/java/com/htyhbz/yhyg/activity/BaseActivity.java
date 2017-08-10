package com.htyhbz.yhyg.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.utils.PrefUtils;

import java.util.HashMap;

/**
 * Created by zongshuo on 2017/7/3.
 */
public abstract class BaseActivity extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }





    /**
     * 拨打电话
     */
    public void call(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 隐藏键盘
     */
    public  void hiddenSoftInput() {
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void toast(Context context,String str){
        if(!TextUtils.isEmpty(str)){
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取网络请求参数
     */
    public HashMap<String,String> getNetworkRequestHashMap() {
        String userid= PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_ID_KEY, "");
        String accesstoken=PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.ACCESS_TOKEN_KEY, "");
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("uuid", InitApp.DEVICE_TOKEN);
        params.put("systemVersion", android.os.Build.VERSION.RELEASE);
        params.put("appVersion",InitApp.VERSION);
        params.put("platform", Build.MODEL);
        params.put("systemType", "Android");
        params.put("requestTime",System.currentTimeMillis()+"");
        params.put("longitude", "1");
        params.put("latitude", "1");
        if(!TextUtils.isEmpty(accesstoken)){
            params.put("token", accesstoken);
        }
        if(!TextUtils.isEmpty(userid)){
            params.put("userID", userid);
        }
        return params;
    }

    /**
     * 获取用户的登录信息
     */
    public String getUserInfo(int flag){
        switch (flag){
            case 0:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_ID_KEY, "");
            case 1:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.AREA_ID_KEY, "");
            case 2:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_INTEGRAL_KEY, "");
            case 3:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.SCORE_SCALE_KEY, "");
        }
        return  "";
    }

    /**
     * 判断用户是否登录
     */

    public boolean getUserIsLogin(){
        String phone=PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_ID_KEY, "");
        if(TextUtils.isEmpty(phone)){
            return false;
        }
        return  true;
    }

    public void getNetWorkPicture(String url,ImageView imageView){
        Glide.with(BaseActivity.this).load(url).into(imageView);
    }
}
