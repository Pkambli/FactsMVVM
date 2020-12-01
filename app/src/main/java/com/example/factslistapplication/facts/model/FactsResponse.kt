package com.example.factslistapplication.facts.model


import com.google.gson.annotations.SerializedName

data class FactsResponse(
    @SerializedName("rows")
    val rows: List<Row>?,
    @SerializedName("title")
    val title: String?
)