package com.rns.farmerexpress.model



data class EdittextModel(
    val viewType : Int,
    val type : String,
    val length : String,
    val placeholder : String,
    val default : String,
    val name : String,
    var editTextValList : ArrayList<String>
    )



