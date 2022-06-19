package com.example.tripwithwheel

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.location.Geocoder
import android.util.Log

// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.example.tripwithwheel.action.FOO"
private const val ACTION_BAZ = "com.example.tripwithwheel.action.BAZ"

private const val EXTRA_PARAM1 = "com.example.tripwithwheel.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.tripwithwheel.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * helper methods.

 */
class AddressIntentService : IntentService("AddressIntentService") {

    private fun geocoder(addr : String) : MutableList<Double> {
        val geocoder = Geocoder(applicationContext)
        val geocodedAddress = geocoder.getFromLocationName(addr, 1)

        val latitude = geocodedAddress[0].latitude
        val longitude = geocodedAddress[0].longitude

        val loc = arrayListOf<Double>(latitude, longitude)

        return loc
    }


    override fun onHandleIntent(intent: Intent?) {
        if(MyApplication.result_spot == null){
            Thread.sleep(2000)
        }
        Log.d("mobileApp", "start")
        val spot = MyApplication.result_spot.TbVwAttractions.row
        for (i in 0 until spot.size) {
            val loc = geocoder(spot[i].ADDRESS)
            MyApplication.spot_loc_lat.add(loc[0])
            MyApplication.spot_loc_lon.add(loc[1])
        }

        val restaurant = MyApplication.result_restaurant.touristFoodInfo.row
        for (i in 0 until restaurant.size) {
            val loc = geocoder(restaurant[i].ADDR)
            MyApplication.restaurant_loc_lat.add(loc[0])
            MyApplication.restaurant_loc_lon.add(loc[1])
        }

        val toilet = MyApplication.result_toilet.viewAmenitiesInfo.row
        for (i in 0 until toilet.size) {
            val loc = geocoder(toilet[i].ADDR)
            MyApplication.toilet_loc_lat.add(loc[0])
            MyApplication.toilet_loc_lon.add(loc[1])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("mobileApp", "end")
    }
    companion object {

    }
}