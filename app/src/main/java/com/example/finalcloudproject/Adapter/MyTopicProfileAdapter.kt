package com.example.finalcloudproject.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Model.MyTopic
import com.example.finalcloudproject.databinding.ItemMyTopicBinding
import com.example.finalcloudproject.databinding.ItemTopicProfileBinding
import java.util.Random

class MyTopicProfileAdapter : RecyclerView.Adapter<MyTopicProfileAdapter.ViewHolder>() {
    var rowindex: Int? = null

    inner class ViewHolder(var binding: ItemTopicProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name = binding.tvNameMyTopic
        var img = binding.imgMyTopic
        var card = binding.itemCardprof
    }

    private val differCallback = object : DiffUtil.ItemCallback<MyTopic>() {
        override fun areItemsTheSame(oldItem: MyTopic, newItem: MyTopic): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MyTopic, newItem: MyTopic): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTopicProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = differ.currentList[position]
        holder.itemView.apply {
            val gd = GradientDrawable(
                GradientDrawable.Orientation.BR_TL, intArrayOf(-0x9e9d9f, -0xececed)
            )
            val gd2 = GradientDrawable(
                GradientDrawable.Orientation.BR_TL, intArrayOf(-0x9e9d9f, -0xececed)
            )

            gd2.setColors(
                intArrayOf(
                    Color.parseColor("#A4B2CEE4"),
                    Color.parseColor("#A4B2CEE4"),
                )
            )
            val random = Random()
            val color =
                Color.argb(200, random.nextInt(255), random.nextInt(50), random.nextInt(200))
            val color2 =
                Color.argb(150, random.nextInt(255), random.nextInt(50), random.nextInt(200))
            gd.setColors(
                intArrayOf(
                    color,
                    color2,
                )
            )


            holder.name.text = topic.name
            Glide.with(this).load(topic.image).into(holder.img)
            setOnClickListener {
                onItemClickListener?.let { it(topic) }
                rowindex = position
                notifyDataSetChanged();
            }
            gd.cornerRadius = 80f
            holder.card.background = gd
            holder.card.cardElevation = 10f


        }
    }

    private var onItemClickListener: ((MyTopic) -> Unit)? = null
    fun setOnItemClickListener(listener: (MyTopic) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemClickListener2: ((MyTopic) -> Unit)? = null
    fun setOnItemClickListener2(listener: (MyTopic) -> Unit) {
        onItemClickListener2 = listener
    }
}