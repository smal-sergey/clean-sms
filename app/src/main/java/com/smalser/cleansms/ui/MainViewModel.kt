package com.smalser.cleansms.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.smalser.cleansms.ui.state.MainUiState
import com.smalser.cleansms.ui.state.smsGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executors

class MainViewModel : ViewModel() { //todo add repository here

    private val _uiState = MutableStateFlow(MainUiState(listOf()))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    //todo temp emitting test events
    private val executor = Executors.newSingleThreadScheduledExecutor()

    init {
        scheduleAddingMessage()
    }

    private fun scheduleAddingMessage() {
        if (_uiState.value.senderUiStates.size < 10) {
            Log.i("MainViewModel", "Scheduling adding new message in 3 seconds")
            executor.schedule(addOneMoreMessage(), 3, java.util.concurrent.TimeUnit.SECONDS)
        }
    }

    private fun addOneMoreMessage() = Runnable {
        _uiState.update {
            val smsList = it.senderUiStates.toMutableList()
            smsList.add(smsGenerator.invoke(smsList.size))
            it.copy(senderUiStates = smsList)
        }
        scheduleAddingMessage()
    }

    //    private var fetchJob: Job? = null
    //
    //    fun fetchArticles(category: String) {
    //        fetchJob?.cancel()
    //        fetchJob = viewModelScope.launch {
    //            try {
    //                val newsItems = repository.newsItemsForCategory(category)
    //                _uiState.update {
    //                    it.copy(newsItems = newsItems)
    //                }
    //            } catch (ioe: IOException) {
    //                // Handle the error and notify the UI when appropriate.
    //                _uiState.update {
    //                    val messages = getMessagesFromThrowable(ioe)
    //                    it.copy(userMessages = messages)
    //                 }
    //            }
    //        }
    //    }

}