package com.example.recyclerview2.model;

public class Varient {
    public String name;
    public int price;

    public Varient(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
