package com.example.finalcloudproject.Firebase.Doctor

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.Topic
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FirebaseSourceQuestionDoctor(val activity: Activity) {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    lateinit var reQuestionListMutableLiveData: MutableLiveData<List<Question>>

    fun addReQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
    ) {
        analytics = Firebase.analytics
        db = Firebase.firestore
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/${Constants.question}/${documentQuestion}/${Constants.reQuestion}")
            .document(question.id!!).set(question.getQuestionHashMap())
        Constants.showSnackBar(
            view, "تم اضافة التعليق", Constants.greenColor
        )
        analytics.logEvent("addReQuestion") {
            param("name_Question", question.user!!.name)
        }
    }

    fun getReQuestion(
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
    ): MutableLiveData<List<Question>> {
        db = Firebase.firestore
        auth = Firebase.auth
        reQuestionListMutableLiveData = MutableLiveData()
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/${Constants.question}/${documentQuestion}/${Constants.reQuestion}")
            .addSnapshotListener() { result, error ->
                val question = result!!.toObjects<Question>()
                reQuestionListMutableLiveData.postValue(question)

            }
        return reQuestionListMutableLiveData

    }

    fun updateReQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
        documentReQuestion: String
    ) {

        db = Firebase.firestore

        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/${Constants.question}/${documentQuestion}/${Constants.reQuestion}")
            .document(documentReQuestion).update(question.getQuestionHashMap())
            .addOnSuccessListener {
                Constants.showSnackBar(
                    view, "تم تعديل ردك", Constants.greenColor
                )
            }.addOnFailureListener {
                Constants.showSnackBar(
                    view, "فشل تعديل ردك", Constants.redColor
                )
                Log.e("asddd",it.message.toString())
            }
    }

    fun deleteReQuestion(
        view: View,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
        documentReQuestion: String
    ) {

        db = Firebase.firestore
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/${Constants.question}/${documentQuestion}/${Constants.reQuestion}")
            .document(documentReQuestion).delete().addOnSuccessListener {
                Constants.showSnackBar(
                    view, "تم حذف ردك", Constants.greenColor
                )

            }

    }
}