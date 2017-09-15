package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
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

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Created by zongshuo on 2017/7/3.
 */
public class AccountFragment extends Fragment implements View.OnClickListener{
    private View currentView = null;
    private TextView unLoginTV,integralTV;
    protected MainActivity mActivity;
    private TextView clearTV;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        unLoginTV= (TextView) currentView.findViewById(R.id.unLoginTV);
        integralTV= (TextView) currentView.findViewById(R.id.integralTV);
        clearTV= (TextView) currentView.findViewById(R.id.clearTV);
        unLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.showLoginOutDialog();
            }
        });
        ((TextView) currentView.findViewById(R.id.accountTV)).setText("账号:"+mActivity.getUserInfo(8));
        currentView.findViewById(R.id.integralLiftingRL).setOnClickListener(this);
        currentView.findViewById(R.id.orderSummaryRL).setOnClickListener(this);
        currentView.findViewById(R.id.currentRecordRL).setOnClickListener(this);
        currentView.findViewById(R.id.modifyPasswordRL).setOnClickListener(this);
        currentView.findViewById(R.id.myCollectionRL).setOnClickListener(this);
        currentView.findViewById(R.id.clearRL).setOnClickListener(this);
        currentView.findViewById(R.id.versionRL).setOnClickListener(this);
        ((TextView)currentView.findViewById(R.id.versionTV)).setText(InitApp.VERSION);

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
            case R.id.clearRL:
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Glide.get(getContext()).clearDiskCache();
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.toast(mActivity,"缓存清理完毕");
                                    clearTV.setText("0.00B");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.versionRL:
                mActivity.checkUpdate(mActivity,true);
                break;
        }
    }


    /**
     * 网络请求
     */
    public void getUserIntegral() {
        try {
            new GetSizeTask(clearTV).execute(new File(mActivity.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
        }catch (Exception e){

        }
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

    @Override
    public void onResume() {
        super.onResume();
        getUserIntegral();
    }


    class GetSizeTask extends AsyncTask<File, Long, Long> {
        private final TextView resultView;
        public GetSizeTask(TextView resultView) { this.resultView = resultView; }
        @Override protected void onPreExecute() { resultView.setText("Calculating..."); }
        @Override protected void onProgressUpdate(Long... values) { /* onPostExecute(values[values.length - 1]); */ }
        @Override protected Long doInBackground(File... dirs) {
            try {
                long totalSize = 0;
                for (File dir : dirs) {
                    publishProgress(totalSize);
                    totalSize += calculateSize(dir);
                }
                return totalSize;
            } catch (RuntimeException ex) {
                long totalSize = -1;
                return totalSize;
            }
        }

        @Override protected void onPostExecute(Long size) {
            String sizeText = android.text.format.Formatter.formatFileSize(resultView.getContext(), size);
            resultView.setText(sizeText);
        }
        private  long calculateSize(File dir) {
            if (dir == null) return 0;
            if (!dir.isDirectory()) return dir.length();
            long result = 0;
            File[] children = dir.listFiles();
            if (children != null)
                for (File child : children)
                    result += calculateSize(child);
            return result;
        }
    }


}
