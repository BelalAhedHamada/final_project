package com.example.finalcloudproject.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.databinding.ItemShowUserBinding
import java.util.Random

class ShowUserFollowerTopicAdapter :
    RecyclerView.Adapter<ShowUserFollowerTopicAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemShowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.tvNameUser
        var email = binding.tvEmailUser
        var image = binding.imgUserQ
    }

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }


    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = differ.currentList[position]

        holder.itemView.apply {

            holder.name.text = user.name
            holder.email.text = user.email



            setOnClickListener {
                onItemClickListener?.let { it(user, holder.image) }
            }


        }
    }

    private var onItemClickListener: ((User, ImageView) -> Unit)? = null
    fun setOnItemClickListener(listener: (User, ImageView) -> Unit) {
        onItemClickListener = listener
    }
}