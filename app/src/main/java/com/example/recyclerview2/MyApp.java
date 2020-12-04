package com.example.recyclerview2;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.firestore.FirebaseFirestore;

public class MyApp extends Application {

    public FirebaseFirestore ff;
    private AlertDialog dialog;
    private ConnectivityManager connectivityManager;

    @Override
    public void onCreate() {
        super.onCreate();

        setup();
    }

    private void setup() {
        ff = FirebaseFirestore.getInstance();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void showLoadingDialog(Context context){
        dialog = new AlertDialog.Builder(context)
                .setTitle("Loading...")
                .setMessage("Please wait!")
                .show();
    }

    public void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isOffline(){
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return !(wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected());
    }

    public void hideDialog(){
        if(dialog != null)
            dialog.dismiss();
    }
}
