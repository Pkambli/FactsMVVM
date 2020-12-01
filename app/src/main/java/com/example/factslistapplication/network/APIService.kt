package com.example.factslistapplication.network

import com.example.factslistapplication.facts.model.FactsResponse
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET("facts.json")
    suspend fun getFacts(): Response<FactsResponse>

}