package com.rns.farmerexpress.model



data class RechargePlan(val success : String ,val hit_credit : String,val api_started: String,val api_expiry : String,val operator:String,
                         val circle : String,val message : String,val plans : FULLTTs)

data class FULLTTs(
    val FULLTT: List<Plan>,
)
data class Plan(
         val rs : String,
         val validity : String,
         val desc : String,
         val Type: String
)



