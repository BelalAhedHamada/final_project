package com.example.finalcloudproject.Model

import java.io.Serializable

class User: Serializable {
    var id:String? =""
    var name:String=""
    var email:String =""
    var address:String=""
    var phone:String=""
    var birth_date:String=""
    var type:Int=0
    var fcm_token:String=""

    constructor()
    constructor(id:String?, name: String, email:String, address:String, phone: String,birth_date:String,type:Int,fcm_token:String){
        this.id = id
        this.name = name
        this.email = email
        this.address = address
        this.phone=phone
        this.birth_date=birth_date
        this.type=type
        this.fcm_token=fcm_token
    }
}