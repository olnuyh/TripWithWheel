package com.example.tripwithwheel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        val backgroundExecutor : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val mainExecutor : Executor = ContextCompat.getMainExecutor(this)
        backgroundExecutor.schedule({
            mainExecutor.execute{
                val intent = Intent(applicationContext, AddressIntentService::class.java)
                startService(intent)

                val intent2 = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent2)
                finish()
            }
        }, 2, TimeUnit.SECONDS)
    }
}