package com.example.recyclerview2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.MyApp;
import com.example.recyclerview2.OrderDetails;
import com.example.recyclerview2.databinding.ItemPlacedBinding;
import com.example.recyclerview2.fcmsender.FCMSender;
import com.example.recyclerview2.fcmsender.MessageFormatter;
import com.example.recyclerview2.model.CartItem;
import com.example.recyclerview2.model.Order;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> map;
    private Context context;
    private Map<String, CartItem> list;
    private ItemPlacedBinding itemPlacedBinding;
    private OrderDetails orderDetails=new OrderDetails();
    private MyApp myApp;
    public OrderAdapter(Context context, List<Order> map,MyApp myApp)
    {
        this.context=context;
        this.map=map;
        this.myApp=myApp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlacedBinding b=ItemPlacedBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.b.userName.setText(map.get(position).userName);
        holder.b.orderID.setText(map.get(position).orderId);
        holder.b.orderTime.setText(map.get(position).timeStamp.toString());
        holder.b.orderItems.setText(map.get(position).map.toString());

        holder.b.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                orderDetails.position=position;
//                Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                sendNotification(map.get(position).orderId,"Accepted");
                myApp.ff.collection("Order").document(map.get(position).orderId).update("status",0);
                holder.b.accept.setVisibility(View.GONE);
                holder.b.decline.setVisibility(View.GONE);

            }
        });

        holder.b.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(map.get(position).orderId,"Declined");
                myApp.ff.collection("Order").document(map.get(position).orderId).update("status",-1);
                holder.b.accept.setVisibility(View.GONE);
                holder.b.decline.setVisibility(View.GONE);
            }
        });
    }

    public void sendNotification(String id,String status) {
        Log.e(String.valueOf(OrderAdapter.this),"called");
        String message = MessageFormatter
                .getSampleMessage("user", "Status "+status, "Order Id:"+id);

        new FCMSender()
                .send(message, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPlacedBinding b;
        public ViewHolder(@NonNull ItemPlacedBinding b) {
            super(b.getRoot());
            this.b=b;
        }
    }
}
