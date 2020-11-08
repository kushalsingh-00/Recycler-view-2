package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.recyclerview2.databinding.ActivityCatlogueBinding;
import com.example.recyclerview2.model.Products;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CatalogActivity extends AppCompatActivity {

    private ActivityCatlogueBinding b;
    private ArrayList<Products> products;
    private ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityCatlogueBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setProductsList();
    }

    private void setProductsList() {

        //Create dataset
        products=new ArrayList<>();

        //create adapter object
        ProductAdapter productAdapter=new ProductAdapter(this,products);

        //set the adapter and layout manager to rv
        b.recyclerView.setAdapter(productAdapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Inflate the options menu

    @Override
    //TODO what is menu in onCreate
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_catalog_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_item) {
            showProductEditorDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProductEditorDialog() {
        new ProductEditorDialog().show(this, new Products(), new ProductEditorDialog.OnProductEditedListener() {
            @Override
            public void onProductEdited(Products product) {

                products.add(product);
                adapter.notifyItemInserted(products.size()-1);
            }

            @Override

            public void onCancelled() {
                Toast.makeText(CatalogActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
            }
        });
    }
}