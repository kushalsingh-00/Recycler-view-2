package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.recyclerview2.adapter.OrderAdapter;
import com.example.recyclerview2.databinding.ActivityOrderDetailsBinding;
import com.example.recyclerview2.databinding.ItemPlacedBinding;
import com.example.recyclerview2.model.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    private ActivityOrderDetailsBinding b;
    private OrderAdapter orderAdapter;
    private Map<String, Order> map=new HashMap<>();
    private MyApp myApp;
    private ItemPlacedBinding itemPlacedBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        myApp= (MyApp) getApplicationContext();
        loadData();
//        Intent i=getIntent();
//        map= (Map<String, Order>) i.getExtras().getSerializable("dataOrder");
        itemPlacedBinding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification("cwscvwevcer3423");
            }
        });
    }

    private void sendNotification(String id) {
//        Log.e(String.valueOf(OrderDetails.this),"called");
//        String message = com.example.user.fcmsender.MessageFormatter
//                .getSampleMessage("admin", "New Order", "Order Id:"+id);
//
//        new com.example.user.fcmsender.FCMSender()
//                .send(message,new Callback());
//
//                .send(message, new Callback() {
//
//
//                    @Override
//                    public void onFailure(Request request, final IOException e) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                new AlertDialog.Builder(OrderDetails.this)
//                                        .setTitle("Failure")
//                                        .setMessage(e.toString())
//                                        .show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(final Response response) throws IOException {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                new AlertDialog.Builder(OrderDetails.this)
//                                        .setTitle("Success")
//                                        .setMessage(response.toString())
//                                        .show();
//                            }
//                        });
//                    }
//                })
    }

    private void loadData() {
        myApp.ff.collection("Order").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
//                                Order order=q.toObject(Order.class);
                                map.put(q.getId(), q.toObject(Order.class));
                                Log.e(String.valueOf(OrderDetails.this),q.getId());
                                Log.e(String.valueOf(OrderDetails.this),q.getData().toString());
                            }
                            setUpOrderAdapter();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetails.this, "Failed To Load", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpOrderAdapter() {
        orderAdapter=new OrderAdapter(this,map);
        b.recyclerView.setAdapter(orderAdapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}