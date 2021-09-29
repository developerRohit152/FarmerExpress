package com.rns.farmerexpress.model

data class LikeCommentShareModel(
   val status : String,
   val isLiked : Int,
   val likes : Int
   )

data class Comment(
//   "session": "78779200931627111417",
//"post_id": "275",
//"type": "comment",
//"detail": "whh"
   val status: String,
   val comments: List<Comments>
)

data class Comments(
   val viewType : Int,
   val user_image: String,
   val user_name: String,
   val comment: String,
   val postId : String?
)

data class Share(
   val status: String,
   val shares: Int
)