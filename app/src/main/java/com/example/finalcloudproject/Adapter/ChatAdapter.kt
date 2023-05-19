package com.example.finalcloudproject.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Model.Message
import com.example.finalcloudproject.R
import com.example.finalcloudproject.databinding.ItemAdvicesDoctorBinding
import com.example.finalcloudproject.databinding.ItemLeftBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChatAdapter(private val context: Context, private val chatList: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null
    var id: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return ViewHolder(view)
//            val binding =
//                ItemLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return ViewHolder(binding)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)
//            val binding =
//                ItemLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return ViewHolder(binding)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        id = chatList[position].id
        if (chat.message != null) {
            holder.txtUserName.text = chat.message
        } else {
            Glide.with(context).load(chat.image).into(holder.img_chat)
            holder.img_chat.visibility = View.VISIBLE
            holder.txtUserName.visibility = View.INVISIBLE
        }

        onItemClickListener?.let { it(chat, holder.txtUserName) }

    }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtUserName: TextView = view.findViewById(R.id.tvMessage)
        val img_chat: ImageView = view.findViewById(R.id.img_chat)
        //  val imgUser: CircleImageView = view.findViewById(R.id.userImage)
    }
//    class ViewHolder(binding: ItemLeftBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        val txtUserName: TextView = binding.tvMessage
//        val img_chat: ImageView = binding.imgChat
//        //  val imgUser: CircleImageView = view.findViewById(R.id.userImage)
//    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }

    }


    private var onItemClickListener: ((Message, TextView) -> Unit)? = null
    fun setOnItemClickListener(listener: (Message, TextView) -> Unit) {
        onItemClickListener = listener
    }

}