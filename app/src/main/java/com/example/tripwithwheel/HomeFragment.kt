package com.example.tripwithwheel

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripwithwheel.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var result_spot : SpotModel //관광지 데이터에 대한 변수
    private lateinit var result_restaurant : RestaurantModel //음식점 데이터에 대한 변수
    private lateinit var result_charging : ChargingModel //충전소 데이터에 대한 변수
    private lateinit var result_toilet : ToiletModel //화장실 데이터에 대한 변수
    private var googleMap : GoogleMap? = null
    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val call_spot : Call<SpotModel> = MyApplication.networkServiceSpot.getSpotList(
            apiKey = "54674a6d79726968313867504c5a4d"
        )

        call_spot?.enqueue(object : Callback<SpotModel>{
            override fun onResponse(call: Call<SpotModel>, response: Response<SpotModel>) {
                if(response.isSuccessful){
                    result_spot = response.body() as SpotModel
                }
            }

            override fun onFailure(call: Call<SpotModel>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        val call_restaurant : Call<RestaurantModel> = MyApplication.networkServiceRestaurant.getRestaurantList(
            apiKey = "54674a6d79726968313867504c5a4d"
        )

        call_restaurant?.enqueue(object : Callback<RestaurantModel> {
            override fun onResponse(call: Call<RestaurantModel>, response: Response<RestaurantModel>) {
                if(response.isSuccessful){
                    result_restaurant = response.body() as RestaurantModel
                }
            }

            override fun onFailure(call: Call<RestaurantModel>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        val call_toilet : Call<ToiletModel> = MyApplication.networkServiceToilet.getToiletList(
            apiKey = "54674a6d79726968313867504c5a4d"
        )

        call_toilet?.enqueue(object : Callback<ToiletModel> {
            override fun onResponse(call: Call<ToiletModel>, response: Response<ToiletModel>) {
                if(response.isSuccessful){
                    result_toilet = response.body() as ToiletModel
                }
            }

            override fun onFailure(call: Call<ToiletModel>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        val call_charging : Call<ChargingModel> = MyApplication.networkServiceCharging.getChargingList(
            apiKey = "54674a6d79726968313867504c5a4d"
        )

        call_charging?.enqueue(object : Callback<ChargingModel> {
            override fun onResponse(call: Call<ChargingModel>, response: Response<ChargingModel>) {
                if(response.isSuccessful){
                    result_charging = response.body() as ChargingModel //충전소 데이터 모두 가져와서 변수에 담기
                }
            }

            override fun onFailure(call: Call<ChargingModel>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        val spot = result_spot.TbVwAttractions.row

        for (i in 0 until spot.size) { //관광지 데이터 각각 주소를 가져와서 geocoder로 위도, 경도 정보를 얻어와 지도에 마커로 표시
            if(spot[i].ADDRESS.equals("")){ //주소에 대한 정보가 없는 장소는 배제
                continue
            }

            val geocoder = Geocoder(context)
            val geocodedAddress = geocoder.getFromLocationName(spot[i].ADDRESS, 1)

            val latitude = geocodedAddress[0].latitude
            val longitude = geocodedAddress[0].longitude

            val position : CameraPosition = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(16F)
                .build()

            googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))

            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_spot)) //관광지는 빨간색 마커로 표시
            markerOp.position(LatLng(latitude, longitude))
            markerOp.title(spot[i].POST_SJ)

            googleMap?.addMarker(markerOp)

        }

        val restaurant = result_restaurant.touristFoodInfo.row

        for (i in 0 until restaurant.size) { //음식점점 데이터 각각 주소를 가져와서 geocoder로 위도, 도 정보를 얻어와 지도에 마커로 표시
            if(restaurant[i].ADDR.equals("")){ //주소에 대한 정보가 없는 장소는 배제
                continue
            }

            val geocoder = Geocoder(context)
            val geocodedAddress = geocoder.getFromLocationName(restaurant[i].ADDR, 1)

            val latitude = geocodedAddress[0].latitude
            val longitude = geocodedAddress[0].longitude

            val position : CameraPosition = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(16F)
                .build()

            googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))

            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant)) //음식점은 주황색 마커로 표시
            markerOp.position(LatLng(latitude, longitude))
            markerOp.title(restaurant[i].SISULNAME)

            googleMap?.addMarker(markerOp)

        }

        val toilet = result_toilet.viewAmenitiesInfo.row

        for (i in 0 until toilet.size) { //화장실 데이터 각각 주소를 가져와서 geocoder로 위도, 경도 정보를 얻어와 지도에 마커로 표시
            if(toilet[i].ADDR.equals("")){ //주소에 대한 정보가 없는 장소는 배제
                continue
            }

            val geocoder = Geocoder(context)
            val geocodedAddress = geocoder.getFromLocationName(toilet[i].ADDR, 1)

            val latitude = geocodedAddress[0].latitude
            val longitude = geocodedAddress[0].longitude

            val position : CameraPosition = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(16F)
                .build()

            googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))

            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_toilet)) //화장실은 노란색 마커로 표시
            markerOp.position(LatLng(latitude, longitude))
            markerOp.title(toilet[i].SISULNAME)

            googleMap?.addMarker(markerOp)

        }

        val charging = result_charging.tbElecWheelChrCharge.row

        for (i in 0 until charging.size) { //충전소 데이터 각각 위도와 경도를 가져와서 지도에 마커로 표시
            if(charging[i].LATITUDE.equals("") || charging[i].LONGITUDE.equals("")){ //위도나 경도에 대한 정보가 없는 장소는 배제
                continue
            }

            val latitude = (charging[i].LATITUDE).toDouble()
            val longitude = (charging[i].LONGITUDE).toDouble()

            val position : CameraPosition = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(16F)
                .build()

            googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))

            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_charging)) //충전소는 초록색 마커로 표시
            markerOp.position(LatLng(latitude, longitude))
            markerOp.title(charging[i].FCLTYNM)

            googleMap?.addMarker(markerOp)

        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}