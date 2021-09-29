package com.rns.farmerexpress.model

data class PollSelectedModel(
    val status : String,
    val message: String,
    val isPollSelected: String,
    val selectedPoll: String,
    val poll : List<Polls>
)
        data class Polls(
            val poll : String,
            val value: Int
        )
