package com.example.multiversenigeria3.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.multiversenigeria3.MainActivity;
import com.example.multiversenigeria3.R;
import com.example.multiversenigeria3.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    // to bind the activity to  its layout
    ActivityRegisterBinding binding ;
    //to Call the firebase Authentication perimeter to allow authentication.
    private FirebaseAuth mAuth;
    //to declare a string information
    String currentUserId;
    //to create the progress dialog bar.
    ProgressDialog pd;
    //to declare the fireStore database  and its path.
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference ;
    //to declare strings of some of our information publicly
    String txtMobileNumber ;
    String txtPassword ;
    String txtName ;
    String txtEmail ;
    String txtConfirmPassword ;
    String StateOfResidence;
    String AreaOfResidence ;
    String UniversityPolytechnicCollege ;
    String MaleAndFemale ;
    //to declare the picture parameters and those to save and send path to the database..
    Uri uri;
    private Bitmap bitmap;
    private String myUri = "";
    private final int REQ = 1;
    private StorageTask uploadTask ;
    private StorageReference storageProfilePicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // to hide the toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // to declare the database
        mAuth = FirebaseAuth.getInstance();
        //to declare the progress dialog path
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        //to declare the path of  the database
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("ProfilePics");
        //to pick the picture and display it in the image
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, REQ);
            }
        });

        //to register the user and save the picked information to the database
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to pick the text inputted in the edittext
                StateOfResidence = binding.stateOfResidence.getText().toString().trim();
                AreaOfResidence = binding.areaOfResidence.getText().toString().trim();
                UniversityPolytechnicCollege = binding.universityPolytechnicCollege.getText().toString().trim();
                txtMobileNumber = Objects.requireNonNull(binding.mobileNumber.getText()).toString().trim();
                txtName = Objects.requireNonNull(binding.name.getText()).toString().trim();
                txtEmail = Objects.requireNonNull(binding.email.getText()).toString().trim();
                txtPassword = Objects.requireNonNull(binding.password.getText()).toString().trim();
                txtConfirmPassword = Objects.requireNonNull(binding.confirmPassword.getText()).toString().trim();
                // to confirm if all text have been filled
                if (TextUtils.isEmpty(txtName)
                        || TextUtils.isEmpty(txtEmail)
                        || TextUtils.isEmpty(UniversityPolytechnicCollege) || TextUtils.isEmpty(AreaOfResidence)
                        || TextUtils.isEmpty(StateOfResidence) || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtMobileNumber)){
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.equals(txtConfirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Password not the same", Toast.LENGTH_SHORT).show();
                }else if (bitmap == null){
                    Toast.makeText(RegisterActivity.this, "Add Profile Image", Toast.LENGTH_SHORT).show();

                }else if (binding.radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, "Pick Gender", Toast.LENGTH_SHORT).show();
                }else if (binding.radioGroupStudent.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, "Are You A Student (yes or no)", Toast.LENGTH_SHORT).show();
                }
                else {
                    //to show saving dialog
                    pd.setMessage("Setting Up Account...");
                    pd.show();
                    //saving the picture to the database storage , {note : permission that anything can be stored should be made in the database rules , to avoid error .}
                    uploadProfileImage();
                }
            }
        });
    }
    private void uploadProfileImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] finalimg = baos.toByteArray();
        final StorageReference fileRef = storageProfilePicsRef
                .child("Profile Picture " + finalimg + ".jpg");
        uploadTask = fileRef.putFile(uri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = (Uri) task.getResult();
                    myUri = downloadUrl.toString();
                    // to save user information with the picture saved id from the storage , to the database
                    registerUser(myUri);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void registerUser(String myUri) {
        //to save an account of the  user using email and password ,  from the collected data
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // to save user perimeter to fireStore database
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                currentUserId = user.getUid();
                documentReference = db.collection("Verse").document("Users").collection("Users").document(currentUserId);
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                String strDate = formatter.format(date);
                Map<String, Object> profile = new HashMap<>();
                profile.put("name", txtName);//
                profile.put("email", txtEmail);//
                profile.put("code", txtPassword);//
                profile.put("mobile", txtMobileNumber);//
                profile.put("profileImage" , myUri);//
                profile.put("dateOfRegistration" , strDate);
                profile.put("stateOfResidence", StateOfResidence);
                profile.put("gender" , MaleAndFemale);
                profile.put("areaOfResidence" , AreaOfResidence);
                profile.put("universityOrPolytechnicCollege" , UniversityPolytechnicCollege);
                profile.put("userBlocked" , "NO");
                profile.put("registeredApplication" , "VerseStore");
                profile.put("Android Version ", Build.VERSION.RELEASE );
                profile.put("Android ID",  Build.ID);
                profile.put("Android Model" ,Build.MODEL);
                profile.put("Android Device" ,  Build.DEVICE);
                profile.put("Android Host" , Build.HOST);
                profile.put("Android Product" , Build.PRODUCT);
                profile.put("Android Manufacturer" ,  Build.MANUFACTURER );
                profile.put("Android Device FingerPrint" ,Build.FINGERPRINT);

                documentReference.set(profile)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                documentReference = db.collection("VerseStore").document("Users").collection("VerseStore").document();

                                documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        pd.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Account Successfully Created ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //to allow the action of picking image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK) {
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.profileImage.setImageBitmap(bitmap);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    MaleAndFemale = "male";
                break;
            case R.id.radio_female:
                if (checked)

                    MaleAndFemale = "female" ;
                break;
        }

    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonStudentClicked(View view) {
        // Is the button now checked?
        boolean checked2 = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked2)
                    binding.uniBox.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_no:
                if (checked2)
                    binding.uniBox.setVisibility(View.GONE);
                binding.universityPolytechnicCollege.setText("Not A Student");

                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

