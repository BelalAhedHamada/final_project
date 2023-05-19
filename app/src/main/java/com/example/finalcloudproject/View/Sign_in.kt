package com.example.finalcloudproject.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Firebase.Auth
import com.example.finalcloudproject.R
import com.example.finalcloudproject.databinding.ActivitySignInBinding

class Sign_in : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Auth(this)

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, Sign_up::class.java))
        }

        binding.btnSignin.setOnClickListener {
            val email = binding.TextFieldEmailIn.editText!!.text
            val password = binding.TextFieldPasswordIn.editText!!.text
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.Sign_in(
                    findViewById(android.R.id.content),
                    email.toString(),
                    password.toString()
                )
            } else {
                Constants.showSnackBar(
                    findViewById(android.R.id.content),
                    "املأ جميع البيانات",
                    Constants.redColor
                )
            }
        }
    }
}