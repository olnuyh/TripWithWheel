package com.example.tripwithwheel

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Row3(val SISULNAME : String, val ADDR : String, val TEL : String, val ST2 : String, val ST5 : String)
data class viewAmenitiesInfo(val row : MutableList<Row3>)
data class ToiletModel(val viewAmenitiesInfo : viewAmenitiesInfo)