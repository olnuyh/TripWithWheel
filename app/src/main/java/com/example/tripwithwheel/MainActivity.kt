package com.example.tripwithwheel

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.tripwithwheel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        //val bgColor = sharedPreferences.getString("change_color", "")
        //binding.mainLayout.setBackgroundColor(Color.parseColor(bgColor))

        binding.navigation.setOnItemSelectedListener { //하단 네비게이션 바에서 선택된 메뉴 확인
            replaceFragment(
                when (it.itemId) { //각 메뉴에 맞는 프래그먼트 지정
                    R.id.menu_home -> HomeFragment()
                    R.id.menu_list -> ListFragment()
                    R.id.menu_schedule -> ScheduleFragment()
                    else -> SettingsFragment()
                }
            )
            true
        }
        binding.navigation.selectedItemId = R.id.menu_home
    }

    private fun replaceFragment(fragment : Fragment){ //지정된 프래그먼트로 현재 프래그먼트 대체
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    }
}