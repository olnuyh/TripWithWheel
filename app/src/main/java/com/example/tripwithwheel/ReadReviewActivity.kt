package com.example.tripwithwheel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tripwithwheel.databinding.ActivityReadReviewBinding

class ReadReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_read_review)

        val binding = ActivityReadReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}