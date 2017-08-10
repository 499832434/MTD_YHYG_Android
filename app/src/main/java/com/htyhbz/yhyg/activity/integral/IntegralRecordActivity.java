package com.htyhbz.yhyg.activity.integral;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.adapter.RecordAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.WithDrawal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/17.
 */
public class IntegralRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private ListView recordLV;
    private List<WithDrawal> list=new ArrayList<WithDrawal>();
    private RecordAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int pageIndex=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_record);

        initView();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        recordLV= (ListView) findViewById(R.id.swipe_target);
        adapter=new RecordAdapter(IntegralRecordActivity.this,list);
        recordLV.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
        pageIndex++;
        request();
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        request();
    }

    /**
     * 网络请求
     */
    private void request() {
        if (!NetworkUtils.isNetworkAvailable(IntegralRecordActivity.this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("userID", getUserInfo(0));
        params.put("pageIndex", pageIndex+"");
        params.put("pageSize", InitApp.PAGESIZE);
        String url=InitApp.getUrlByParameter(ApiConstants.WITH_DRAW_RECORD_API,params,true);
        Log.e("RecordURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RecordRe", response);
                        try {
                            if(1==pageIndex){
                                list.clear();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj= (JSONObject) infoArr.get(i);
                                    WithDrawal withDrawal=new WithDrawal();
                                    withDrawal.setWithdrawalAmount(obj.getInt("withdrawalAmount"));
                                    withDrawal.setWithdrawalStatus(obj.getString("withdrawalStatus"));
                                    withDrawal.setWithdrawalTime(obj.getString("withdrawalTime"));
                                    list.add(withDrawal);
                                }
                                if(list.size()>0){
                                    adapter.notifyDataSetChanged();
                                }
                            }else{
//                                toast(IntegralRecordActivity.this,jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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
