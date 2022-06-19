package com.example.tripwithwheel

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Row2(val SISULNAME : String, val ADDR : String, val TEL : String, val ST2 : String, val ST5 : String)
data class touristFoodInfo(val row : MutableList<Row2>)
data class RestaurantModel(val touristFoodInfo : touristFoodInfo)
