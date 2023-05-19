package com.example.finalcloudproject.Firebase

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Message
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FirebaseSource(val activity: Activity) {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var storge: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    lateinit var progressDialog: ProgressDialog
    lateinit var usersListMutableLiveData: MutableLiveData<List<User>>
    lateinit var messageListMutableLiveData: MutableLiveData<List<Message>>
    lateinit var messagePrivateListMutableLiveData: MutableLiveData<List<Message>>
    lateinit var notificationListMutableLiveData: MutableLiveData<List<Notification>>

    fun getUser(): MutableLiveData<List<User>> {
        db = Firebase.firestore
        auth = Firebase.auth
        val userslist = ArrayList<User>()
        usersListMutableLiveData = MutableLiveData()
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            val users = it.toObject<User>()
            users?.let { it1 -> userslist.add(it1) }
            usersListMutableLiveData.postValue(userslist)
        }
        return usersListMutableLiveData
    }

    fun sendMessageTopic(message: Message, documentMyTopic: String, imgeUri: Uri?) {
        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        db = Firebase.firestore
        storge = Firebase.storage
        storageRef = storge!!.reference
        if (imgeUri != null) {
            Log.e("aa", "imgeUri Fi $imgeUri")
            storageRef!!.child("imageMessageCourse/" + message.id).putFile(imgeUri)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        message.image = uri.toString()
                        db.collection("${Constants.myTopic}/${documentMyTopic}/${Constants.message}")
                            .document(message.id)
                            .set(message.getMessageHashMap())
                    }
                }.addOnFailureListener { exception ->
                }.addOnProgressListener {
                    val progress: Double =
                        100.0 * it.bytesTransferred / it.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        } else {
            Log.e("aa", "imgeUri els $imgeUri")
            progressDialog.dismiss()
            db.collection("${Constants.myTopic}/${documentMyTopic}/${Constants.message}")
                .document(message.id)
                .set(message.getMessageHashMap())
        }

    }


    fun deleteMessageTopic(documentMyTopic: String, documentChat: String) {
        db = Firebase.firestore
        db.collection("${Constants.myTopic}/${documentMyTopic}/${Constants.message}")
            .document(documentChat).delete()
    }

    fun getMessageCourse(documentMyTopic: String): MutableLiveData<List<Message>> {
        db = Firebase.firestore
        val chatList: ArrayList<Message> = arrayListOf()
        messageListMutableLiveData = MutableLiveData()
        db.collection("${Constants.myTopic}/${documentMyTopic}/${Constants.message}")
            .orderBy("time", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
                chatList.clear()
                for (document in value!!) {
                    val chat = document.toObject<Message>()
                    chatList.add(chat)
                    messageListMutableLiveData.postValue(chatList)
                }
            }
        return messageListMutableLiveData
    }


    fun sendMessagePrivate(message: Message, documentUsers: String) {
        db = Firebase.firestore
        db.collection("users/${documentUsers}/${Constants.message}").document(message.id)
            .set(message.getMessageHashMap())
    }

    fun deleteMessagePrivate(documentUsers: String, documentChat: String) {
        db = Firebase.firestore
        db.collection("users/${documentUsers}/${Constants.message}").document(documentChat).delete()
    }

    fun getMessagePrivate(documentUsers: String): MutableLiveData<List<Message>> {
        db = Firebase.firestore
        val chatList = ArrayList<Message>()
        messagePrivateListMutableLiveData = MutableLiveData()
        db.collection("users/${documentUsers}/message").orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                chatList.clear()
                for (document in value!!) {
                    val chat = document.toObject<Message>()
                    chatList.add(chat)
                    messagePrivateListMutableLiveData.postValue(chatList)
                }
            }
        return messagePrivateListMutableLiveData
    }


    fun storeNotification(notification: Notification, documentUsers: String) {
        db = Firebase.firestore
        db.collection("users/${documentUsers}/${Constants.notification}").document(notification.id)
            .set(notification.getNotificationHashMap())
    }

    fun getNotification(documentUsers: String): MutableLiveData<List<Notification>> {
        db = Firebase.firestore
        val notificationList = ArrayList<Notification>()
        notificationListMutableLiveData = MutableLiveData()
        db.collection("users/${documentUsers}/${Constants.notification}").orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                notificationList.clear()
                for (document in value!!) {
                    val notification = document.toObject<Notification>()
                    notificationList.add(notification)
                    notificationListMutableLiveData.postValue(notificationList)
                }
            }
        return notificationListMutableLiveData
    }




}