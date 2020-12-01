package com.example.factslistapplication

import com.example.factslistapplication.facts.model.FactsResponse
import com.example.factslistapplication.facts.repository.FactsRepository
import com.example.factslistapplication.network.API
import com.example.factslistapplication.network.APIService
import com.example.factslistapplication.network.Result
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class FactsRepositoryTest {

    private val SUT = FactsRepository()
    private val apiService = mockk<APIService>()

    @Test
    fun fetchFacts_successful_should_provide_data() = runBlocking {
        mockkObject(API)
        val gson = Gson()
        val response = gson.fromJson(FACTS_FAKE_RESPONSE, FactsResponse::class.java)
        coEvery { apiService.getFacts() } returns Response.success(response)
        every { API.service } returns apiService
        val result = SUT.fetchFacts()
        // Then the user is received
        assertEquals(Result.Success(response), result)
    }

    @Test
    fun fetchFacts_failure_should_handle_failure() = runBlocking {
        mockkObject(API)
        coEvery { apiService.getFacts() } returns Response.error(400, errorResponseBody)
        every { API.service } returns apiService
        val result = SUT.fetchFacts()
        assertTrue(result is Result.Error)
    }
}