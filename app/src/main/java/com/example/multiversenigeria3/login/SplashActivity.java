package com.example.multiversenigeria3.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.multiversenigeria3.databinding.ActivitySplashBinding;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {


    //declaring binding
    ActivitySplashBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //to hide tool bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //if else statement to determine if internet connection is on or off

        if (isConnected()) {
            int t = 2500;
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, PreMainActivity.class));
                finish();
            }, t);

        } else {
            startActivity(new Intent(SplashActivity.this, NoInternet.class));
            Toast.makeText(SplashActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    //action to check internet connection...
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}