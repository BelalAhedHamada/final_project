package com.example.finalcloudproject.View.Doctor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.AdvicesDoctorAdapter
import com.example.finalcloudproject.Adapter.ShowUserFollowerTopicAdapter
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentShowUserFollowersTopicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ShowUserFollowersTopicFragment : Fragment() {
    private lateinit var binding: FragmentShowUserFollowersTopicBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    val args: ShowUserFollowersTopicFragmentArgs by navArgs()
    lateinit var showUserFollowerTopicAdapter: ShowUserFollowerTopicAdapter
    lateinit var auth: FirebaseAuth
    lateinit var userss: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowUserFollowersTopicBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        val topic = args.userTopic
        auth = Firebase.auth
        userss = arrayListOf()
        setupReceycliew()
        palliatveViewModel.getUserTopic(auth.currentUser!!.uid, topic.id.toString())
        palliatveViewModel.usersTopicList?.observe(viewLifecycleOwner, Observer {
            for (item in it) {
                for (users in item.users!!) {
//                    users as HashMap<String, User>
//                    users.id
                    if (users.id.toString() != "") {
                        binding.animationView10.visibility = View.GONE
                        binding.textView38.visibility = View.GONE
//                        val items = User(
//                            users.get("id").toString(),
//                            users.get("name").toString(),
//                            users.get("email").toString(),
//                            users.get("address").toString(),
//                            users.get("phone").toString(),
//                            users.get("birth_date").toString(),
//                            0,
//                            users.get("fcm_token").toString()
//                        )
                        val items = User(
                            users.id.toString(),
                            users.name,
                            users.email,
                            users.address,
                            users.phone,
                            users.birth_date,
                            users.type,
                            users.fcm_token
                        )
                        userss.add(items)
                        showUserFollowerTopicAdapter.differ.submitList(userss)
                    } else {
                        binding.animationView10.visibility = View.VISIBLE
                        binding.textView38.visibility = View.VISIBLE
                    }

                }

            }

        })

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        showUserFollowerTopicAdapter.setOnItemClickListener { user, imageView ->
            imageView.setOnClickListener {

                val Bundle = Bundle().apply {
                    putSerializable("user", user)
                }
                if (findNavController().currentDestination?.id == R.id.showUserFollowersTopicFragment) {
                    findNavController().navigate(
                        R.id.action_showUserFollowersTopicFragment_to_chooseChatOrSendBottomSheet,
                        Bundle
                    )
                }

            }
        }


        return binding.root
    }

    fun setupReceycliew() {
        showUserFollowerTopicAdapter = ShowUserFollowerTopicAdapter()
        binding.rvShowUser.apply {
            adapter = showUserFollowerTopicAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

    fun navigateToActivity() {
        val intent = Intent(activity, ChatActivity::class.java)
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            // Do something here
        }
    }
}