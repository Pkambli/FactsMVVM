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

    private fun emitState(uiState: UIState<List<Row>>) {
        _factsState.value = uiState
    }

    fun fetchLatestData() {
        emitState(UIState.Loading(true))
        viewModelScope.launch {
            val result = factsRepository.fetchFacts()
            when (result) {
                is Result.Success -> {
                    emitState(UIState.Loading(false))
                    val rowItemList = result.data.rows
                    if (!rowItemList.isNullOrEmpty()) {
                        emitState(UIState.Success(rowItemList))
                    } else {
                        emitState(UIState.Success(emptyList()))
                    }
                }
                is Result.Error -> {
                    emitState(UIState.Loading(false))
                    emitState(UIState.Error(UiError(result.exception)))
                }
            }
        }
    }
}

class FactsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FactsViewModel(FactsRepository()) as T
    }
}