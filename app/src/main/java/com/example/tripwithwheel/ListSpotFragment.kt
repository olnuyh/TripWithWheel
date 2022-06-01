package com.example.tripwithwheel

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripwithwheel.databinding.FragmentListSpotBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListSpotFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListSpotFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        val binding = FragmentListSpotBinding.inflate(inflater, container, false)
        val call : Call<SpotModel> = MyApplication.networkServiceSpot.getSpotList(
            apiKey = "54674a6d79726968313867504c5a4d"
        )

        call?.enqueue(object : Callback<SpotModel>{
            override fun onResponse(call: Call<SpotModel>, response: Response<SpotModel>) {
                if(response.isSuccessful){
                    binding.spotRecyclerView.layoutManager = LinearLayoutManager(activity)
                    var result = response.body() as SpotModel
                    binding.spotRecyclerView.adapter = SpotAdapter(activity as Context, result.TbVwAttractions.row)
                }
            }

            override fun onFailure(call: Call<SpotModel>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListSpotFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListSpotFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}