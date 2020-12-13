package com.example.factslistapplication.facts.repository

import android.util.Log
import com.example.factslistapplication.facts.model.FactsResponse
import com.example.factslistapplication.network.API
import com.example.factslistapplication.network.APIService
import com.example.factslistapplication.network.Result
import com.example.factslistapplication.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class FactsRepository(val service: APIService) {

    suspend fun fetchFacts(): Result<FactsResponse> = withContext(Dispatchers.IO) {
        return@withContext safeApiCall({
            val response = service.getFacts()
            if (response.isSuccessful) {
                val factsResponse = response.body()
                if (factsResponse != null) {
                    return@safeApiCall Result.Success(factsResponse)
                }
            }
            Result.Error(IOException("Something went wrong."))
        }, "Something went wrong.")
    }
}