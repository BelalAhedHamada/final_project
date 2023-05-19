package com.example.finalcloudproject.View.Doctor

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.ChatAdapter
import com.example.finalcloudproject.Firebase.FirebaseSource
import com.example.finalcloudproject.Model.Message
import com.example.finalcloudproject.Model.MyTopic
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.NotificationData
import com.example.finalcloudproject.Model.PushNotification
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.ViewModel.PalliativeRepository
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.ViewModel.PalliativeViewModelProviderFactory
import com.example.finalcloudproject.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)

        val repository = PalliativeRepository(this)
        val viewModelProviderFactory = PalliativeViewModelProviderFactory(repository)
        palliatveViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            PalliativeViewModel::class.java
        )
        auth = Firebase.auth
        binding.chRecyclerView.layoutManager = LinearLayoutManager(this)

        val topic = intent.getSerializableExtra("topic") as MyTopic?
        val users = intent.getSerializableExtra("usersT") as User?

        binding.imgBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.tvName.text = users!!.name


        binding.btnSend.setOnClickListener {
            var message: String = binding.etaMessage.text.toString()
            var time = System.currentTimeMillis()
            if (message.isEmpty()) {
                Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show()
                binding.etaMessage.setText("")
            } else {
                if (topic?.idDoctor == null) {
                    palliatveViewModel.sendMessagePrivate(
                        Message(
                            UUID.randomUUID().toString(),
                            auth.currentUser!!.uid,
                            auth.currentUser!!.uid,
                            binding.etaMessage.editableText.toString(),
                            "",
                            time,
                            null
                        ), users?.id.toString()
                    )

                    val topic = "/topics/${users.id.toString()}"
                    PushNotification(
                        NotificationData("Message", binding.etaMessage.text.toString()),
                        topic
                    ).also {
                        palliatveViewModel.sendNotification(
                            it,
                            Notification(
                                UUID.randomUUID().toString(),
                                "Message",
                                binding.etaMessage.text.toString(),
                                time
                            ),
                            auth.currentUser!!.uid,
                        )
                    }
                } else {
                    palliatveViewModel.sendMessagePrivate(
                        Message(
                            UUID.randomUUID().toString(),
                            auth.currentUser!!.uid,
                            topic?.idDoctor.toString(),
                            binding.etaMessage.editableText.toString(),
                            "",
                            time,
                            null
                        ), auth.currentUser!!.uid
                    )

                    val topicNo = "/topics/${topic.idDoctor.toString()}"
                    PushNotification(
                        NotificationData(users.name, binding.etaMessage.text.toString()),
                        topicNo
                    ).also {
                        palliatveViewModel.sendNotification(
                            it,
                            Notification(
                                UUID.randomUUID().toString(),
                                "Message ${users.name}",
                                binding.etaMessage.text.toString(),
                                time
                            ),
                            topic.idDoctor.toString()
                        )
                    }
                }

            }
            binding.etaMessage.setText("")
        }

        if (topic?.idDoctor == null) {
            palliatveViewModel.getMessagePrivate(users?.id.toString())
                .observe(this, androidx.lifecycle.Observer {
                    val chatAdapter = ChatAdapter(this, it)
                    binding.chRecyclerView.adapter = chatAdapter
                    binding.chRecyclerView.scrollToPosition(it.size - 1);
                    chatAdapter.setOnItemClickListener { chat, textView ->
                        textView.setOnLongClickListener(View.OnLongClickListener {
                            binding.imgDeleteMessageUser.visibility = View.VISIBLE
                            binding.imgDeleteMessageUser.setOnClickListener {
                                palliatveViewModel.deleteMessagePrivate(users!!.id!!, chat.id)
                                binding.imgDeleteMessageUser.visibility = View.INVISIBLE
                            }
                            true
                        })

                    }

                })

        } else {
            palliatveViewModel.getMessagePrivate(auth.currentUser!!.uid)
                .observe(this, androidx.lifecycle.Observer {
                    val chatAdapter = ChatAdapter(this, it)
                    binding.chRecyclerView.adapter = chatAdapter
                    binding.chRecyclerView.scrollToPosition(it.size - 1);
                    chatAdapter.setOnItemClickListener { chat, textView ->
                        textView.setOnLongClickListener(View.OnLongClickListener {
                            if (chat.senderId == auth.currentUser!!.uid) {
                                binding.imgDeleteMessageUser.visibility = View.VISIBLE
                                binding.imgDeleteMessageUser.setOnClickListener {
                                    palliatveViewModel.deleteMessagePrivate(
                                        auth.currentUser!!.uid,
                                        chat.id
                                    )
                                    binding.imgDeleteMessageUser.visibility = View.INVISIBLE
                                }
                            }

                            true
                        })

                    }
                })

        }


        setContentView(binding.root)

    }
}