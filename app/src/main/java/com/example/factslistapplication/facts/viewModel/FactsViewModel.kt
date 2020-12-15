package com.example.factslistapplication.facts.viewModel

import androidx.lifecycle.*
import com.example.factslistapplication.common.UIState
import com.example.factslistapplication.facts.model.Row
import com.example.factslistapplication.facts.model.UiError
import com.example.factslistapplication.facts.repository.FactsRepository
import com.example.factslistapplication.network.Result
import kotlinx.coroutines.launch

class FactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {

    private val _factsState = MutableLiveData<UIState<List<Row>>>()
    val factsState: LiveData<UIState<List<Row>>>
        get() = _factsState

    private var isAPICalling = false

    init {
        fetchLatestData()
    }

    private fun emitState(uiState: UIState<List<Row>>) {
        _factsState.value = uiState
    }

    fun fetchLatestData() {

        if(isAPICalling) return

        isAPICalling = true
        emitState(UIState.Loading(true))
        viewModelScope.launch {
            when (val result = factsRepository.fetchFacts()) {
                is Result.Success -> {
                    isAPICalling = false
                    emitState(UIState.Loading(false))
                    val rowItemList = result.data.rows
                    if (!rowItemList.isNullOrEmpty()) {
                        emitState(UIState.Success(rowItemList))
                    } else {
                        emitState(UIState.Success(emptyList()))
                    }
                }
                is Result.Error -> {
                    isAPICalling = false
                    emitState(UIState.Loading(false))
                    emitState(UIState.Error(UiError(result.exception)))
                }
            }
        }
    }
}

class FactsViewModelFactory(private val factsRepository: FactsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FactsViewModel(factsRepository) as T
    }
}