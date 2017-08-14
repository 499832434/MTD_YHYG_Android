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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.service.LocationService;
import com.htyhbz.yhyg.utils.PrefUtils;

import java.util.HashMap;

/**
 * Created by zongshuo on 2017/7/3.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private LocationService locationService;


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
        params.put("longitude", getUserInfo(6));
        params.put("latitude", getUserInfo(7));
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
            case 4:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.SHOPPING_CAT_DATA, "");
            case 5:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_AREA_KEY, "");
            case 6:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.LONGITUDE_KEY, "");
            case 7:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.LATITUDE_KEY, "");
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


    public void getPositon(){
        // -----------location config ------------
        locationService = InitApp.initApp.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = BaseActivity.this.getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                    Toast.makeText(BaseActivity.this,"gps定位成功",Toast.LENGTH_SHORT).show();
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                    Toast.makeText(BaseActivity.this,"网络定位成功",Toast.LENGTH_SHORT).show();
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    Toast.makeText(BaseActivity.this,"离线定位成功，离线定位结果也是有效的",Toast.LENGTH_SHORT).show();
//                } else if (location.getLocType() == BDLocation.TypeServerError) {
//                    Toast.makeText(BaseActivity.this,"服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因",Toast.LENGTH_SHORT).show();
//                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                    Toast.makeText(BaseActivity.this,"网络不同导致定位失败，请检查网络是否通畅",Toast.LENGTH_SHORT).show();
//                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                    Toast.makeText(BaseActivity.this,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机",Toast.LENGTH_SHORT).show();
//                }
                logMsg(location.getLatitude(),location.getLongitude());
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };


    public void logMsg(double Latitude ,double Longitude) {
        try {
            PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.LONGITUDE_KEY, Longitude+"");
            PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.LATITUDE_KEY, Latitude+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
