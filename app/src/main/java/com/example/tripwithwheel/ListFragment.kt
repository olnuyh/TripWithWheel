package com.example.tripwithwheel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.tripwithwheel.databinding.FragmentListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    class MyFragmentAdapter(fragment : Fragment) : FragmentStateAdapter(fragment){
        val fragments : List<Fragment>
        init{
            fragments = listOf(ListSpotFragment(), ListRestaurantFragment(), ListToiletFragment(), ListChargingFragment())
        }
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
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
        val binding = FragmentListBinding.inflate(inflater, container, false)
        binding.listViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL //뷰페이저 동작 방향 가로로 설정
        binding.listViewPager.adapter = MyFragmentAdapter(this) //뷰페이저 어댑터 연결

        val tabTitles = listOf<String>("관광지", "음식점", "화장실", "충전소") //탭에 보여질 이름
        TabLayoutMediator(binding.listTab, binding.listViewPager){ //뷰페이저와 탭레이아웃 연동
            tab, position -> tab.text = tabTitles[position]
        }.attach()

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}