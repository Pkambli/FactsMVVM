package com.example.factslistapplication.network

import com.example.factslistapplication.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {

    var BASE_URL = BuildConfig.BASE_URL
//    var BASE_URL = "http://localhost:8080/"

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpInterceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .header("Content-type", "application/json")
            .header("charset", "UTF-8")
            .build()

        chain.proceed(request)
    }

    // OkhttpClient for building http request url
    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(interceptor)
        .addNetworkInterceptor(httpInterceptor)
        .build()

    fun retrofit(baseUrl: String): APIService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APIService::class.java)

}

