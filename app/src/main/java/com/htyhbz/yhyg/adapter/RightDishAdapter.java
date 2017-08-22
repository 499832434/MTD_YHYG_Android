package com.htyhbz.yhyg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;
import com.htyhbz.yhyg.activity.shoppingcat.ShoppingCatActivity;
import com.htyhbz.yhyg.imp.ShopCartImp;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.ProductMenu;
import com.htyhbz.yhyg.vo.ShopCart;

import java.util.ArrayList;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class RightDishAdapter extends RecyclerView.Adapter {
    private final int MENU_TYPE = 0;
    private final int DISH_TYPE = 1;
    private final int HEAD_TYPE = 2;

    private Context mContext;
    private ArrayList<ProductMenu> mMenuList;
    private int mItemCount;
    private ShopCart shopCart;
    private ShopCartImp shopCartImp;
    private int checkPosition;

    public RightDishAdapter(Context mContext, ArrayList<ProductMenu> mMenuList, ShopCart shopCart,int checkPosition){
        this.mContext = mContext;
        this.mMenuList = mMenuList;
        this.mItemCount = mMenuList.size();
        this.shopCart = shopCart;
        for(ProductMenu menu:mMenuList){
            mItemCount+=menu.getProductList().size();
        }
        this.checkPosition=checkPosition;
    }

    public ShopCart getShopCart() {
        return shopCart;
    }

    public void setShopCart(ShopCart shopCart) {
        this.shopCart = shopCart;
    }

    @Override
    public int getItemViewType(int position) {
        int sum=0;
        for(ProductMenu menu:mMenuList){
            if(position==sum){
                return MENU_TYPE;
            }
            sum+=menu.getProductList().size()+1;
        }
        return DISH_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==MENU_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_menu_item, parent, false);
            MenuViewHolder viewHolder = new MenuViewHolder(view);
            return viewHolder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_dish_item, parent, false);
            DishViewHolder viewHolder = new DishViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position)==MENU_TYPE){
            MenuViewHolder menuholder = (MenuViewHolder)holder;
            if(menuholder!=null) {
                menuholder.right_menu_title.setText(getMenuByPosition(position).getCatagory().getCatalog());
                menuholder.right_menu_layout.setContentDescription(position+"");
            }
        }else {
            final DishViewHolder dishholder = (DishViewHolder) holder;
            if (dishholder != null) {

                final Product product = getDishByPosition(position);
                dishholder.right_dish_name_tv.setText(product.getproductName());
                dishholder.right_dish_price_tv.setText(product.getproductPrice() + "");
                dishholder.right_dish_layout.setContentDescription(position + "");
                if(!TextUtils.isEmpty(product.getproductPictureUrl())){
                    ((BaseActivity)mContext).getNetWorkPicture(product.getproductPictureUrl(),dishholder.pictureIV);
                }
                dishholder.right_dish_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPosition=position-1;
                        notifyDataSetChanged();
                        ((ShoppingCatActivity)mContext).showCenterCart(view, product);
                    }
                });
                int count = 0;
                if(shopCart.getShoppingSingleMap().containsKey(product)){
                    count = shopCart.getShoppingSingleMap().get(product);
                }
                if(count<=0){
                    dishholder.right_dish_remove_iv.setVisibility(View.GONE);
                    dishholder.right_dish_account_tv.setVisibility(View.GONE);
                }else {
                    dishholder.right_dish_remove_iv.setVisibility(View.VISIBLE);
                    dishholder.right_dish_account_tv.setVisibility(View.VISIBLE);
                    dishholder.right_dish_account_tv.setText(count+"");
                }
                dishholder.right_dish_add_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(shopCart.addShoppingSingle(product)) {
                            notifyItemChanged(position);
                            if(shopCartImp!=null)
                                shopCartImp.add(view,position);
                        }
                        checkPosition=position-1;
                        notifyDataSetChanged();
                    }
                });

                dishholder.right_dish_remove_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(shopCart.subShoppingSingle(product)){
                            notifyItemChanged(position);
                            if(shopCartImp!=null)
                                shopCartImp.remove(view,position);
                        }
                        checkPosition=position-1;
                        notifyDataSetChanged();
                    }
                });

                Log.e("checkPosition",checkPosition+"==="+position);
                if((checkPosition+1)==(position)){
                    dishholder.rightLL.setBackgroundColor(Color.parseColor("#cccccc"));
                }else{
                    dishholder.rightLL.setBackgroundColor(Color.parseColor("#00000000"));
                }
            }
        }
    }

    public ProductMenu getMenuByPosition(int position){
        int sum =0;
        for(ProductMenu menu:mMenuList){
            if(position==sum){
                return menu;
            }
            sum+=menu.getProductList().size()+1;
        }
        return null;
    }

    public Product getDishByPosition(int position){
        for(ProductMenu menu:mMenuList){
            if(position>0 && position<=menu.getProductList().size()){
                return menu.getProductList().get(position-1);
            }
            else{
                position-=menu.getProductList().size()+1;
            }
        }
        return null;
    }

    public ProductMenu getMenuOfMenuByPosition(int position){
        for(ProductMenu menu:mMenuList){
            if(position==0)return menu;
            if(position>0 && position<=menu.getProductList().size()){
                return menu;
            }
            else{
                position-=menu.getProductList().size()+1;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public ShopCartImp getShopCartImp() {
        return shopCartImp;
    }

    public void setShopCartImp(ShopCartImp shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout right_menu_layout;
        private TextView right_menu_title;
        public MenuViewHolder(View itemView) {
            super(itemView);
            right_menu_layout = (LinearLayout)itemView.findViewById(R.id.right_menu_item);
            right_menu_title = (TextView)itemView.findViewById(R.id.right_menu_tv);
        }
    }

    private class DishViewHolder extends RecyclerView.ViewHolder{
        private TextView right_dish_name_tv;
        private TextView right_dish_price_tv;
        private LinearLayout right_dish_layout;
        private ImageView right_dish_remove_iv;
        private ImageView right_dish_add_iv;
        private TextView right_dish_account_tv;
        private ImageView pictureIV;
        private LinearLayout rightLL;

        public DishViewHolder(View itemView) {
            super(itemView);
            pictureIV= (ImageView) itemView.findViewById(R.id.pictureIV);
            right_dish_name_tv = (TextView)itemView.findViewById(R.id.right_dish_name);
            right_dish_price_tv = (TextView)itemView.findViewById(R.id.right_dish_price);
            right_dish_layout = (LinearLayout)itemView.findViewById(R.id.right_dish_item);
            right_dish_remove_iv = (ImageView)itemView.findViewById(R.id.right_dish_remove);
            right_dish_add_iv = (ImageView)itemView.findViewById(R.id.right_dish_add);
            right_dish_account_tv = (TextView) itemView.findViewById(R.id.right_dish_account);
            rightLL= (LinearLayout) itemView.findViewById(R.id.rightLL);
        }
    }



}

