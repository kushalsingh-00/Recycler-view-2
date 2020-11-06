package com.example.recyclerview2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.databinding.ProductItemBinding;
import com.example.recyclerview2.model.Products;

import java.util.List;

//adapter for lists of products
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Products> productList;

    public ProductAdapter(Context context, List<Products> productList)
    {
        this.context=context;
        this.productList = productList;
    }

    //inflate the view for item and create a viewholder object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //1. Inflate the layout for product.xml
        ProductItemBinding b=ProductItemBinding.inflate(
                LayoutInflater.from(context),parent,false);

        //2. create viewholder object and return
        return new ViewHolder(b);
    }

    //binds the data to view
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //get the data at position

        final Products products=productList.get(position);

        //Bind the data
        holder.b.name.setText(String.format("%s Rs. %d)",products.name,products.price));

        holder.b.qyt.setText(products.qyt+"");
        holder.b.decBtn.setVisibility(products.qyt>0?View.VISIBLE:View.GONE);
        holder.b.qyt.setVisibility(products.qyt>0?View.VISIBLE:View.GONE);

        holder.b.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.qyt++;
                    notifyItemChanged(position);
            }
        });

        holder.b.decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.qyt--;
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    //holds the view for each item

    public static class ViewHolder extends RecyclerView.ViewHolder {

    public ProductItemBinding b;

        public ViewHolder(@NonNull ProductItemBinding b) {
            super(b.getRoot());
            this.b=b;
        }
    }
}
