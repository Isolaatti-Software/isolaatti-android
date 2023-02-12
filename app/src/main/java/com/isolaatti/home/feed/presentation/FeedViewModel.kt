package com.isolaatti.home.feed.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.home.feed.data.remote.FeedDto
import com.isolaatti.home.feed.domain.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val feedRepository: FeedRepository) : ViewModel() {

    private val _feed: MutableLiveData<FeedDto> = MutableLiveData()
    val feed: LiveData<FeedDto> get() = _feed

    private fun getLastId(): Long = try {_feed.value?.data?.last()?.post?.id ?: 0} catch (e: NoSuchElementException) { 0 }

    fun getFeed() {
        viewModelScope.launch {
            feedRepository.getNextPage(0, 20).collect {
                _feed.postValue(it)
            }
        }
    }
}