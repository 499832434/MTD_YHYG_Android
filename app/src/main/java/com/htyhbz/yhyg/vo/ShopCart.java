package com.htyhbz.yhyg.vo;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class ShopCart  {
    private int shoppingAccount;//商品总数
    private double shoppingTotalPrice;//商品总价钱
    private Map<Product,Integer> shoppingSingle;//单个物品的总价价钱

    public ShopCart(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle = new HashMap<Product,Integer>();
    }
    public ShopCart(int shoppingAccount,double shoppingTotalPrice,Map<Product,Integer> shoppingSingle){
        this.shoppingAccount = shoppingAccount;
        this.shoppingTotalPrice = shoppingTotalPrice;
        this.shoppingSingle = shoppingSingle;
    }

    public int getShoppingAccount() {
        return shoppingAccount;
    }

    public double getShoppingTotalPrice() {
        return shoppingTotalPrice;
    }

    public Map<Product, Integer> getShoppingSingleMap() {
        return shoppingSingle;
    }

    public boolean addShoppingSingle(Product product){
        int remain = product.getproductRemain();
        if(remain<=0)
            return false;
        product.setproductRemain(--remain);
        int num = 0;
        if(shoppingSingle.containsKey(product)){
            num = shoppingSingle.get(product);
        }
//        for(Product product1:shoppingSingle.keySet()){
//            if(product1.getproductId()==product.getproductId()){
//                num = shoppingSingle.get(product1);
//                break;
//            }
//        }
        num+=1;
        shoppingSingle.put(product,num);
//        Log.e("TAG", "addShoppingSingle: " + shoppingSingle.size()+"=="+product.getproductId());

        shoppingTotalPrice += product.getproductPrice();
        shoppingAccount++;
        Log.e("TAG+",shoppingAccount+"");
        return true;
    }

    public boolean subShoppingSingle(Product product){
        int num = 0;
        if(shoppingSingle.containsKey(product)){
            num = shoppingSingle.get(product);
        }
        if(num<=0) return false;
        num--;
        int remain = product.getproductRemain();
        product.setproductRemain(++remain);
        shoppingSingle.put(product,num);
        if (num ==0) shoppingSingle.remove(product);

        shoppingTotalPrice -= product.getproductPrice();
        shoppingAccount--;
        Log.e("TAG-",shoppingAccount+"");
        return true;
    }

    public int getProductAccount() {
        return shoppingSingle.size();
    }

    public void clear(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle.clear();
    }
}
