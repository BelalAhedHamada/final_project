package com.example.finalcloudproject.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Model.Advices
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.databinding.ItemAdvicesDoctorBinding
import java.util.Random

class AdvicesDoctorAdapter:RecyclerView.Adapter<AdvicesDoctorAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemAdvicesDoctorBinding):RecyclerView.ViewHolder(binding.root){
        var name = binding.tvNameAdvices
        var des = binding.tvDesAdvices
        var img = binding.imgAdvices
        var card = binding.cvAdvices
    }

    private val differCallback = object : DiffUtil.ItemCallback<Advices>() {
        override fun areItemsTheSame(oldItem: Advices, newItem: Advices): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Advices, newItem: Advices): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemAdvicesDoctorBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val advices = differ.currentList[position]
        holder.itemView.apply {

            holder.name.text = advices.name
            holder.des.text = advices.description
            Glide.with(this).load(advices.image).into(holder.img)

            setOnClickListener {
                onItemClickListener?.let { it(advices) }
            }
        }
    }

    private var onItemClickListener: ((Advices) -> Unit)? = null
    fun setOnItemClickListener(listener: (Advices) -> Unit) {
        onItemClickListener = listener
    }
}