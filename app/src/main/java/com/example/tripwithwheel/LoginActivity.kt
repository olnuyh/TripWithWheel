    package com.example.tripwithwheel

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tripwithwheel.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val email = binding.loginId.text.toString()
            val password = binding.loginPwd.text.toString()

            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email

                            MyApplication.loginType = "firebase"
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(baseContext, "이메일 인증을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(baseContext, "로그인 실패, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.e("mobileApp", "토큰 정보 보기 실패", error)
                }
                else if (tokenInfo != null) {
                    Log.i("mobileApp", "토큰 정보 보기 성공")
                }
            }

            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("mobileApp", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("mobileApp", "카카오계정으로 로그인 성공 ${token.accessToken}")

                    // 사용자 정보 요청 (기본)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("mobileApp", "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i("mobileApp", "사용자 정보 요청 성공 ${user.kakaoAccount?.email}")
                            MyApplication.loginType = "kakao"

                            var scopes = mutableListOf<String>()

                            if(user.kakaoAccount?.email != null){
                                MyApplication.email = user.kakaoAccount?.email

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            else if(user.kakaoAccount?.emailNeedsAgreement == true){
                                Log.i("mobileApp", "사용자에게 추가 동의 필요")
                                scopes.add("account_email")
                                UserApiClient.instance.loginWithNewScopes(this, scopes){token, error ->
                                    if(error != null){
                                        Log.d("mobileApp", "추가 동의 실패", error)
                                    }
                                    else{
                                        //사용자에게 정보 재요청
                                        UserApiClient.instance.me{ user, error ->
                                            if(error != null){
                                                Log.i("mobileApp", "사용자 정보 요청 실패", error)
                                            }
                                            else if(user != null){
                                                MyApplication.email = user.kakaoAccount?.email.toString()
                                                MyApplication.loginType = "kakao"

                                                val intent = Intent(this, MainActivity::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                Log.e("mobileApp", "이메일 획득 불가", error)
                            }
                        }
                    }
                }
            }

            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }
            else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }

        }

        binding.goToSignupBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}