package com.example.recyclerview2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.databinding.VarientBasedProductBinding;
import com.example.recyclerview2.databinding.WeightBasedProductBinding;
import com.example.recyclerview2.model.Products;

import java.util.ArrayList;
import java.util.List;

//adapter for lists of products
public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //Need For Inflating Layouts
    private Context context;

    //List Of Data
    public List<Products> visible,allProducts;

    //for getting item postion to perform edit and remove function
    int lastListItemSelected;

    public ProductAdapter(Context context, List<Products> productList)
    {
        this.context=context;
        this.visible=new ArrayList<>(productList);
        this.allProducts = productList;
    }

    //inflate the view for item and create a viewholder object
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==Products.WEIGHT_BASED)
        {
            WeightBasedProductBinding wb=WeightBasedProductBinding.inflate(
                    LayoutInflater.from(context)
                    ,parent
                    ,false
            );
            //create and return weight based product view holder
            return new WeightBasedProductVH(wb);
        }
        else {
            VarientBasedProductBinding vb = VarientBasedProductBinding.inflate(
                    LayoutInflater.from(context)
                    , parent
                    , false
            );

            //craete and return varient based product view holder
            return new VarientBasedProductVH(vb);
        }
    }

    //returns view type
    @Override
    public int getItemViewType(int position) {
        return visible.get(position).type;
    }

    //binds the data to view
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        //get the data at position

        final Products products=visible.get(position);

        //Bind the data
       if(products.type==Products.WEIGHT_BASED)
       {
           //get binding
           WeightBasedProductBinding b=((WeightBasedProductVH) holder).b;

           //Bind data
           b.name.setText(products.name);
           b.pricePerKg.setText("Rs. " + products.price);
           b.minQty.setText("MinQty - " + products.qyt + "kg");

           setupContextualMenu(b.getRoot());
       }
       else
       {
           VarientBasedProductBinding b=((VarientBasedProductVH) holder).b;

           b.name.setText(products.name);
           b.varients.setText(products.variantsString());

           setupContextualMenu(b.getRoot());
       }

       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               lastListItemSelected=holder.getAdapterPosition();
               return false;
           }
       });
    }

    private void setupContextualMenu(ConstraintLayout root) {
        root.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if(!(context instanceof CatalogActivity))
                    return;

                //casting the context to catalog type
                ((CatalogActivity) context).getMenuInflater().inflate(R.menu.product_contextual_menu,menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return visible.size();
    }

    public void filter(String newText) {
        String query=newText.toLowerCase();
        visible=new ArrayList<>();
        for (Products p:
             allProducts) {
            if(p.name.toLowerCase().contains(query))
                visible.add(p);
            }
        notifyDataSetChanged();
    }

    //View holder for weight based items
    public static class WeightBasedProductVH extends RecyclerView.ViewHolder{
        WeightBasedProductBinding b;

        public WeightBasedProductVH(WeightBasedProductBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }




    //View Holder for varient based items
    public static class VarientBasedProductVH extends RecyclerView.ViewHolder{
        VarientBasedProductBinding b;

        public VarientBasedProductVH(VarientBasedProductBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

}
