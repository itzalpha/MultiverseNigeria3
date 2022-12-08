package com.example.multiversenigeria3.me;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.example.multiversenigeria3.databinding.ActivityCommunityGuidelinesBinding;

import java.util.Objects;

public class CommunityGuidelines extends AppCompatActivity {

    ActivityCommunityGuidelinesBinding binding ;
    public String filename = "community.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityGuidelinesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("file:///android_asset/" + filename);

    }
}