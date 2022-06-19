package com.example.tripwithwheel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripwithwheel.databinding.ItemRecyclerviewBinding

class ChargingViewHolder(val binding : ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)
class ChargingAdapter(val context : Context, val datas : MutableList<Row4>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChargingViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ChargingViewHolder).binding
        val model = datas!![position]
        binding.name.text = model.FCLTYNM
        binding.address.text = model.RDNMADR
        binding.tel.text = model.INSTITUTIONPHONENUMBER
        binding.info.text = "위치: " + model.INSTLLCDESC
        val arr = arrayOf(model.WEEKDAYOPEROPENHHMM, model.WEEKDAYOPERCOLSEHHMM)
        binding.etc.text = "운영시간: " + arr.joinToString(" ~ ")
    }
}