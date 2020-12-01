package com.example.factslistapplication.facts.viewModel

import androidx.lifecycle.*
import com.example.factslistapplication.facts.model.Row
import com.example.factslistapplication.facts.model.UiError
import com.example.factslistapplication.facts.repository.FactsRepository
import com.example.factslistapplication.network.Result
import kotlinx.coroutines.launch

class FactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {

    private val _factsLiveData = MutableLiveData<List<Row>>()
    val factsLiveData: LiveData<List<Row>>
        get() = _factsLiveData

    private val _errorLiveData = MutableLiveData<UiError>()
    val errorLiveData: LiveData<UiError>
        get() = _errorLiveData

    private val _progressLiveData = MutableLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean>
        get() = _progressLiveData

    fun fetchLatestData() {
        _progressLiveData.postValue(true)
        viewModelScope.launch {
            val result = factsRepository.fetchFacts()
            when (result) {
                is Result.Success -> {
                    _progressLiveData.postValue(false)
                    val rowItemList = result.data.rows
                    if (!rowItemList.isNullOrEmpty()) {
                        _factsLiveData.value = rowItemList
                    } else {
                        _factsLiveData.value = emptyList()
                    }
                }
                is Result.Error -> {
                    _progressLiveData.postValue(false)
                    _errorLiveData.value = UiError(result.exception)
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