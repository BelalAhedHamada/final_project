package com.example.finalcloudproject.ViewModel

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.finalcloudproject.Firebase.Doctor.FirebaseSourceAdvicesDoctor
import com.example.finalcloudproject.Firebase.Doctor.FirebaseSourceQuestionDoctor
import com.example.finalcloudproject.Firebase.Doctor.FirebaseSourceTopicDoctor
import com.example.finalcloudproject.Firebase.FirebaseSource
import com.example.finalcloudproject.Firebase.Patient.FirebaseSourceAdvicesPatient
import com.example.finalcloudproject.Firebase.Patient.FirebaseSourceQuestionPatient
import com.example.finalcloudproject.Firebase.Patient.FirebaseSourceTopicPatient
import com.example.finalcloudproject.Model.Advices
import com.example.finalcloudproject.Model.Message
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.Question
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.Model.User

class PalliativeRepository(
    activity: Activity
) {
    val firebaseSourceTopicDoctor = FirebaseSourceTopicDoctor(activity)
    val firebaseSourceAdvicesDoctor = FirebaseSourceAdvicesDoctor(activity)
    val firebaseSourceTopicPatient = FirebaseSourceTopicPatient(activity)
    val firebaseSource = FirebaseSource(activity)
    val firebaseSourceAdvicesPatient = FirebaseSourceAdvicesPatient(activity)
    val firebaseSourceQuestionPatient = FirebaseSourceQuestionPatient(activity)
    val firebaseSourceQuestionDoctor = FirebaseSourceQuestionDoctor(activity)

    // Topics
    fun addTopic(view: View, topic: Topic, image: Uri?) =
        firebaseSourceTopicDoctor.addTopic(view, topic, image)

    fun getDoctorTopic(uid: String) = firebaseSourceTopicDoctor.getDoctorTopic(uid)
    fun updateTopic(view: View, topic: Topic, image: Uri?, document: String) =
        firebaseSourceTopicDoctor.updateTopic(view, topic, image, document)

    fun deleteTopic(view: View, document: String) =
        firebaseSourceTopicDoctor.deleteTopic(view, document)

    fun updateSeeTopic(view: View, document: String, imgView: ImageView) =
        firebaseSourceTopicDoctor.updateSeeTopic(view, document, imgView)

    //Advices
    fun addAdvice(
        view: View,
        advices: Advices,
        image: Uri?,
        video: Uri?,
        document: String,
        code: String
    ) = firebaseSourceAdvicesDoctor.addAdvices(view, advices, image, video, document, code)

    fun getAdvice(document: String) = firebaseSourceAdvicesDoctor.getAdvices(document)
    fun updateAdvice(
        view: View,
        advices: Advices,
        image: Uri?,
        video: Uri?,
        document: String,
        documentAdvice: String,
        code: String
    ) = firebaseSourceAdvicesDoctor.updateAdvices(
        view,
        advices,
        image,
        video,
        document,
        documentAdvice,
        code
    )

    fun deleteAdvice(view: View, document: String, documentAdvice: String) =
        firebaseSourceAdvicesDoctor.deleteAdvice(view, document, documentAdvice)

    fun updateSeeAdvice(view: View, document: String, documentAdvice: String) =
        firebaseSourceAdvicesDoctor.updateSeeAdvice(view, document, documentAdvice)

    // topic patient
    fun getTopic() = firebaseSourceTopicPatient.getTopic()
    fun getUser() = firebaseSource.getUser()

    fun updateUsersMyTopic(view: View, idTopic: String, user: User) =
        firebaseSourceTopicPatient.updateUsersMyTopic(view, idTopic, user)

    fun getMyTopic() = firebaseSourceTopicPatient.getMyTopic()

    // advices patient
    fun getAdvicesPatient(document: String) =
        firebaseSourceAdvicesPatient.getAdvicesPatient(document)

    fun showUserAdvices(
        user: User, documentTopic: String,
        documentAdvices: String,
    ) =
        firebaseSourceAdvicesPatient.showUserAdvices(user, documentTopic, documentAdvices)

    fun getUserShowAdvices(documentTopic: String, documentAdvices: String) =
        firebaseSourceAdvicesPatient.getUserShowAdvices(documentTopic, documentAdvices)

    fun addQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
    ) =
        firebaseSourceQuestionPatient.addQuestion(view, question, documentTopic, documentAdvices)


    fun getUserTopic(uid: String, documentTopic: String) =
        firebaseSourceTopicDoctor.getUserTopic(uid, documentTopic)

    fun getCountUserShowAdvice(
        documentTopic: String,
        documentAdvice: String,
        textView: TextView
    ) =
        firebaseSourceAdvicesDoctor.getCountUserShowAdvice(documentTopic, documentAdvice, textView)

    fun getQuestion(
        documentTopic: String,
        documentAdvice: String,
    ) =
        firebaseSourceQuestionPatient.getQuestion(documentTopic, documentAdvice)


    fun addReQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
    ) =
        firebaseSourceQuestionDoctor.addReQuestion(
            view,
            question,
            documentTopic,
            documentAdvices,
            documentQuestion
        )

    fun getReQuestion(
        documentTopic: String,
        documentAdvice: String,
        documentQuestion: String
    ) =
        firebaseSourceQuestionDoctor.getReQuestion(documentTopic, documentAdvice, documentQuestion)

    fun updateReQuestion(
        view: View,
        question: Question,
        documentTopic: String,
        documentAdvices: String,
        documentQuestion: String,
        documentReQuestion: String

    ) =
        firebaseSourceQuestionDoctor.updateReQuestion(
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
        firebaseSourceQuestionDoctor.deleteReQuestion(
            view,
            documentTopic,
            documentAdvices,
            documentQuestion,
            documentReQuestion
        )


    fun sendMessageTopic(
        message: Message, documentMyTopic: String, imgeUri: Uri?
    ) = firebaseSource.sendMessageTopic(message, documentMyTopic, imgeUri)

    fun getMessageTopic(
        documentMyTopic: String
    ) = firebaseSource.getMessageCourse(documentMyTopic)

    fun deleteMessageTopic(
        documentMyTopic: String, documentChat: String
    ) = firebaseSource.deleteMessageTopic(documentMyTopic, documentChat)


    fun sendMessagePrivate(
        message: Message, documentUsers: String
    ) = firebaseSource.sendMessagePrivate(message, documentUsers)

    fun getMessagePrivate(
        documentUsers: String
    ) = firebaseSource.getMessagePrivate(documentUsers)

    fun deleteMessagePrivate(
        documentUsers: String, documentChat: String
    ) = firebaseSource.deleteMessagePrivate(documentUsers, documentChat)


    fun storeNotification(
        notification: Notification, documentUsers: String
    ) = firebaseSource.storeNotification(notification, documentUsers)

    fun getNotification(
        documentUsers: String
    ) = firebaseSource.getNotification(documentUsers)


    fun searchTopic(
        text: String
    ) = firebaseSourceTopicPatient.searchTopic(text)

    fun getTopicsExplore() = firebaseSourceTopicPatient.getTopicsExplore()


}