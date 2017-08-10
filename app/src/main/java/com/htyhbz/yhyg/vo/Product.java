package com.htyhbz.yhyg.vo;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class Product {

    private String productName;
    private double productPrice;
    private int productAmount=Integer.MAX_VALUE;//产品可增加最大数
    private int productRemain=Integer.MAX_VALUE;//产品可删除最大数
    private int productId;
    private int productType;
    private String productDetail;//产品简介
    private int orderProductCount;//产品选中数量
    private String productPictureUrl;
    private String productVideoUrl;
    private int isCollected;

    public String getProductVideoUrl() {
        return productVideoUrl;
    }

    public void setProductVideoUrl(String productVideoUrl) {
        this.productVideoUrl = productVideoUrl;
    }

    public int getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(int isCollected) {
        this.isCollected = isCollected;
    }

    public int getorderProductCount() {
        return orderProductCount;
    }

    public void setorderProductCount(int orderProductCount) {
        this.orderProductCount = orderProductCount;
    }

    public int getproductId() {
        return productId;
    }

    public void setproductId(int productId) {
        this.productId = productId;
    }

    public int getproductType() {
        return productType;
    }

    public void setproductType(int productType) {
        this.productType = productType;
    }

    public String getproductDetail() {
        return productDetail;
    }

    public void setproductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getproductPictureUrl() {
        return productPictureUrl;
    }

    public void setproductPictureUrl(String productPictureUrl) {
        this.productPictureUrl = productPictureUrl;
    }


    public Product(String productName, double productPrice, int productAmount){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
        this.productRemain = productAmount;
    }
    public Product(){
    }


    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public double getproductPrice() {
        return productPrice;
    }

    public void setproductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getproductAmount() {
        return productAmount;
    }

    public void setproductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getproductRemain() {
        return productRemain;
    }

    public void setproductRemain(int productRemain) {
        this.productRemain = productRemain;
    }

    public int hashCode(){
        int code = this.productName.hashCode()+(int)this.productPrice;
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this)return true;

        return obj instanceof Product &&
                this.productName.equals(((Product)obj).productName) &&
                this.productPrice ==  ((Product)obj).productPrice &&
                this.productAmount == ((Product)obj).productAmount &&
                this.productRemain == ((Product)obj).productRemain;
    }
}
