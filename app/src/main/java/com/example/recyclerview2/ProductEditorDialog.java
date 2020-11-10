package com.example.recyclerview2;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.recyclerview2.databinding.DialogProductEditBinding;
import com.example.recyclerview2.model.Products;

import java.util.regex.Pattern;

public class ProductEditorDialog {
    private DialogProductEditBinding b;
    private Products products;

    void show(final Context context, final Products products, final OnProductEditedListener listener)
    {
        this.products=products;
        b=DialogProductEditBinding.inflate(LayoutInflater.from(context));

        //Create Dialog
        new AlertDialog.Builder(context)
                .setTitle("Edit Product")
                .setView(b.getRoot())
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(areProductDetailsValid())
                            listener.onProductEdited(ProductEditorDialog.this.products); //TODO
                        else
                            Toast.makeText(context, "Invalid Details!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancelled();
                    }
                })
                .show();
        setupRadioGroup();
        filldetails();
    }

    private void filldetails() {
        b.name.setText(products.name);

        b.productType.check(products.type==Products.WEIGHT_BASED ? R.id.weight_based_rbtn : R.id.variants_based_rbtn);

        if(products.type==Products.WEIGHT_BASED)
        {
            b.price.setText(products.price+"");
            b.minQty.setText(products.qtyToString());
        }
        else
            b.varients.setText(products.variantsString());
    }

    private void setupRadioGroup() {
        b.productType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.weight_based_rbtn)
                {
                    b.weightBasedRoot.setVisibility(View.VISIBLE);
                    b.variantsRoot.setVisibility(View.GONE);
                }
                else
                {
                    b.variantsRoot.setVisibility(View.VISIBLE);
                    b.weightBasedRoot.setVisibility(View.GONE);
                }
            }
        });
    }


    private boolean areProductDetailsValid() {
        //checking name
        String name=b.name.getText().toString();
        if(name.isEmpty())
            return false;

        products.name=name;

        switch (b.productType.getCheckedRadioButtonId()){
            case R.id.weight_based_rbtn :
//                products.type=products.WEIGHT_BASED;

                String pricePerKg=b.price.getText().toString().trim();
                String minQty=b.minQty.getText().toString().trim();

                //checking price and quantity fields
                if(pricePerKg.isEmpty()||minQty.isEmpty()||!minQty.matches("\\d+(kg|g)"))
                    return false;

                products.initWeightProducts(name
                        ,Integer.parseInt(pricePerKg)
                        ,extractMinQyt(minQty));

                return true;

            case R.id.variants_based_rbtn :
//                products.type=products.VARIANTS_BASED;

                String varients=b.varients.getText().toString().trim();

                products.initVarientProducts(name);

                return areVarientsValid(varients);
        }

        return false;

    }

    private boolean areVarientsValid(String varients) {
        if(varients.length()==0)
            return true;

        String[] varientsplit=varients.split("\n");
        Pattern pattern=Pattern.compile("^\\w+(\\s|\\w)+,\\d$");
        for (String s :
                varientsplit) {
            if (!pattern.matcher(s).matches())
                return false;
        }

        products.fromVarientString(varientsplit);
        return true;
    }

    private float extractMinQyt(String minQty) {
        if(minQty.contains("kg"))
            return Integer.parseInt(minQty.replace("kg",""));
        else
            return Integer.parseInt(minQty.replace("g",""))/1000f;
    }

    //Listener Interface to notify Activity of Dialog events
    interface OnProductEditedListener{
        void onProductEdited(Products products);
        void onCancelled();
    }
}

