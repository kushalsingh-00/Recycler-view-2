package com.example.recyclerview2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Products implements Serializable {

    public static final int WEIGHT_BASED = 0, VARIANTS_BASED = 1;

    public String name;
    public float qyt;
    public int price;
    public int type;
    public List<Variant> variants;

    public Products() {}

    public void fromVarientString(String[] varientsplit) {
        variants=new ArrayList<>();
        for (String s :
                varientsplit) {
            String[] v = s.split(",");
            variants.add(new Variant(v[0],Integer.parseInt(v[1])));
        }
    }

    @Override
    public String toString() {
        return "Products{" +
                "name='" + name + '\'' +
                ", qyt=" + qyt +
                ", price=" + price +
                ", type=" + type +
                ", variants=" + variants +
                '}';
    }
}
