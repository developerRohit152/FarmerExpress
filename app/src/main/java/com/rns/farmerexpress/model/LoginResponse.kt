package com.rns.farmerexpress.model

data class LoginResponse(val status : String?, val message : String?)

data class OTPVeri(val status : String?,val session : String?,val message : String?)
