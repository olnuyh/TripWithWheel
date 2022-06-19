package com.example.tripwithwheel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripwithwheel.databinding.ActivityReadReviewBinding
import com.google.firebase.firestore.ktx.toObject

class ReadReviewActivity : AppCompatActivity() {
    lateinit var binding : ActivityReadReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_read_review)

        binding = ActivityReadReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCheckPermission(this)

        binding.goToAddReview.setOnClickListener{
            if(MyApplication.checkAuth()){
                val intent = Intent(this, WriteReviewActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "인증을 진행해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth() || MyApplication.email != null){
            makeRecyclerView()
            binding.reviews.visibility = View.VISIBLE
        }
        else{
            binding.reviews.visibility = View.GONE
        }
    }


    private fun makeRecyclerView(){
        MyApplication.db.collection("reviews_"+ MyApplication.markerName)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemData>()
                for(document in result){
                    val item = document.toObject(ItemData::class.java)
                    item.docid = document.id
                    itemList.add(item)
                }
                binding.reviews.layoutManager = LinearLayoutManager(this)
                binding.reviews.adapter = ReviewAdapter(this, itemList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }
}