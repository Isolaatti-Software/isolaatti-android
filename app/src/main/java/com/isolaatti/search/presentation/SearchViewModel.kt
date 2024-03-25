package com.isolaatti.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.search.data.SearchApi
import com.isolaatti.search.data.SearchDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    fun search(query: String) {
        viewModelScope.launch {

        }
    }

}