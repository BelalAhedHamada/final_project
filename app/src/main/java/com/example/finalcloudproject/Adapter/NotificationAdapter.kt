package com.example.finalcloudproject.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.databinding.ItemNotificationBinding

class NotificationAdapter(val activity: Activity)  : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val message = binding.tvMessage
    }

    private val differCallback = object : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = differ.currentList[position]
        holder.itemView.apply {

            holder.title.text = notification.title
            holder.message.text = notification.message


            setOnClickListener {
                onItemClickListener?.let { it(notification) }
            }


        }
    }

    private var onItemClickListener: ((Notification) -> Unit)? = null
    fun setOnItemClickListener(listener: (Notification) -> Unit) {
        onItemClickListener = listener
    }


}