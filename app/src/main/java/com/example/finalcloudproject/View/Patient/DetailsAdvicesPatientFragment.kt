package com.example.finalcloudproject.View.Patient

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Adapter.AdvicesDoctorAdapter
import com.example.finalcloudproject.Adapter.QuestionAdapter
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Firebase.Auth
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.View.Doctor.ChatActivity
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.AddQuestionDialogBinding
import com.example.finalcloudproject.databinding.FragmentDetailsAdvicesPatientBinding
import com.example.finalcloudproject.databinding.FragmentDetailsTopicPatientBinding
import com.example.finalcloudproject.databinding.SelectDoctorOrPatientBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID

class DetailsAdvicesPatientFragment : Fragment() {
    private lateinit var binding: FragmentDetailsAdvicesPatientBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    val args: DetailsAdvicesPatientFragmentArgs by navArgs()
    var users: User? = null
    lateinit var auth: FirebaseAuth
    lateinit var questionAdapter: QuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsAdvicesPatientBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel
        val advice = args.advice
        val topic = args.myTopic
        auth = Firebase.auth

        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                users = item
            }
        })
        binding.tvNameAdvices.text = advice.name
        binding.tvDesAdvices.text = advice.description
        Glide.with(requireActivity()).load(advice.image).into(binding.imgDetAdvices)

        val navBar: DrawerLayout = requireActivity().findViewById(R.id.drawerLayout)
        navBar.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // ExoPlayer  Run video
        val player = ExoPlayer.Builder(requireActivity()).build()
        binding.videoViewTech.player = player
        val mediaItem: MediaItem =
            MediaItem.fromUri(Uri.parse("${advice.video}"))
        player.setMediaItem(mediaItem)
        player.prepare()

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imgChat.setOnClickListener {
            val i = Intent(activity, ChatActivity::class.java)
            i.putExtra("usersT", users)
            i.putExtra("topic", topic)
            startActivity(i)
        }

        binding.btnGoAddQ.setOnClickListener {
            showDialogAddQuestion(
                requireContext(),
                UUID.randomUUID().toString(),
                users!!, topic.id.toString(), advice.id.toString(), binding.root
            )
        }

        setupReceycliew()

        palliatveViewModel.getQuestion(topic.id.toString(), advice.id.toString())
        palliatveViewModel.userQuestionList?.observe(
            viewLifecycleOwner,
            Observer {
                questionAdapter.differ.submitList(it)
                binding.rvQuestion.adapter?.notifyDataSetChanged()
            })

        questionAdapter.setOnItemClickListener { question ->
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("question11", question)
                putSerializable("advice11", advice)
                putSerializable("topic11", topic)
            }
            findNavController().navigate(
                R.id.action_detailsAdvicesPatientFragment_to_reQuestionPatientFragment,
                Bundle
            )
//            findNavController().navigate(
//                R.id.action_detailsAdvicesPatientFragment_to_reQuestionPatient2Fragment,Bundle
//            )
        }

        return binding.root
    }

    private fun showDialogAddQuestion(
        context: Context,
        id: String,
        user: User,
        documentTopic: String,
        documentAdvices: String,
        view: View
    ) {
        var binding: AddQuestionDialogBinding =
            AddQuestionDialogBinding.inflate(layoutInflater)
        var dialog = MaterialAlertDialogBuilder(context).setView(binding.root).show()
        binding.editAddQ.hint = "اكتب سؤالك"
        binding.TextFieldAddQ.editText!!.hint = "اكتب سؤالك"
        binding.btnAddQ.setOnClickListener {
            if (binding.TextFieldAddQ.editText!!.text.isNotEmpty()) {
                palliatveViewModel.addQuestion(
                    binding.root,
                    Question(id, binding.TextFieldAddQ.editText!!.text.toString(), user),
                    documentTopic,
                    documentAdvices
                )
            } else {
                Constants.showSnackBar(
                    view,
                    "لا يمكن ترك التعليق فارغ",
                    Constants.redColor
                )
            }

            dialog.dismiss()
        }
    }

    fun setupReceycliew() {
        questionAdapter = QuestionAdapter(requireActivity())
        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }


}