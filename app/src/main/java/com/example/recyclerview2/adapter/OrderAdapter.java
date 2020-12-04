package com.example.recyclerview2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.databinding.ItemPlacedBinding;
import com.example.recyclerview2.model.CartItem;
import com.example.recyclerview2.model.Order;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Map<String,Order> map;
    private Context context;
    private Map<String, CartItem> list;
    private ItemPlacedBinding itemPlacedBinding;
    public OrderAdapter(Context context,Map<String,Order> map)
    {
        this.context=context;
        this.map=map;
        this.map=new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlacedBinding b=ItemPlacedBinding.inflate(LayoutInflater.from(context),parent,false);

        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.b.userName.setText(map.get(position).userName);
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
