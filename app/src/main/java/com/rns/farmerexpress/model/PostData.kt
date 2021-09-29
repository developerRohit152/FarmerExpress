package com.rns.farmerexpress.model


data class PostData(

    val status : String,
    val message : String,
    val post_id: String,

//    val session : String,
//    val type : String,
//    val discription: String,
//    val poll: List<Poll>,
//    val image: List<Image>,
//    val contact: String,
//    val tags: List<Tags>
)

data class  Image(
    val data : String,
)data class  Poll(
    val example1 : String,
)data class  Tags(
    val tag1 : String,
)




