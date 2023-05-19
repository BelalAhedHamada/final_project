package com.example.finalcloudproject.Firebase.Doctor

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FirebaseSourceTopicDoctor(val activity: Activity) {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    lateinit var progressDialog: ProgressDialog
    var storge: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    lateinit var topicDoctorListMutableLiveData: MutableLiveData<List<Topic>>
    lateinit var show_UserListMutableLiveData: MutableLiveData<List<Topic>>

    fun addTopic(view: View, topic: Topic, imgeUri: Uri?) {

        storge = Firebase.storage
        storageRef = storge!!.reference
        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        db = Firebase.firestore
        analytics = Firebase.analytics
        imgeUri?.let {
            storageRef!!.child("image/" + "${topic.id}").putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        topic.image = uri.toString()
                        db.collection(Constants.topics).document(topic.id.toString()).set(topic)
                            .addOnSuccessListener {
                                Constants.showSnackBar(
                                    view,
                                    "تم اضافة الموضوع",
                                    Constants.greenColor
                                )
                                analytics.logEvent("addTopic") {
                                    param("name_course", topic.name!!)
                                }

                            }
                    }
                }.addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Constants.showSnackBar(
                        view,
                        "فشل اضافة الموضوع",
                        Constants.redColor
                    )
                    Log.e("exc", exception.message.toString())
                }.addOnProgressListener {
                    val progress: Double =
                        100.0 * it.bytesTransferred / it.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }


    }

    fun getDoctorTopic(uid: String): MutableLiveData<List<Topic>> {
        db = Firebase.firestore
        auth = Firebase.auth
        val topicList = ArrayList<Topic>()
        topicDoctorListMutableLiveData = MutableLiveData()
        db.collection(Constants.topics).addSnapshotListener() { result, error ->
            topicList.clear()
            for (topics in result!!) {
                val topic = topics!!.toObject<Topic>()
                if (uid == topic.idDoctor) {
                    topic?.let { topicList.add(it) }
                    topicDoctorListMutableLiveData.postValue(topicList)
                }
            }
        }
        return topicDoctorListMutableLiveData

    }

    fun updateTopic(view: View, topic: Topic, img: Uri?, document: String) {
        storge = Firebase.storage
        storageRef = storge!!.reference
        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        db = Firebase.firestore

//        val data = hashMapOf<String, Any?>(
//            "id" to topic.id,
//            "name" to topic.name,
//            "description" to topic.description,
//            "image" to topic.image,
//            "idDoctor" to topic.idDoctor,
//            "time" to topic.time,
//            "state" to topic.state
//        )
        val data = hashMapOf<String, Any?>(
            "id" to topic.id,
            "name" to topic.name,
            "description" to topic.description,
//            "image" to topic.image,
            "idDoctor" to topic.idDoctor,
            "time" to topic.time,
            "state" to topic.state
        )
        if (img != null) {
//            data.put("image",topic.image)
            storageRef!!.child("image/" + "${topic.id}").putFile(img!!)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                    topic.image = uri.toString()
                        Log.e("sdasd", uri.toString())
                        data.put("image", uri.toString())
                        db.collection(Constants.topics).document(document).update(data)
                            .addOnSuccessListener {
                                Constants.showSnackBar(
                                    view,
                                    "تم تعديل الموضوع",
                                    Constants.greenColor
                                )
                            }
                    }
                }.addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Constants.showSnackBar(
                        view,
                        "فشل تعديل الموضوع",
                        Constants.redColor
                    )
                }.addOnProgressListener {
                    val progress: Double =
                        100.0 * it.bytesTransferred / it.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        } else {
            db.collection(Constants.topics).document(document).update(data)
                .addOnSuccessListener {
                    progressDialog.dismiss()

                    Constants.showSnackBar(
                        view,
                        "تم تعديل الموضوع",
                        Constants.greenColor
                    )
                }

        }

//        storageRef!!.child("image/" + "${topic.id}").putFile(img!!)
//            .addOnSuccessListener { taskSnapshot ->
//                progressDialog.dismiss()
//                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
////                    topic.image = uri.toString()
//                    Log.e("sdasd",uri.toString())
//                    db.collection(Constants.topics).document(document).update(data)
//                        .addOnSuccessListener {
//                            Constants.showSnackBar(
//                                view,
//                                "تم تعديل الموضوع",
//                                Constants.greenColor
//                            )
//                        }
//                }
//            }.addOnFailureListener { exception ->
//                progressDialog.dismiss()
//                Constants.showSnackBar(
//                    view,
//                    "فشل تعديل الموضوع",
//                    Constants.redColor
//                )
//            }.addOnProgressListener {
//                val progress: Double =
//                    100.0 * it.bytesTransferred / it.totalByteCount
//                progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
//            }
    }

    fun deleteTopic(view: View, document: String) {
        storge = Firebase.storage
        storageRef = storge!!.reference
        db = Firebase.firestore
        db.collection(Constants.topics).document(document).get()
            .addOnSuccessListener {
                storageRef!!.child("image/${it.get("id")}").delete()
                db.collection(Constants.topics).document(document).delete()
                db.collection(Constants.myTopic).document(document).delete()
                Constants.showSnackBar(
                    view,
                    "تم حذف الموضوع",
                    Constants.greenColor
                )

            }

    }

//    fun updateSeeTopic(view: View, document: String) {
//        db = Firebase.firestore
//        analytics = Firebase.analytics
//        db.collection(Constants.topics).document(document).addSnapshotListener { it, error ->
//            val topic = it!!.toObject<Topic>()
//            if (topic!!.state == 0) {
//                db.collection(Constants.topics).document(document)
//                    .update("state", 1)
//                Constants.showSnackBar(
//                    view,
//                    "تم اخفاء الموضوع",
//                    Constants.greenColor
//                )
//                analytics.logEvent("seeTopic") {
//                    param("name_Topic", topic.name.toString())
//                }
//            } else {
//                db.collection(Constants.topics).document(document)
//                    .update("state", 0)
//                Constants.showSnackBar(
//                    view,
//                    "تم اضهار الموضوع",
//                    Constants.greenColor
//                )
//
//            }
//
//        }
//
//
//    }

    fun updateSeeTopic(view: View, document: String, imgView: ImageView) {
        db = Firebase.firestore
        analytics = Firebase.analytics
        db.collection(Constants.topics).document(document).get().addOnSuccessListener {
            val topic = it!!.toObject<Topic>()
            if (topic!!.state == 0) {
                db.collection(Constants.topics).document(document)
                    .update("state", 1)
                Constants.showSnackBar(
                    view,
                    "تم اخفاء الموضوع",
                    Constants.greenColor
                )
//                imgView.setImageResource(R.drawable.unhidden)

                analytics.logEvent("seeTopic") {
                    param("name_Topic", topic.name.toString())
                }
            } else {
                db.collection(Constants.topics).document(document)
                    .update("state", 0)
                Constants.showSnackBar(
                    view,
                    "تم اضهار الموضوع",
                    Constants.greenColor
                )
//                imgView.setImageResource(R.drawable.hidden)

            }
        }


    }

    fun getUserTopic(uid: String, documentTopic: String): MutableLiveData<List<Topic>> {
        db = Firebase.firestore
        auth = Firebase.auth
        val topicList = ArrayList<Topic>()
        show_UserListMutableLiveData = MutableLiveData()
        db.collection(Constants.topics).document(documentTopic)
            .addSnapshotListener() { result, error ->
                topicList.clear()
                val topic = result!!.toObject<Topic>()
                if (uid == topic?.idDoctor) {
                    topic.let { topicList.add(it) }
                    show_UserListMutableLiveData.postValue(topicList)
                }

            }

        return show_UserListMutableLiveData

    }
}