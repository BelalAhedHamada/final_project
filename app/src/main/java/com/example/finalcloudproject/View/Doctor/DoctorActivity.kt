package com.example.finalcloudproject.View.Doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeRepository
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.ViewModel.PalliativeViewModelProviderFactory
import com.example.finalcloudproject.databinding.ActivityDoctorBinding

class DoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorBinding
    lateinit var palliatveViewModel:PalliativeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController

        val repository = PalliativeRepository(this)
        val viewModelProviderFactory= PalliativeViewModelProviderFactory(repository)
        palliatveViewModel = ViewModelProvider(this,viewModelProviderFactory).get(PalliativeViewModel::class.java)

        palliatveViewModel.getUser()
    }
}