package com.example.tripwithwheel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripwithwheel.databinding.ItemReviewRecyclerviewBinding

class MyViewHolder(val binding : ItemReviewRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)
class ReviewAdapter(val context : Context, val itemList : MutableList<ItemData>) : RecyclerView.Adapter<MyViewHolder>(){
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemReviewRecyclerviewBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList.get(position)
        holder.binding.run{
            itemEmailView.text = data.email
            itemDateView.text = data.date
            itemContentView.text = data.contents
        }

        val imageRef = MyApplication.storage.reference.child("images").child(MyApplication.markerName).child("${data.docid}.jpg")

        imageRef.downloadUrl.addOnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.itemImageView)
            }
        }
    }
}