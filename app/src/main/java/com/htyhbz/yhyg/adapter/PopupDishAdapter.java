package com.htyhbz.yhyg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.shoppingcat.ShoppingCatActivity;
import com.htyhbz.yhyg.imp.ShopCartImp;
import com.htyhbz.yhyg.utils.DensityUtil;
import com.htyhbz.yhyg.vo.Product;
import com.htyhbz.yhyg.vo.ShopCart;

import java.util.ArrayList;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class PopupDishAdapter extends RecyclerView.Adapter{

    private static String TAG = "PopupDishAdapter";
    private ShopCart shopCart;
    private Context context;
    private int itemCount;
    private ArrayList<Product> productList;
    private ShopCartImp shopCartImp;
    private RelativeLayout mainRL;

    public PopupDishAdapter(Context context, ShopCart shopCart,RelativeLayout mainRL){
        this.shopCart = shopCart;
        this.context = context;
        this.itemCount = shopCart.getProductAccount();
        this.productList = new ArrayList<Product>();
        this.mainRL=mainRL;
        productList.addAll(shopCart.getShoppingSingleMap().keySet());
        Log.e(TAG, "PopupDishAdapter: " + this.itemCount);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_dish_item, parent, false);
        DishViewHolder viewHolder = new DishViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DishViewHolder dishholder = (DishViewHolder)holder;
        final Product product = getDishByPosition(position);
        if(product !=null) {
            dishholder.right_dish_name_tv.setText(product.getproductName());
            dishholder.right_dish_price_tv.setText(product.getProductPrice() + "");
            int num = shopCart.getShoppingSingleMap().get(product);
            dishholder.right_dish_account_tv.setText(num+"");

            dishholder.right_dish_add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shopCart.addShoppingSingle(product)) {
                        notifyItemChanged(position);
                        if(shopCartImp!=null)
                            shopCartImp.add(view,position);
                    }
                }
            });

            dishholder.right_dish_remove_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shopCart.subShoppingSingle(product)){
                        productList.clear();
                        productList.addAll(shopCart.getShoppingSingleMap().keySet());
                        itemCount = shopCart.getProductAccount();;
                        notifyDataSetChanged();
                        if(shopCartImp!=null)
                            shopCartImp.remove(view,position);
//                        android.view.ViewGroup.LayoutParams params= mainRL.getLayoutParams();
//                        if(shopCart.getShoppingSingleMap().size()==1){
//                            params.height= DensityUtil.dip2px(context,90+50);
//                        }else if(shopCart.getShoppingSingleMap().size()==2){
//                            params.height=DensityUtil.dip2px(context,90+50*2);
//                        }else if(shopCart.getShoppingSingleMap().size()==3){
//                            params.height=DensityUtil.dip2px(context,90+50*3);
//                        }else{
//                            params.height=DensityUtil.dip2px(context,90+50*4);
//                        }
//                        mainRL.setLayoutParams(params);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.itemCount;
    }

    public Product getDishByPosition(int position){
        return productList.get(position);
    }

    public ShopCartImp getShopCartImp() {
        return shopCartImp;
    }

    public void setShopCartImp(ShopCartImp shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    private class DishViewHolder extends RecyclerView.ViewHolder{
        private TextView right_dish_name_tv;
        private TextView right_dish_price_tv;
        private LinearLayout right_dish_layout;
        private ImageView right_dish_remove_iv;
        private ImageView right_dish_add_iv;
        private TextView right_dish_account_tv;

        public DishViewHolder(View itemView) {
            super(itemView);
            right_dish_name_tv = (TextView)itemView.findViewById(R.id.right_dish_name);
            right_dish_price_tv = (TextView)itemView.findViewById(R.id.right_dish_price);
            right_dish_layout = (LinearLayout)itemView.findViewById(R.id.right_dish_item);
            right_dish_remove_iv = (ImageView)itemView.findViewById(R.id.right_dish_remove);
            right_dish_add_iv = (ImageView)itemView.findViewById(R.id.right_dish_add);
            right_dish_account_tv = (TextView) itemView.findViewById(R.id.right_dish_account);
        }

    }
}
