package com.htyhbz.yhyg.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.Toast;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
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
    private Marker mMarkerA;
    private String enterpriseLongitude,enterpriseLatitude;
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
        mActivity.getPositon();
        enterpriseLongitude = getArguments().getString(LONGITUDE);
        enterpriseLatitude = getArguments().getString(LATITUDE);
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
                    mMarkerA= (Marker) mBaiduMap.addOverlay(ooA);
                    mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                        public boolean onMarkerClick(final Marker marker) {
                            if (marker == mMarkerA) {
                                startNavi();
                            }
                            return true;
                        }
                    });
                }

            }
        }, 500);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (EnterpriseDetailActivity)context;
    }


    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        LatLng pt1 = new LatLng(Double.parseDouble(mActivity.getUserInfo(7)),Double.parseDouble(mActivity.getUserInfo(6)));
        LatLng pt2 = new LatLng(Double.parseDouble(enterpriseLatitude),Double.parseDouble(enterpriseLongitude));

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapNavi(para, mActivity);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(mActivity);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }



}
