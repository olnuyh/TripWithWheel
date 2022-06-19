package com.example.tripwithwheel

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.example.tripwithwheel.databinding.ActivityMainBinding
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity(){
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_logout){
            if(MyApplication.loginType.equals("firebase")){
                MyApplication.auth.signOut()
                MyApplication.email = null
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                finish()
            }
            else if(MyApplication.loginType.equals("kakao")){
                UserApiClient.instance.logout{ error ->
                    if(error != null){
                        Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment : Fragment){ //지정된 프래그먼트로 현재 프래그먼트 대체
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    }
}