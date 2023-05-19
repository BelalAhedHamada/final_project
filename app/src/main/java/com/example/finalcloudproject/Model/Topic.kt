package com.example.finalcloudproject.Model

import java.io.Serializable

class Topic : Serializable {
    var id: String? = ""
    var name: String? = ""
    var description: String? = ""
    var image: String? = ""
    var idDoctor: String? = ""
    var time: Long? = 0
    var state: Int? = 0
    var doctor:String?=""
    var users:ArrayList<User>?=null


    constructor()
    constructor(
        id: String?,
        name: String?,
        description: String?,
        image: String?,
        time: Long,
        idDoctor: String?,
        state: Int? = 0,
        doctor:String,
        users:ArrayList<User>?,
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.image = image
        this.idDoctor = idDoctor
        this.time = time
        this.state = state
        this.doctor = doctor
        this.users = users
    }
}