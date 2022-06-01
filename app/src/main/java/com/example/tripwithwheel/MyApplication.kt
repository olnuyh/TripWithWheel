package com.example.tripwithwheel

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application(){
    companion object{
        var networkServiceSpot : NetworkService
        var networkServiceRestaurant : NetworkService
        var networkServiceToilet : NetworkService
        var networkServiceCharging : NetworkService
        val retrofit : Retrofit
            get() = Retrofit.Builder()
                .baseUrl("http://openapi.seoul.go.kr:8088/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init{
            networkServiceSpot = retrofit.create(NetworkService::class.java)
            networkServiceRestaurant = retrofit.create(NetworkService::class.java)
            networkServiceToilet = retrofit.create(NetworkService::class.java)
            networkServiceCharging = retrofit.create(NetworkService::class.java)

        }
    }
}