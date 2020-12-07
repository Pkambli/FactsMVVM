package com.example.factslistapplication

import com.example.factslistapplication.common.URLProvider

class TestURLProvider : URLProvider() {

    override fun getBaseUrl(): String {
       return "http://localhost:8080/"
    }
}