package com.example.finalcloudproject.View.Doctor

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentUpdateReQuestionbottomSheetListDialogItemBinding
import com.example.finalcloudproject.databinding.FragmentUpdateReQuestionbottomSheetListDialogBinding

class UpdateReQuestionbottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUpdateReQuestionbottomSheetListDialogBinding? = null

    private val binding get() = _binding!!
    val args: UpdateReQuestionbottomSheetFragmentArgs by navArgs()
    private lateinit var palliatveViewModel: PalliativeViewModel
    var users: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =
            FragmentUpdateReQuestionbottomSheetListDialogBinding.inflate(inflater, container, false)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel

        val topic = args.topic
        val advice = args.advice
        val question = args.question
        val reQuestion = args.reQuestion
        binding.editAddQ.append(reQuestion.description)
        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                users = item
            }
        })
        binding.btnAddQ.setOnClickListener {
            if (binding.TextFieldAddQ.editText!!.text.isNotEmpty()) {
                palliatveViewModel.updateReQuestion(
                    binding.root,
                    Question(
                        reQuestion.id.toString(),
                        binding.TextFieldAddQ.editText!!.text.toString(),
                        users!!
                    ),
                    topic.id.toString(),
                    advice.id.toString(),
                    question.id.toString(),
                    reQuestion.id.toString()
                )
            } else {
                Constants.showSnackBar(
                    binding.root,
                    "لا يمكن ترك التعليق فارغ",
                    Constants.redColor
                )
            }

//            val Bundle = Bundle().apply {
//                putSerializable("question", question)
//                putSerializable("advice", advice)
//                putSerializable("topic", topic)
//            }
//            findNavController().navigate(
//                R.id.action_updateReQuestionbottomSheetFragment_to_reQuestionDoctorFragment,
//                Bundle
//            )
        }

        return binding.root

    }


}