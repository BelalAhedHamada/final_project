package com.example.finalcloudproject.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.R
import com.example.finalcloudproject.databinding.ItemTopicBinding
import java.util.Random

class TopicAdapter() : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.nameTopic
        var image = binding.imageTopic
        var card = binding.itemC
    }

    var rowindex: Int = 0

    private val differCallback = object : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = differ.currentList[position]
        holder.itemView.apply {
            val gd = GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x9e9d9f, -0xececed)
            )


            val random = Random()
            val color =
                Color.argb(200, random.nextInt(255), random.nextInt(50), random.nextInt(200))
            val color2 =
                Color.argb(100, random.nextInt(255), random.nextInt(50), random.nextInt(200))
            gd.colors = intArrayOf(
                color,
                color2,
            )
            val c = intArrayOf(color, color2)

//            if (rowindex == position) {
//                y = -50f
//
//            } else {
//                y = +40f
//                holder.card.elevation = -3f
//            }
            holder.card.setCardBackgroundColor(color)
            holder.name.text = topic.name
            holder.card.background = gd
            gd.cornerRadius = 70f
            Glide.with(this).load(topic.image).into(holder.image)

            setOnClickListener {
                onItemClickListener?.let { it(topic,holder.image,holder.name,c) }
            }


        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Topic, ImageView, TextView, IntArray) -> Unit)? = null
    fun setOnItemClickListener(listener: (Topic, ImageView, TextView, IntArray) -> Unit) {
        onItemClickListener = listener
    }

}