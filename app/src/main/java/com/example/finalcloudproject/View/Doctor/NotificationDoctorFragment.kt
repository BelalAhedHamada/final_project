package com.example.finalcloudproject.View.Doctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.NotificationAdapter
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentNotificationDoctorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class NotificationDoctorFragment : Fragment() {
    lateinit var binding: FragmentNotificationDoctorBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var auth: FirebaseAuth
    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationDoctorBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        auth = Firebase.auth
        setupReceycliew()
        palliatveViewModel.getNotification(auth.currentUser!!.uid)
        palliatveViewModel.notificationList?.observe(
            viewLifecycleOwner,
            Observer {
                Log.e("sdasd1", it.size.toString())
                notificationAdapter.differ.submitList(it)
                binding.rvNotification.adapter?.notifyDataSetChanged()
            })

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    fun setupReceycliew() {
        notificationAdapter = NotificationAdapter(requireActivity())
        binding.rvNotification.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }


}