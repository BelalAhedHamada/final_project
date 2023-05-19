package com.example.finalcloudproject.View.Doctor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalcloudproject.R
import com.example.finalcloudproject.databinding.FragmentChooseChatOrSendBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseChatOrSendBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: FragmentChooseChatOrSendBottomSheetBinding
    val args: ChooseChatOrSendBottomSheetArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChooseChatOrSendBottomSheetBinding.inflate(layoutInflater)
        val users = args.user
        binding.tvChatUser.setOnClickListener {
            val i = Intent(activity, ChatActivity::class.java)
            i.putExtra("usersT", users)
            startActivity(i)
        }
        binding.tvSendNotf.setOnClickListener {
            val Bundle = Bundle().apply {
                putSerializable("users", users)
            }
            findNavController().navigate(
                R.id.action_chooseChatOrSendBottomSheet_to_sendNotificationFragment,
                Bundle
            )
        }
        return binding.root
    }


}