package com.example.tripwithwheel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripwithwheel.databinding.ItemRecyclerviewBinding

class SpotViewHolder(val binding : ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)
class SpotAdapter(val context : Context, val datas : MutableList<Row>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SpotViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SpotViewHolder).binding
        val model = datas!![position]
        binding.name.text = model.POST_SJ
        binding.address.text = model.ADDRESS
        binding.tel.text = model.CMMN_TELNO
    }
}