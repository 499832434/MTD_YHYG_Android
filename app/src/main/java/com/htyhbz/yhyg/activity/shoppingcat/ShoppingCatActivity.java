package com.htyhbz.yhyg.activity.shoppingcat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
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
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.order.OrderSettlementActivity;
import com.htyhbz.yhyg.adapter.LeftMenuAdapter;
import com.htyhbz.yhyg.adapter.RightDishAdapter;
import com.htyhbz.yhyg.imp.ShopCartImp;
import com.htyhbz.yhyg.utils.PointFTypeEvaluator;
import com.htyhbz.yhyg.view.CustomTitleBar;
import com.htyhbz.yhyg.view.FakeAddImageView;
import com.htyhbz.yhyg.view.ShopCartCenterDialog;
import com.htyhbz.yhyg.view.ShopCartDialog;
import com.htyhbz.yhyg.vo.Dish;
import com.htyhbz.yhyg.vo.DishMenu;
import com.htyhbz.yhyg.vo.ShopCart;

import java.util.ArrayList;

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
    private DishMenu headMenu;
    private LeftMenuAdapter leftAdapter;
    private RightDishAdapter rightAdapter;
    private ArrayList<DishMenu> dishMenuList;//数据源
    private boolean leftClickType = false;//左侧菜单点击引发的右侧联动
    private ShopCart shopCart;
    //    private FakeAddImageView fakeAddImageView;
    private ImageView shoppingCartView;
    private FrameLayout shopingCartLayout;
    private TextView totalPriceTextView;
    private TextView totalPriceNumTextView;
    private RelativeLayout mainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shoppingcat);

        initData();
        initView();
        initAdapter();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        leftMenu.setLayoutManager(new LinearLayoutManager(this));
        rightMenu.setLayoutManager(new LinearLayoutManager(this));

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
                    DishMenu menu = rightAdapter.getMenuOfMenuByPosition(position);

                    if(leftClickType || !menu.getMenuName().equals(headMenu.getMenuName())) {
                        if (dy> 0 && headerLayout.getTranslationY()<=1 && headerLayout.getTranslationY()>= -1 * headerLayout.getMeasuredHeight()*4/5 && !leftClickType) {// underView.getTop()>9
                            int dealtY = underView.getTop() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        }
                        else if(dy<0 && headerLayout.getTranslationY()<=0 && !leftClickType) {
                            headerView.setText(menu.getMenuName());
                            int dealtY = underView.getBottom() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        }
                        else{
                            headerLayout.setTranslationY(0);
                            headMenu = menu;
                            headerView.setText(headMenu.getMenuName());
                            for (int i = 0; i < dishMenuList.size(); i++) {
                                if (dishMenuList.get(i) == headMenu) {
                                    leftAdapter.setSelectedNum(i);
                                    break;
                                }
                            }
                            if(leftClickType)leftClickType=false;
                            Log.e(TAG, "onScrolled: "+menu.getMenuName() );
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
        shopCart = new ShopCart();
        dishMenuList = new ArrayList<DishMenu>();
        ArrayList<Dish> dishs1 = new ArrayList<Dish>();
        dishs1.add(new Dish("烟花名称1",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称2",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称3",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称4",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称5",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称6",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称7",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称8",1.0,Integer.MAX_VALUE));
        dishs1.add(new Dish("烟花名称9",1.0,Integer.MAX_VALUE));
        DishMenu breakfast = new DishMenu("烟花类",dishs1);

        ArrayList<Dish> dishs2 = new ArrayList<Dish>();
        dishs2.add(new Dish("爆竹名称1",1.0,Integer.MAX_VALUE));
        dishs2.add(new Dish("爆竹名称2",1.0,Integer.MAX_VALUE));
        DishMenu launch = new DishMenu("爆竹类",dishs2);

        ArrayList<Dish> dishs3 = new ArrayList<Dish>();
        dishs3.add(new Dish("套餐名称1",1.0,Integer.MAX_VALUE));
        DishMenu evening = new DishMenu("套餐类",dishs3);

        ArrayList<Dish> dishs4 = new ArrayList<Dish>();
        dishs4.add(new Dish("小烟花名称1",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称2",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称3",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称4",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称5",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称6",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称7",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称8",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称9",1.0,Integer.MAX_VALUE));
        dishs4.add(new Dish("小烟花名称10",1.0,Integer.MAX_VALUE));
        DishMenu menu1 = new DishMenu("小烟花",dishs4);

        dishMenuList.add(breakfast);
        dishMenuList.add(launch);
        dishMenuList.add(evening);
        dishMenuList.add(menu1);
    }

    private void initAdapter(){
        leftAdapter = new LeftMenuAdapter(this,dishMenuList);
        rightAdapter = new RightDishAdapter(this,dishMenuList,shopCart);
        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        leftAdapter.addItemSelectedListener(this);
        rightAdapter.setShopCartImp(this);
        rightMenu.setItemAnimator(null);
        initHeadView();
    }

    private void initHeadView(){
        headMenu = rightAdapter.getMenuOfMenuByPosition(0);
        headerLayout.setContentDescription("0");
        headerView.setText(headMenu.getMenuName());
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
            DishMenu menu = rightAdapter.getMenuOfMenuByPosition(position+1);
            headMenu = menu;
            headerView.setText(headMenu.getMenuName());
            for (int i = 0; i < dishMenuList.size(); i++) {
                if (dishMenuList.get(i) == headMenu) {
                    leftAdapter.setSelectedNum(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onLeftItemSelected(int position, DishMenu menu) {
        int sum=0;
        for(int i = 0;i<position;i++){
            sum+=dishMenuList.get(i).getDishList().size()+1;
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

    public void showCenterCart(View view,Dish dish) {
        ShopCartCenterDialog dialog = new ShopCartCenterDialog(this, shopCart, dish,R.style.CommonDialog);
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
}
