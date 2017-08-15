package com.htyhbz.yhyg.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/13.
 */
public class OrderInfo {
    private String orderID;
    private String orderSendTime;
    private String useIntegralCount;
    private String orderType;
    private String orderAllPrice;
    private String actualPayPrice;
    private List<Product> list =new ArrayList<Product>();

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderSendTime() {
        return orderSendTime;
    }

    public void setOrderSendTime(String orderSendTime) {
        this.orderSendTime = orderSendTime;
    }

    public String getUseIntegralCount() {
        return useIntegralCount;
    }

    public void setUseIntegralCount(String useIntegralCount) {
        this.useIntegralCount = useIntegralCount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderAllPrice() {
        return orderAllPrice;
    }

    public void setOrderAllPrice(String orderAllPrice) {
        this.orderAllPrice = orderAllPrice;
    }

    public String getActualPayPrice() {
        return actualPayPrice;
    }

    public void setActualPayPrice(String actualPayPrice) {
        this.actualPayPrice = actualPayPrice;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
}
