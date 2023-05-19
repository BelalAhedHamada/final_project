package com.example.finalcloudproject.View.Patient

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.NotificationData
import com.example.finalcloudproject.Model.PushNotification
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentDetailsTopicPatientBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID

class DetailsTopicPatientFragment : Fragment() {
    private lateinit var binding: FragmentDetailsTopicPatientBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var auth: FirebaseAuth
    var user: User? = null

    val args: DetailsTopicPatientFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsTopicPatientBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel
        auth = Firebase.auth

        val topic = args.topic
        val color = args.color

        val navBar: DrawerLayout = requireActivity().findViewById(R.id.drawerLayout)
        navBar.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        val gd = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x9e9d9f, -0xececed)
        )

        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                user = item
            }
        })
        gd.setColors(color)
        binding.cardViewDet.background = gd
        gd.cornerRadius = 50f
        binding.cardViewDet.elevation = 200f
        Glide.with(this).load(topic.image).into(binding.imgTopicDes)
        binding.tvNameTopicDet.text = topic.name
        binding.tvDesTopic.text = topic.description
        binding.tvNameDoctor.text = topic.doctor
        binding.tvCountUsers.text = "+${(topic.users?.count()?.minus(1))}"

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFollow.setOnClickListener {
            palliatveViewModel.updateUsersMyTopic(binding.root, topic.id.toString(), user!!)

            val topics = "/topics/${auth.currentUser!!.uid}"
            PushNotification(
                NotificationData("متابعة موضوع", " تم متابعة موضوع ${topic.name}"),
                topics
            ).also {
                palliatveViewModel.sendNotification(
                    it,
                    Notification(
                        UUID.randomUUID().toString(),
                        "متابعة موضوع",
                        " تم متابعة موضوع ${topic.name}",
                        System.currentTimeMillis()
                    ),
                    auth.currentUser!!.uid,
                )
            }
        }

        return binding.root
    }
}