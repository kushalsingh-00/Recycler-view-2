package com.example.recyclerview2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.recyclerview2.databinding.ActivityCatlogueBinding;
import com.example.recyclerview2.model.Products;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CatalogActivity extends AppCompatActivity {

    private ActivityCatlogueBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityCatlogueBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setProductsList();
    }

    private void setProductsList() {
        //create dataset

        List<Products> list=new ArrayList<>(
                Arrays.asList(
                        new Products("dcsdc",0,0),new Products("cdcsc",0,0)
                )

        );

        //create adapter object
        ProductAdapter productAdapter=new ProductAdapter(this,list);

        //set the adapter and layout manager to rv
        b.recyclerView.setAdapter(productAdapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}