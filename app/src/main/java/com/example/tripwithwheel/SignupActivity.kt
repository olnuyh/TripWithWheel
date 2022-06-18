package com.example.tripwithwheel

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tripwithwheel.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signup)

        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            val email = binding.signupId.text.toString()
            val password = binding.signupPwd.text.toString()
            //val nickname = binding.nickName.text.toString()

            MyApplication.auth.createUserWithEmailAndPassword(email, password) //사용자가 입력한 이메일과 비밀번호를 기반으로 회원가입
                .addOnCompleteListener(this) { task ->
                    binding.signupId.text.clear()
                    binding.signupPwd.text.clear()

                    if(task.isSuccessful){
                        MyApplication.auth.currentUser?.sendEmailVerification() //사용자가 입력한 이메일에 대한 검증 과정
                            ?.addOnCompleteListener { sendTask ->
                                if(sendTask.isSuccessful){
                                    Toast.makeText(baseContext, "회원가입 성공! 메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else{
                                    Toast.makeText(baseContext, "메일 발송 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    else{
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}