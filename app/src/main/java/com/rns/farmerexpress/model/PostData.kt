package com.rns.farmerexpress.model


data class PostData(

    val status : String,
    val message : String,
    val post_id: String,
)

data class PostDelete(
    val status : String,
    val message: String
)


