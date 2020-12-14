package com.example.factslistapplication

import android.app.Application
import com.example.factslistapplication.common.URLProvider
import com.example.factslistapplication.network.API
import com.example.factslistapplication.network.APIService

open class FactsApplication : Application() {

   lateinit var service: APIService

    override fun onCreate() {
        super.onCreate()
        val url = URLProvider()
        service = API.retrofit(url.getBaseUrl())
    }

}