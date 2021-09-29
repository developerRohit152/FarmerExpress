package com.rns.farmerexpress.model



data class NewsModel(
    val viewType : Int,
    val post_id : String,
    val category: String,
    val title: String,
    val name: String
)

data class NewsDataModel(
    val post_id : String,
    val category: String,
    val title: String,
    val name: String,
    val city: String,
    val date: String,
    val post: String
)



