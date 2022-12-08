package com.example.multiversenigeria3.ui.apps

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiversenigeria3.adapter.AppItemAdapter
import com.example.multiversenigeria3.databinding.FragmentDashboardBinding
import com.example.multiversenigeria3.model.AppModel
import com.google.firebase.firestore.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userAppArrayList: ArrayList<AppModel>
    private lateinit var myAppAdapter: AppItemAdapter

    private  var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val local_app_recyclerview : RecyclerView = binding.localAppsRecyclerview
        local_app_recyclerview.layoutManager = GridLayoutManager(context , 3)
        local_app_recyclerview.setHasFixedSize(true)
        userAppArrayList = arrayListOf()
        myAppAdapter = context?.let { AppItemAdapter(it , userAppArrayList) }!!
        local_app_recyclerview.adapter = myAppAdapter
        AppLocalEventChangeListener()

        binding.search.setOnClickListener {
            Toast.makeText(context, "Search by App name or App category ", Toast.LENGTH_SHORT).show()
            if(binding.searchEditText.isShown){
                binding.searchEditText.visibility = (View.GONE)
                binding.textEPQ.visibility = View.VISIBLE
            }else
            {
                binding.textEPQ.visibility = View.GONE
                binding.searchEditText.visibility = (View.VISIBLE)
                binding.searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                    override fun afterTextChanged(s: Editable) {
                        filters(s.toString())
                    }
                })
            }
        }

    }


    private fun filters(search: String) {
        val filterlist: java.util.ArrayList<AppModel> = java.util.ArrayList<AppModel>()
        for (  item in userAppArrayList) {
            if (item.AppName?.toLowerCase()?.contains(search.toLowerCase()) == true  || item.AppCategory?.toLowerCase()?.contains(search.toLowerCase()) == true  )

            {
                filterlist.add(item)
            }
        }
        myAppAdapter.Filteredlist(filterlist)

    }

    private fun AppLocalEventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("VerseStore").document("Users").collection("Nigerian Local Apps")
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