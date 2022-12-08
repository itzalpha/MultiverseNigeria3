package com.example.multiversenigeria3.me;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiversenigeria3.databinding.ActivitySendFeedbackBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SendFeedback extends AppCompatActivity {

    ActivitySendFeedbackBinding binding ;
    FirebaseFirestore db;
    ProgressDialog pd;
    DocumentReference documentReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySendFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        binding.reportAProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reportDescription = binding.descriptionReport.getText().toString().trim();

                if (TextUtils.isEmpty(reportDescription) ){
                    Toast.makeText(SendFeedback.this, "Missing Credentials", Toast.LENGTH_SHORT).show();
                } else {

                    saveReport(reportDescription );
                }

            }
        });




    }

    private void saveReport(String reportDescription) {

        pd.setMessage("Please Wait!");
        pd.show();
        FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();
        String  currentUserId = id.getUid();
        documentReference = db.collection("VerseStore").document("Admin").collection("VerseStoreSendFeedback").document();

        Map<String,Object> user = new HashMap<>();
        user.put("VerseStoreFeedBack",reportDescription);
        user.put("VerseStoreFeedBackUserId",currentUserId);

        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        binding.descriptionReport.setText("");
                        Toast.makeText(SendFeedback.this,"Feedback Sent to Administrators",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(SendFeedback.this,"Failed to send Feedback",Toast.LENGTH_SHORT).show();
                    }
                });

    }

}