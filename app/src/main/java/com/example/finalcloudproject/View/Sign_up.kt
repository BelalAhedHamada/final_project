package com.example.finalcloudproject.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Firebase.Auth
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.databinding.ActivitySignUpBinding
import com.example.finalcloudproject.databinding.SelectDoctorOrPatientBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Sign_up : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Auth(this)

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this,Sign_in::class.java))
        }
        binding.btnSignup.setOnClickListener {
            val name = binding.TextFieldNameUp.editText!!.text
            val email = binding.TextFieldEmailUp.editText!!.text
            val phone = binding.TextFieldPhoneUp.editText!!.text
            val address = binding.TextFieldAddressUp.editText!!.text
            val birth = binding.TextFieldBirthDateUp.editText!!.text
            val password = binding.TextFieldPasswordUp.editText!!.text
            val comPassword = binding.TextFieldComPasswordUp.editText!!.text
            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && birth.isNotEmpty() &&
                password.isNotEmpty() && comPassword.isNotEmpty()
            ) {
                if (password.toString() == comPassword.toString()) {
                    showDialogSign(
                        this, password.toString(),
                        name.toString(),
                        email.toString(),
                        phone.toString(),
                        address.toString(),
                        birth.toString(),
                        "",

                        )
//                    auth.Sign_up(
//                        findViewById(android.R.id.content), password.toString(), User(
//                            null,
//                            name.toString(),
//                            email.toString(),
//                            phone.toString(),
//                            address.toString(),
//                            birth.toString(),
//                            0,
//                            ""
//                        )
//                    )
                } else {
                    Constants.showSnackBar(
                        findViewById(android.R.id.content),
                        "يجب ان تكون كلمة المرور وتأكيد كلمة المرور نفس البيانات",
                        Constants.redColor
                    )
                }

            } else {
                Constants.showSnackBar(
                    findViewById(android.R.id.content),
                    "املأ جميع البيانات",
                    Constants.redColor
                )
            }
        }
    }

    private fun showDialogSign(
        context: Context,
        password: String,
        name: String,
        email: String,
        address: String,
        phone: String,
        birth_date: String,
        fcm_token: String
    ) {
        var binding: SelectDoctorOrPatientBinding =
            SelectDoctorOrPatientBinding.inflate(layoutInflater)
        var dialog = MaterialAlertDialogBuilder(context).setView(binding.root).show()
        val auth = Auth(this)

        binding.btnDoctor.setOnClickListener {
            auth.Sign_up(
                findViewById(android.R.id.content), password, User(
                    null,
                    name,
                    email,
                    phone,
                    address,
                    birth_date,
                    1,
                    fcm_token
                )
            )
            dialog.dismiss()

        }

        binding.btnPatient.setOnClickListener {
            auth.Sign_up(
                findViewById(android.R.id.content), password, User(
                    null,
                    name,
                    email,
                    phone,
                    address,
                    birth_date,
                    0,
                    fcm_token
                )
            )
            dialog.dismiss()
        }
    }
}