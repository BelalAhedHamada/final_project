package com.example.finalcloudproject.View.Doctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.NotificationData
import com.example.finalcloudproject.Model.PushNotification
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentSendNotificationBinding
import java.util.UUID

class SendNotificationFragment : Fragment() {
    lateinit var binding: FragmentSendNotificationBinding
    val args: SendNotificationFragmentArgs by navArgs()
    private lateinit var palliatveViewModel: PalliativeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendNotificationBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel

        val user = args.users

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSend.setOnClickListener {
            if (binding.edtTitle.text.isNotEmpty() && binding.editDescription.text.isNotEmpty()) {
                val topic = "/topics/${user.id.toString()}"
                PushNotification(
                    NotificationData(
                        binding.edtTitle.text.toString(),
                        binding.editDescription.text.toString()
                    ),
                    topic
                ).also {
                    palliatveViewModel.sendNotification(
                        it,
                        Notification(
                            UUID.randomUUID().toString(),
                            binding.edtTitle.text.toString(),
                            binding.editDescription.text.toString(),
                            System.currentTimeMillis()
                        ),
                        user.id.toString(),
                    )
                }
                Constants.showSnackBar(
                    binding.root, "تم إرسال الإشعار",
                    Constants.greenColor
                )
                binding.editDescription.text = null
                binding.edtTitle.text = null
            } else {
                Constants.showSnackBar(
                    binding.root, "إملا الحقول المطلوبة",
                    Constants.redColor
                )
            }

        }
        return binding.root
    }

}