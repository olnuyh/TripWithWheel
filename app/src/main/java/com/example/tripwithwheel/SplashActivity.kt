package com.example.tripwithwheel

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.concurrent.*
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        val service = Intent(applicationContext, AddressIntentService::class.java)
        startService(service)

        val backgroundExecutor : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val mainExecutor : Executor = ContextCompat.getMainExecutor(this)
        backgroundExecutor.schedule({
            mainExecutor.execute{
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2, TimeUnit.SECONDS)
    }

    private fun geocoder(addr : String) : MutableList<Double> {
        val geocoder = Geocoder(applicationContext)
        val geocodedAddress = geocoder.getFromLocationName(addr, 1)

        val latitude = geocodedAddress[0].latitude
        val longitude = geocodedAddress[0].longitude

        val loc = arrayListOf<Double>(latitude, longitude)

        return loc
    }
}