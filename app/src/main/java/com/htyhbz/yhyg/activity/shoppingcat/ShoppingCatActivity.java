package com.htyhbz.yhyg.activity.shoppingcat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.htyhbz.yhyg.ApiConstants;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.order.OrderSettlementActivity;
import com.htyhbz.yhyg.adapter.LeftMenuAdapter;
import com.htyhbz.yhyg.adapter.RightDishAdapter;
import com.htyhbz.yhyg.imp.ShopCartImp;
import com.htyhbz.yhyg.net.HighRequest;
import com.htyhbz.yhyg.net.NetworkUtils;
import com.htyhbz.yhyg.utils.PointFTypeEvaluator;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.FakeAddImageView;
import com.htyhbz.yhyg.view.ShopCartCenterDialog;
import com.htyhbz.yhyg.view.ShopCartDialog;
import com.htyhbz.yhyg.vo.Catagory;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.ProductMenu;
import com.htyhbz.yhyg.vo.ShopCart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class ShoppingCatActivity extends BaseActivity implements LeftMenuAdapter.onItemSelectedListener,ShopCartImp,ShopCartDialog.ShopCartDialogImp,ShopCartCenterDialog.ShopCartCneterDialogImp{
    private final static String TAG = "MainActivity";
    private RecyclerView leftMenu;//左侧菜单栏
    private RecyclerView rightMenu;//右侧菜单栏
    private TextView headerView,shoppingCatCommitTextView;
    private LinearLayout headerLayout;//右侧菜单栏最上面的菜单
    private LinearLayout bottomLayout;
    private ProductMenu headMenu;
    private LeftMenuAdapter leftAdapter;
    private RightDishAdapter rightAdapter;
    private ArrayList<ProductMenu> productMenuList;//数据源
    private boolean leftClickType = false;//左侧菜单点击引发的右侧联动
    private ShopCart shopCart;
    //    private FakeAddImageView fakeAddImageView;
    private ImageView shoppingCartView;
    private FrameLayout shopingCartLayout;
    private TextView totalPriceTextView;
    private TextView totalPriceNumTextView;
    private RelativeLayout mainLayout;
    private int categoryId,productId,productType;
    private LinearLayoutManager rightMangear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shoppingcat);

        initData();
        initView();
//        initAdapter();
        getShoppingCartList();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopCart cat=rightAdapter.getShopCart();
                HashMap<String,Object> map=new HashMap<String, Object>();
                map.put("shoppingAccount",cat.getShoppingAccount());
                map.put("shoppingTotalPrice", cat.getShoppingTotalPrice());
                Map<Product,Integer> shoppingSingle=cat.getShoppingSingleMap();
                ArrayList<HashMap> list=new ArrayList<HashMap>();
                for(Product product:shoppingSingle.keySet()){
                    HashMap<String,Object> map1=new HashMap<String, Object>();
                    map1.put("shoppingsingleTotal",shoppingSingle.get(product));
                    map1.put("productId",product.getproductId());
                    map1.put("productName",product.getproductName());
                    map1.put("productDetail",product.getproductDetail());
                    map1.put("productPictureUrl",product.getproductPictureUrl());
                    map1.put("isCollected",product.getIsCollected());
                    map1.put("productVideoUrl",product.getProductVideoUrl());
                    map1.put("productPrice",product.getproductPrice());
                    list.add(map1);
                }
                map.put("shoppinglist",list);
                finish();
            }
        });
        mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        leftMenu = (RecyclerView)findViewById(R.id.left_menu);
        rightMenu = (RecyclerView)findViewById(R.id.right_menu);
        headerView = (TextView)findViewById(R.id.right_menu_tv);
        headerLayout = (LinearLayout)findViewById(R.id.right_menu_item);
//        fakeAddImageView = (FakeAddImageView)findViewById(R.id.right_dish_fake_add);
        bottomLayout = (LinearLayout)findViewById(R.id.shopping_cart_bottom);
        shoppingCartView = (ImageView) findViewById(R.id.shopping_cart);
        shopingCartLayout = (FrameLayout) findViewById(R.id.shopping_cart_layout);
        totalPriceTextView = (TextView)findViewById(R.id.shopping_cart_total_tv);
        totalPriceNumTextView = (TextView)findViewById(R.id.shopping_cart_total_num);
        shoppingCatCommitTextView= (TextView) findViewById(R.id.shopping_cart_commit_tv);
        shoppingCatCommitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShoppingCatActivity.this, OrderSettlementActivity.class));
            }
        });
        rightMangear=new LinearLayoutManager(this);
        leftMenu.setLayoutManager(new LinearLayoutManager(this));
        rightMenu.setLayoutManager(rightMangear);

        rightMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if( recyclerView.canScrollVertically(1)==false) {//无法下滑
                    showHeadView();
                    return;
                }
                View underView = null;
                if(dy>0)
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(),headerLayout.getMeasuredHeight()+1);
                else
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(),0);
                if(underView!=null && underView.getContentDescription()!=null ){
                    int position = Integer.parseInt(underView.getContentDescription().toString());
                    ProductMenu menu = rightAdapter.getMenuOfMenuByPosition(position);

                    if(leftClickType || !menu.getCatagory().getCatalog().equals(headMenu.getCatagory().getCatalog())) {
                        if (dy> 0 && headerLayout.getTranslationY()<=1 && headerLayout.getTranslationY()>= -1 * headerLayout.getMeasuredHeight()*4/5 && !leftClickType) {// underView.getTop()>9
                            int dealtY = underView.getTop() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        }
                        else if(dy<0 && headerLayout.getTranslationY()<=0 && !leftClickType) {
                            headerView.setText(menu.getCatagory().getCatalog());
                            int dealtY = underView.getBottom() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        }
                        else{
                            headerLayout.setTranslationY(0);
                            headMenu = menu;
                            headerView.setText(headMenu.getCatagory().getCatalog());
                            for (int i = 0; i < productMenuList.size(); i++) {
                                if (productMenuList.get(i) == headMenu) {
                                    leftAdapter.setSelectedNum(i);
                                    break;
                                }
                            }
                            if(leftClickType)leftClickType=false;
                            Log.e(TAG, "onScrolled: "+menu.getCatagory().getCatalog());
                        }
                    }
                }
            }
        });

        shopingCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCart(view);
            }
        });
    }

    private void initData(){
        categoryId=getIntent().getIntExtra("categoryId",-1);
        productId=getIntent().getIntExtra("productId",-1);
        productType=getIntent().getIntExtra("productType",-1);
        shopCart = new ShopCart();
        productMenuList = new ArrayList<ProductMenu>();
//        ArrayList<Product> dishs1 = new ArrayList<Product>();
//        dishs1.add(new Product("烟花名称1",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称2",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称3",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称4",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称5",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称6",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称7",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称8",1.0,Integer.MAX_VALUE));
//        dishs1.add(new Product("烟花名称9",1.0,Integer.MAX_VALUE));
//        Catagory cata1= new Catagory();
//        cata1.setCatalog("烟花类");
//        ProductMenu breakfast = new ProductMenu(cata1,dishs1);
//
//        ArrayList<Product> dishs2 = new ArrayList<Product>();
//        dishs2.add(new Product("爆竹名称1",1.0,Integer.MAX_VALUE));
//        dishs2.add(new Product("爆竹名称2", 1.0, Integer.MAX_VALUE));
//        Catagory cata2= new Catagory();
//        cata2.setCatalog("爆竹类");
//        ProductMenu launch = new ProductMenu(cata2,dishs2);
//
//        ArrayList<Product> dishs3 = new ArrayList<Product>();
//        dishs3.add(new Product("套餐名称1", 1.0, Integer.MAX_VALUE));
//        Catagory cata3= new Catagory();
//        cata3.setCatalog("套餐类");
//        ProductMenu evening = new ProductMenu(cata3,dishs3);
//
//
//
//        ArrayList<Product> dishs4 = new ArrayList<Product>();
//        dishs4.add(new Product("小烟花名称1",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称2",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称3",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称4",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称5",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称6",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称7",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称8", 1.0, Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称9",1.0,Integer.MAX_VALUE));
//        dishs4.add(new Product("小烟花名称10", 1.0, Integer.MAX_VALUE));
//        Catagory cata4= new Catagory();
//        cata4.setCatalog("小烟花");
//        ProductMenu menu1 = new ProductMenu(cata4,dishs4);
//
//
//        productMenuList.add(breakfast);
//        productMenuList.add(launch);
//        productMenuList.add(evening);
//        productMenuList.add(menu1);

    }

    private void initAdapter(int leftPosition,int rightPosition){
        Log.e("categoryId++",leftPosition+"=="+rightPosition);

        leftAdapter = new LeftMenuAdapter(this, productMenuList);
        rightAdapter = new RightDishAdapter(this, productMenuList,shopCart);
        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        leftAdapter.addItemSelectedListener(this);
        rightAdapter.setShopCartImp(this);
        rightMenu.setItemAnimator(null);
        initHeadView(leftPosition,rightPosition);
    }

    private void initHeadView(int leftPosition,int rightPosition){
//        leftAdapter.setSelectedNum(leftPosition);
        headMenu = rightAdapter.getMenuOfMenuByPosition(0);
        headerLayout.setContentDescription("0");
        headerView.setText(headMenu.getCatagory().getCatalog());
        if(-1==productId){
            onLeftItemSelected(leftPosition,null);
        }else{
            onLeftItemSelected(leftPosition,null);
            MoveToPosition(rightMangear, rightMenu, rightPosition);
        }
//        MoveToPosition(rightMangear,rightMenu,rightPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leftAdapter.removeItemSelectedListener(this);
    }

    private void showHeadView(){
        headerLayout.setTranslationY(0);
        View underView = rightMenu.findChildViewUnder(headerView.getX(),0);
        if(underView!=null && underView.getContentDescription()!=null){
            int position = Integer.parseInt(underView.getContentDescription().toString());
            ProductMenu menu = rightAdapter.getMenuOfMenuByPosition(position);
            headMenu = menu;
            headerView.setText(headMenu.getCatagory().getCatalog());
//            for (int i = 0; i < productMenuList.size(); i++) {
//                if (productMenuList.get(i) == headMenu) {
//                    leftAdapter.setSelectedNum(i);
//                    break;
//                }
//            }
        }
    }

    @Override
    public void onLeftItemSelected(int position, ProductMenu menu) {
        int sum=0;
        for(int i = 0;i<position;i++){
            sum+= productMenuList.get(i).getProductList().size()+1;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) rightMenu.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(sum,0);
        leftClickType = true;
        leftAdapter.setSelectedNum(position);
    }

    @Override
    public void add(View view,int position) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        shoppingCartView.getLocationInWindow(cartLocation);
        rightMenu.getLocationInWindow(recycleLocation);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1]-recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1]-recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;

        final FakeAddImageView fakeAddImageView = new FakeAddImageView(this);
        mainLayout.addView(fakeAddImageView);
        fakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue);
        fakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.setVisibility(View.VISIBLE);
        ObjectAnimator addAnimator = ObjectAnimator.ofObject(fakeAddImageView, "mPointF",
                new PointFTypeEvaluator(controlP), startP, endP);
        addAnimator.setInterpolator(new AccelerateInterpolator());
        addAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fakeAddImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fakeAddImageView.setVisibility(View.GONE);
                mainLayout.removeView(fakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(shoppingCartView,"scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(shoppingCartView,"scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();

        showTotalPrice();
    }

    @Override
    public void remove(View view,int position) {
        showTotalPrice();
    }

    public void showTotalPrice(){
        if(shopCart!=null && shopCart.getShoppingTotalPrice()>0){
            totalPriceTextView.setVisibility(View.VISIBLE);
            totalPriceTextView.setText("共￥ " + shopCart.getShoppingTotalPrice());
            totalPriceNumTextView.setVisibility(View.VISIBLE);
            totalPriceNumTextView.setText("" + shopCart.getShoppingAccount());
            shopingCartLayout.setBackgroundResource(R.drawable.circle_checked);
            shoppingCatCommitTextView.setVisibility(View.VISIBLE);
        }else {
            totalPriceTextView.setVisibility(View.GONE);
            totalPriceNumTextView.setVisibility(View.GONE);
            shopingCartLayout.setBackgroundResource(R.drawable.circle_uncheck);
            shoppingCatCommitTextView.setVisibility(View.GONE);
        }
    }

    private void showCart(View view) {
        if(shopCart!=null && shopCart.getShoppingAccount()>0){
            ShopCartDialog dialog = new ShopCartDialog(this,shopCart,R.style.cartdialog);
            Window window = dialog.getWindow();
            dialog.setShopCartDialogImp(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.show();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            params.dimAmount =0.5f;
            window.setAttributes(params);
        }
    }

    public void showCenterCart(View view,Product product) {
        ShopCartCenterDialog dialog = new ShopCartCenterDialog(this, shopCart, product,R.style.CommonDialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        dialog.setShopCartDialogImp(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        showTotalPrice();
        rightAdapter.notifyDataSetChanged();
    }

    @Override
    public void dialogCenterDismiss() {
        showTotalPrice();
        rightAdapter.notifyDataSetChanged();
    }


    /**
     * 网络请求
     */
    private void getShoppingCartList() {
        if (!NetworkUtils.isNetworkAvailable(ShoppingCatActivity.this)) {
            return;
        }

        final HashMap<String,String> params=getNetworkRequestHashMap();
        params.put("userID", getUserInfo(0));
        params.put("areaID", getUserInfo(1));
        String url= InitApp.getUrlByParameter(ApiConstants.SHOPPING_CART_LIST_API, params, true);
        Log.e("getShoppingCartListURl", url);

        HighRequest request = new HighRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getShoppingCartListRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                int rightPosition = 0;
                                int leftPosition = 0;
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                int num=0;
                                for(int i=0;i<infoArr.length();i++){
                                    num+=1;
                                    ProductMenu menu=new ProductMenu();
                                    JSONObject obj= (JSONObject) infoArr.get(i);
                                    Catagory cata=new Catagory();
                                    cata.setCatalog(obj.getString("catalog"));
                                    cata.setCategoryId(obj.getString("categoryId"));
                                    cata.setCategoryImageUrl(obj.getString("categoryImageUrl"));
                                    JSONArray dataArr=obj.getJSONArray("data");
                                    if(categoryId==obj.getInt("categoryId")){
                                        leftPosition=i;
                                    }
                                    if(productType==obj.getInt("categoryId")){
                                        leftPosition=i;
                                    }
                                    ArrayList<Product> productList=new ArrayList<Product>();
                                    for(int j=0;j<dataArr.length();j++){
                                        num+=1;
                                        JSONObject data=dataArr.getJSONObject(j);
                                        Product product=new Product();
                                        if(productId==data.getInt("productId")){
                                            rightPosition=num-2;
                                        }
                                        product.setproductId(data.getInt("productId"));
                                        product.setproductName(data.getString("productName"));
                                        product.setproductDetail(data.getString("productDetail"));
                                        product.setproductPictureUrl(ApiConstants.BASE_URL + data.getString("productPictureUrl"));
                                        product.setIsCollected(data.getInt("isCollected"));
                                        product.setProductVideoUrl(data.getString("productVideoUrl"));
                                        product.setproductPrice(data.getDouble("productPrice"));
                                        productList.add(product);
                                    }
                                    menu.setCatagory(cata);
                                    menu.setProductList(productList);
                                    productMenuList.add(menu);
                                }
                                if(productMenuList.size()>0){
                                  initAdapter(leftPosition,rightPosition);
                                }
                            }else{
                                toast(ShoppingCatActivity.this,jsonObject.getString("msg"));
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

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }
}
