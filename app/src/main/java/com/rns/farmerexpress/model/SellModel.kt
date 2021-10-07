package com.rns.farmerexpress.model

import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Constructor
import java.util.*

data class SellModel(

    val category : List<Categories>,

    )

data class Categories(
    val viewType : Int,
    val id : Int,
    @SerializedName("image")
    val catImage : String ,
    @SerializedName("name")
    val catName : String,
    val field : String?,
    val category : Int,
)



