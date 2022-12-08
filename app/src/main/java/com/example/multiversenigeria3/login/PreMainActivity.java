package com.example.multiversenigeria3.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.multiversenigeria3.MainActivity;
import com.example.multiversenigeria3.R;
import com.example.multiversenigeria3.databinding.ActivityPreMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class PreMainActivity extends AppCompatActivity {
    //to bind the activity to its layout
    ActivityPreMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding
        binding = ActivityPreMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //to hide the toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to get the firebase user reference , to check existence.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //if user does not exist stay here else go to the MainActivity
        if(user != null){
            Intent intent = new Intent(PreMainActivity.this , MainActivity.class);
            startActivity(intent);
        }
        //to allow file to go to login Activity to log in to the application
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreMainActivity.this , LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        //to allow file  to go to register activity to sign up to the application
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreMainActivity.this , RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });




    }
    //to allow on back pressed to bring out dialog , to confirm if user want to exit  or not .
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PreMainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}