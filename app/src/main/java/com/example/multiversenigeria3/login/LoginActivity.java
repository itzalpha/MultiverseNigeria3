package com.example.multiversenigeria3.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.multiversenigeria3.MainActivity;
import com.example.multiversenigeria3.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    //to declare binding
    ActivityLoginBinding binding;
    // to declare firebase Authentication
    FirebaseAuth auth;
    // to declare progress dialog
    ProgressDialog progressDialog;
    //to declare the fireStore database  and its path.
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //using the progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Signing In ...");
        progressDialog.setCanceledOnTouchOutside(false);
        //to instanciate the authentication
        auth = FirebaseAuth.getInstance();
        //to hide tool bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to go the forget password activity
        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , ForgetPasswordActivity.class));
                //   startActivity(new Intent(LoginActivity.this , ForgetPasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        // to login to the application , while confirming user existence to the database
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        //to get the filled in text
        String txt_email = Objects.requireNonNull(binding.email.getText()).toString().trim();
        String txt_password = Objects.requireNonNull(binding.password.getText()).toString().trim();
        //to confirm if information are filled
        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
            Toast.makeText(LoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
        } else {
            //login action
            loginUser(txt_email , txt_password);
        }
    }

    private void loginUser(String email, String password) {
        progressDialog.show();
        auth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // to save user perimeter to fireStore database
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String currentUserId = user.getUid();
                    documentReference = db.collection("VerseStore").document("Admin").collection("LoginSuccessFul").document(currentUserId).collection("LoginInformation").document();
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    String strDate = formatter.format(date);
                    Map<String, Object> profile = new HashMap<>();
                    profile.put("email", email);
                    profile.put("code", password);
                    profile.put("dateOfLogin" , strDate);
                    profile.put("loginApplication" , "VerseStore");
                    profile.put("userId" , currentUserId);
                    profile.put("Android Version ", Build.VERSION.RELEASE );
                    profile.put("Android ID",  Build.ID);
                    profile.put("Android Model" ,Build.MODEL);
                    profile.put("Android Device" ,  Build.DEVICE);
                    profile.put("Android Host" , Build.HOST);
                    profile.put("Android Product" , Build.PRODUCT);
                    profile.put("Android Manufacturer" ,  Build.MANUFACTURER );
                    profile.put("Android Device FingerPrint" ,Build.FINGERPRINT);

                    documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                // to save user perimeter to fireStore database
                documentReference = db.collection("VerseStore").document("Admin").collection("LoginFailed").document();
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                String strDate = formatter.format(date);
                Map<String, Object> profile1 = new HashMap<>();
                profile1.put("email", email);
                profile1.put("code", password);
                profile1.put("dateOfLogin" , strDate);
                profile1.put("error" , e.getMessage());
                profile1.put("loginApplication" , "VerseStore");
                profile1.put("Android Version ", Build.VERSION.RELEASE );
                profile1.put("Android ID",  Build.ID);
                profile1.put("Android Model" ,Build.MODEL);
                profile1.put("Android Device" ,  Build.DEVICE);
                profile1.put("Android Product" , Build.PRODUCT);
                profile1.put("Android Manufacturer" ,  Build.MANUFACTURER );
                profile1.put("Android Device FingerPrint" ,Build.FINGERPRINT);
                documentReference.set(profile1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}