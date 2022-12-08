package com.example.multiversenigeria3.info

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiversenigeria3.R
import com.example.multiversenigeria3.databinding.ActivityChannelnfoBinding
import com.example.multiversenigeria3.review.ReviewItemAdapter
import com.example.multiversenigeria3.review.ReviewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ChannelnfoActivity : AppCompatActivity() {

    private lateinit var userArrayList: ArrayList<ReviewModel>
    private lateinit var myAdapter: ReviewItemAdapter
    private var db = FirebaseFirestore.getInstance()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var currentUserId : String
    private lateinit var docId: String

    private lateinit var binding : ActivityChannelnfoBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChannelnfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val id = FirebaseAuth.getInstance().currentUser
         currentUserId = id!!.uid

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val date = current.format(formatter)

        val channelTitle = intent.getStringExtra("channelTitle")
        val channelCategory = intent.getStringExtra("channelCategory")
        val channelUploadDate = intent.getStringExtra("channelUploadDate")
        val channelAlternativeLink = intent.getStringExtra("channelAlternativeLink")
        val channelInformation = intent.getStringExtra("channelInformation")
         docId = intent.getStringExtra("docId").toString()
        val channelLink = intent.getStringExtra("channelLink")
        val channelAuthor = intent.getStringExtra("channelAuthor")


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)



        binding.infoAppTitle.text = channelTitle
        binding.categoryRating.text = "Category : $channelCategory \t Authored by $channelAuthor"
        binding.infoAppInformation.text = channelInformation
        binding.alternativeLink.text = channelAlternativeLink
        binding.playStoreLink.text = channelLink

        binding.appInfoImageComment.setOnClickListener {

            if (binding.commentsReviewLayout.visibility == View.GONE) {
                binding.commentsReviewLayout.visibility = View.VISIBLE
            } else {
                binding.commentsReviewLayout.visibility = View.GONE
            }

        }

        binding.appInfoShare.setOnClickListener {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.app_name)
                .setText("Check Out the Discovery Channel title : $channelTitle , In the App  ''Verse Store'' . \n Download App : http://play.google.com/store/apps/details?id=" +applicationContext.packageName)
                .startChooser()
        }

        binding.appInfoImageLike.setOnClickListener {
            val dob = db.collection("VerseStore").document("Admin").collection("Channels_Likes")
                .document(docId.toString()).collection("Users").document(currentUserId)
            dob.get().addOnCompleteListener { task ->

                if (task.result.exists()) {
                    Toast.makeText(this, "UnLike ", Toast.LENGTH_LONG).show()
                } else {
                    val doc =
                        db.collection("VerseStore").document("Admin").collection("Channels_Likes")
                            .document(docId.toString()).collection("Users").document(currentUserId)

                    val hashMap = hashMapOf<String, Any>(
                        "UserLikedId" to currentUserId,
                        "UserLikedDate" to date,
                    )
                    doc.set(hashMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Like", Toast.LENGTH_LONG).show()
                        }
                }
            }
        }

        db.collection("VerseStore").document("Admin").collection("Channels_Likes")
            .document(docId.toString()).collection("Users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var count = 0
                    for (document in task.result) {
                        count++
                    }
                    Log.d("TAG", count.toString() + "")
                    binding.infoAppTextLikeNumbers.text = (count.toString() + "")
                } else {
                    Log.d("Tag", "Error getting documents: ", task.exception)
                }
            }

        val dob = db.collection("VerseStore").document("Admin").collection("Channels_Likes")
            .document(docId.toString()).collection("Users").document(currentUserId)
        checkLike(dob, binding.imageLike)


        binding.appReviewButton.setOnClickListener {
            val review = binding.appReviewText.text.toString().trim()
            if (review.isNotEmpty()) {
                addReview()
            } else {
                Toast.makeText(this, "Review is empty", Toast.LENGTH_SHORT).show()
            }
        }

        val recyclerView: RecyclerView = binding.appReviewRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        myAdapter = ReviewItemAdapter(this, userArrayList)
        recyclerView.adapter = myAdapter
        EventChangeListener()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("VerseStore").document("Admin").collection("Reviews").document("ChannelsReviews")
            .collection(docId)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userArrayList.add(dc.document.toObject(ReviewModel::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addReview() {
        progressDialog.show()
        val review = binding.appReviewText.text.toString().trim()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val date = current.format(formatter)

        val db = FirebaseFirestore.getInstance().collection("VerseStore").document("Admin")
            .collection("Reviews").document("ChannelsReviews").collection(docId).document(currentUserId)
        val docId: String = db.id
        val hashMap = hashMapOf<String, Any>(
            "UserReviewerID" to currentUserId,
            "UserReviewComment" to review,
            "UserReviewDate" to date,
            "UserReviewDocumentId" to docId

        )

        db.set(hashMap)
            .addOnSuccessListener {
                binding.appReviewText.setText("")
                binding.commentsReviewLayout.visibility = View.GONE
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this, "Review Added Successfully", Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener { exception ->
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this, "Error adding Review $exception", Toast.LENGTH_SHORT).show()

            }

    }


    fun checkLike(dob: DocumentReference, ImageLike: ImageView) {
        dob.get().addOnSuccessListener { task ->
            if (task.exists()) {
                ImageLike.setColorFilter(Color.RED)
            } else {

            }
        }
    }
}