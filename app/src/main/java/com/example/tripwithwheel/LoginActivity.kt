package com.example.tripwithwheel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tripwithwheel.databinding.ActivityLoginBinding

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
                    binding.loginId.text.clear()
                    binding.loginPwd.text.clear()

                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email

                            val intent = Intent(this, ReadReviewActivity::class.java)
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

        binding.goToSignupBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}