package com.example.fribu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fribu.databinding.RecyclerRowBinding
import com.example.fribu.models.Post
import com.squareup.picasso.Picasso

class RecyclerAdapter (val arrayList: ArrayList<Post>): RecyclerView.Adapter<RecyclerAdapter.PostHolder>() {
    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.txtrecycleremail.text = arrayList[position].email
        holder.binding.txtrecyclercomment.text = arrayList[position].comment
        Picasso.get().load(arrayList[position].downurl).into(holder.binding.recyclerimageview)
    }
}