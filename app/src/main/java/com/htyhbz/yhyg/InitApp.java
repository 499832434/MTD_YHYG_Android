package com.htyhbz.yhyg;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.htyhbz.yhyg.service.LocationService;
import com.htyhbz.yhyg.utils.Md5;
import com.htyhbz.yhyg.utils.PackageUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/5.
 */
public class InitApp extends Application {
    public static final String USER_PRIVATE_DATA = "USER_PRIVATE_DATA";
    public static final String USER_NAME_KEY = "USER_NAME_KEY";
    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String AREA_ID_KEY = "AREA_ID_KEY";
    public static final String USER_AREA_KEY = "USER_AREA_KEY";
    public static final String USER_INTEGRAL_KEY = "USER_INTEGRAL_KEY";
    public static final String SCORE_SCALE_KEY = "SCORE_SCALE_KEY";
    public static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";
    public static final String SHOPPING_CAT_DATA = "SHOPPING_CAT_DATA";
    public static final String LONGITUDE_KEY = "LONGITUDE_KEY";//经度
    public static final String LATITUDE_KEY = "LATITUDE_KEY";//纬度
    public static final String USER_PREMISSION_KEY = "USER_PREMISSION_KEY";//用户权限

    public static LocationService locationService;
    public Vibrator mVibrator;
    public static InitApp initApp;
    public static String VERSION = "0";
    public static String PAGESIZE = "50";
    private Mac mac;
    private SecretKeySpec signingKey;
    public static final String HMAC_SHA_1 = "HmacSHA1";
    private static UUID uuid;
    public static String DEVICE_TOKEN = "";
    public static String PUBLIC_KEY = "bbb1694f0b8157dfbc2a521434466cac";
    public static final String PREFS_DEVICE_ID = "device_id";
    public static final String PREFS_FILE = "device_id.xml";
    private RequestQueue mRequestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }



    private void initApp() {
        initApp=this;
        VERSION = PackageUtils.getAppVersionName(this);
        initUserPref();
        initSignatureTools();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }



    public void initUserPref() {
        initDeviceId(getApplicationContext());
        DEVICE_TOKEN = new Md5(uuid == null ? "null" : uuid.toString()).compute();
    }

    public void initDeviceId(Context context) {
        if (uuid == null) {
            if (uuid == null) {
                final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);
                if (id != null) {
                    uuid = UUID.fromString(id);
                } else {
                    try {
                        initUUID(context, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        try {
                            initUUID(context);
                        } catch (Exception ex) {
                        }
                    }
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                }
            }
        }
    }

    private void initUUID(Context context) throws UnsupportedEncodingException {
        initUUID(context, null);
    }

    private void initUUID(Context context, String encoding) throws UnsupportedEncodingException {
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (encoding != null) {//encoding == null判断反
            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes(encoding)) : null;
        } else {
            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes()) : null;
        }
        if (uuid == null) {
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidId != null) {
                if (encoding != null) {//encoding == null判断反
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes(encoding));
                } else {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes());
                }

            } else {
                uuid = UUID.fromString("null");
            }
        }
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty

        RetryPolicy retryPolicy = new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(retryPolicy);

        getRequestQueue().add(req);
    }


    private String sort(List<String> reqParams) {
        Collections.sort(reqParams);
        StringBuilder sb = new StringBuilder();
        for (String item : reqParams) {
            sb.append(item);
        }
        Log.e("eee", sb.toString());
        return sb.toString();
    }


    public synchronized String getSig(Map<String, String> params) {
        List<String> paramsList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsList.add(entry.getKey() + entry.getValue());
        }
        byte[] rawHmac = null;
        try {
            rawHmac = mac.doFinal(sort(paramsList).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            rawHmac = mac.doFinal(sort(paramsList).getBytes());
        }
        String result = Base64.encodeToString(rawHmac, Base64.NO_WRAP);
        return result;
    }

    private void initSignatureTools() {
        try {
            signingKey = new SecretKeySpec(PUBLIC_KEY.getBytes("UTF-8"), HMAC_SHA_1);
        } catch (UnsupportedEncodingException e) {
            signingKey = new SecretKeySpec(PUBLIC_KEY.getBytes(), HMAC_SHA_1);
        }
        try {
            mac = Mac.getInstance(HMAC_SHA_1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            mac.init(signingKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 化工积分key加密
     * */
    public static String  getUrlByParameter(String subjectUrl,HashMap<String,String> map,Boolean isSign){
        try {
            StringBuffer sb=new StringBuffer(subjectUrl);
            int i=0;
            for(String key:map.keySet()){
                if(i==0){
                    sb.append(key+"="+URLEncoder.encode(map.get(key), "utf-8"));
                }else {
                    sb.append("&"+key+"="+URLEncoder.encode(map.get(key), "utf-8"));
                }
                i++;
            }
            if(isSign){
                sb.append("&sign="+ URLEncoder.encode(initApp.getSig(map), "utf-8"));
            }
            return sb.toString();
//            return URLEncoder.encode(sb.toString(), "utf-8");
        }catch (Exception e){
            return "";
        }
    }

    public static String HMACSHA256(byte[] data)
    {
        try  {
            SecretKeySpec signingKey = new SecretKeySpec(PUBLIC_KEY.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

}
