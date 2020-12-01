package com.example.factslistapplication.facts.repository

import com.example.factslistapplication.facts.model.FactsResponse
import com.example.factslistapplication.network.API
import com.example.factslistapplication.network.Result
import com.example.factslistapplication.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class FactsRepository {

    suspend fun fetchFacts(): Result<FactsResponse> = withContext(Dispatchers.IO) {
            return@withContext safeApiCall({
                val response = API.service.getFacts()
                if (response.isSuccessful){
                    val factsResponse = response.body()
                    if (factsResponse != null) {
                        return@safeApiCall Result.Success(factsResponse)
                    }
                }
                Result.Error(IOException("Access denied."))
            }, "Something went wrong.")
        }

}