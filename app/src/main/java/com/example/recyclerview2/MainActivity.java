package com.example.recyclerview2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new WeightPicker().show(this, new WeightPicker.OnWeightPickedListener() {
            @Override
            public void onWeightPicked(int kg, int g) {
                Toast.makeText(MainActivity.this, "kg is "+kg, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "g is "+g, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWeightCanceledListener() {
                Toast.makeText(MainActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}