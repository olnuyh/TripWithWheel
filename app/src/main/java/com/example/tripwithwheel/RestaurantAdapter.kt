package com.example.tripwithwheel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripwithwheel.databinding.ItemRecyclerviewBinding

class RestaurantViewHolder(val binding : ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)
class RestaurantAdapter(val context : Context, val datas : MutableList<Row2>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RestaurantViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as RestaurantViewHolder).binding
        val model = datas!![position]
        binding.name.text = model.SISULNAME
        binding.address.text = model.ADDR
        binding.tel.text = model.TEL
    }
}