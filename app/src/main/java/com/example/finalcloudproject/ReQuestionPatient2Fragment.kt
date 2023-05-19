package com.example.finalcloudproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.finalcloudproject.databinding.FragmentReQuestionPatient2Binding

class ReQuestionPatient2Fragment : Fragment() {
    lateinit var binding: FragmentReQuestionPatient2Binding
    val args: ReQuestionPatient2FragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReQuestionPatient2Binding.inflate(layoutInflater)

        val topic = args.topic3

        return binding.root
    }
}