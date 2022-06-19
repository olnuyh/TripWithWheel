package com.example.tripwithwheel

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.tripwithwheel.databinding.FragmentScheduleBinding
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.File

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentScheduleBinding

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
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val date = i.toString() + (i2 + 1).toString() + i3.toString()
            val path = context?.filesDir.toString() + "/" + MyApplication.email + "_" + date + ".txt"
            val file = File(path)
            if(!file.exists()){
                Log.d("mobileApp", "파일 없음")
                binding.scheduleLayout.visibility = View.GONE
            }
            else{
                val readFile = file.readLines()
                var j = 1
                binding.scheduleLayout.removeAllViews()
                for(i in readFile){
                    val textView = TextView(context)
                    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams.setMargins(100, 40, 0, 0)
                    textView.layoutParams = layoutParams
                    textView.text = j.toString() + ". "+ i.toString()
                    val color = ContextCompat.getColor(context as MainActivity, R.color.sub)
                    textView.setTextColor(color)
                    textView.setTypeface(Typeface.createFromAsset(resources.assets, "small_font.ttf"), Typeface.BOLD)
                    textView.setTextSize(Dimension.SP, 22F)
                    binding.scheduleLayout.addView(textView)
                    j++
                }
                binding.scheduleLayout.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}