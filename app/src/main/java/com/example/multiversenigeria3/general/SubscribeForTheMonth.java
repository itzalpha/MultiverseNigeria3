package com.example.multiversenigeria3.general;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.multiversenigeria3.R;
import com.example.multiversenigeria3.databinding.ActivitySubscribeForTheMonthBinding;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SubscribeForTheMonth extends AppCompatActivity {

    ActivitySubscribeForTheMonthBinding binding ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog pd;
    DocumentReference documentReference1 ;
    DocumentReference documentReference2 ;

    private Integer amount;
    private String email ;

    private String currency = "NGN";
    private String publicKey ="FLWPUBK-927f4cede66b2059a659e43df616a14d-X";
    private String encryptionKey ="5b4b37bcc376be358e7457fe";
    private static final String TAG = "RAVI_PAYMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscribeForTheMonthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        binding.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    amount = Integer.valueOf(binding.amount.getText().toString().trim());
                    email = binding.email.getText().toString().trim();
                    if (TextUtils.isEmpty(amount.toString()) && TextUtils.isEmpty(email)){
                        Toast.makeText(SubscribeForTheMonth.this, "Input Amount", Toast.LENGTH_SHORT).show();
                    }else {
                        payMoney(amount , email);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(SubscribeForTheMonth.this, "Input Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void payMoney(Integer amount, String email) {
        UUID uuid = UUID.randomUUID();
        new RaveUiManager(SubscribeForTheMonth.this).setAmount(amount)
                .setCurrency(currency)
                .setEmail(email)
                .setPublicKey(publicKey)
                .setEncryptionKey(encryptionKey)
                .setTxRef(uuid.toString()) // unique order id
                .acceptAccountPayments(false)
                .acceptCardPayments(true)
                .acceptMpesaPayments(false)
                .acceptAchPayments(false)
                .acceptGHMobileMoneyPayments(false)
                .acceptUgMobileMoneyPayments(false)
                .acceptZmMobileMoneyPayments(false)
                .acceptRwfMobileMoneyPayments(false)
                .acceptSaBankPayments(false)
                .acceptUkPayments(false)
                .acceptBankTransferPayments(true)
                .acceptUssdPayments(false)
                .acceptBarterPayments(false)
                .acceptFrancMobileMoneyPayments(false,null)
                .allowSaveCardFeature(true)
                .onStagingEnv(false)
                .isPreAuth(false)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .withTheme(R.style.Theme_RavePayment)
                .initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Log.d(TAG, "onActivityResult: " + "SUCCESS " + message);
                Toast.makeText(this, "Payment SUCCESS", Toast.LENGTH_SHORT).show();
                subscribe();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Log.d(TAG, "onActivityResult: " + "ERROR " + message);
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Log.d(TAG, "onActivityResult: " + "CANCELLED " + message);
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void subscribe() {
        pd.setMessage("Please Wait!");
        pd.show();

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dater = formatter2.format(date) ;
        String strDate = formatter.format(date);
        FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();
        String  currentUserId = id.getUid();

        Map<String,Object> user = new HashMap<>();
        user.put("paidUserId",currentUserId);//
        user.put("paidDate",strDate);//
        user.put("paidFullDate",dater);//
        user.put("paidAmount" , amount);//

        documentReference1 = db.collection("VerseStore").document("Users").collection("Buy Us Coffee").document(strDate).collection("Individuals").document(currentUserId);
        documentReference1.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        documentReference2 = db.collection("VerseStore").document("Admin").collection("Buy Us Coffee").document("History").collection("PaidUsers").document();
                        documentReference2.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                pd.dismiss();
                                Toast.makeText(SubscribeForTheMonth.this,"Coffee Bought Successfully ." ,Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(SubscribeForTheMonth.this,"Failed to Buy A Coffee",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(SubscribeForTheMonth.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}