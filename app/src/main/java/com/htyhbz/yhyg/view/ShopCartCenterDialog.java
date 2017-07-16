package com.htyhbz.yhyg.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.shoppingcat.ShoppingCatActivity;
import com.htyhbz.yhyg.adapter.PopupDishAdapter;
import com.htyhbz.yhyg.imp.ShopCartImp;
import com.htyhbz.yhyg.vo.Dish;
import com.htyhbz.yhyg.vo.ShopCart;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class ShopCartCenterDialog extends Dialog  {

    private ShopCart shopCart;
    private Dish dish;
    private ShopCartCneterDialogImp shopCartDialogImp;
    private TextView showTV;
    private RelativeLayout showRL;
    private ImageView addIV,removeIV;
    private TextView numTV;
    private Context context;

    public ShopCartCenterDialog(Context context, ShopCart shopCart, Dish dish,int themeResId) {
        super(context, themeResId);
        this.shopCart = shopCart;
        this.dish = dish;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_center_popupview);
        initView();
        initData();
    }

    private void initView(){
        showTV= (TextView) findViewById(R.id.showTV);
        showTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shopCart!=null &&shopCart.addShoppingSingle(dish)) {
                    if(shopCart.getShoppingAccount()>0){
                        numTV.setText(shopCart.getShoppingSingleMap().get(dish)+"");
                        ((ShoppingCatActivity)context).showTotalPrice();
                        showTV.setVisibility(View.GONE);
                        showRL.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        showRL= (RelativeLayout) findViewById(R.id.showRL);
        addIV= (ImageView) findViewById(R.id.right_dish_add);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopCart != null && shopCart.addShoppingSingle(dish)) {
                    if (shopCart.getShoppingAccount() > 0) {
                        numTV.setText(shopCart.getShoppingSingleMap().get(dish) + "");
                        showTV.setVisibility(View.GONE);
                        showRL.setVisibility(View.VISIBLE);
                    } else {
                        showTV.setVisibility(View.VISIBLE);
                        showRL.setVisibility(View.GONE);
                    }
                    ((ShoppingCatActivity) context).showTotalPrice();
                }

            }
        });
        removeIV= (ImageView) findViewById(R.id.right_dish_remove);
        removeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shopCart!=null &&shopCart.subShoppingSingle(dish)){
                    if(shopCart.getShoppingAccount()>0){
                        numTV.setText(shopCart.getShoppingSingleMap().get(dish) + "");
                        showTV.setVisibility(View.GONE);
                        showRL.setVisibility(View.VISIBLE);
                    }else{
                        showTV.setVisibility(View.VISIBLE);
                        showRL.setVisibility(View.GONE);
                    }
                    ((ShoppingCatActivity)context).showTotalPrice();
                }

            }
        });
        numTV= (TextView) findViewById(R.id.right_dish_account);
    }

    private void initData(){
        if(shopCart!=null && shopCart.getShoppingSingleMap().get(dish)!=null){
            numTV.setText(shopCart.getShoppingSingleMap().get(dish)+"");
            showTV.setVisibility(View.GONE);
            showRL.setVisibility(View.VISIBLE);
        }else{
            showTV.setVisibility(View.VISIBLE);
            showRL.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        super.show();
        //animationShow(500);
    }

    @Override
    public void dismiss() {
        //animationHide(500);
        if(shopCartDialogImp!=null){
            shopCartDialogImp.dialogCenterDismiss();
        }
        ShopCartCenterDialog.super.dismiss();
    }



    public ShopCartCneterDialogImp getShopCartDialogImp() {
        return shopCartDialogImp;
    }

    public void setShopCartDialogImp(ShopCartCneterDialogImp shopCartDialogImp) {
        this.shopCartDialogImp = shopCartDialogImp;
    }



    public interface ShopCartCneterDialogImp{
        public void dialogCenterDismiss();
    }

}