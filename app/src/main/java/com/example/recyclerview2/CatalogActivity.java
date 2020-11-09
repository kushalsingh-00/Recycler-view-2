package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.recyclerview2.databinding.ActivityCatlogueBinding;
import com.example.recyclerview2.model.Products;

import java.util.ArrayList;
import java.util.Arrays;


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

        products = new ArrayList<>(Arrays.asList(
                new Products("Apple", 100, 1)
                , new Products("Orange", 100, 1)
                , new Products("Grapes", 100, 1)
                , new Products("Kiwi", 100, 1)
        ));

        //create adapter object
        adapter=new ProductAdapter(this,products);

        //set the adapter and layout manager to rv
        b.recyclerView.setAdapter(adapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Inflate the options menu

    @Override
    //TODO what is menu in onCreate
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_catalog_options,menu);

        SearchManager manager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        assert manager != null;
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
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

    //menthod to handle clicks in contextual menu

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit :
                Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show();
                edit();
                return true;
            case R.id.remove :
                Toast.makeText(this, "Remove clicked", Toast.LENGTH_SHORT).show();
                remove();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void edit() {
        Products selected=products.get(adapter.lastListItemSelected);

        new ProductEditorDialog().show(this, selected, new ProductEditorDialog.OnProductEditedListener() {
            @Override
            public void onProductEdited(Products product) {
                products.set(adapter.lastListItemSelected,product);
                adapter.notifyItemChanged(adapter.lastListItemSelected);
            }

            @Override
            public void onCancelled() {
                Toast.makeText(CatalogActivity.this, "Cancelled Pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void remove() {
        new AlertDialog.Builder(this)
                .setTitle("Do you want to remove the item")
//                .setView(b.getRoot())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        products.remove(adapter.lastListItemSelected);
                        adapter.notifyItemRemoved(adapter.lastListItemSelected);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CatalogActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}