package com.example.recyclerview2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import com.example.recyclerview2.adapter.OrderAdapter;
import com.example.recyclerview2.databinding.ActivityOrderDetailsBinding;
import com.example.recyclerview2.databinding.ItemPlacedBinding;
import com.example.recyclerview2.fcmsender.FCMSender;
import com.example.recyclerview2.fcmsender.MessageFormatter;
import com.example.recyclerview2.model.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class OrderDetails extends AppCompatActivity {

    private ActivityOrderDetailsBinding b;
    private OrderAdapter orderAdapter;
    private List<Order> map=new ArrayList<>();
    private MyApp myApp;
    private ItemPlacedBinding itemPlacedBinding;
    public int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        myApp= (MyApp) getApplicationContext();
        loadData();

//        replyMessage();
    }

//    public void replyMessage() {
//        itemPlacedBinding.accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendNotification(map.get(position).orderId);
//            }
//        });
//    }

    public void sendNotification(String id) {
        Log.e(String.valueOf(OrderDetails.this),"called");
        String message = MessageFormatter
                .getSampleMessage("user", "Status ", "Order Id:"+id);

        new FCMSender()
                .send(message, new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(OrderDetails.this)
                                        .setTitle("Failure")
                                        .setMessage(e.toString())
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull final okhttp3.Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(OrderDetails.this)
                                        .setTitle("Success")
                                        .setMessage(response.toString())
                                        .show();
                            }
                        });
                    }
                });

    }

    private void loadData() {
        myApp.ff.collection("Order").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                                Log.e("this",q.toString());
                                Order order=q.toObject(Order.class);
                                order.orderId=q.getId();
                                map.add(order);
//                                Log.e(String.valueOf(OrderDetails.this),q.getId());
//                                Log.e(String.valueOf(OrderDetails.this),q.getData().toString());
                            }

                            Log.e("map",map.toString());
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
        orderAdapter=new OrderAdapter(this,map,myApp);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        b.recyclerView.addItemDecoration(itemDecor);
        b.recyclerView.setAdapter(orderAdapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}