package com.example.multiversenigeria3.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.multiversenigeria3.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {
    // binding declaration
    ActivityForgetPasswordBinding binding;
    //firebase Authentication declaration
    private FirebaseAuth auth;
    //progress dialog declaration
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        progressDialog.setMessage("Loading......");

        // to validate email and send link to retrieve password ..
        binding.forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private void validateData() {
        // to check if data exist in the email space
        if(Objects.requireNonNull(binding.emailEdittextForgetPassword.getText()).toString().trim().isEmpty()){
            binding.emailEdittextForgetPassword.setError("Email Required");
            binding.emailEdittextForgetPassword.requestFocus();
        }else {
            progressDialog.show();
            forgetPassword();
        }
    }

    private void forgetPassword() {
        auth.sendPasswordResetEmail(Objects.requireNonNull(binding.emailEdittextForgetPassword.getText()).toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Check Your Email to Reset Your Password ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPasswordActivity.this , LoginActivity.class));
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Error : "+ Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        super.onBackPressed();
    }
}