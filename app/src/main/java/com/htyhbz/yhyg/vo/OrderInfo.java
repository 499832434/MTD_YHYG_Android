package com.htyhbz.yhyg.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/13.
 */
public class OrderInfo {
    private int orderID;
    private String orderSendTime;
    private int useIntegralCount;
    private int orderType;
    private int orderAllPrice;
    private List<Product> list =new ArrayList<Product>();

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderSendTime() {
        return orderSendTime;
    }

    public void setOrderSendTime(String orderSendTime) {
        this.orderSendTime = orderSendTime;
    }

    public int getUseIntegralCount() {
        return useIntegralCount;
    }

    public void setUseIntegralCount(int useIntegralCount) {
        this.useIntegralCount = useIntegralCount;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderAllPrice() {
        return orderAllPrice;
    }

    public void setOrderAllPrice(int orderAllPrice) {
        this.orderAllPrice = orderAllPrice;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
}
