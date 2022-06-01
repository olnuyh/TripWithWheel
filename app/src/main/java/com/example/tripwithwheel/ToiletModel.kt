package com.example.tripwithwheel

data class Row3(val SISULNAME : String, val ADDR : String, val TEL : String)
data class viewAmenitiesInfo(val row : MutableList<Row3>)
data class ToiletModel(val viewAmenitiesInfo : viewAmenitiesInfo)