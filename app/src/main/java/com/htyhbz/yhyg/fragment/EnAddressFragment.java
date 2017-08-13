package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.enterprise.EnterpriseDetailActivity;


/**
 * Created by zongshuo on 2017/7/6.
 */
public class EnAddressFragment extends Fragment {
    private View currentView = null;
    private EnterpriseDetailActivity mActivity;
    private final static String LONGITUDE = "LONGITUDE";
    private final static String LATITUDE = "LATITUDE";
    SupportMapFragment map;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_en_address, container, false);
        initView();
        return currentView;
    }

    public static EnAddressFragment newInstance(String param1,String param2) {
        EnAddressFragment fragment = new EnAddressFragment();
        Bundle args = new Bundle();
        args.putString(LONGITUDE,param1);
        args.putString(LATITUDE,param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        String enterpriseLongitude = getArguments().getString(LONGITUDE);
        String enterpriseLatitude = getArguments().getString(LATITUDE);
        if(TextUtils.isEmpty(enterpriseLongitude)||TextUtils.isEmpty(enterpriseLatitude)){
            return;
        }
        if("null".equals(enterpriseLongitude)||"null".equals(enterpriseLatitude)){
            return;
        }
        Log.e("address",enterpriseLongitude+"=="+enterpriseLatitude);
        Intent intent = mActivity.getIntent();
        MapStatus.Builder builder = new MapStatus.Builder();
        final  LatLng p = new LatLng(Double.parseDouble(enterpriseLatitude),Double.parseDouble(enterpriseLongitude));
        builder.target(p);
        builder.overlook(-20).zoom(15);
        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(builder.build())
                .compassEnabled(false).zoomControlsEnabled(false);
        map = SupportMapFragment.newInstance(bo);
        FragmentManager manager = mActivity.getSupportFragmentManager();
        manager.beginTransaction().add(R.id.map, map, "map_fragment").commit();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (map.getMapView() == null) {
                    handler.postDelayed(this, 500);
                } else {
                    mBaiduMap = map.getMapView().getMap();
                    MarkerOptions ooA = new MarkerOptions().position(p).icon(bdA)
                            .zIndex(9).draggable(true);
                    mBaiduMap.addOverlay(ooA);
                }

            }
        }, 500);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (EnterpriseDetailActivity)context;
    }



}
