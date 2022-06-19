package com.example.tripwithwheel

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tripwithwheel.databinding.ActivityReadReviewBinding
import com.example.tripwithwheel.databinding.FragmentHomeBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private var param1: String? = null
    private var param2: String? = null
    private var googleMap : GoogleMap? = null
    private lateinit var binding : FragmentHomeBinding
    lateinit var spot : List<Row>
    lateinit var restaurant : List<Row2>
    lateinit var toilet : List<Row3>
    lateinit var charging : List<Row4>
    lateinit var apiClient : GoogleApiClient
    lateinit var providerClient : FusedLocationProviderClient
    lateinit var mainActivity : MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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

        binding.cardInfo.movementMethod = ScrollingMovementMethod()

        providerClient = LocationServices.getFusedLocationProviderClient(mainActivity)
        apiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if(it.all{permission -> permission.value == true}){
                apiClient.connect()
            }else{
                Toast.makeText(mainActivity, "권한 거부..", Toast.LENGTH_LONG).show()
            }
        }

        if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_NETWORK_STATE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                )
            )
        }else{
            apiClient.connect()
        }

        binding.curFab.setOnClickListener {
            providerClient.lastLocation.addOnSuccessListener(
                mainActivity,
                object : OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        p0?.let{
                            val latitude = p0.latitude
                            val longitude = p0.longitude
                            val position : CameraPosition = CameraPosition.Builder()
                                .target(LatLng(latitude,longitude))
                                .zoom(16F)
                                .build()
                            googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
                        }
                    }
                }
            )
            apiClient.disconnect()
            if(binding.cardView.visibility == View.VISIBLE){
                binding.cardView.visibility = View.GONE
            }
        }

        binding.cardToReview.setOnClickListener {
            val intent = Intent(mainActivity, ReadReviewActivity::class.java)
            startActivity(intent)
        }

        binding.cardToRegistration.setOnClickListener {
            val calendar : Calendar = Calendar.getInstance()

            DatePickerDialog(mainActivity,
                object : DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                        val filename = p1.toString() + (p2 + 1).toString() + p3.toString()
                        val file = File(mainActivity.filesDir, "${MyApplication.email}_" + "$filename" +".txt")

                        if(!file.exists()){
                            val writeStream: OutputStreamWriter = file.writer()
                            writeStream.write(MyApplication.markerName)
                            writeStream.flush()
                        }
                        else{
                            file.appendText("\n" + MyApplication.markerName)
                        }
                    }
                },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show()
        }

        return binding.root
    }

    private fun moveMap(latitude : Double, longitude : Double){
        val latLng = LatLng(latitude, longitude)
        val position : CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16F)
            .build()
        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        val markerOp = MarkerOptions()
        markerOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.curloc))
        markerOp.position(latLng)
        markerOp.title("cur")
        googleMap?.addMarker(markerOp)
    }

    override fun onConnected(p0: Bundle?) {
        if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED){
            providerClient.lastLocation.addOnSuccessListener(
                mainActivity,
                object : OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        p0?.let{
                            val latitude = p0.latitude
                            val longitude = p0.longitude
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



    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        spot = MyApplication.result_spot.TbVwAttractions.row

        for (i in 0 until spot.size) { //관광지 데이터 각각 주소를 가져와서 geocoder로 위도, 경도 정보를 얻어와 지도에 마커로 표시
            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) //관광지는 빨간색 마커로 표시
            markerOp.position(LatLng(MyApplication.spot_loc_lat[i], MyApplication.spot_loc_lon[i]))
            markerOp.title(spot[i].POST_SJ)

            googleMap?.addMarker(markerOp)

        }

        restaurant = MyApplication.result_restaurant.touristFoodInfo.row

        for (i in 0 until restaurant.size) { //음식점 데이터 각각 주소를 가져와서 geocoder로 위도, 도 정보를 얻어와 지도에 마커로 표시
            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)) //음식점은 주황색 마커로 표시
            markerOp.position(LatLng(MyApplication.restaurant_loc_lat[i], MyApplication.restaurant_loc_lon[i]))
            markerOp.title(restaurant[i].SISULNAME)

            googleMap?.addMarker(markerOp)
        }

        toilet = MyApplication.result_toilet.viewAmenitiesInfo.row

        for (i in 0 until toilet.size) { //화장실 데이터 각각 주소를 가져와서 geocoder로 위도, 경도 정보를 얻어와 지도에 마커로 표시
            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) //화장실은 노란색 마커로 표시
            markerOp.position(LatLng(MyApplication.toilet_loc_lat[i], MyApplication.toilet_loc_lon[i]))
            markerOp.title(toilet[i].SISULNAME)

            googleMap?.addMarker(markerOp)

        }

        charging = MyApplication.result_charging.tbElecWheelChrCharge.row

        for (i in 0 until charging.size) { //충전소 데이터 각각 위도와 경도를 가져와서 지도에 마커로 표시
            val latitude = (charging[i].LATITUDE).toDouble()
            val longitude = (charging[i].LONGITUDE).toDouble()
            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) //충전소는 초록색 마커로 표시
            markerOp.position(LatLng(latitude, longitude))
            markerOp.title(charging[i].FCLTYNM)
            googleMap?.addMarker(markerOp)
        }

        p0!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(p0: Marker?): Boolean {
                if(!p0!!.title.equals("cur")){
                    binding.cardView.visibility = View.VISIBLE
                    binding.cardName.text = p0?.title

                    when(p0!!.id.substring(1).toInt() / 7){
                        0 ->{
                            val cardMarker = spot.find{ it.POST_SJ.equals(p0?.title)}
                            binding.cardAddr.text = cardMarker!!.ADDRESS
                            binding.cardTel.text = cardMarker!!.CMMN_TELNO
                            binding.cardInfo.text = cardMarker!!.BF_DESC
                            binding.cardEtc.text = ""
                            MyApplication.markerName = cardMarker!!.POST_SJ
                        }
                        1 ->{
                            val cardMarker = restaurant.find{ it.SISULNAME.equals(p0?.title)}
                            binding.cardAddr.text = cardMarker!!.ADDR
                            binding.cardTel.text = cardMarker!!.TEL

                            var parking = ""
                            var toilet = ""

                            if(cardMarker!!.ST2.equals("Y")){
                                parking = "장애인 전용 주차구역 O"
                            }else{
                                parking = "장애인 전용 주차구역 X"
                            }

                            if(cardMarker!!.ST5.equals("Y")){
                                toilet = "장애인 화장실 O"
                            }else{
                                toilet = "장애인 화장실 X"
                            }

                            binding.cardInfo.text = parking
                            binding.cardEtc.text = toilet
                            MyApplication.markerName = cardMarker!!.SISULNAME
                        }
                        2 ->{
                            val cardMarker = toilet.find{ it.SISULNAME.equals(p0?.title)}
                            binding.cardAddr.text = cardMarker!!.ADDR
                            binding.cardTel.text = cardMarker!!.TEL
                            var parking = ""
                            var toilet = ""

                            if(cardMarker!!.ST2.equals("Y")){
                                parking = "장애인 전용 주차구역 O"
                            }else{
                                parking = "장애인 전용 주차구역 X"
                            }

                            if(cardMarker!!.ST5.equals("Y")){
                                toilet = "장애인 화장실 O"
                            }else{
                                toilet = "장애인 화장실 X"
                            }

                            binding.cardInfo.text = parking
                            binding.cardEtc.text = toilet
                            MyApplication.markerName = cardMarker!!.SISULNAME
                        }
                        3 ->{
                            val cardMarker = charging.find{ it.FCLTYNM.equals(p0?.title)}
                            binding.cardAddr.text = cardMarker!!.RDNMADR
                            binding.cardTel.text = cardMarker!!.INSTITUTIONPHONENUMBER
                            binding.cardInfo.text = "위치: " + cardMarker!!.INSTLLCDESC
                            val arr = arrayOf(cardMarker!!.WEEKDAYOPEROPENHHMM, cardMarker!!.WEEKDAYOPERCOLSEHHMM)
                            binding.cardEtc.text = "운영시간: " + arr.joinToString(" ~ ")
                            MyApplication.markerName = cardMarker!!.FCLTYNM
                        }
                    }
                }
                return true
            }
        })

        p0!!.setOnMapClickListener (object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                binding.cardView.visibility = View.GONE
            }
        })
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