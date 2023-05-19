package com.example.finalcloudproject.View.Patient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentUpdateReQuestionPatientBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UpdateReQuestionPatientBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentUpdateReQuestionPatientBottomSheetBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    var users: User? = null
    val args: UpdateReQuestionPatientBottomSheetFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateReQuestionPatientBottomSheetBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as MainActivity).palliatveViewModel

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