package com.example.finalcloudproject.View.Doctor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.TopicAdapter
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.R
import com.example.finalcloudproject.View.Sign_in
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentHomeDBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [HomeDFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeDFragment : Fragment() {
    private lateinit var binding: FragmentHomeDBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var auth: FirebaseAuth
    lateinit var topicAdapter: TopicAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeDBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        auth = Firebase.auth
        palliatveViewModel.getDoctorTopic(auth.currentUser!!.uid)
        setupReceyclview()
        palliatveViewModel.topicsDoctor?.observe(viewLifecycleOwner, Observer {
            topicAdapter.differ.submitList(it)
            binding.recyclerView.adapter?.notifyDataSetChanged()
        })
        binding.GoAddTopic.setOnClickListener {
            findNavController().navigate(R.id.action_homeDFragment_to_addTopicFragment)
        }

        topicAdapter.setOnItemClickListener { topic, _, _, color ->
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("topic", topic)
                putIntArray("color", color)
            }

            if (findNavController().currentDestination?.id == R.id.homeDFragment) {
                findNavController().navigate(
                    R.id.action_homeDFragment_to_detailsToicDoctorFragment,
                    Bundle
                )
            }
        }

        binding.imgNotification.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeDFragment_to_notificationDoctorFragment
            )
        }

        binding.imgBtnLogOutT.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, Sign_in::class.java))
            requireActivity().finish()
        }

        binding.TextSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                palliatveViewModel.searchTopic(query.toString())
                palliatveViewModel.searchTopicList?.observe(viewLifecycleOwner, Observer {
                    topicAdapter.differ.submitList(it)
                })

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "" || newText == null) {
                    palliatveViewModel.topicsDoctor!!.observe(viewLifecycleOwner, Observer {
                        topicAdapter.differ.submitList(it)
                    })
                }
                return true
            }

        })


        return binding.root
    }

    fun setupReceyclview() {
        topicAdapter = TopicAdapter()
        binding.recyclerView.apply {
            adapter = topicAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
    }
}