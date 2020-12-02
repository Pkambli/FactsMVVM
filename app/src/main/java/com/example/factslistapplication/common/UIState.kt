package com.example.factslistapplication.common

import com.example.factslistapplication.facts.model.UiError

sealed class UIState<out T : Any> {
    data class Success<out T : Any>(val data: T) : UIState<T>()
    data class Error(val error: UiError) : UIState<Nothing>()
    data class Loading(val isLoading: Boolean) : UIState<Nothing>()
}