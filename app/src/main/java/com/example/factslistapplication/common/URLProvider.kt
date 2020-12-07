package com.example.factslistapplication.common

open class URLProvider {

    private val productionUrl = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/"
//    private val productionUrl = "http://localhost:8080/"

    open fun getBaseUrl() = productionUrl

}