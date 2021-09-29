package com.rns.farmerexpress.model

import com.denzcoskun.imageslider.models.SlideModel
import java.lang.reflect.Constructor

data class HomeModel(
    val viewType : Int,
    val userImage : Int ,
    val userName : String,
    val location : String,
    val content : String,
    val time : String,
    val images : ArrayList<SlideModel>,
    val likeCount : String,
    val commentCount : String,
    val shareCount : String,
    )

//data class HomeModels(val uName: String ,val work : String )


