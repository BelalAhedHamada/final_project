package com.example.finalcloudproject.Model

import java.io.Serializable

class Notification : Serializable {
    var id: String = ""
    var title: String? = ""
    var message: String? = ""
    var time: Long? = 0

    constructor()

    constructor(
        id: String,
        title: String,
        message: String?,
        time: Long,
    ) {
        this.id = id
        this.title = title
        this.message = message
        this.time = time
    }

    fun getNotificationHashMap(): HashMap<String, Any?> {
        val data = hashMapOf<String, Any?>(
            "id" to id,
            "title" to title,
            "message" to message,
            "time" to time,
        )
        return data
    }


}