package com.example.recyclerview2.model;

import java.util.List;

public class Inventory {
    public List<Products> products;

    public Inventory() {
    }

    public Inventory(List<Products> products) {
        this.products = products;
    }
}
