package com.example.tripwithwheel

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkService {
    @GET("{api_key}/json/TbVwAttractions/1/5/")
    fun getSpotList(
        @Path("api_key") apiKey : String
    ): Call<SpotModel>

    @GET("{api_key}/json/touristFoodInfo/1/5/")
    fun getRestaurantList(
        @Path("api_key") apiKey : String
    ): Call<RestaurantModel>

    @GET("{api_key}/json/viewAmenitiesInfo/1/5/")
    fun getToiletList(
        @Path("api_key") apiKey : String
    ): Call<ToiletModel>

    @GET("{api_key}/json/tbElecWheelChrCharge/1/5/")
    fun getChargingList(
        @Path("api_key") apiKey : String
    ): Call<ChargingModel>

}