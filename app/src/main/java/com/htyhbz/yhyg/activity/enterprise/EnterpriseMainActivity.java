package com.htyhbz.yhyg.activity.enterprise;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
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
import com.htyhbz.yhyg.activity.login.LoginActivity;
import com.htyhbz.yhyg.adapter.EnterpriseAdapter;
import com.htyhbz.yhyg.adapter.RecordAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.WheelView;
import com.htyhbz.yhyg.vo.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zongshuo on 2017/7/17.
 */
public class EnterpriseMainActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private ListView enterpriseLV;
    private List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
    private EnterpriseAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private String flag = "";
    private TextView titleTV, cityTV;
    private List<Province> provincesList = new ArrayList<Province>();
    private int townId=-1;
    private int position1=0,position2=0,position3=0;
    private int pageIndex=1;

    protected ArrayList<String> mProvinceDatas=new ArrayList<String>();
    protected Map<String, ArrayList<String>> mCitisDatasMap = new HashMap<String, ArrayList<String>>();
    protected Map<String, ArrayList<String>> mDistrictDatasMap = new HashMap<String, ArrayList<String>>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName;
    private View contentView;
    private PopupWindow addrPopWindow;
    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mCountyPicker;
    private LinearLayout boxBtnCancel;
    private LinearLayout boxBtnOk;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_main);
        initData();
        initView();
        initPopupWindow();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
        }
    }

    private void initView() {
        titleTV = (TextView) findViewById(R.id.titleTextView);
        cityTV = (TextView) findViewById(R.id.cityTV);
        cityTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTown();
            }
        });
        if ("2".equals(flag)) {
            titleTV.setText("批发企业");
        } else {
            titleTV.setText("厂家");
        }
        findViewById(R.id.unLoginTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginOutDialog();
            }
        });
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        enterpriseLV = (ListView) findViewById(R.id.swipe_target);
        enterpriseLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EnterpriseMainActivity.this, EnterpriseDetailActivity.class);
                intent.putExtra("flag", flag);
                intent.putExtra("title",enterpriseList.get(i).getEnterpriseName());
                intent.putExtra("enterpriseID",enterpriseList.get(i).getEnterpriseID());
                startActivity(intent);
            }
        });
        adapter = new EnterpriseAdapter(EnterpriseMainActivity.this, enterpriseList);
        enterpriseLV.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });

    }

    private void initPopupWindow(){
        contentView = LayoutInflater.from(this).inflate(
                R.layout.addr_picker, null);
        mProvincePicker = (WheelView) contentView.findViewById(R.id.province);
        mCityPicker = (WheelView) contentView.findViewById(R.id.city);
        mCountyPicker = (WheelView) contentView.findViewById(R.id.county);
        boxBtnCancel = (LinearLayout) contentView.findViewById(R.id.box_btn_cancel);
        boxBtnOk = (LinearLayout) contentView.findViewById(R.id.box_btn_ok);
        addrPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //addrPopWindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
        addrPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                position1=id;
                String provinceText = mProvinceDatas.get(id);
                if (!mCurrentProviceName.equals(provinceText)) {
                    mCurrentProviceName = provinceText;
                    ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                    mCityPicker.resetData(mCityData);
                    mCityPicker.setDefault(0);
                    mCurrentCityName = mCityData.get(0);

                    ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mDistrictData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mDistrictData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                position2 = id;
                ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                String cityText = mCityData.get(id);
                if (!mCurrentCityName.equals(cityText)) {
                    mCurrentCityName = cityText;
                    ArrayList<String> mCountyData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mCountyData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mCountyData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCountyPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                position3 = id;
                ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                String districtText = mDistrictData.get(id);
                if (!mCurrentDistrictName.equals(districtText)) {
                    mCurrentDistrictName = districtText;
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        boxBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrPopWindow.dismiss();
            }
        });

        boxBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaaa",position1+"=="+position2+"=="+position3);
                String addr = mCurrentProviceName;
                townId = provincesList.get(position1).getId();
                if (!mCurrentCityName.equals("无")) {
                    addr = addr + "-" + mCurrentCityName;
                    townId = provincesList.get(position1).getList().get(position2).getId();
                }
                if (!mCurrentDistrictName.equals("无")) {
                    addr = addr + "-" + mCurrentDistrictName;
                    townId = provincesList.get(position1).getList().get(position2).getList().get(position3).getId();
                }
                cityTV.setText(Html.fromHtml("<u>"+addr+"</u>"));
                addrPopWindow.dismiss();

                enterpriseList.clear();
                getEnterpriseList();
            }
        });



    }
    @Override
    public void onLoadMore() {
        pageIndex++;
        getEnterpriseList();
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        getEnterpriseList();
    }

    /**
     * 网络请求
     */
    private void getTown() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        final HashMap<String, String> params = getNetworkRequestHashMap();
        params.put("userID", getUserInfo(0));
        String url="";
        if ("2".equals(flag)) {
            url = InitApp.getUrlByParameter(ApiConstants.GET_ENTERPRISE_CITY_API, params, true);
        } else {
            url = InitApp.getUrlByParameter(ApiConstants.GET_FACTORY_CITY_API, params, true);
        }
        Log.e("getTownURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getTownRe", response);
                        try {
                            mProvinceDatas.clear();
                            mCitisDatasMap.clear();
                            mDistrictDatasMap.clear();
                            provincesList.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray array1 = jsonObject.getJSONArray("info");
                                for (int i = 0; i < array1.length(); i++) {
                                    JSONObject obj1 = array1.getJSONObject(i);
                                    Province province = new Province();
                                    province.setId(obj1.getInt("id"));
                                    province.setName(obj1.getString("name"));
                                    mProvinceDatas.add(obj1.getString("name"));
                                    JSONArray array2 = obj1.getJSONArray("sub");
                                    List<City> cityList = new ArrayList<City>();
                                    ArrayList<String> list2 = new ArrayList<String>();
                                    for (int j = 0; j < array2.length(); j++) {
                                        JSONObject obj2 = array2.getJSONObject(j);
                                        City city = new City();
                                        city.setName(obj2.getString("name"));
                                        list2.add(obj2.getString("name"));
                                        city.setId(obj2.getInt("id"));
                                        city.setPid(obj2.getInt("pid"));
                                        JSONArray array3 = obj2.getJSONArray("sub");
                                        List<District> districtList = new ArrayList<District>();
                                        ArrayList<String> list3 = new ArrayList<String>();
                                        for (int z = 0; z < array3.length(); z++) {
                                            JSONObject obj3 = array3.getJSONObject(z);
                                            District district = new District();
                                            district.setPid(obj3.getInt("pid"));
                                            district.setId(obj3.getInt("id"));
                                            district.setName(obj3.getString("name"));
                                            list3.add(obj3.getString("name"));
                                            districtList.add(district);
                                        }
                                        if(list3.size()==0){
                                            list3.add("无");
                                        }
                                        mDistrictDatasMap.put(obj2.getString("name"),list3);
                                        city.setList(districtList);
                                        cityList.add(city);
                                    }
                                    if(list2.size()==0){
                                        list2.add("无");
                                        ArrayList<String> list3 = new ArrayList<String>();
                                        list3.add("无");
                                        mDistrictDatasMap.put("无",list3);
                                    }
                                    mCitisDatasMap.put(obj1.getString("name"),list2);
                                    province.setList(cityList);
                                    provincesList.add(province);
                                }

                                if(provincesList.size()>0){
                                    setData();
                                    addrPopWindow.showAtLocation(findViewById(R.id.LL), Gravity.BOTTOM, 0, 0);
                                }

                            } else {
                                toast(EnterpriseMainActivity.this, jsonObject.getString("msg"));
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

    private void getEnterpriseList() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        final HashMap<String, String> params = getNetworkRequestHashMap();
        params.put("userID", getUserInfo(0));
        if(townId==-1){
            params.put("townID", "");
        }else{
            params.put("townID", townId+"");
        }
        params.put("pageIndex", pageIndex+"");
        params.put("pageSize", InitApp.PAGESIZE);
        String url="";
        if ("2".equals(flag)) {
            url = InitApp.getUrlByParameter(ApiConstants.ENTERPRISE_CITY_LIST_API, params, true);
        }else{
            url = InitApp.getUrlByParameter(ApiConstants.ENTERPRISE_FACTORY_LIST_API, params, true);
        }
        Log.e("getEnterpriseListURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getEnterpriseListRe", response);
                        try {
                            if(1==pageIndex){
                                enterpriseList.clear();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray array=jsonObject.getJSONArray("info");
                                for(int i=0;i<array.length();i++){
                                    JSONObject obj=array.getJSONObject(i);
                                    Enterprise enterprise=new Enterprise();
                                    enterprise.setEnterpriseAddress(obj.getString("enterpriseAddress"));
                                    enterprise.setEnterpriseID(obj.getInt("enterpriseID"));
                                    if(TextUtils.isEmpty(obj.getString("enterpriseImageUrl"))||"null".equals(obj.getString("enterpriseImageUrl"))){
                                        enterprise.setEnterpriseImageUrl("");
                                    }else{
                                        enterprise.setEnterpriseImageUrl(ApiConstants.BASE_URL + obj.getString("enterpriseImageUrl"));
                                    }
                                    enterprise.setEnterpriseName(obj.getString("enterpriseName"));
                                    enterprise.setEnterprisePhone(obj.getString("enterprisePhone"));
                                    enterpriseList.add(enterprise);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                toast(EnterpriseMainActivity.this, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
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

    private void setData(){
        position3=0;
        position2=0;
        position1=0;
        mProvincePicker.setData(mProvinceDatas);
        mProvincePicker.setDefault(0);
        mCurrentProviceName = mProvinceDatas.get(0);

        ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
        mCityPicker.setData(mCityData);
        mCityPicker.setDefault(0);
        mCurrentCityName = mCityData.get(0);

        ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
        mCountyPicker.setData(mDistrictData);
        mCountyPicker.setDefault(0);
        mCurrentDistrictName = mDistrictData.get(0);
    }


}
