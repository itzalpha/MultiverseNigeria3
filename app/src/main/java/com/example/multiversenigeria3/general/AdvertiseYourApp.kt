package com.example.multiversenigeria3.general

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.multiversenigeria3.R
import com.example.multiversenigeria3.databinding.ActivityAdvertiseYourAppBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AdvertiseYourApp : AppCompatActivity() {

    private lateinit var binding : ActivityAdvertiseYourAppBinding
    private lateinit var advertName : String
    private lateinit var advertInfo : String
    private lateinit var advertContact : String

    var AppAndChannel: String? = null

    private lateinit var currentUserId : String

    private lateinit var progressDialog : ProgressDialog
    var documentReference: DocumentReference? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertiseYourAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // to hide the toolbar
        supportActionBar?.hide()

        val id = FirebaseAuth.getInstance().currentUser
        currentUserId = id!!.uid

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading....")
        progressDialog.setCancelable(false)

        binding.advertiseButton.setOnClickListener {
           advertName = binding.advertiseNameTitle.text.toString().trim()
            advertInfo = binding.advertiseInfo.text.toString().trim()
             advertContact = binding.advertiseContact.text.toString().trim()

            if (advertName.isNotEmpty() && advertInfo.isNotEmpty() && advertContact.isNotEmpty()  ){
                upload()
            }else if (binding.radioGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "Pick Advert Type", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this , "Credentials not complete" , Toast.LENGTH_SHORT).show()
            }
        }




    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upload() {
        progressDialog.show()
        documentReference = FirebaseFirestore.getInstance().collection("VerseStore").document("Admin").collection("Advertise").document()
        val docId = documentReference!!.id
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val date = current.format(formatter)
        val hashMap = hashMapOf<String , Any>(
            "title" to advertName,
            "information" to advertInfo,
            "contact" to advertContact,
            "pdfDate" to date ,
            "category" to AppAndChannel.toString(),
            "pdfPostId" to    docId,
            "postertId" to currentUserId ,

        )
        documentReference!!.set(hashMap)
            .addOnSuccessListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                binding.advertiseNameTitle.setText("")
                binding.advertiseInfo.setText("")
                binding.advertiseContact.setText("")
                Toast.makeText(this, "Sent to Admin Successfully", Toast.LENGTH_SHORT)
                    .show()

            }
            .addOnFailureListener { exception ->
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Error Sending to Admin $exception", Toast.LENGTH_SHORT).show()
            }


    }

    fun onRadioButtonClicked(view: View) {
        // Is the button now checked?
        val checked = (view as RadioButton).isChecked
        when (view.getId()) {
            R.id.radio_channel -> if (checked) AppAndChannel = "channel"
            R.id.radio_app -> if (checked) AppAndChannel = "app"
        }
    }

}