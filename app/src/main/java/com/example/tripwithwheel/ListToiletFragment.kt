package com.example.tripwithwheel

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripwithwheel.databinding.FragmentListSpotBinding
import com.example.tripwithwheel.databinding.FragmentListToiletBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListToiletFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListToiletFragment : Fragment() {
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
        val binding = FragmentListToiletBinding.inflate(inflater, container, false)
        binding.toiletRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.toiletRecyclerView.adapter = ToiletAdapter(activity as Context, MyApplication.result_toilet.viewAmenitiesInfo.row)
        binding.toiletRecyclerView.addItemDecoration(DividerItemDecoration(activity as Context, LinearLayoutManager.VERTICAL))

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListToiletFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListToiletFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}