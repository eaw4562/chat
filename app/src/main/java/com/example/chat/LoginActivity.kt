package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chat.databinding.ActivityLoginInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginInBinding

    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            login(email, password)
        }

        //회원가입 버튼
        binding.signUpBtn.setOnClickListener {
            val intent : Intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful) {
                    //로그인 성공
                    val intent : Intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"로그인 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    //로그인 실패
                    Toast.makeText(this,"로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.d("Login", "Error : ${task.exception}")
                }
            }
    }
}