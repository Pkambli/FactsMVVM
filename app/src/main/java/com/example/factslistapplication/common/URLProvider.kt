package com.example.factslistapplication.common

open class URLProvider {

    private val productionUrl = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/"

    open fun getBaseUrl() = productionUrl

}