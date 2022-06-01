package com.example.tripwithwheel

data class Row(val POST_SJ : String, val ADDRESS : String, val CMMN_TELNO : String)
data class TbVwAttractions(val row : MutableList<Row>)
data class SpotModel(val TbVwAttractions : TbVwAttractions)