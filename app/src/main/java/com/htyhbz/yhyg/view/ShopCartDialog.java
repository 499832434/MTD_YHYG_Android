package com.htyhbz.yhyg.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.adapter.PopupDishAdapter;
import com.htyhbz.yhyg.imp.ShopCartImp;
import com.htyhbz.yhyg.vo.ShopCart;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class ShopCartDialog  extends Dialog implements View.OnClickListener,ShopCartImp {

    private LinearLayout linearLayout,bottomLayout,clearLayout;
    private FrameLayout shopingcartLayout;
    private ShopCart shopCart;
    private TextView totalPriceTextView,shoppingCatCommitTextView;
    private TextView totalPriceNumTextView;
    private RecyclerView recyclerView;
    private PopupDishAdapter dishAdapter;
    private ShopCartDialogImp shopCartDialogImp;

    public ShopCartDialog(Context context, ShopCart shopCart,int themeResId) {
        super(context,themeResId);
        this.shopCart = shopCart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_popupview);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        clearLayout = (LinearLayout)findViewById(R.id.clear_layout);
        shopingcartLayout = (FrameLayout)findViewById(R.id.shopping_cart_layout);
        bottomLayout = (LinearLayout)findViewById(R.id.shopping_cart_bottom);
        totalPriceTextView = (TextView)findViewById(R.id.shopping_cart_total_tv);
        shoppingCatCommitTextView= (TextView) findViewById(R.id.shopping_cart_commit_tv);
        totalPriceNumTextView = (TextView)findViewById(R.id.shopping_cart_total_num);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setItemAnimator(null);
        shopingcartLayout.setOnClickListener(this);
        bottomLayout.setOnClickListener(this);
        clearLayout.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dishAdapter = new PopupDishAdapter(getContext(),shopCart);
        recyclerView.setAdapter(dishAdapter);
        dishAdapter.setShopCartImp(this);
        shoppingCatCommitTextView.setOnClickListener(this);
        showTotalPrice();

        findViewById(R.id.mainRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        animationShow(500);
    }

    @Override
    public void dismiss() {
        animationHide(500);
    }

    private void showTotalPrice(){
        if(shopCart!=null && shopCart.getShoppingTotalPrice()>0){
            totalPriceTextView.setVisibility(View.VISIBLE);
            totalPriceTextView.setText("共￥ " + shopCart.getShoppingTotalPrice());
            totalPriceNumTextView.setVisibility(View.VISIBLE);
            totalPriceNumTextView.setText("" + shopCart.getShoppingAccount());
            shopingcartLayout.setBackgroundResource(R.drawable.circle_checked);
            shoppingCatCommitTextView.setVisibility(View.VISIBLE);
        }else {
            totalPriceTextView.setVisibility(View.GONE);
            totalPriceNumTextView.setVisibility(View.GONE);
            shopingcartLayout.setBackgroundResource(R.drawable.circle_uncheck);
            shoppingCatCommitTextView.setVisibility(View.GONE);
        }
    }

    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 1000, 0).setDuration(mDuration)
        );
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY",0,1000).setDuration(mDuration)
        );
        animatorSet.start();

        if(shopCartDialogImp!=null){
            shopCartDialogImp.dialogDismiss();
        }

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ShopCartDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shopping_cart_bottom:
            case R.id.shopping_cart_layout:
                this.dismiss();
                break;
            case R.id.clear_layout:
                clear();
                break;
            case R.id.shopping_cart_commit_tv:
                break;
        }
    }

    @Override
    public void add(View view, int postion) {
        showTotalPrice();
    }

    @Override
    public void remove(View view, int postion) {
        showTotalPrice();
        if(shopCart.getShoppingAccount()==0){
            this.dismiss();
        }
    }

    public ShopCartDialogImp getShopCartDialogImp() {
        return shopCartDialogImp;
    }

    public void setShopCartDialogImp(ShopCartDialogImp shopCartDialogImp) {
        this.shopCartDialogImp = shopCartDialogImp;
    }

    public interface ShopCartDialogImp{
        public void dialogDismiss();
    }

    public void clear(){
        shopCart.clear();
        showTotalPrice();
        if(shopCart.getShoppingAccount()==0){
            this.dismiss();
        }
    }
}
