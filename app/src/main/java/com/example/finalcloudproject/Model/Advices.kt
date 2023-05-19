package com.example.finalcloudproject.Model

import java.io.Serializable

class Advices:Serializable {
    var id:String? =""
    var name:String?=""
    var description:String?=""
    var time:Long?=0
    var state = true
    var video:String?=""
    var image:String?=""
    constructor()
    constructor(id:String,name:String,description:String,time:Long,state:Boolean,image:String,video:String){
        this.id=id
        this.name=name
        this.description=description
        this.time=time
        this.state=state
        this.video=video
        this.image=image
    }
    constructor(id:String,name:String,description:String,time:Long,state:Boolean,image:String){
        this.id=id
        this.name=name
        this.description=description
        this.time=time
        this.state=state
        this.image=image

    }

    fun getAdvicesHashMap(): HashMap<String, Any?> {
        val data = hashMapOf<String, Any?>(
            "id" to id,
            "name" to name,
            "description" to description,
            "time" to time,
            "state" to state,
//            "image" to image,
//            "video" to video
        )
        return data
    }
}