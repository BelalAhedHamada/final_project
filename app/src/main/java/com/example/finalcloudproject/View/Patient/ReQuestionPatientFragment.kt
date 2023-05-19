package com.example.finalcloudproject.View.Patient

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalcloudproject.Adapter.QuestionAdapter
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.AddQuestionDialogBinding
import com.example.finalcloudproject.databinding.FragmentReQuestionDoctorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.UUID


class ReQuestionPatientFragment : Fragment() {
    private lateinit var binding: FragmentReQuestionDoctorBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    var users: User? = null
    lateinit var questionAdapter: QuestionAdapter
    val args: ReQuestionPatientFragmentArgs by navArgs()
    lateinit var auth: FirebaseAuth

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReQuestionDoctorBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel

        auth = Firebase.auth

        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                users = item
            }
        })
        val topic = args.topic11
        val advice = args.advice11
        val question = args.question11

        binding.tvNameUserQ.text = question.user!!.name
        binding.tvDesQ.text = question.description

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddReQ.setOnClickListener {
            showDialogAddReQuestion(
                requireContext(),
                UUID.randomUUID().toString(),
                users!!,
                topic.id.toString(),
                advice.id.toString(),
                question.id.toString(),
                binding.root
            )
        }

        setupReceycliew()
        palliatveViewModel.getReQuestion(
            topic.id.toString(),
            advice.id.toString(),
            question.id.toString()
        )
        palliatveViewModel.reQuestionList?.observe(
            viewLifecycleOwner,
            Observer {
                questionAdapter.differ.submitList(it)
                binding.rvReQuestion.adapter?.notifyDataSetChanged()
            })


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
                val reQuestion = questionAdapter.differ.currentList[position]
                if (direction == ItemTouchHelper.LEFT) {
                    if (reQuestion.user!!.id == auth.currentUser!!.uid) {
                        palliatveViewModel.deleteReQuestion(
                            binding.root,
                            topic.id.toString(),
                            advice.id.toString(),
                            question.id.toString(),
                            reQuestion.id.toString()
                        )
                    } else {
                        Constants.showSnackBar(
                            binding.root,
                            "لا يمكنك حذف الرد",
                            Constants.redColor
                        )
                    }


                } else if (direction == ItemTouchHelper.RIGHT) {

//                    showDialogUpdateReQuestion(
//                        requireContext(),
//                        UUID.randomUUID().toString(),
//                        users!!,
//                        topic.id.toString(),
//                        advice.id.toString(),
//                        question.id.toString(),
//                        reQuestion.id.toString(),
//                        binding.root
//                    )

                    if (reQuestion.user!!.id == auth.currentUser!!.uid) {
                        val Bundle = Bundle().apply {
                            putSerializable("question", question)
                            putSerializable("advice", advice)
                            putSerializable("topic", topic)
                            putSerializable("reQuestion", reQuestion)
                        }
                        findNavController().navigate(
                            R.id.action_reQuestion11PatientFragment_to_updateReQuestionPatientBottomSheetFragment,
                            Bundle
                        )
                    } else {
                        Constants.showSnackBar(
                            binding.root,
                            "لا يمكنك تعديل الرد",
                            Constants.redColor
                        )
                    }


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
            attachToRecyclerView(binding.rvReQuestion)
        }

        return binding.root
    }

    private fun showDialogAddReQuestion(
        context: Context,
        id: String,
        user: User,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
        view: View
    ) {
        var binding: AddQuestionDialogBinding =
            AddQuestionDialogBinding.inflate(layoutInflater)
        var dialog = MaterialAlertDialogBuilder(context).setView(binding.root).show()
        binding.tvTitle.text = "إضافة رد"
        binding.editAddQ.hint = "اكتب ردك"
        binding.TextFieldAddQ.editText!!.hint = "اكتب ردك"
        binding.btnAddQ.setOnClickListener {
            if (binding.TextFieldAddQ.editText!!.text.isNotEmpty()) {
                palliatveViewModel.addReQuestion(
                    binding.root,
                    Question(id, binding.TextFieldAddQ.editText!!.text.toString(), user),
                    documentTopic,
                    documentAdvices,
                    documentQuestion
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
        binding.rvReQuestion.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }


}