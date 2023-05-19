package com.example.finalcloudproject.Firebase.Patient

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.Model.User
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FirebaseSourceQuestionPatient(var activity: Activity) {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    lateinit var progressDialog: ProgressDialog
    var storge: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    lateinit var questionListMutableLiveData: MutableLiveData<List<Question>>

    fun addQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
    ) {
        analytics = Firebase.analytics
        db = Firebase.firestore
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/${Constants.question}")
            .document(question.id!!).set(question.getQuestionHashMap())
        Constants.showSnackBar(
            view, "تم اضافة التعليق", Constants.greenColor
        )
        analytics.logEvent("addQuestion") {
            param("name_Question", question.user!!.name)
        }
    }

    fun getQuestion(
        documentTopic: String,
        documentAdvices: String,
    ): MutableLiveData<List<Question>> {
        db = Firebase.firestore
        auth = Firebase.auth
        questionListMutableLiveData = MutableLiveData()
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/${Constants.question}")
            .addSnapshotListener() { result, error ->
                val question = result!!.toObjects<Question>()
                questionListMutableLiveData.postValue(question)

            }
        return questionListMutableLiveData

    }
}