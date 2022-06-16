package com.example.tripwithwheel

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : MultiDexApplication() {
    companion object {
        var networkServiceSpot: NetworkService
        var networkServiceRestaurant: NetworkService
        var networkServiceToilet: NetworkService
        var networkServiceCharging: NetworkService
        lateinit var result_spot: SpotModel
        lateinit var result_restaurant: RestaurantModel
        lateinit var result_toilet: ToiletModel
        lateinit var result_charging: ChargingModel
        lateinit var string : String
        var spot_loc_lat : MutableList<Double> = mutableListOf()
        var spot_loc_lon : MutableList<Double> = mutableListOf()
        var restaurant_loc_lat : MutableList<Double> = mutableListOf()
        var restaurant_loc_lon : MutableList<Double> = mutableListOf()
        var toilet_loc_lat : MutableList<Double> = mutableListOf()
        var toilet_loc_lon : MutableList<Double> = mutableListOf()

        val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl("http://openapi.seoul.go.kr:8088/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        init {
            networkServiceSpot = retrofit.create(NetworkService::class.java).also {
                it.getSpotList("54674a6d79726968313867504c5a4d")
                    .enqueue(object : Callback<SpotModel> {
                        override fun onResponse(
                            call: Call<SpotModel>,
                            response: Response<SpotModel>
                        ) {
                            if (response.isSuccessful) {
                                result_spot = response.body() as SpotModel
                            }
                        }

                        override fun onFailure(call: Call<SpotModel>, t: Throwable) {
                            Log.d("mobileApp", "onFailure")
                        }
                    })
                }

            networkServiceRestaurant = retrofit.create(NetworkService::class.java).also{
                it.getRestaurantList("54674a6d79726968313867504c5a4d")
                    .enqueue(object : Callback<RestaurantModel> {
                        override fun onResponse(
                            call: Call<RestaurantModel>,
                            response: Response<RestaurantModel>
                        ) {
                            if (response.isSuccessful) {
                                result_restaurant = response.body() as RestaurantModel
                            }
                        }

                        override fun onFailure(call: Call<RestaurantModel>, t: Throwable) {
                            Log.d("mobileApp", "onFailure")
                        }
                    })
                }

            networkServiceToilet = retrofit.create(NetworkService::class.java).also{
                it.getToiletList("54674a6d79726968313867504c5a4d")
                    .enqueue(object : Callback<ToiletModel> {
                        override fun onResponse(
                            call: Call<ToiletModel>,
                            response: Response<ToiletModel>
                        ) {
                            if (response.isSuccessful) {
                                result_toilet = response.body() as ToiletModel
                            }
                        }

                        override fun onFailure(call: Call<ToiletModel>, t: Throwable) {
                            Log.d("mobileApp", "onFailure")
                        }
                    })
                }

            networkServiceCharging = retrofit.create(NetworkService::class.java).also{
                it.getChargingList("54674a6d79726968313867504c5a4d")
                    .enqueue(object : Callback<ChargingModel> {
                        override fun onResponse(
                            call: Call<ChargingModel>,
                            response: Response<ChargingModel>
                        ) {
                            if (response.isSuccessful) {
                            result_charging = response.body() as ChargingModel
                        }
                    }

                        override fun onFailure(call: Call<ChargingModel>, t: Throwable) {
                            Log.d("mobileApp", "onFailure")
                        }
                    })
                }

            }

        lateinit var auth: FirebaseAuth //파이어베이스 Authentication 객체를 전역 변수로 사용
        var email: String? = null

        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }

    }

        override fun onCreate() {
            super.onCreate()
            auth = Firebase.auth
            KakaoSdk.init(this, "b24008d58fe7744b00916fefa69494b1")
        }
    }