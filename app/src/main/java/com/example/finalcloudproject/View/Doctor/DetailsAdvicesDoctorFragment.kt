package com.example.finalcloudproject.View.Doctor

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Adapter.QuestionAdapter
import com.example.finalcloudproject.R
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentDetailsAdvicesDoctorkBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class DetailsAdvicesDoctorFragment : Fragment() {
    private lateinit var binding: FragmentDetailsAdvicesDoctorkBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    val args: DetailsAdvicesDoctorFragmentArgs by navArgs()
    lateinit var questionAdapter: QuestionAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsAdvicesDoctorkBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        val advice = args.advice
        val topic = args.topic


        binding.tvNameAdvices.text = advice.name
        binding.tvDesAdvices.text = advice.description
        Glide.with(requireActivity()).load(advice.image).into(binding.imgDetAdvices)
        palliatveViewModel.getCountUserShowAdvice(
            topic.id.toString(),
            advice.id.toString(),
            binding.tvCountSeeAdv
        )

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
                putSerializable("question", question)
                putSerializable("advice", advice)
                putSerializable("topic", topic)
            }
            findNavController().navigate(
                R.id.action_detailsAdvicesDoctorkFragment_to_reQuestionDoctorFragment,
                Bundle
            )
        }




        return binding.root
    }

    fun setupReceycliew() {
        questionAdapter = QuestionAdapter(requireActivity())
        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}