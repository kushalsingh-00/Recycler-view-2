package com.example.recyclerview2;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;

import com.example.recyclerview2.databinding.WeightPickerDialogBinding;

public class WeightPicker {

    /**
     * There are 9 TODOs in this file, locate them using the window for it given at the bottom.
     * Also, complete them in order.
     *
     * TODO 1 : Design layout weight_picker_dialog.xml for this WeightPicker Dialog
     *          with 2 NumberPickers (for kg & g)
     */

    public void show(Context context, final OnWeightPickedListener listener)
    {
        final WeightPickerDialogBinding b=WeightPickerDialogBinding.inflate(LayoutInflater.from(context));

        new AlertDialog.Builder(context)
                .setTitle("Pick Weight")
                .setView(b.getRoot())
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO 3 : Replace 0s & assign kg & g values from respective NumberPickers
                        int kg = 0, g = 0;

                        kg=(int) b.PickerKg.getValue();
                        g=(int) b.PickerG.getValue()*50;

                        //TODO 4 : Add GuardCode to prevent user from selecting 0kg 0g. If so, then return
                        if(kg==0&&g==0)
                            return;

                        listener.onWeightPicked(kg,g);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onWeightCanceledListener();

                    }
                })
                .show();

        setupNumberPickers(b.PickerKg,b.PickerG);

        //TODO 5 : Call new WeightPicker().show() in MainActivity and pass (this, new OnWeight...)

        //TODO 6 : Show toast of selected weight in onWeightPicked() method

        //TODO 7 : Find appropriate solution for : NumberPicker not formatting the first item

        //TODO 8 : Test your code :)

        //TODO 9 : Try to understand the flow as to how our Listener interface is working
    }

    private void setupNumberPickers(NumberPicker pickerKG,NumberPicker pickerG) {
        //TODO 2 : Define this method to setup kg & g NumberPickers as per the given ranges
        //kg Range - 0kg to 10kg
        //g Range - 0g to 950g

        pickerKG.setMinValue(0);
        pickerKG.setMaxValue(10);
        pickerG.setMinValue(0);
        pickerG.setMaxValue(19);

        pickerKG.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value+"kg";
            }
        });

        pickerG.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value*50+"g";
            }
        });

        View firstItemKg = pickerKG.getChildAt(0);
        if (firstItemKg != null) {
            firstItemKg.setVisibility(View.INVISIBLE);
        }
        View firstItemg = pickerG.getChildAt(0);
        if (firstItemg != null) {
            firstItemg.setVisibility(View.INVISIBLE);
        }

    }

    interface OnWeightPickedListener {
        void onWeightPicked(int kg,int g);
        void onWeightCanceledListener();
    }
}
