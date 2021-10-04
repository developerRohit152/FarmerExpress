package com.rns.farmerexpress.model

data class GetSinglePost(
  val status : String,
  val message: String,
  val post : List<SinglePost>

)

data class SinglePost(
    val viewType : Int,
    val post_id : Int,
    val user_id : Int,
    val user_name : String,
    val user_image : String,
    val post_images : String,
    val post_tags : String?,
    val discription : String,
    val contact : String?,
    val location : String,
    val type : String,
    val color : String?,
    val likes : Int,
    val comments : String,
    val shares : String,
    val isLiked : Int,
    val isPollSelected : String,
    val selectedPoll : String?,
    val poll : String?,
    val date : String,

)
