package com.example.factslistapplication.server

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit

class MockServer {

    //  valid response dispatcher
    class ResponseDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val data = InstrumentationRegistry.getInstrumentation().context.assets.open("data.json").bufferedReader().use {
                it.readText()
            }
            return MockResponse().setResponseCode(200).setBody(data)
        }
    }
    //  error dispatcher
    class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
        }
    }
}