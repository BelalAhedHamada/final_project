package com.example.finalcloudproject.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.View.Patient.MainActivity
import com.example.finalcloudproject.ViewModel.PalliativeRepository
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.ViewModel.PalliativeViewModelProviderFactory
import com.example.finalcloudproject.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var motionLayout: MotionLayout
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var palliatveViewModel: PalliativeViewModel
    var user: User? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = PalliativeRepository(this)
        val viewModelProviderFactory = PalliativeViewModelProviderFactory(repository)
        palliatveViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            PalliativeViewModel::class.java
        )

        auth = Firebase.auth

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)


        motionLayout = binding.splash
        motionLayout.startLayoutAnimation()
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {


            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (auth.currentUser?.uid == null) {
                    val i = Intent(this@SplashScreenActivity, Sign_in::class.java)
                    startActivity(i)
                    finish()
                } else {
                    palliatveViewModel.getUser()
                    palliatveViewModel.user?.observe(this@SplashScreenActivity, Observer { it ->
                        for (item in it) {
                            user = item
                            if (auth.currentUser?.uid != null && user?.type == 1) {
                                val i = Intent(this@SplashScreenActivity, DoctorActivity::class.java)
                                startActivity(i)
                                finish()
                            } else {
                                val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                        }
                    })



                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })

    }

    private fun isLogin() {
        val isLogin = sharedPreferences.getBoolean("isLogin", false)
        val type = sharedPreferences.getInt("type", 0)
        val id = sharedPreferences.getString("id", "")
        Log.e("isLogin", sharedPreferences.getBoolean("isLogin", false).toString())
        Log.e("isLogin", sharedPreferences.getInt("type", 0).toString())
        Log.e("isLogin", sharedPreferences.getString("id", "").toString())
        if (isLogin && type == 0) {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("id_user", id)
            startActivity(i)
            finish()
        } else if (isLogin && type == 1) {
            val i = Intent(this, DoctorActivity::class.java)
            i.putExtra("id_user", id)
            startActivity(i)
            finish()
        } else {
            startActivity(Intent(this, Sign_in::class.java))
            finish()
        }
    }
}