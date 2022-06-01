package com.example.tripwithwheel

data class Row2(val SISULNAME : String, val ADDR : String, val TEL : String)
data class touristFoodInfo(val row : MutableList<Row2>)
data class RestaurantModel(val touristFoodInfo : touristFoodInfo)
