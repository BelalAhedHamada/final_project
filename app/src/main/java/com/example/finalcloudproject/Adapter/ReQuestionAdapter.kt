package com.example.finalcloudproject.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.View.Patient.MainActivity
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.ItemQuestionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ReQuestionAdapter(val activity: Activity) :
    RecyclerView.Adapter<ReQuestionAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvNameUserQ
        val des = binding.tvDesQ
        val tv_add_re = binding.tvAddRe
    }

    private val differCallback = object : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = differ.currentList[position]
        holder.itemView.apply {

            holder.name.text = question.user!!.name
            holder.des.text = question.description



            setOnClickListener {
                onItemClickListener?.let { it(question) }
            }


        }
    }

    private var onItemClickListener: ((Question) -> Unit)? = null
    fun setOnItemClickListener(listener: (Question) -> Unit) {
        onItemClickListener = listener
    }
}