package com.example.tripwithwheel

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Row4(val FCLTYNM : String, val RDNMADR : String, val INSTITUTIONPHONENUMBER : String, val LATITUDE : String, val LONGITUDE : String)
data class tbElecWheelChrCharge(val row : MutableList<Row4>)
data class ChargingModel(val tbElecWheelChrCharge : tbElecWheelChrCharge)