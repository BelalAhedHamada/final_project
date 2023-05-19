package com.example.finalcloudproject.View.Patient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.AdvicesDoctorAdapter
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentAdvicesProfileBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AdvicesProfileBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAdvicesProfileBottomSheetBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    val args: AdvicesProfileBottomSheetFragmentArgs by navArgs()
    lateinit var advicesAdapter: AdvicesDoctorAdapter
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvicesProfileBottomSheetBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel
        val topic = args.topic

        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                user = item
            }
        })

        setupReceycleViewAdvices()
        palliatveViewModel.getAdvice(topic.id!!)
        palliatveViewModel.advicesDoctor?.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.animationView14.visibility = View.VISIBLE
                binding.textView47.visibility = View.VISIBLE
            } else {
                binding.animationView14.visibility = View.GONE
                binding.textView47.visibility = View.GONE
                advicesAdapter.differ.submitList(it)

            }
        })

        advicesAdapter.setOnItemClickListener { advice ->
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("advice", advice)
                putSerializable("myTopic", topic)
            }
            findNavController().navigate(
                R.id.action_advicesProfileBottomSheetFragment_to_detailsAdvicesPatientFragment,
                Bundle
            )
            palliatveViewModel.showUserAdvices(
                user!!, topic.id.toString(), advice.id.toString()
            )
        }
        return binding.root
    }

    fun setupReceycleViewAdvices() {
        advicesAdapter = AdvicesDoctorAdapter()
        binding.rvAdvSheet.apply {

            adapter = advicesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}