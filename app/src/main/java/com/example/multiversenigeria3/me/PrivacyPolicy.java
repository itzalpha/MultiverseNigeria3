package com.example.multiversenigeria3.me;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiversenigeria3.databinding.ActivityPrivacyPolicyBinding;

import java.util.Objects;

public class PrivacyPolicy extends AppCompatActivity {
    public String filename = "privacy.txt";

    ActivityPrivacyPolicyBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("file:///android_asset/" + filename);


    }
}