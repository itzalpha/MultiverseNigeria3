package com.example.multiversenigeria3.me;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.multiversenigeria3.databinding.ActivityJoinOurTeamBinding;
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

public class JoinOurTeam extends AppCompatActivity {
    ActivityJoinOurTeamBinding binding;
    FirebaseFirestore db;
    ProgressDialog pd;
    DocumentReference documentReference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinOurTeamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        binding.sendToAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yourSkill = binding.yourSkill.getText().toString().trim();
                String aboutYourSelf = binding.aboutYourself.getText().toString().trim();
                String whyAreYourInterested = binding.whyAreYouInterested.getText().toString().trim();
                String contact = binding.contact.getText().toString().trim();

                if (TextUtils.isEmpty(yourSkill) || TextUtils.isEmpty(aboutYourSelf) || TextUtils.isEmpty(whyAreYourInterested) || TextUtils.isEmpty(contact)){
                    Toast.makeText(JoinOurTeam.this, "Missing Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    saveReport(yourSkill ,aboutYourSelf , whyAreYourInterested , contact );
                }

            }
        });




    }

    private void saveReport(String yourSkill, String aboutYourSelf, String whyAreYourInterested, String contact) {
        pd.setMessage("Please Wait!");
        pd.show();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();
        String  currentUserId = id.getUid();
        documentReference = db.collection("VerseStore").document("Admin").collection("VerseStoreJoinOurTeam").document();

        Map<String,Object> user = new HashMap<>();
        user.put("VerseStoreJoinOurTeamYourSkill", yourSkill);
        user.put("VerseStoreJoinOurTeamAboutYourself" , aboutYourSelf);
        user.put("VerseStoreJoinOurTeamWhyAreYouInterested", whyAreYourInterested);
        user.put("VerseStoreJoinOurTeamContact",contact);
        user.put("VerseStoreReportUserId",currentUserId);
        user.put("VerseStoreJoinOurTeamDate" , strDate);

        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        binding.yourSkill.setText("");
                        binding.aboutYourself.setText("");
                        binding.whyAreYouInterested.setText("");
                        binding.contact.setText("");
                        Toast.makeText(JoinOurTeam.this,"Sent to the Administrator",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(JoinOurTeam.this,"Failed to send Report",Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
