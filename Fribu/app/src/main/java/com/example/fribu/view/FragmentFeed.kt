package com.example.fribu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import com.example.fribu.R
import com.example.fribu.adapter.RecyclerAdapter
import com.example.fribu.databinding.FragmentFeedBinding
import com.example.fribu.models.Post
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


class FragmentFeed : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var popup: PopupMenu
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    val postsArray :ArrayList<Post> = arrayListOf()
    private var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { butonatiklandi(it) }
        popup = PopupMenu(requireContext(),binding.floatingActionButton)
        val inflate = popup.menuInflater
        inflate.inflate(R.menu.my_pop_menu,popup.menu)
        popup.setOnMenuItemClickListener(this)
        verilerial()
        adapter = RecyclerAdapter(postsArray)
        binding.recyclerFeed.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.recyclerFeed.adapter = adapter

    }
    private fun verilerial(){
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error!=null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else{
                if (value !=null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        for (document in documents){
                            val comment = document.get("comment") as String
                            val email = document.get("userEmail") as String
                            val downloadUrl = document.get("dowloandurl") as String

                            val post = Post(email,comment,downloadUrl)
                            postsArray.add(post)
                        }
                        adapter!!.notifyDataSetChanged()
                    }

                }
            }
        }
    }
    private fun butonatiklandi(view: View){
        popup.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.yuklemeitem){
            val action = FragmentFeedDirections.actionFragmentFeedToFragmentYukle()
            Navigation.findNavController(requireView()).navigate(action)
        }
        else if (item?.itemId==R.id.cikisitem){
            auth.signOut()
           val action = FragmentFeedDirections.actionFragmentFeedToSignInPageFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        return true
    }

}