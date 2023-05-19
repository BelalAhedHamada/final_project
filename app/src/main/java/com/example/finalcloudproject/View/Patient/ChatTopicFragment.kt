package com.example.finalcloudproject.View.Patient

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.ChatAdapter
import com.example.finalcloudproject.Model.Message
import com.example.finalcloudproject.Model.MyTopic
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.NotificationData
import com.example.finalcloudproject.Model.PushNotification
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentChatTopicBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID


class ChatTopicFragment : Fragment() {
    lateinit var binding: FragmentChatTopicBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    val args: ChatTopicFragmentArgs by navArgs()
    lateinit var auth: FirebaseAuth
    var topic: MyTopic? = null
    var imgUrl: Uri? = null
    lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatTopicBinding.inflate(layoutInflater)
        binding.chatactivityRecyclerView.layoutManager = LinearLayoutManager(activity)

        palliatveViewModel = (activity as MainActivity).palliatveViewModel

        auth = Firebase.auth
        topic = args.topic

        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_chatTopicFragment_to_homeFragment2)
        }

        binding.btnSendMessageactiv.setOnClickListener {

            var message: String = binding.etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(activity, "message is empty", Toast.LENGTH_SHORT).show()
            } else {

                palliatveViewModel.sendMessageTopic(
                    Message(
                        UUID.randomUUID().toString(),
                        auth.currentUser!!.uid,
                        null,
                        binding.etMessage.editableText.toString(),
                        topic!!.id.toString(),
                        System.currentTimeMillis(), null
                    ), topic!!.id.toString(), null
                )
                for (users in topic?.users!!) {
                    if (users.id != "" && users.id != auth.currentUser!!.uid) {

                        val topics = "/topics/${users.id}"
                        PushNotification(
                            NotificationData("massge", binding.etMessage.text.toString()),
                            topics
                        ).also {
                            users!!.id?.let { it1 ->
                                palliatveViewModel.sendNotification(
                                    it,
                                    Notification(
                                        UUID.randomUUID().toString(),
                                        "رسالة من ${topic!!.name}",
                                        binding.etMessage.text.toString(),
                                        System.currentTimeMillis()
                                    ),
                                    it1,
                                )
                            }
                        }
                    }


                }

                binding.etMessage.setText("")

            }

        }
        palliatveViewModel.getMessageTopic(topic?.id!!)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                chatAdapter = ChatAdapter(requireActivity(), it)
                binding.chatactivityRecyclerView.adapter = chatAdapter
                binding.chatactivityRecyclerView.scrollToPosition(it.size - 1);
                chatAdapter.setOnItemClickListener { chat, textView ->
                    textView.setOnLongClickListener(View.OnLongClickListener {
                        if (chat.senderId == auth.currentUser!!.uid) {
                            binding.imgDeleteMessageUser.visibility = View.VISIBLE
                            binding.imgDeleteMessageUser.setOnClickListener {

                                palliatveViewModel.deleteMessageTopic(topic?.id!!, chat.id)
                                binding.imgDeleteMessageUser.visibility = View.INVISIBLE
                            }
                        }


                        true
                    })

                }
            })

        binding.btnSendImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .start()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(requireContext(), "ASDASDASDASD", Toast.LENGTH_SHORT).show()

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            imgUrl = data!!.data

            palliatveViewModel.sendMessageTopic(
                Message(
                    UUID.randomUUID().toString(),
                    auth.currentUser!!.uid,
                    null,
                    null,
                    topic!!.id.toString(),
                    System.currentTimeMillis(), imgUrl.toString()
                ), topic!!.id.toString(), imgUrl
            )

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}