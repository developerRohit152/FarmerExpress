package com.rns.farmerexpress.model

data class StateData(var StateCode: Int, var StateName: String)

data class DisData(
    val DisttCode : Int,
    val DistrictName : String
)

data class SubDist(
    val SubdistCode : Int,
    val SubdistName : String
)


data class Village(
    val RevenueVillagecode : Int,
    val RevenueVillageName : String
)
