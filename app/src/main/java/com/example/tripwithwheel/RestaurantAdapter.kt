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

        var parking = ""
        var toilet = ""

        if(model.ST2.equals("Y")){
            parking = "장애인 전용 주차구역 O"
        }else{
            parking = "장애인 전용 주차구역 X"
        }

        if(model.ST5.equals("Y")){
            toilet = "장애인 화장실 O"
        }else{
            toilet = "장애인 화장실 X"
        }

        binding.info.text = parking
        binding.etc.text = toilet
    }
}