package com.example.multiversenigeria3.me;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiversenigeria3.databinding.ActivityMultiverseBinding;


public class Multiverse extends AppCompatActivity {

    ActivityMultiverseBinding binding ;
    String facebook = "https://web.facebook.com/VerseCity-101020629332298/";
    //com.facebook.lite
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiverseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.facebookCeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/101020629332298"));
//                    startActivity(intent);
//                } catch(Exception e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://web.facebook.com/VerseCity-101020629332298/")));
//                }

                Uri uri = Uri.parse("fb://page/101020629332298");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.facebook.lite");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://web.facebook.com/VerseCity-101020629332298/")));
                }

            }
        });

        binding.instagramCeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.instagram.com/itzlonelyalphamk/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/itzlonelyalphamk/")));
                }
            }
        });

        binding.twitterCeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://twitter.com/KoredeAhmed1");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.twitter.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/KoredeAhmed1")));
                }


            }
        });


    }


}