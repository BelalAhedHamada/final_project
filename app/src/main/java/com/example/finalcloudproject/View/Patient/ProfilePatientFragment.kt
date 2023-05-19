package com.example.finalcloudproject.View.Patient

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.MyTopicAdapter
import com.example.finalcloudproject.Adapter.MyTopicProfileAdapter
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.View.Sign_in
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentProfilePatientBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfilePatientFragment : Fragment() {
    lateinit var binding: FragmentProfilePatientBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var auth: FirebaseAuth
    var user: User? = null
    lateinit var myTopicProfileAdapter: MyTopicProfileAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilePatientBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel
        auth = Firebase.auth
        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                user = item
                binding.tvNameProfile.text = user!!.name
                binding.tvEmailProfile.text = user!!.email
            }
        })
        setupReceycleViewMyTopic()






        palliatveViewModel.getMyTopic()
        setupReceycleViewMyTopic()
        palliatveViewModel.myTopic?.observe(viewLifecycleOwner, Observer {
            myTopicProfileAdapter.differ.submitList(it)
            binding.rvTopicProfile.adapter?.notifyDataSetChanged()
        })

        binding.imgBtnLogOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity,Sign_in::class.java))
            requireActivity().finish()

        }

        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_profilePatientFragment_to_homeFragment2)
        }

        myTopicProfileAdapter.setOnItemClickListener {
            var bundle = Bundle().apply {
                putSerializable("topic",it)
            }
            findNavController().navigate(R.id.action_profilePatientFragment_to_advicesProfileBottomSheetFragment,bundle)

        }
        
        return binding.root
    }

    fun setupReceycleViewMyTopic() {
        myTopicProfileAdapter = MyTopicProfileAdapter()
        binding.rvTopicProfile.apply {
            adapter = myTopicProfileAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
    }

}