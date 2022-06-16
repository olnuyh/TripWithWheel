package com.example.tripwithwheel

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tripwithwheel.databinding.FragmentHomeBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
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

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        return binding.root
    }

    /*
    private fun moveMap(latitude : Double, longitude : Double){
        val latLng = LatLng(latitude, longitude)
        val position : CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        val markerOp = MarkerOptions()
        markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOp.position(latLng)
        markerOp.title("My Location")
        googleMap?.addMarker(markerOp)
    }
    override fun onConnected(p0: Bundle?) {
        if(ContextCompat.checkSelfPermission(context as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED){
            providerClient.lastLocation.addOnSuccessListener(
                context as MainActivity,
                object : OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        p0?.let{
                            val latitude = p0.latitude
                            val longitude = p0.longitude
                            Log.d("mobileApp", "lat: $latitude, lng: $longitude")
                            moveMap(latitude, longitude)
                        }
                    }
                }
            )
            apiClient.disconnect()
        }
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
    }
    override fun onConnectionSuspended(p0: Int) {
    }
     */

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        val spot = MyApplication.result_spot.TbVwAttractions.row

        for (i in 0 until spot.size) { //관광지 데이터 각각 주소를 가져와서 geocoder로 위도, 경도 정보를 얻어와 지도에 마커로 표시
            if(spot[i].ADDRESS.equals("")){ //주소에 대한 정보가 없는 장소는 배제
                continue
            }

            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) //관광지는 빨간색 마커로 표시
            markerOp.position(LatLng(MyApplication.spot_loc_lat[i], MyApplication.spot_loc_lon[i]))
            markerOp.title(spot[i].POST_SJ)

            googleMap?.addMarker(markerOp)

        }

        val restaurant = MyApplication.result_restaurant.touristFoodInfo.row

        for (i in 0 until restaurant.size) { //음식점 데이터 각각 주소를 가져와서 geocoder로 위도, 도 정보를 얻어와 지도에 마커로 표시
            if(restaurant[i].ADDR.equals("")){ //주소에 대한 정보가 없는 장소는 배제
                continue
            }

            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)) //음식점은 주황색 마커로 표시
            markerOp.position(LatLng(MyApplication.restaurant_loc_lat[i], MyApplication.restaurant_loc_lon[i]))
            markerOp.title(restaurant[i].SISULNAME)

            googleMap?.addMarker(markerOp)
        }

        val toilet = MyApplication.result_toilet.viewAmenitiesInfo.row

        for (i in 0 until toilet.size) { //화장실 데이터 각각 주소를 가져와서 geocoder로 위도, 경도 정보를 얻어와 지도에 마커로 표시
            if(toilet[i].ADDR.equals("")){ //주소에 대한 정보가 없는 장소는 배제
                continue
            }

            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) //화장실은 노란색 마커로 표시
            markerOp.position(LatLng(MyApplication.toilet_loc_lat[i], MyApplication.toilet_loc_lon[i]))
            markerOp.title(toilet[i].SISULNAME)

            googleMap?.addMarker(markerOp)

        }

        val charging = MyApplication.result_charging.tbElecWheelChrCharge.row
        for (i in 0 until charging.size) { //충전소 데이터 각각 위도와 경도를 가져와서 지도에 마커로 표시
            if(charging[i].LATITUDE.equals("") || charging[i].LONGITUDE.equals("")){ //위도나 경도에 대한 정보가 없는 장소는 배제
                continue
            }
            val latitude = (charging[i].LATITUDE).toDouble()
            val longitude = (charging[i].LONGITUDE).toDouble()
            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) //충전소는 초록색 마커로 표시
            markerOp.position(LatLng(latitude, longitude))
            markerOp.title(charging[i].FCLTYNM)
            googleMap?.addMarker(markerOp)
        }

        /*
        providerClient = LocationServices.getFusedLocationProviderClient(context as MainActivity)
        apiClient = GoogleApiClient.Builder(context as MainActivity)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if(it.all{ permission -> permission.value == true}){
                apiClient.connect()
            }else{
                Toast.makeText(context as MainActivity, "권한 거부..", Toast.LENGTH_LONG).show()
            }
        }
        if(ContextCompat.checkSelfPermission(context as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context as MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context as MainActivity, Manifest.permission.ACCESS_NETWORK_STATE) !== PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                )
            )
        }
        else{
            apiClient.connect()
        }
         */
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