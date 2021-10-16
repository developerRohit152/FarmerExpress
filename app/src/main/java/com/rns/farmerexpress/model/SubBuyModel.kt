package com.rns.farmerexpress.model



data class SubBuyModel(
    val status : Int,
    val store : List<SubBuyData>

    )

data class SubBuyData(
    val store_id: Int,
    val user_name: String,
    val user_image: String,
    val place: String,
    val description: String,
    val store_images: String?,
    val contact: String,
    val distance: Float,
    val likes: Int,
    val isLiked: Int,
    val date: String,
)




