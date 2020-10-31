package com.example.recyclerview2.model;

import java.io.Serializable;

public class Products implements Serializable {
    public String name;
    public int qyt,price;

    public Products(String name, int qyt, int price) {
        this.name = name;
        this.qyt = qyt;
        this.price = price;
    }
}
