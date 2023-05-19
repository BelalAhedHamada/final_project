package com.example.finalcloudproject.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PalliativeViewModelProviderFactory(private val palliativeRepository: PalliativeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PalliativeViewModel(palliativeRepository) as T
    }

}