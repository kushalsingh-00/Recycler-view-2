package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.recyclerview2.adapter.ProductAdapter;
import com.example.recyclerview2.databinding.ActivityCatlogueBinding;
import com.example.recyclerview2.model.Inventory;
import com.example.recyclerview2.model.Order;
import com.example.recyclerview2.model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CatalogActivity extends AppCompatActivity {

    private ActivityCatlogueBinding b;
    private List<Products> products;
    private ProductAdapter adapter;
    private SearchView searchView;
    private ItemTouchHelper itemTouchHelper;
    public boolean isDragAndDropOn;

    String sharedPref="com.example.recyclerview2";
    private Gson gson;
    private MyApp myApp;
    private SharedPreferences preferences;

    private Order order;
    private Map<String,Order> map=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityCatlogueBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        preferences=getSharedPreferences(sharedPref,MODE_PRIVATE);
        gson=new Gson();

        myApp= (MyApp) getApplicationContext();

        loadPriviousData();

        //creating topic for getting order notification
        FirebaseMessaging.getInstance().subscribeToTopic("admin");

        b.orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderActivity();
            }
        });

    }

    private void openOrderActivity() {

        Intent i=new Intent(CatalogActivity.this,OrderDetails.class);
//        i.putExtra("dataOrder", (Serializable) map);
        startActivity(i);
    }

    private void setProductsList() {

        //create adapter object
        adapter=new ProductAdapter(this,products);

        //set the adapter and layout manager to rv
        b.recyclerView.setAdapter(adapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b.recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );

        dragAndDrop();
    }

    private void dragAndDrop() {
        ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int initialPosition=viewHolder.getAdapterPosition();
                int finalPosition=target.getAdapterPosition();
                Collections.swap(adapter.visible,initialPosition,finalPosition);
                b.recyclerView.getAdapter().notifyItemMoved(initialPosition,finalPosition);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        itemTouchHelper=new ItemTouchHelper(simpleCallback);
    }

    //Inflate the options menu

    @Override
    //TODO what is menu in onCreate
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_catalog_options,menu);

        searchView=(SearchView) menu.findItem(R.id.search).getActionView();

        SearchManager manager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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


    private void loadPriviousData()
    {
        String data=preferences.getString(sharedPref,null);

        if(data!=null)
        {
            products = new Gson().fromJson(data, new TypeToken<List<Products>>(){}.getType());
            setProductsList();
        }
        else
            fetchFromCloud();

    }

    private void fetchFromCloud() {
        if(myApp.isOffline())
        {
            myApp.showToast(this,"It Seems Your Device Is Ofline");
        }

        myApp.showLoadingDialog(this);

        myApp.ff.collection("Inventory").document("product")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            Inventory i=documentSnapshot.toObject(Inventory.class);
                            products= i.products;
                            saveLocally();
                        }
                        else{
                            products=new ArrayList<>();
                            setProductsList();
                            myApp.hideDialog();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(myApp, "Failed to load", Toast.LENGTH_SHORT).show();
                        myApp.hideDialog();
                    }
                });
    }
    // on Back Press save data

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("Do you want to save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveDataToDB();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void saveDataToDB() {
        if(myApp.isOffline())
            myApp.showToast(this,"It Seems your device is ofline");

        myApp.showLoadingDialog(this);

        Inventory inventory=new Inventory(products);

        myApp.ff.collection("inventory").document("products")
                .set(inventory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CatalogActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        saveLocally();
                        myApp.hideDialog();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CatalogActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        myApp.hideDialog();
                    }
                });

    }

    // saving data locally

    private void saveLocally()
    {
        SharedPreferences.Editor editor=preferences.edit();
        String s=gson.toJson(products);
        editor.putString(sharedPref,s).apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item :
            showProductEditorDialog();
            return true;

            case R.id.sort :
                sort();
                return true;

            case R.id.drag :
                toggleDragAndDrop(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleDragAndDrop(MenuItem item) {
        bgColor(item);

        if(isDragAndDropOn)
            itemTouchHelper.attachToRecyclerView(null);
        else
            itemTouchHelper.attachToRecyclerView(b.recyclerView);

        isDragAndDropOn=!isDragAndDropOn;
    }

    private void bgColor(MenuItem item) {
        Drawable ic=item.getIcon();
        if(isDragAndDropOn)
            ic.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        else
            ic.setColorFilter(getResources().getColor(R.color.yellow),PorterDuff.Mode.SRC_ATOP);

        item.setIcon(ic);
    }

    private void sort() {
        Collections.sort(adapter.visible, new Comparator<Products>(){
            @Override
            public int compare(Products a, Products b) {
                return a.name.compareToIgnoreCase(b.name);
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void showProductEditorDialog() {
        new ProductEditorDialog().show(this, new Products(), new ProductEditorDialog.OnProductEditedListener() {
            @Override
            public void onProductEdited(Products product) {
                adapter.allProducts.add(product);

                if(isNameInQuery(product.name)) {
                    adapter.visible.add(product);
                    adapter.notifyItemInserted(adapter.visible.size()-1);
                }
            }

            @Override
            public void onCancelled() {
                Toast.makeText(CatalogActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNameInQuery(String name) {
        String query = searchView.getQuery().toString().toLowerCase();
        return name.toLowerCase().contains(query);
    }

    //method to handle clicks in contextual menu

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
        Products selected=adapter.visible.get(adapter.lastListItemSelected);

        new ProductEditorDialog().show(this, selected, new ProductEditorDialog.OnProductEditedListener() {
            @Override
            public void onProductEdited(Products product) {
//                if(!product.name.toLowerCase().contains(getSearchQuery()))
//                adapter.visible.remove(adapter.lastListItemSelected);

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

                        Products removed=adapter.visible.get(adapter.lastListItemSelected);

                        products.remove(removed);
                        adapter.visible.remove(removed);
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