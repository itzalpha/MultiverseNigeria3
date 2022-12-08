package com.example.multiversenigeria3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.multiversenigeria3.info.AppInfoActivity
import com.example.multiversenigeria3.R
import com.example.multiversenigeria3.model.AppModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_item.view.*

import java.util.*
import kotlin.collections.ArrayList


class AppItemAdapter(val context: Context, private var userList : ArrayList<AppModel>) :
    RecyclerView.Adapter<AppItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.app_item ,parent , false  )
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : AppModel = userList[position]

        // TODO:Assign Value to xml object
        holder.appName.text = user.AppName
        Glide.with(context).load(user.AppImageUri).into(holder.appImage)

        // TODO:Perform Action in xml object
        holder.layout.setOnClickListener {
            val intent = Intent(context, AppInfoActivity::class.java)
            intent.putExtra("AppName", user.AppName)
            intent.putExtra("AppCategory", user.AppCategory)
            intent.putExtra("AppImageUri", user.AppImageUri)
            intent.putExtra("AppUploadDate", user.AppUploadDate)
            intent.putExtra("AppAlternativeLink", user.AppAlternativeLink)
            intent.putExtra("AppInformation" , user.AppInformation)
            intent.putExtra("docId" ,  user.AppDocumentId)
            intent.putExtra("AppPlayStoreLink", user.AppPlayStoreLink)
            intent.putExtra("AppVerseRate" ,  user.AppVerseRate)
            context.startActivity(intent)

        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun Filteredlist(filterlist: java.util.ArrayList<AppModel>) {
        userList = filterlist
        notifyDataSetChanged()
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val appImage = view.app_image
        val appName = view.app_name
        val layout = view.layout_app

    }

}