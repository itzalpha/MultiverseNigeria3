package com.example.multiversenigeria3.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.multiversenigeria3.info.ChannelnfoActivity
import com.example.multiversenigeria3.R
import com.example.multiversenigeria3.model.ChannelModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.channel_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class ChannelItemAdapter(val context: Context, private var userList : ArrayList<ChannelModel>) :
    RecyclerView.Adapter<ChannelItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.channel_item ,parent , false  )
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : ChannelModel = userList[position]


        // TODO:Assign Value to xml object
        holder.channel_title.text = user.ChannelTitle
        holder.channel_category.text = user.ChannelCategory
        holder.channel_info.text = user.ChannelInformation
        holder.channel_author_name.text = user.ChannelAuthor
        Glide.with(context).load(user.ChannelAuthorImageUri).into(holder.channel_author_image)
        Glide.with(context).load(user.ChannelImageUri).into(holder.channel_image)

        // TODO:Perform Action in xml object

        holder.layout.setOnClickListener {
            val intent = Intent(context, ChannelnfoActivity::class.java)
            intent.putExtra("channelTitle", user.ChannelTitle)
            intent.putExtra("channelInformation", user.ChannelInformation)
            intent.putExtra("channelAuthor", user.ChannelAuthor)
            intent.putExtra("channelUploadDate", user.ChannelUploadDate)
            intent.putExtra("channelLink", user.ChannelLink)
            intent.putExtra("channelAlternativeLink" , user.ChannelAlternativeLink)
            intent.putExtra("channelCategory" , user.ChannelCategory)
            intent.putExtra("docId" ,  user.ChannelDocumentId)
            context.startActivity(intent)

        }

    }


    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun Filteredlist(filterlist: java.util.ArrayList<ChannelModel>) {
        userList = filterlist
        notifyDataSetChanged()
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val channel_title = view.channel_title
        val channel_info = view.channel_information
        val channel_category = view.channel_category
        val channel_image = view.channel_image
        val layout = view.channel_layout
        val channel_author_name = view.authorName
        val channel_author_image = view.authorImage
    }

}