package com.example.tripwithwheel

import android.location.Geocoder
import com.example.tripwithwheel.databinding.ActivityMainBinding
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Row(val POST_SJ : String, val ADDRESS : String, val CMMN_TELNO : String)
data class TbVwAttractions(val row : MutableList<Row>)
data class SpotModel(val TbVwAttractions : TbVwAttractions)