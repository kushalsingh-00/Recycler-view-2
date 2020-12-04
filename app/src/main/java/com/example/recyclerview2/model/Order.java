package com.example.recyclerview2.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    public int status;
    public String orderId;
    public Timestamp timeStamp;

    public String userName, userPhoneNo, userAddress;
    public Map<String, CartItem> map;

    public int subTotal;

    public Order(){}


    public Order(String userName, String userAddress, String userPhoneNo, HashMap<String, CartItem> map, int subTotal, int status) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoneNo=userPhoneNo;
        this.map = map;
        this.subTotal = subTotal;
        this.status = status;
        this.timeStamp = Timestamp.now();
    }

    public static class OrderStatus {
        public static final int PLACED = 1 // Initially (U)
                , DELIVERED = 0, DECLINED = -1;     //(A)
    }

    @Override
    public String toString() {
        return "Order{" +
                "status=" + status +
                ", orderId='" + orderId + '\'' +
                ", timeStamp=" + timeStamp +
                ", userName='" + userName + '\'' +
                ", userPhoneNo='" + userPhoneNo + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", map=" + map +
                ", subTotal=" + subTotal +
                '}';
    }
}
