package com.example.finalcloudproject.ViewModel

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalcloudproject.Model.Advices
import com.example.finalcloudproject.Model.Message
import com.example.finalcloudproject.Model.MyTopic
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.PushNotification
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.Notification.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PalliativeViewModel(
    private val palliativeRepository: PalliativeRepository
) : ViewModel() {
    var topicsDoctor: MutableLiveData<List<Topic>>? = null
    var topicsPatient: MutableLiveData<List<Topic>>? = null
    var advicesDoctor: MutableLiveData<List<Advices>>? = null
    var user: MutableLiveData<List<User>>? = null
    var myTopic: MutableLiveData<List<MyTopic>>? = null
    var advicesPatient: MutableLiveData<List<Advices>>? = null
    var usersAdvicesList: MutableLiveData<List<User>?>? = null
    var usersTopicList: MutableLiveData<List<Topic>>? = null
    var userQuestionList: MutableLiveData<List<Question>>? = null
    var reQuestionList: MutableLiveData<List<Question>>? = null
    var messageTopicList: MutableLiveData<List<Message>>? = null
    var messagePrivateList: MutableLiveData<List<Message>>? = null
    var notificationList: MutableLiveData<List<Notification>>? = null
    var searchTopicList: MutableLiveData<List<Topic>?>? = null
    var topicExploreList: MutableLiveData<List<Topic>>? = null


    fun addTopic(view: View, topic: Topic, image: Uri?) {
        palliativeRepository.addTopic(view, topic, image)
    }

    fun getDoctorTopic(uid: String) {
        topicsDoctor = palliativeRepository.getDoctorTopic(uid)
    }

    fun updateTopic(view: View, topic: Topic, image: Uri?, document: String) {
        palliativeRepository.updateTopic(view, topic, image, document)
    }

    fun deleteTopic(view: View, document: String) {
        palliativeRepository.deleteTopic(view, document)
    }

    fun updateSeeTopic(view: View, document: String, imgView: ImageView) {
        palliativeRepository.updateSeeTopic(view, document, imgView)
    }


    // Advice

    fun addAdvice(
        view: View,
        advices: Advices,
        image: Uri?,
        video: Uri?,
        document: String,
        code: String
    ) = palliativeRepository.addAdvice(view, advices, image, video, document, code)

    //    fun getDoctorAdvice(uid:String) = firebaseSourceTopicDoctor.getDoctorTopic(uid)
    fun updateAdvice(
        view: View,
        advices: Advices,
        image: Uri?,
        video: Uri?,
        document: String,
        documentAdvice: String,
        code: String
    ) = palliativeRepository.updateAdvice(
        view,
        advices,
        image,
        video,
        document,
        documentAdvice,
        code
    )

    fun deleteAdvice(view: View, document: String, documentAdvice: String) =
        palliativeRepository.deleteAdvice(view, document, documentAdvice)

    fun updateSeeAdvice(view: View, document: String, documentAdvice: String) =
        palliativeRepository.updateSeeAdvice(view, document, documentAdvice)

    fun getAdvice(document: String) {
        advicesDoctor = palliativeRepository.getAdvice(document)
    }

    // topic patient

    fun getTopic() {
        topicsPatient = palliativeRepository.getTopic()
    }

    fun getUser() {
        user = palliativeRepository.getUser()
    }

    fun updateUsersMyTopic(view: View, idTopic: String, user: User) =
        palliativeRepository.updateUsersMyTopic(view, idTopic, user)

    fun getMyTopic() {
        myTopic = palliativeRepository.getMyTopic()
    }

    // advi

    fun getAdvicesPatient(document: String) {
        advicesPatient = palliativeRepository.getAdvicesPatient(document)
    }

    fun showUserAdvices(
        user: User, documentTopic: String,
        documentAdvices: String,
    ) =
        palliativeRepository.showUserAdvices(user, documentTopic, documentAdvices)


    fun getUserShowAdvices(documentTopic: String, documentAdvices: String) {
        usersAdvicesList = palliativeRepository.getUserShowAdvices(documentTopic, documentAdvices)
    }

    fun addQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
    ) =
        palliativeRepository.addQuestion(view, question, documentTopic, documentAdvices)

    fun getUserTopic(uid: String, documentTopic: String) {
        usersTopicList = palliativeRepository.getUserTopic(uid, documentTopic)
    }

    fun getCountUserShowAdvice(documentTopic: String, documentAdvice: String, textView: TextView) {
        palliativeRepository.getCountUserShowAdvice(documentTopic, documentAdvice, textView)
    }

    fun getQuestion(
        documentTopic: String,
        documentAdvice: String,
    ) {
        userQuestionList = palliativeRepository.getQuestion(documentTopic, documentAdvice)
    }

    fun addReQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
    ) =
        palliativeRepository.addReQuestion(
            view,
            question,
            documentTopic,
            documentAdvices,
            documentQuestion
        )

    fun getReQuestion(
        documentTopic: String,
        documentAdvice: String,
        documentQuestion: String,

        ) {
        reQuestionList =
            palliativeRepository.getReQuestion(documentTopic, documentAdvice, documentQuestion)
    }

    fun updateReQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
        documentReQuestion: String

    ) =
        palliativeRepository.updateReQuestion(
            view,
            question,
            documentTopic,
            documentAdvices,
            documentQuestion,
            documentReQuestion
        )

    fun deleteReQuestion(
        view: View,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
        documentReQuestion: String

    ) =
        palliativeRepository.deleteReQuestion(
            view,
            documentTopic,
            documentAdvices,
            documentQuestion,
            documentReQuestion
        )

    fun sendMessageTopic(
        message: Message, documentMyTopic: String, imgeUri: Uri?
    ) = palliativeRepository.sendMessageTopic(message, documentMyTopic, imgeUri)

    fun getMessageTopic(
        documentMyTopic: String
    ) = palliativeRepository.getMessageTopic(documentMyTopic)


    fun deleteMessageTopic(
        documentMyTopic: String, documentChat: String
    ) = palliativeRepository.deleteMessageTopic(documentMyTopic, documentChat)


    fun sendMessagePrivate(
        message: Message, documentUsers: String
    ) = palliativeRepository.sendMessagePrivate(message, documentUsers)

    fun getMessagePrivate(
        documentUsers: String
    ) =
        palliativeRepository.getMessagePrivate(documentUsers)


    fun deleteMessagePrivate(
        documentUsers: String, documentChat: String
    ) = palliativeRepository.deleteMessagePrivate(documentUsers, documentChat)


    fun sendNotification(
        pushNotification: PushNotification,
        notification: Notification,
        documentUsers: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(pushNotification)
            if (response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
                palliativeRepository.storeNotification(notification, documentUsers)
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

    fun getNotification(
        documentUsers: String
    ) {
        notificationList = palliativeRepository.getNotification(documentUsers)
    }

    fun searchTopic(
        text: String
    ) {
        searchTopicList = palliativeRepository.searchTopic(text)
    }

    fun getTopicsExplore() {
        topicExploreList = palliativeRepository.getTopicsExplore()

    }


}