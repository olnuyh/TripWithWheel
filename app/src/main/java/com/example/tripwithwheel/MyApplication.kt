package com.example.tripwithwheel

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : MultiDexApplication(){
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

        lateinit var auth : FirebaseAuth //파이어베이스 Authentication 객체를 전역 변수로 사용
        var email : String? = null

        fun checkAuth() : Boolean{
            var currentUser = auth.currentUser
            return currentUser?.let{
                email = currentUser.email
                currentUser.isEmailVerified
            }?: let{
                false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}