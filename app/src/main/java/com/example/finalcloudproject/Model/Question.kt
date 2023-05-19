package com.example.finalcloudproject.Model

import java.io.Serializable

class Question : Serializable {
    var id: String? = ""
    var description: String? = ""
    var user: User? = null


    constructor()
    constructor(id: String, description: String,user: User) {
        this.id = id
        this.description = description
        this.user = user
    }

    fun getQuestionHashMap(): HashMap<String, Any?> {
        val data = hashMapOf<String, Any?>(
            "id" to id,
            "description" to description,
            "user" to user,
        )
        return data
    }
}