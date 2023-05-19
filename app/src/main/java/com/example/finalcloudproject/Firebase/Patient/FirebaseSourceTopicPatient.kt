package com.example.finalcloudproject.Firebase.Patient

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.MyTopic
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.Model.User
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseSourceTopicPatient(val activity: Activity) {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    lateinit var progressDialog: ProgressDialog
    var storge: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    lateinit var topicDoctorListMutableLiveData: MutableLiveData<List<Topic>>
    lateinit var myTopicListMutableLiveData: MutableLiveData<List<MyTopic>>
    lateinit var searchListMutableLiveData: MutableLiveData<List<Topic>?>
    lateinit var topicExploreListMutableLiveData: MutableLiveData<List<Topic>>

    fun getTopic(): MutableLiveData<List<Topic>> {
        db = Firebase.firestore
        auth = Firebase.auth
        val topicList = ArrayList<Topic>()
        topicDoctorListMutableLiveData = MutableLiveData()
        db.collection(Constants.topics).addSnapshotListener() { result, error ->
            topicList.clear()
            for (topics in result!!) {
                val topic = topics!!.toObject<Topic>()
                if (topic.state != 1) {
                    topic.let { topicList.add(it) }
                    topicDoctorListMutableLiveData.postValue(topicList)
                }

            }
        }
        return topicDoctorListMutableLiveData

    }

    fun addMyTopic(view: View, topic: Topic) {
        db = Firebase.firestore
        auth = Firebase.auth
        analytics = Firebase.analytics
        db.collection(Constants.myTopic).document(topic.id!!).set(topic)
            .addOnSuccessListener {
                FirebaseMessaging.getInstance().subscribeToTopic(topic.id.toString())
                Snackbar.make(view, "تم متابعة الموضوع", Snackbar.LENGTH_LONG).apply {
                    animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                    setBackgroundTint(Color.parseColor(Constants.greenColor))
                    setTextColor(Color.parseColor("#FFFFFF"))
                    show()
                }
                analytics.logEvent("addMyTopic") {
                    param("name_topic", topic.name!!)
                }
            }

    }

    fun updateUsersMyTopic(view: View, idTopic: String, users: User) {
        val users = hashMapOf(
            "id" to users.id,
            "name" to users.name,
            "email" to users.email,
            "address" to users.address,
            "phone" to users.phone,
            "birth_date" to users.birth_date
        ) as Map<String, Any>

        val washingtonRef = db.collection(Constants.topics).document(idTopic)

// Atomically add a new region to the "users" array field.
        washingtonRef.update("users", FieldValue.arrayUnion(users)).addOnSuccessListener {
            db.collection(Constants.topics).document(idTopic).get().addOnSuccessListener { value ->
                val topic = value!!.toObject<Topic>()
                addMyTopic(view, topic!!)
            }
        }


    }

    fun getMyTopic(): MutableLiveData<List<MyTopic>> {
        db = Firebase.firestore
        val topicList = ArrayList<MyTopic>()
        myTopicListMutableLiveData = MutableLiveData()
        auth = Firebase.auth
        db.collection(Constants.myTopic).addSnapshotListener { result, error ->
            topicList.clear()
            for (document in result!!) {
                val topic = document.toObject<MyTopic>()
                val topicUsers = topic.users
                Log.e("sdasd", topicUsers.isNullOrEmpty().toString())

                if (topicUsers != null) {
                    for (users in topicUsers) {
                        Log.e("sdasd", users.id.toString())
                        if (auth.currentUser!!.uid == users.id) {
                            topicList.add(topic)
                            myTopicListMutableLiveData.postValue(topicList)
                        }
                    }
                }

            }
        }

        return myTopicListMutableLiveData

    }

    fun searchTopic(text: String): MutableLiveData<List<Topic>?> {
        db = Firebase.firestore
        analytics = Firebase.analytics
        val topiclist = ArrayList<Topic>()
        searchListMutableLiveData = MutableLiveData()
        db.collection(Constants.topics).whereEqualTo("name", text)
            .addSnapshotListener { value, error ->
                topiclist.clear()
                if (value!!.isEmpty) {
                    searchListMutableLiveData.postValue(null)
                } else {
                    for (item in value!!) {
                        val topic = item.toObject<Topic>()
                        if (topic.state != 1) {
                            topiclist.add(topic)
                            searchListMutableLiveData.postValue(topiclist)
                        }

                    }
                    analytics.logEvent("searchTopic") {
                        param("search_name", text)
                    }
                }
            }
        return searchListMutableLiveData
    }


    fun getTopicsExplore(): MutableLiveData<List<Topic>> {
        db = Firebase.firestore
        val topiclist = ArrayList<Topic>()
        topicExploreListMutableLiveData = MutableLiveData()
        db.collection(Constants.topics).orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                for (document in value!!) {
                    val topic = document.toObject<Topic>()
                    if (topic.state != 1) {
                        topiclist.add(topic)
                        topicExploreListMutableLiveData.postValue(topiclist)
                    }
                }


            }

        return topicExploreListMutableLiveData

    }


}