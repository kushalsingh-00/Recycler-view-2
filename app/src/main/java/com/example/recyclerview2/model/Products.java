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
    public List<Varient> varients;

    public Products() {}

    //Weight based
    public Products(String name, int pricePerKg, float minQty)
    {
        type = WEIGHT_BASED;
        this.name = name;
        this.price = pricePerKg;
        this.qyt = minQty;
    }

    //Varient based
    public Products(String name) {
        type = VARIANTS_BASED;
        this.name = name;
    }

    public void initWeightProducts(String name, int pricePerKg, float minQty)
    {
        type = WEIGHT_BASED;
        this.name = name;
        this.price = pricePerKg;
        this.qyt = minQty;
    }

    //Varient based
    public void initVarientProducts(String name) {
        type = VARIANTS_BASED;
        this.name = name;
    }

    public void fromVarientString(String[] varientsplit) {
        varients =new ArrayList<>();
        for (String s :
                varientsplit) {
            String[] v = s.split(",");
            varients.add(new Varient(v[0],Integer.parseInt(v[1])));
        }
    }

    public String qtyToString()
    {
        if(qyt<1)
        {
            int converted=(int) qyt*1000;
            return converted+"g";
        }

        return ((int) qyt)+"kg";
    }


    @Override
    public String toString() {
        return "Products{" +
                "name='" + name + '\'' +
                ", qyt=" + qyt +
                ", price=" + price +
                ", type=" + type +
                ", variants=" + varients +
                '}';
    }

    public String variantsString(){
        String variantsString = varients.toString();
        return variantsString
                .replaceFirst("\\[", "")
                .replaceFirst("]", "")
                .replaceAll(",", "\n");
    }
}
