package com.example.tripwithwheel

data class Row4(val FCLTYNM : String, val RDNMADR : String, val INSTITUTIONPHONENUMBER : String)
data class tbElecWheelChrCharge(val row : MutableList<Row4>)
data class ChargingModel(val tbElecWheelChrCharge : tbElecWheelChrCharge)