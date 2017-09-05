package com.htyhbz.yhyg.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListView;
import android.widget.Toast;
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
import com.htyhbz.yhyg.activity.MainActivity;
import com.htyhbz.yhyg.adapter.OrderTypeAdapter;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.view.MyListView;
import com.htyhbz.yhyg.vo.OrderInfo;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/6.
 */
public class OrderTypeFragment extends ErrorsFragment implements OnRefreshListener, OnLoadMoreListener {
    private View currentView = null;
    private MyListView orderTypeLV;
    private OrderTypeAdapter adapter;
    private List<OrderInfo>  orderInfoList=new ArrayList<OrderInfo>();
    private List<UserInfo>  userInfoList=new ArrayList<UserInfo>();
    private MainActivity mActivity;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int pageIndex=1;
    private final static String ORDERTYPE = "orderType";
    private int orderType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_order_type, container, false);
        initView();
        return currentView;
    }

    public static OrderTypeFragment newInstance(int param1) {
        OrderTypeFragment fragment = new OrderTypeFragment();
        Bundle args = new Bundle();
        args.putInt(ORDERTYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        orderType=getArguments().getInt(ORDERTYPE);
        orderTypeLV= (MyListView) currentView.findViewById(R.id.listView);
        adapter=new OrderTypeAdapter(mActivity,orderInfoList,userInfoList);
        orderTypeLV.setAdapter(adapter);

        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }

    @Override
    public void onLoadMore() {
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
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        params.put("userID", mActivity.getUserInfo(0));
        params.put("orderType", orderType+"");
        params.put("pageIndex", pageIndex+"");
        params.put("pageSize", InitApp.PAGESIZE);
        String url=InitApp.getUrlByParameter(ApiConstants.ORDER_LIST_API,params,true);
        Log.e("orderListURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("orderListRe", response);
                        try {
                            if(1==pageIndex){
                                orderInfoList.clear();
                                userInfoList.clear();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj1= (JSONObject) infoArr.get(i);
                                    JSONObject obj=obj1.getJSONObject("orderInfo");
                                    OrderInfo info=new OrderInfo();
                                    info.setOrderID(obj.getString("orderID"));
                                    info.setOrderSendTime(obj.getString("orderTime"));
                                    info.setUseIntegralCount(obj.getString("useIntegralCount"));
                                    info.setOrderAllPrice(obj.getDouble("orderAllPrice")+"");
                                    info.setOrderType(obj.getString("orderType"));
                                    info.setActualPayPrice(obj.getDouble("actualPayPrice")+"");
                                    JSONArray productArr=obj.getJSONArray("orderProductions");
                                    List<Product> productList=new ArrayList<Product>();
                                    for(int j=0;j<productArr.length();j++){
                                        JSONObject obj3=productArr.getJSONObject(j);
                                        Product product=new Product();
                                        product.setproductId(obj3.getInt("productId"));
                                        product.setproductName(obj3.getString("productName"));
                                        product.setProductPrice(obj3.getDouble("productPrice"));
                                        if(TextUtils.isEmpty(obj3.getString("productPictureUrl"))||"null".equals(obj3.getString("productPictureUrl"))){
                                            product.setproductPictureUrl("");
                                        }else{
                                            product.setproductPictureUrl(ApiConstants.BASE_URL+obj3.getString("productPictureUrl"));
                                        }
                                        product.setorderProductCount(obj3.getInt("orderProductionsCount"));
                                        productList.add(product);
                                    }
                                    info.setList(productList);
                                    orderInfoList.add(info);

                                    JSONObject obj2=obj1.getJSONObject("userInfo");
                                    UserInfo userInfo=new UserInfo();
                                    userInfo.setTownId(obj2.getString("townID"));
                                    userInfo.setOrderAddress(obj2.getString("orderAddress"));
                                    userInfo.setOrderDetailAddress(obj2.getString("orderDetailAddress"));
                                    userInfo.setOrderSendTime(obj2.getString("orderSendTime"));
                                    userInfo.setReceiverPhone(obj2.getString("receiverPhone"));
                                    userInfo.setReceiverName(obj2.getString("receiverName"));
                                    userInfo.setMark(obj2.getString("mark"));
                                    userInfoList.add(userInfo);
                                }
                                if(orderInfoList.size()==0){
                                    orderTypeLV.setVisibility(View.GONE);
                                    currentView.findViewById(R.id.layoutError).setVisibility(View.VISIBLE);
                                    showErrorLayout(currentView.findViewById(R.id.layoutError), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            swipeToLoadLayout.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    swipeToLoadLayout.setRefreshing(true);
                                                }
                                            });
                                        }
                                    },4);
                                }else {
                                    orderTypeLV.setVisibility(View.VISIBLE);
                                    currentView.findViewById(R.id.layoutError).setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                            }else{
                                mActivity.toast(mActivity, jsonObject.getString("msg"));
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

    /**
     * 网络请求
     */
    public void deleteOrder(String orderID,final View v, final int index) {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        final HashMap<String,String> params=mActivity.getNetworkRequestHashMap();
        params.put("userID", mActivity.getUserInfo(0));
        params.put("orderID", orderID);
        String url=InitApp.getUrlByParameter(ApiConstants.DELETE_ORDER_API,params,true);
        Log.e("deleteOrderURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("deleteOrderRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
//                                pageIndex=1;
//                                request();
                                deleteShow(v, index);
                            }else{
                                mActivity.toast(mActivity, jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {

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

    private void deleteShow(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                orderInfoList.remove(index);
                userInfoList.remove(index);

                OrderTypeAdapter.ViewHolder vh = (OrderTypeAdapter.ViewHolder)v.getTag();
                vh.needInflate = true;
                if (orderInfoList.size() > 0) {
                    orderTypeLV.setVisibility(View.VISIBLE);
                    currentView.findViewById(R.id.layoutError).setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    orderTypeLV.setVisibility(View.GONE);
                    currentView.findViewById(R.id.layoutError).setVisibility(View.VISIBLE);
                    showErrorLayout(currentView.findViewById(R.id.layoutError), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            swipeToLoadLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeToLoadLayout.setRefreshing(true);
                                }
                            });
                        }
                    },4);
                }
                Toast.makeText(getActivity(),"订单已删除", Toast.LENGTH_SHORT).show();
            }
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        };

        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                }
                else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al!=null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
}
