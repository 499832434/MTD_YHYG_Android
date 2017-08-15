package com.htyhbz.yhyg.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zongshuo on 2017/7/13.
 */
public class UserInfo implements Parcelable {
    private String orderAddress;
    private String orderDetailAddress;
    private String orderSendTime;
    private String receiverPhone;
    private String receiverName;
    private String mark;
    private String townId;

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderDetailAddress() {
        return orderDetailAddress;
    }

    public void setOrderDetailAddress(String orderDetailAddress) {
        this.orderDetailAddress = orderDetailAddress;
    }

    public String getOrderSendTime() {
        return orderSendTime;
    }

    public void setOrderSendTime(String orderSendTime) {
        this.orderSendTime = orderSendTime;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderAddress);
        parcel.writeString(orderDetailAddress);
        parcel.writeString(orderSendTime);
        parcel.writeString(receiverPhone);
        parcel.writeString(receiverName);
        parcel.writeString(mark);
        parcel.writeString(townId);
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Creator(){

        @Override
        public UserInfo createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            UserInfo userInfo = new UserInfo();
            userInfo.setOrderAddress(source.readString());
            userInfo.setOrderDetailAddress(source.readString());
            userInfo.setOrderSendTime(source.readString());
            userInfo.setReceiverPhone(source.readString());
            userInfo.setReceiverName(source.readString());
            userInfo.setMark(source.readString());
            userInfo.setTownId(source.readString());
            return userInfo;
        }

        @Override
        public UserInfo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new UserInfo[size];
        }
    };
}
