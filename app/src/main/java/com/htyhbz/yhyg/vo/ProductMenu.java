package com.htyhbz.yhyg.vo;

import java.util.ArrayList;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class ProductMenu {
    private String menuName;
    private Catagory catagory;
    private ArrayList<Product> productList;

    public ProductMenu(){

    }

    public Catagory getCatagory() {
        return catagory;
    }

    public void setCatagory(Catagory catagory) {
        this.catagory = catagory;
    }

    public ProductMenu(Catagory menuName, ArrayList dishList){
        this.catagory = menuName;
        this.productList = dishList;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
