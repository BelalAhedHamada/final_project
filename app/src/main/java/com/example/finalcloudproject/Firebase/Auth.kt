package com.example.finalcloudproject.Firebase

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.View.Patient.MainActivity
import com.example.finalcloudproject.View.Sign_in
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class Auth(val activity: Activity) {
    lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    lateinit var db: FirebaseFirestore
    lateinit var progressDialog: ProgressDialog

    fun Sign_in(view: View, Email: String, Password: String) {
        analytics = Firebase.analytics
        auth = Firebase.auth
        db = Firebase.firestore
        var sharedPref: SharedPreferences? = null
        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener {
            if (it.isSuccessful) {
                FirebaseMessaging.getInstance().subscribeToTopic(auth.currentUser!!.uid)
                db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
                    Log.e("isLogin", sharedPref?.getBoolean("isLogin", false).toString())
                    Log.e("isLogin", sharedPref?.getInt("type", 0).toString())
                    Log.e("isLogin", sharedPref?.getString("id", "").toString())
                    val users = it.toObject<User>()
                    val editor = sharedPref?.edit()
                    editor?.putBoolean("isLogin", true)
                    editor?.putInt("type", users!!.type)
                    editor?.putString("id", users!!.id)
                    editor?.apply()
                    if (users!!.type == 1) {
                        val i = Intent(activity, DoctorActivity::class.java)
                        activity.startActivity(i)
                        activity.finish()
                        analytics.setUserProperty("User_type", "Doctor")

                    } else {
                        val i = Intent(activity, MainActivity::class.java)
                        activity.startActivity(i)
                        activity.finish()
                        analytics.setUserProperty("User_type", "Patient")
                    }
                }

//                } else {
//                        val i = Intent(activity, Student::class.java)
//                        activity.startActivity(i)
//                        activity.finish()
//                    analytics.setUserProperty("User_type", "Student")
//                }
                analytics.logEvent("my_login") {
                    param("email_name", auth.currentUser!!.email.toString())
                }

            } else {
                Constants.showSnackBar(
                    view,
                    "يرجى التحقق من اسم المستخدم أو كلمة السر الخاص بك",
                    Constants.redColor
                )
            }
        }
    }

    fun Sign_up(view: View, password: String, users: User) {
        auth = Firebase.auth
        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        auth.createUserWithEmailAndPassword(users.email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                createUser(
                    User(
                        auth.currentUser!!.uid,
                        users.name,
                        users.email,
                        users.address,
                        users.phone,
                        users.birth_date,
                        users.type,
                        users.fcm_token
                    )
                )
                progressDialog.dismiss()
                Constants.showSnackBar(
                    view,
                    "تم تسجيلك بنجاح",
                    Constants.greenColor
                )
                Thread.sleep(700)
                val i = Intent(activity, Sign_in::class.java)
                activity.startActivity(i)
                activity.finish()
            } else {
                progressDialog.dismiss()
                Constants.showSnackBar(
                    view,
                    "فشل تسجيل الحساب",
                    Constants.redColor
                )
            }
        }.addOnFailureListener {
            Log.e("error", it.message.toString())
        }
    }

    fun createUser(users: User) {
        db = Firebase.firestore
        auth = Firebase.auth
        db.collection("users").document(auth.currentUser!!.uid)
            .set(users).addOnSuccessListener {
            }
    }

//    fun getUser(document:String): MutableLiveData<List<User>> {
//        db = Firebase.firestore
//        auth = Firebase.auth
//        val userslist = ArrayList<User>()
//        usersListMutableLiveData = MutableLiveData()
//        db.collection("users").document(document).get().addOnSuccessListener {
//            val users = it.toObject<User>()
//            users?.let { it1 -> userslist.add(it1) }
//            usersListMutableLiveData.postValue(userslist)
//            Log.e("user","user1111 ${userslist.size}")
//        }
//        Log.e("user","user222 ${userslist.size}")
//        return usersListMutableLiveData
//    }

    fun getUser(document: String): ArrayList<User> {
        db = Firebase.firestore
        auth = Firebase.auth
        val userslist = ArrayList<User>()
        db.collection("users").document(document).get().addOnSuccessListener {
            val users = it.toObject<User>()
            users?.let { it1 -> userslist.add(it1) }
            Log.e("user", "user1111 ${userslist.size}")
        }
        Log.e("user", "user222 ${userslist.size}")
        return userslist
    }
}