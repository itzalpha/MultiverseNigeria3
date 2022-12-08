package com.example.multiversenigeria3.ui.channels

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiversenigeria3.adapter.ChannelItemAdapter
import com.example.multiversenigeria3.databinding.FragmentWebsitesBinding
import com.example.multiversenigeria3.model.ChannelModel
import com.google.firebase.firestore.*

class WebsitesFragment : Fragment() {

    companion object {
        fun newInstance() = WebsitesFragment()
    }

    private lateinit var viewModel: WebsitesViewModel
    private var _binding : FragmentWebsitesBinding ?= null

    private lateinit var userArrayList : ArrayList<ChannelModel>
    private lateinit var myAdapter : ChannelItemAdapter
    private  var db = FirebaseFirestore.getInstance()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebsitesBinding.inflate(inflater, container, false)
        val root: View = binding.root

         populate()

        binding.channels.setOnClickListener {
          populate()
        }
        binding.verseCityBlog.setOnClickListener {

            val search =      "verseCity Blog"
            filters(search)

        }
        binding.websites.setOnClickListener {

            val search =     "websites"
            filters(search)

        }
        binding.personalities.setOnClickListener {

            val search = "personalities"
            filters(search)

        }
        binding.blogs.setOnClickListener {

            val search =  "blogs"
            filters(search)

        }
        binding.youtube.setOnClickListener {

            val search ="youtube"
            filters(search)

        }
        binding.tictok.setOnClickListener {

            val search ="tictok"
            filters(search)

        }
        binding.facebook.setOnClickListener {

            val search = "facebook"
            filters(search)

        }
        binding.instagram.setOnClickListener {

            val search = "instagram"
            filters(search)

        }
        binding.twitter.setOnClickListener {

            val search = "twitter"
            filters(search)

        }
        binding.others.setOnClickListener {

            val search = "others"
            filters(search)

        }

        binding.search.setOnClickListener {
            Toast.makeText(context, "Search by Blog title", Toast.LENGTH_SHORT).show()
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
                        filtersSearch(s.toString())
                    }
                })
            }
        }

        return root


    }

    private fun populate() {
        val recyclerView : RecyclerView = binding.channelsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        myAdapter = context?.let { ChannelItemAdapter(it, userArrayList) }!!
        recyclerView.adapter = myAdapter
        EventChangeListener()
    }


    private fun filtersSearch(search: String) {
        val filterlist: java.util.ArrayList<ChannelModel> = java.util.ArrayList<ChannelModel>()
        for (  item in userArrayList) {
            if (item.ChannelTitle?.toLowerCase()
                    ?.contains(search.toLowerCase()) == true

            ) {
                filterlist.add(item)
            }
        }
        myAdapter.Filteredlist(filterlist)



    }

    private fun filters(search: String) {
        val filterlist: java.util.ArrayList<ChannelModel> = java.util.ArrayList<ChannelModel>()
        for (  item in userArrayList) {
            if (item.ChannelCategory?.toLowerCase()
                    ?.contains(search.toLowerCase()) == true

            ) {
                filterlist.add(item)
            }
        }
        myAdapter.Filteredlist(filterlist)



    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("VerseStore").document("Users").collection("Channels")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null){
                        return
                    }

                    for (dc : DocumentChange in  value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(ChannelModel::class.java))
                            myAdapter.notifyDataSetChanged()
                        }
                        myAdapter.notifyDataSetChanged()
                    }



                }

            })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WebsitesViewModel::class.java)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}