package com.rns.farmerexpress.model

data class SessionRes(val status : String,val message : String,val data : List<dataSession>)

data class dataSession(val name: String,val mobile: String,val photo: String,val email: String,val location: String,val village: String,val sex: String,val dob: String,
                       val profession: String,val about: String)

