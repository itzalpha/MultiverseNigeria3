package com.example.multiversenigeria3.review

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.multiversenigeria3.R

import kotlinx.android.synthetic.main.item_comment.view.*
import java.util.*
import kotlin.collections.ArrayList

class ReviewItemAdapter(val context : Context , private val userList : ArrayList<ReviewModel>) :
     RecyclerView.Adapter<ReviewItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment ,parent , false  )
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : ReviewModel = userList[position]
        holder.review_comments.text = user.UserReviewComment
        holder.review_user_name.text = user.UserReviewDate
        holder.review_image_text.text = (user.UserReviewComment?.let { getSafeSubstrings(it, 1) })

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(Color.parseColor("#" + mColors[Random().nextInt(50)]))
        holder.image_text_background.background = shape

    }

    private fun getSafeSubstrings(s: String, i: Int): String? {
        if (!TextUtils.isEmpty(s)) {
            if (s.length >= i) {
                return s.substring(0, i)
            }
        }
        return s
    }

    override fun getItemCount(): Int {
        return userList.size
        notifyDataSetChanged()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val review_image_text = view.tutorImageText
        val review_user_name = view.commentPoster
        val review_comments = view.commentReview
        val image_text_background = view.circle_background

    }

    var mColors = arrayOf(
        "FFEBEE", "FFCDD2", "EF9A9A", "E57373", "EF5350",
        "F44336", "E53935", "5E35B1", "512DA8", "4527A0",
        "311B92", "B388FF", "7C4DFF", "303F9F", "283593",
        "1A237E", "FF5722", "E8F5E9", "C8E6C9", "A5D6A7",
        "8C9EFF", "64B5F6", "42A5F5", "2196F3", "1E88E5",
        "81C784", "66BB6A", "4CAF50", "43A047", "C6FF00",
        "AEEA00", "FFFDE7", "FFF9C4", "FF9100", "FF6D00",
        "FFF590", "FFF176", "FFEE58", "FFEB3B", "FDD835",
        "F57C00", "EF6C00", "E65100", "FFD180", "FFAB40",
        "FBE9A7", "FFCCBC", "FFAB91", "FF8A65", "FF7043",
    )
}


