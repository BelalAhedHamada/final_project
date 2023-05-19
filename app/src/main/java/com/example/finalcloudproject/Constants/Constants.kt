package com.example.finalcloudproject.Constants

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class Constants {

    companion object {
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY =
            "AAAAiSOnL0s:APA91bEde5SsQhvFwCYThjl5bW72RuQ8ri9WaLgasNSFl8yH_sIPs5pRi3oK_DHkVten1BW2Jp0FaolW0gi9870cEMEd3v8nCy_V-F3URKgmrlNuWn3hqNLGRRFL1MhXtQnmB2svIDc8" // get firebase server key from firebase project setting
        const val auth = "AIzaSyDG8-d4XNkKusTL7ffnSQmPjICba5HVmA4"
        const val CONTENT_TYPE1 = "application/json"
        const val redColor = "#E30425"
        const val greenColor = "#1AD836"
        const val topics = "topics"
        const val advices = "advices"
        const val myTopic = "myTopic"
        const val question = "question"
        const val reQuestion = "reQuestion"
        const val message = "message"
        const val notification = "notification"

        fun showSnackBar(view: View, title: String, color: String) {
            Snackbar.make(view, title, Snackbar.LENGTH_LONG).apply {
                animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                setBackgroundTint(Color.parseColor(color))
                setTextColor(Color.parseColor("#FFFFFF"))
                show()
            }// apply
        }// show snackBar
    }
}