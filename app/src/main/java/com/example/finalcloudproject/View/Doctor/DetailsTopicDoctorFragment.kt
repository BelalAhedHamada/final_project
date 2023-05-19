package com.example.finalcloudproject.View.Doctor

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.PopupMenu
import android.widget.ToggleButton
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Adapter.AdvicesDoctorAdapter
import com.example.finalcloudproject.Adapter.TopicAdapter
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentDetailsToicDoctorBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class DetailsTopicDoctorFragment : Fragment() {

    private lateinit var binding: FragmentDetailsToicDoctorBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var topic: Topic
    val args: DetailsTopicDoctorFragmentArgs by navArgs()
    lateinit var advicesAdapter: AdvicesDoctorAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsToicDoctorBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        topic = args.topic
        val color = args.color


        setupReceycliew()
        palliatveViewModel.getAdvice(topic.id.toString())
        palliatveViewModel.advicesDoctor?.observe(viewLifecycleOwner, Observer {
            advicesAdapter.differ.submitList(it)
            binding.rvAdvicesDoctor.adapter?.notifyDataSetChanged()
        })


//        binding.imgSee.setChecked(false)
        Log.e("sdasd", topic.state.toString())
        if (topic.state == 0) {
            binding.imgSee.setImageResource(R.drawable.unhidden)
            binding.imgSee.tag = 0
        } else {
            binding.imgSee.setImageResource(R.drawable.hidden)
            binding.imgSee.tag = 1
        }


//        binding.imgSee.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            palliatveViewModel.updateSeeTopic(binding.root, topic.id.toString(), binding.imgSee)
//        })

        binding.imgSee.setOnClickListener {
            if (binding.imgSee.tag == 0) {
                binding.imgSee.tag = 1
                binding.imgSee.setImageResource(R.drawable.unhidden)
            } else {
                binding.imgSee.tag = 0
                binding.imgSee.setImageResource(R.drawable.hidden)
            }
            palliatveViewModel.updateSeeTopic(binding.root, topic.id.toString(), binding.imgSee)
        }


        val gd = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x9e9d9f, -0xececed)
        )

        gd.setColors(color)
        binding.cardView16.background = gd
        gd.cornerRadius = 50f
        binding.cardView16.elevation = 200f
        Glide.with(this).load(topic.image).into(binding.imageDD)
        binding.tvNameTopic.text = topic.name

        binding.imageButton2.setOnClickListener {
            findNavController().navigateUp()
        }
        registerForContextMenu(binding.imgMenu)

        binding.imgMenu.setOnClickListener {
            showMenu(binding.imgMenu, R.menu.menu_opp)
        }

        binding.btnGoAddAdvices.setOnClickListener {
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("topic", topic)
            }
            findNavController().navigate(
                R.id.action_detailsTopicDoctorFragment_to_addAdvicesFragment,
                Bundle
            )
        }

        advicesAdapter.setOnItemClickListener { advice ->
            val Bundle = Bundle()
            Bundle.apply {
                putSerializable("advice", advice)
                putSerializable("topic", topic)
            }
            findNavController().navigate(
                R.id.action_detailsTopicDoctorFragment_to_detailsAdvicesDoctorkFragment,
                Bundle
            )
        }


        binding.btnGoShowPatient.setOnClickListener {
            val bundle = Bundle()
            bundle.apply {
                putSerializable("userTopic", topic)
            }
            findNavController().navigate(
                R.id.action_detailsTopicDoctorFragment_to_showUserFollowersTopicFragment,
                bundle
            )
        }


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            val deleteColor = ContextCompat.getColor(context!!, R.color.redd)
            val updateColor = ContextCompat.getColor(context!!, R.color.green)
            val deleteicon = R.drawable.ic_baseline_delete_24
            val updateicon = R.drawable.ic_baseline_edit_24
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val advices = advicesAdapter.differ.currentList[position]
                if (direction == ItemTouchHelper.LEFT) {
                    palliatveViewModel.deleteAdvice(
                        binding.root,
                        topic.id.toString(),
                        advices.id.toString()
                    )

                } else if (direction == ItemTouchHelper.RIGHT) {

                    val Bundle = Bundle().apply {
                        putSerializable("advice", advices)
//                        putString("idTopic", topic.id)
                        putSerializable("idTopic", topic)
                    }
                    findNavController().navigate(
                        R.id.action_detailsTopicDoctorFragment_to_updateAdvicesFragment,
                        Bundle
                    )
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(deleteColor)
                    .addSwipeLeftActionIcon(deleteicon)
                    .addSwipeRightBackgroundColor(updateColor)
                    .addSwipeRightActionIcon(updateicon)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }


        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvAdvicesDoctor)
        }





        return binding.root
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.update_menu -> {
                    val Bundle = Bundle()
                    Bundle.apply {
                        putSerializable("topic", topic)
                    }
                    findNavController().navigate(
                        R.id.action_detailsTopicDoctorFragment_to_updateTopicDoctorFragment,
                        Bundle
                    )

                }

                R.id.delete_menu -> {
                    palliatveViewModel.deleteTopic(binding.root, topic.id.toString())
                    Thread.sleep(300)
                    findNavController().navigate(
                        R.id.action_detailsTopicDoctorFragment_to_homeDFragment
                    )
                }
            }
            true
        }
        popup.setOnDismissListener {

        }

        popup.show()
    }

    fun setupReceycliew() {
        advicesAdapter = AdvicesDoctorAdapter()
        binding.rvAdvicesDoctor.apply {
            adapter = advicesAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}