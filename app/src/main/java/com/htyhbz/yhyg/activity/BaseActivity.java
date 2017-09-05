package com.htyhbz.yhyg.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.service.LocationService;
import com.htyhbz.yhyg.utils.PackageUtils;
import com.htyhbz.yhyg.utils.PrefUtils;
import com.htyhbz.yhyg.view.ShopCartDialog;
import com.htyhbz.yhyg.view.ShopLoginOutDialog;
import com.htyhbz.yhyg.view.StatusBarCompat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by zongshuo on 2017/7/3.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private LocationService locationService;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#F4A100"));
//            StatusBarCompat.translucentStatusBar(this);
//        }
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
            case 8:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_NAME_KEY, "");
            case 9:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PREMISSION_KEY, "");
        }
        return  "";
    }


    /**
     * 退出登录
     */
    public void clearLoginInfo(){
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_ID_KEY, "");
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
        Glide.with(BaseActivity.this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
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


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public  void callPhone(String number){
        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));
        startActivity(intent);
    }

    /**
     * 显示异常提示
     *
     * @param i 0 服务器开小差
     *          1 抱歉，没有相关搜索结果
     *          2 网络不给力
     *          3 没有连接网络
     *          4 无相关内容展示
     *          5 你尚未收藏内容
     */
    public void showErrorLayout(final View view, View.OnClickListener errorListener, int i) {
        switch (i) {
//            case 0:
//                setErrorLayout(view, R.drawable.no_server_error, "服务器开小差了，请稍后再试", "点击屏幕刷新", errorListener);
//                break;
//            case 1:
//                setErrorLayout(view, R.drawable.no_data_error, "抱歉，没有相关搜索结果", "", errorListener);
//                break;
//            case 2:
//                setErrorLayout(view, R.drawable.no_network_instability_error, "网络不给力，请稍后重试", "点击屏幕刷新", errorListener);
//                break;
//            case 3:
//                setErrorLayout(view, R.drawable.no_network_error, "没有连接网络", "请连接之后，点击屏幕刷新", errorListener);
//                break;
            case 4:
                setErrorLayout(view, R.drawable.icon_empty, "无相关内容展示", "点击屏幕刷新", errorListener);
                break;
            case 5:
                setErrorLayout(view, R.drawable.no_collect_error, "您尚未收藏内容", "", errorListener);
                break;
            case 6:
                setErrorLayout(view, R.drawable.no_collect_error, "无法查看更多相关信息", "", errorListener);
                break;
            default:
                break;
        }
    }
    /**
     * 修改异常布局
     */

    private void setErrorLayout(View view, int errorImage, String errorTitle, String errorText, View.OnClickListener errorListener) {
        ((ImageView) view.findViewById(R.id.errorImageView)).setImageDrawable(getResources().getDrawable(errorImage));
        ((TextView) view.findViewById(R.id.detailTextView)).setText(errorTitle);
        ((TextView) view.findViewById(R.id.errorTextView)).setText(errorText);
        if (errorListener != null)
            view.setOnClickListener(errorListener);
    }


    public void showLoginOutDialog() {
        ShopLoginOutDialog dialog = new ShopLoginOutDialog(this,R.style.cartdialog);
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount =0.5f;
        window.setAttributes(params);
    }


    /**
     * 检查更新
     *
     * @param isShowDialog 如果已经是最新版本，是否显示提示信息 ， true 显示 ，false 不显示
     */
    public void checkUpdate(final Context context, final boolean isShowDialog) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        String url= InitApp.getUrlByParameter(ApiConstants.GET_VERSION_API, params, true);
        Log.e("checkUpdateURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("checkUpdateRe", response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            if ("0".equalsIgnoreCase(jo.getString("code"))) {
                                if (!TextUtils.isEmpty(jo.getString("info"))) {
                                    JSONObject info = jo.getJSONObject("info");
                                    String memo = info.getString("appVersionMsg");
                                    int vcode = info.getInt("appVersion");
                                    if (vcode > PackageUtils.getAppVersionCode(context)) {
                                        final String url = info.getString("appUrl");
                                        StringBuffer sb = new StringBuffer();
//                                        sb.append("新版本将会有更好的服务和用户体验，建议您下载升级新版本。");
                                        String[] memos = null;
                                        if (memo != null) {
                                            memos = memo.split("&&");
                                        }
                                        for (String m : memos) {
                                            if (!"".equals(m)) {
                                                sb.append("\n");
                                                sb.append(m);
                                            }
                                        }
                                        Dialog dialog = new AlertDialog.Builder(context)
                                                .setTitle("客户端升级")
                                                .setMessage(sb.toString())
                                                .setPositiveButton("确定",// 设置确定按钮
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                update(context, url);
                                                            }
                                                        })
                                                .setNegativeButton("以后再说",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                                dialog.dismiss();
                                                            }
                                                        }).create();
                                        dialog.show();
                                    } else {
                                        if (isShowDialog)
                                            Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
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

    /**
     * 更新下载
     */
    private void update(Context context, String url) {
        try {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                Toast.makeText(context, "存储卡暂时不可用，无法下载", Toast.LENGTH_LONG).show();
                return;
            }
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                String name = Environment.getExternalStorageDirectory().getAbsolutePath();
                name += "/scidownload/";
                File dir = new File(name);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Uri dest = Uri.fromFile(new File(name + PackageUtils.getPackageName(context) + ".apk"));
                request.setDestinationUri(dest);
                request.setTitle("正在下载新版本烟花易购客户端");
                request.setDescription("烟花易购客户端升级，更精彩");
                request.setMimeType("application/vnd.android.package-archive");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                long downloadId = downloadManager.enqueue(request);
                PrefUtils.putLong(context, InitApp.DOWNLOAD_TASK_PREF, InitApp.DOWNLOAD_TASK_ID_KEY, downloadId);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }
}
