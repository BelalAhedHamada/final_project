package com.example.finalcloudproject.View.Patient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Firebase.Auth
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeRepository
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.ViewModel.PalliativeViewModelProviderFactory
import com.example.finalcloudproject.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var palliatveViewModel: PalliativeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navView: NavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
        binding.bottomNavigationView.setupWithNavController(navController)


        val repository = PalliativeRepository(this)
        val viewModelProviderFactory = PalliativeViewModelProviderFactory(repository)
        palliatveViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            PalliativeViewModel::class.java
        )

        palliatveViewModel.getUser()
//        val navController = findNavController(R.id.fragmentContainerView)
//        binding.bottomNavigationView.setupWithNavController(navController)
        auth = Firebase.auth

    }
}