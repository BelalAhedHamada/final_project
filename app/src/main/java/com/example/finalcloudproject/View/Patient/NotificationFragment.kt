package com.example.finalcloudproject.View.Patient

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.NotificationAdapter
import com.example.finalcloudproject.Adapter.QuestionAdapter
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class NotificationFragment : Fragment() {
    lateinit var binding: FragmentNotificationBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var notificationAdapter: NotificationAdapter
    lateinit var auth: FirebaseAuth

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel
        auth = Firebase.auth

        setupReceycliew()
        palliatveViewModel.getNotification(auth.currentUser!!.uid)
        palliatveViewModel.notificationList?.observe(
            viewLifecycleOwner,
            Observer {
                Log.e("sdasd1",it.size.toString())
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