package com.example.finalcloudproject.View.Patient

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalcloudproject.Adapter.AdvicesDoctorAdapter
import com.example.finalcloudproject.Adapter.ExploerAdapter
import com.example.finalcloudproject.Adapter.MyTopicAdapter
import com.example.finalcloudproject.Adapter.TopicAdapter
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.MyTopic
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var topicAdapter: TopicAdapter
    lateinit var myTopicAdapter: MyTopicAdapter
    lateinit var advicesAdapter: AdvicesDoctorAdapter
    lateinit var exploerAdapter: ExploerAdapter
    var myTopic: MyTopic? = null
    var idTopic: String? = ""
    var users: User? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel

        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                users = item
            }
        })
        // bottom sheet
        BottomSheetBehavior.from(binding.sheet).apply {
            peekHeight = 150
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        // get topic
        palliatveViewModel.getTopic()
        setupReceyclviewTopic()
        palliatveViewModel.topicsPatient?.observe(viewLifecycleOwner, Observer {
            topicAdapter.differ.submitList(it)
            binding.rvTopic.adapter?.notifyDataSetChanged()
            binding.sizeTopic.text = "${it.size} مواضيع , المزيد قادم "
        })

        topicAdapter.setOnItemClickListener { topic, _, _, color ->
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("topic", topic)
                putIntArray("color", color)
            }
            findNavController().navigate(
                R.id.action_homeFragment2_to_detailsTopicPatientFragment,
                Bundle
            )

        }

        palliatveViewModel.getTopicsExplore()
        setupReceyclviewExploer()
        palliatveViewModel.topicExploreList?.observe(viewLifecycleOwner, Observer {
            exploerAdapter.differ.submitList(it)
            binding.rvEx.adapter?.notifyDataSetChanged()
        })

        exploerAdapter.setOnItemClickListener { topic, _, _, color ->
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("topic", topic)
                putIntArray("color", color)
            }
            findNavController().navigate(
                R.id.action_homeFragment2_to_detailsTopicPatientFragment,
                Bundle
            )

        }


        palliatveViewModel.getMyTopic()
        setupReceycleViewMyTopic()
        palliatveViewModel.myTopic?.observe(viewLifecycleOwner, Observer {
            myTopicAdapter.differ.submitList(it)
            binding.rvMyTopic.adapter?.notifyDataSetChanged()
        })

        setupReceycleViewAdvices()
        myTopicAdapter.setOnItemClickListener {
            binding.textView7show.visibility = View.GONE
            palliatveViewModel.getAdvice(it.id.toString())
            idTopic = it.id
            myTopic = it
            palliatveViewModel.advicesDoctor?.observe(viewLifecycleOwner, Observer {
                binding.animationView7.visibility = View.GONE
                binding.textView42.visibility = View.GONE
                advicesAdapter.differ.submitList(it)
                if (it.isEmpty()) {
                    advicesAdapter.differ.submitList(null)
                    binding.animationView7.visibility = View.VISIBLE
                    binding.textView42.visibility = View.VISIBLE
                } else {
                    binding.animationView7.visibility = View.GONE
                    binding.textView42.visibility = View.GONE
                    advicesAdapter.differ.submitList(it)
                }

            })
        }

        advicesAdapter.setOnItemClickListener { advice ->
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("advice", advice)
                putSerializable("myTopic", myTopic)
            }
            findNavController().navigate(
                R.id.action_homeFragment2_to_detailsAdvicesPatientFragment,
                Bundle
            )
            palliatveViewModel.showUserAdvices(
                users!!, idTopic.toString(), advice.id.toString()
            )
        }

        myTopicAdapter.setOnItemClickListener2 {
            if (myTopic != null) {
                val Bundle = Bundle().apply {
                    putSerializable("topic", myTopic)
                }
                findNavController().navigate(R.id.action_homeFragment2_to_chatTopicFragment, Bundle)


            } else {
                Constants.showSnackBar(
                    binding.root, "يجب عليك تحديد كورس",
                    Constants.redColor
                )
            }
        }

        binding.cvDrawerLayout.setOnClickListener {
            val navBar: DrawerLayout = requireActivity().findViewById(R.id.drawerLayout)

            navBar.open()
        }



        binding.goProfile.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment2_to_profilePatientFragment
            )

        }
        binding.TextSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                palliatveViewModel.searchTopic(query.toString())
                palliatveViewModel.searchTopicList?.observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        topicAdapter.differ.submitList(null)
                        binding.animationViewx.visibility = View.VISIBLE
                    } else {
                        binding.animationViewx.visibility = View.GONE
                        topicAdapter.differ.submitList(it)

                    }
                })

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "" || newText == null) {
                    binding.animationViewx.visibility = View.GONE

                    palliatveViewModel.topicsPatient!!.observe(viewLifecycleOwner, Observer {
                        topicAdapter.differ.submitList(it)
                    })

                }
                return true
            }

        })


        MobileAds.initialize(activity as MainActivity) {}

        val adRequest = AdRequest.Builder().build()
        binding.adViewHome.loadAd(adRequest)

        binding.adViewHome.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.e("aa","onAdLoaded")
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                Log.e("aa","onAdFailedToLoad  ${adError.message}")
            }

            override fun onAdOpened() {
                Log.e("aa","onAdOpened")

            }

            override fun onAdClicked() {
                Log.e("aa","onAdClicked")
            }

            override fun onAdClosed() {
                Log.e("aa","onAdClosed")
            }
        }



        return binding.root
    }

    fun setupReceyclviewTopic() {
        topicAdapter = TopicAdapter()
        binding.rvTopic.apply {
            adapter = topicAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
    }

    fun setupReceycleViewMyTopic() {
        myTopicAdapter = MyTopicAdapter()
        binding.rvMyTopic.apply {
            adapter = myTopicAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
    }

    fun setupReceycleViewAdvices() {
        advicesAdapter = AdvicesDoctorAdapter()

        binding.rvAdv.apply {


            adapter = advicesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    fun setupReceyclviewExploer() {
        exploerAdapter = ExploerAdapter()
        binding.rvEx.apply {
            adapter = exploerAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
    }
}