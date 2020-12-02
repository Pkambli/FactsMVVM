package com.example.factslistapplication.utils

import java.io.IOException
import com.example.factslistapplication.network.Result
import java.net.UnknownHostException

/**
 * Wrap a suspending API [call] in try/catch. In case an exception is thrown, a [Result.Error] is
 * created based on the [errorMessage].
 */
suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        // An exception was thrown when calling the API so we're converting this to an IOException
        Result.Error(IOException(parseError(errorMessage, e), e))
    }
}

fun parseError(errorMessage: String, e: Exception) = when (e) {
    is UnknownHostException -> "Please check your internet connection"
    else -> errorMessage
}
