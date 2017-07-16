package com.htyhbz.yhyg.vo;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class Dish {

    private String dishName;
    private double dishPrice;
    private int dishAmount;//产品可增加最大数
    private int dishRemain;//产品可删除最大数
    private int dishId;
    private int dishType;
    private String dishDetail;//产品简介
    private int orderdishCount;//产品选中数量
    private String dishPictureUrl;



    public int getOrderdishCount() {
        return orderdishCount;
    }

    public void setOrderdishCount(int orderdishCount) {
        this.orderdishCount = orderdishCount;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getDishType() {
        return dishType;
    }

    public void setDishType(int dishType) {
        this.dishType = dishType;
    }

    public String getDishDetail() {
        return dishDetail;
    }

    public void setDishDetail(String dishDetail) {
        this.dishDetail = dishDetail;
    }

    public String getDishPictureUrl() {
        return dishPictureUrl;
    }

    public void setDishPictureUrl(String dishPictureUrl) {
        this.dishPictureUrl = dishPictureUrl;
    }


    public Dish(String dishName,double dishPrice,int dishAmount){
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishAmount = dishAmount;
        this.dishRemain = dishAmount;
    }


    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public int getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(int dishAmount) {
        this.dishAmount = dishAmount;
    }

    public int getDishRemain() {
        return dishRemain;
    }

    public void setDishRemain(int dishRemain) {
        this.dishRemain = dishRemain;
    }

    public int hashCode(){
        int code = this.dishName.hashCode()+(int)this.dishPrice;
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this)return true;

        return obj instanceof Dish &&
                this.dishName.equals(((Dish)obj).dishName) &&
                this.dishPrice ==  ((Dish)obj).dishPrice &&
                this.dishAmount == ((Dish)obj).dishAmount &&
                this.dishRemain == ((Dish)obj).dishRemain;
    }
}
