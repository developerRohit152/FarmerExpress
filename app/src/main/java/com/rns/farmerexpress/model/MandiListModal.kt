package com.rns.farmerexpress.model



data class MandiListModal(
    val records : List<Records>,
    )

data class Records(
    val state : String,
    val district : String,
    val market : String,
    val commodity : String,
    val variety : String,
    val arrival_date : String,
    val min_price : String,
    val max_price : String,
    val modal_price : String,
)



