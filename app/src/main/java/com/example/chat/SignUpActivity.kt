package com.example.chat

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class SignUpActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignUpBinding
    lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //인증 초기화 코드
        mAuth = Firebase.auth

        //db 초기화
        mDbRef = Firebase.database.reference

        binding.signUpBtn.setOnClickListener {
            val name = binding.nameEdit.text.toString().trim()
            val email = binding.emailEdit.text.toString().trim()
            val password = binding.passwordEdit.text.toString().trim()

            signUp(name,email, password)
        }
    }

    //회원 가입
    private fun signUp(name:String, email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    //회원가입 성공

                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()


                    val intent: Intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                }else{
                    //회원가입 실패
                    Toast.makeText(this,"회원가입 실패", Toast.LENGTH_SHORT).show()
                    Log.d("SignUp","Error: ${task.exception}")
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uId:String){
        mDbRef.child("user").child(uId).setValue(User(name, email, uId))
    }
}