package com.example.finalcloudproject.Firebase.Patient

import android.app.Activity
import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Advices
import com.example.finalcloudproject.Model.User
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseSourceAdvicesPatient(val activity: Activity) {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    lateinit var progressDialog: ProgressDialog
    var storge: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    lateinit var advicesPatientListMutableLiveData: MutableLiveData<List<Advices>>
    lateinit var usersAdvicesListMutableLiveData: MutableLiveData<List<User>?>

    fun getAdvicesPatient(document: String): MutableLiveData<List<Advices>> {
        db = Firebase.firestore
        advicesPatientListMutableLiveData = MutableLiveData()
        db.collection("${Constants.topics}/${document}/${Constants.advices}/")
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { result, error ->
                val advices1 = result!!.toObjects<Advices>()
                advicesPatientListMutableLiveData.postValue(advices1)
            }
        return advicesPatientListMutableLiveData

    }

    fun showUserAdvices(
        users: User,
        documentTopic: String,
        documentAdvices: String,
    ) {
        db = Firebase.firestore
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/users")
            .document(users.id!!)
            .set(users)
    }

    fun getUserShowAdvices(
        documentTopic: String,
        documentAdvices: String,
    ): MutableLiveData<List<User>?> {
        auth = Firebase.auth
        db = Firebase.firestore
        usersAdvicesListMutableLiveData = MutableLiveData()
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvices}/users")
            .addSnapshotListener { result, error ->
                if (result!!.isEmpty){
                    usersAdvicesListMutableLiveData.postValue(null)
                }else{
                    val user = result.toObjects<User>()
                    usersAdvicesListMutableLiveData.postValue(user)
                }

            }
        return usersAdvicesListMutableLiveData

    }
}