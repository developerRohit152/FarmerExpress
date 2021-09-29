package com.rns.farmerexpress.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class OperatorResModel(val response : List<OperatorModel>)

data class OperatorModel(
    val operator_name: String,
    val operator_id: String,
    val service_type: String,
    val status: String,
    val bill_fetch: String,
    val bbps_enabled: String,
    val message: String,
    val description: String,
    val amount_minimum: String,
    val amount_maximum: String
)




