package com.example.multiversenigeria3.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiversenigeria3.adapter.AppItemAdapter
import com.example.multiversenigeria3.databinding.FragmentHomeBinding
import com.example.multiversenigeria3.model.AppModel
import com.google.firebase.firestore.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAppArrayList: ArrayList<AppModel>
    private lateinit var myAppAdapter: AppItemAdapter



    private  var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val latest_app_recyclerview : RecyclerView = binding.AppsRecyclerview
        latest_app_recyclerview.layoutManager = GridLayoutManager(context, 3)
        latest_app_recyclerview.setHasFixedSize(true)
        userAppArrayList = arrayListOf()
        myAppAdapter = context?.let { AppItemAdapter(it , userAppArrayList) }!!
        latest_app_recyclerview.adapter = myAppAdapter
        AppEventChangeListener()




        return root
    }

    private fun AppEventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("VerseStore").document("Users").collection("Home Apps")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userAppArrayList.add(dc.document.toObject(AppModel::class.java))
                            myAppAdapter.notifyDataSetChanged()
                        }
                        myAppAdapter.notifyDataSetChanged()
                    }
                }
            })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}