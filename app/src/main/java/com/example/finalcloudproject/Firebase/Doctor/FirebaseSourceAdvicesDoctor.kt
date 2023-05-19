package com.example.finalcloudproject.Firebase.Doctor

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Advices
import com.example.finalcloudproject.Model.Topic
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FirebaseSourceAdvicesDoctor(val activity: Activity) {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    lateinit var progressDialog: ProgressDialog
    var storge: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    lateinit var advicesDoctorListMutableLiveData: MutableLiveData<List<Advices>>

    fun addAdvices(
        view: View, advices: Advices, uriImage: Uri?, uriVideo: Uri?, document: String, code: String
    ) {
        storge = Firebase.storage
        storageRef = storge!!.reference
        analytics = Firebase.analytics
        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        db = Firebase.firestore
        val data = hashMapOf<String, Any?>(
            "id" to advices.id,
            "name" to advices.name,
            "description" to advices.description,
            "time" to advices.time,
            "state" to advices.state,
//            "image" to image,
//            "video" to video
        )
        if (code == "5000") {
            storageRef!!.child("image/advices/" + "${advices.id}").putFile(uriImage!!)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        advices.image = uri.toString()
                        data.put("image", uri.toString())
                        db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                            .document(advices.id!!).set(data)
                        Constants.showSnackBar(
                            view, "تم اضافة النصيحة", Constants.greenColor
                        )
                    }
                    analytics.logEvent("addAdvices") {
                        param("name_advices", advices.name!!)
                    }
                }.addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Constants.showSnackBar(
                        view, "فشل اضافة النصيحة", Constants.redColor
                    )
                }.addOnProgressListener {
                    val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        } else {
            storageRef!!.child("image/advices/" + "${advices.id}").putFile(uriImage!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        advices.image = uri.toString()
                        data.put("image", uri.toString())

                        storageRef!!.child("video/" + "${advices.id}").putFile(uriVideo!!)
                            .addOnSuccessListener { videotaskSnapshot ->

                                progressDialog.dismiss()
                                videotaskSnapshot.storage.downloadUrl.addOnSuccessListener { uriv ->
                                    data.put("video", uriv.toString())

//                                    advices.video = uriv.toString()
                                    db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                                        .document(advices.id!!).set(data)
                                    Constants.showSnackBar(
                                        view, "تم اضافة النصيحة", Constants.greenColor
                                    )
                                    analytics.logEvent("addAdvices") {
                                        param("name_advices", advices.name!!)
                                    }

                                }
                            }

                    }
                }.addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Constants.showSnackBar(
                        view, "فشل اضافة النصيحة", Constants.redColor
                    )
                }.addOnProgressListener {
                    val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }

    }

//    fun updateAdvices(
//        view: View,
//        advices: Advices,
//        uriImage: Uri?,
//        uriVideo: Uri?,
//        document: String,
//        documentAdvice: String,
//        code: String
//    ) {
//        storge = Firebase.storage
//        storageRef = storge!!.reference
//        analytics = Firebase.analytics
//        progressDialog = ProgressDialog(activity)
//        progressDialog.setCancelable(false)
//        progressDialog.setMessage("Loading...")
//        progressDialog.show()
//        db = Firebase.firestore
//        if (code == "5000") {
//            storageRef!!.child("image/advices/" + "${advices.id}").putFile(uriImage!!)
//                .addOnSuccessListener { taskSnapshot ->
//                    progressDialog.dismiss()
//                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        advices.image = uri.toString()
//                        db.collection("${Constants.topics}/${document}/${Constants.advices}/")
//                            .document(documentAdvice)
//                            .update(advices.getAdvicesHashMap())
//                        Constants.showSnackBar(
//                            view,
//                            "تم تعديل النصيحة",
//                            Constants.greenColor
//                        )
//                    }
//                    analytics.logEvent("updateAdvices") {
//                        param("name_advices", advices.name!!)
//                    }
//                }.addOnFailureListener { exception ->
//                    progressDialog.dismiss()
//                    Constants.showSnackBar(
//                        view,
//                        "فشل تعديل النصيحة",
//                        Constants.redColor
//                    )
//                }.addOnProgressListener {
//                    val progress: Double =
//                        100.0 * it.bytesTransferred / it.totalByteCount
//                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
//                }
//        } else {
//            storageRef!!.child("image/advices/" + "${advices.id}").putFile(uriImage!!)
//                .addOnSuccessListener { taskSnapshot ->
//                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        advices.image = uri.toString()
//                        storageRef!!.child("video/" + "${advices.id}").putFile(uriVideo!!)
//                            .addOnSuccessListener { videotaskSnapshot ->
//                                progressDialog.dismiss()
//                                videotaskSnapshot.storage.downloadUrl.addOnSuccessListener { uriv ->
//                                    advices.video = uriv.toString()
//                                    db.collection("${Constants.topics}/${document}/${Constants.advices}/")
//                                        .document(documentAdvice)
//                                        .update(advices.getAdvicesHashMap())
//                                    Constants.showSnackBar(
//                                        view,
//                                        "تم تعديل النصيحة",
//                                        Constants.greenColor
//                                    )
//                                    analytics.logEvent("updateAdvices") {
//                                        param("name_advices", advices.name!!)
//                                    }
//
//                                }
//                            }
//
//                    }
//                }.addOnFailureListener { exception ->
//                    progressDialog.dismiss()
//                    Constants.showSnackBar(
//                        view,
//                        "فشل تعديل النصيحة",
//                        Constants.redColor
//                    )
//                }.addOnProgressListener {
//                    val progress: Double =
//                        100.0 * it.bytesTransferred / it.totalByteCount
//                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
//                }
//        }
//
//    }


    fun updateAdvices(
        view: View,
        advices: Advices,
        uriImage: Uri?,
        uriVideo: Uri?,
        document: String,
        documentAdvice: String,
        code: String
    ) {
        storge = Firebase.storage
        storageRef = storge!!.reference
        analytics = Firebase.analytics
        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        db = Firebase.firestore
        val data = hashMapOf<String, Any?>(
            "id" to advices.id,
            "name" to advices.name,
            "description" to advices.description,
            "time" to advices.time,
            "state" to advices.state,
        )
        if (code == "5000") {
            if (uriImage != null) {
                storageRef!!.child("image/advices/" + "${advices.id}").putFile(uriImage!!)
                    .addOnSuccessListener { taskSnapshot ->
                        progressDialog.dismiss()
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            data.put("image", uri.toString())
                            db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                                .document(documentAdvice).update(data)
                            Constants.showSnackBar(
                                view, "تم تعديل النصيحة", Constants.greenColor
                            )
                        }
                        analytics.logEvent("updateAdvices") {
                            param("name_advices", advices.name!!)
                        }
                    }.addOnFailureListener { exception ->
                        progressDialog.dismiss()
                        Constants.showSnackBar(
                            view, "فشل تعديل النصيحة", Constants.redColor
                        )

                    }.addOnProgressListener {
                        val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            } else {
                db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                    .document(documentAdvice).update(data)
                progressDialog.dismiss()
                Constants.showSnackBar(
                    view, "تم تعديل النصيحة", Constants.greenColor
                )
            }

        } else {
            if (uriVideo != null && uriImage != null) {
                storageRef!!.child("image/advices/" + "${advices.id}").putFile(uriImage!!)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            data.put("image", uri.toString())

                            storageRef!!.child("video/" + "${advices.id}").putFile(uriVideo!!)
                                .addOnSuccessListener { videotaskSnapshot ->
                                    progressDialog.dismiss()
                                    videotaskSnapshot.storage.downloadUrl.addOnSuccessListener { uriv ->
                                        data.put("video", uriv.toString())

                                        db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                                            .document(documentAdvice)
                                            .update(data)
                                        Constants.showSnackBar(
                                            view, "تم تعديل النصيحة", Constants.greenColor
                                        )
                                        analytics.logEvent("updateAdvices") {
                                            param("name_advices", advices.name!!)
                                        }

                                    }
                                }

                        }
                    }.addOnFailureListener { exception ->
                        progressDialog.dismiss()
                        Constants.showSnackBar(
                            view, "فشل تعديل النصيحة", Constants.redColor
                        )
                    }.addOnProgressListener {
                        val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            } else if (uriVideo != null) {
                storageRef!!.child("video/" + "${advices.id}").putFile(uriVideo!!)
                    .addOnSuccessListener { videotaskSnapshot ->
                        progressDialog.dismiss()
                        videotaskSnapshot.storage.downloadUrl.addOnSuccessListener { uriv ->
                            data.put("video", uriv.toString())
                            db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                                .document(documentAdvice)
                                .update(data)
                            Constants.showSnackBar(
                                view, "تم تعديل النصيحة", Constants.greenColor
                            )
                            analytics.logEvent("updateAdvices") {
                                param("name_advices", advices.name!!)
                            }

                        }
                    }
            } else {
                db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                    .document(documentAdvice).update(data)
                progressDialog.dismiss()
                Constants.showSnackBar(
                    view, "تم تعديل النصيحة", Constants.greenColor
                )
                analytics.logEvent("updateAdvices") {
                    param("name_advices", advices.name!!)
                }
            }

        }

    }

    fun deleteAdvice(view: View, document: String, documentAdvice: String) {
        storge = Firebase.storage
        storageRef = storge!!.reference
        db = Firebase.firestore
        analytics = Firebase.analytics

        db.collection("${Constants.topics}/${document}/${Constants.advices}/")
            .document(documentAdvice).get().addOnSuccessListener {
                val advices = it!!.toObject<Advices>()

                storageRef!!.child("video/${advices!!.id}").delete()
                storageRef!!.child("image/advices/${advices.id}").delete()
                db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                    .document(documentAdvice).delete()
                Constants.showSnackBar(
                    view, "تم حذف النصيحة", Constants.greenColor
                )
                analytics.logEvent("deleteAdvices") {
                    param("name_advices", advices.name.toString())
                }

            }

    }

    fun updateSeeAdvice(view: View, document: String, documentAdvice: String) {
        db = Firebase.firestore
        analytics = Firebase.analytics
        db.collection("${Constants.topics}/${document}/${Constants.advices}/")
            .document(documentAdvice).get().addOnSuccessListener {
                val advices = it!!.toObject<Advices>()
                if (advices!!.state) {
                    db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                        .document(documentAdvice).update("state", false)
                    Constants.showSnackBar(
                        view, "تم اخفاء النصيحة", Constants.greenColor
                    )
                    analytics.logEvent("seeAdvice") {
                        param("name_advice", advices.name.toString())
                    }
                } else {
                    db.collection("${Constants.topics}/${document}/${Constants.advices}/")
                        .document(documentAdvice).update("state", true)
                    Constants.showSnackBar(
                        view, "تم اضهار النصيحة", Constants.greenColor
                    )

                }
            }
    }

    fun getAdvices(document: String): MutableLiveData<List<Advices>> {
        db = Firebase.firestore
        advicesDoctorListMutableLiveData = MutableLiveData()
        db.collection("${Constants.topics}/${document}/${Constants.advices}/")
            .orderBy("time", Query.Direction.ASCENDING).addSnapshotListener { result, error ->
                val advices1 = result!!.toObjects<Advices>()
                advicesDoctorListMutableLiveData.postValue(advices1)
            }
        return advicesDoctorListMutableLiveData

    }

    fun getCountUserShowAdvice(
        documentTopic: String, documentAdvice: String, textView: TextView
    ) {
        db = Firebase.firestore
        db.collection("${Constants.topics}/${documentTopic}/${Constants.advices}/${documentAdvice}/users")
            .addSnapshotListener { value, error ->
                textView.text = value!!.size().toString()
            }
    }
}