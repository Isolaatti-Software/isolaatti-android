package com.isolaatti.posting.comments.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.domain.use_case.GetComments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(private val getComments: GetComments) : ViewModel() {
    private val _comments: MutableLiveData<List<CommentDto>> = MutableLiveData()

    val comments: LiveData<List<CommentDto>> get() = _comments

    /**
     * postId to query comments for. First page will be fetched when set.
     * Call getContent() to get more content if available
      */
    var postId: Long = 0
        set(value) {
            field = value
            getContent()
    }

    private var lastId: Long = 0L

    fun getContent() {
        viewModelScope.launch {
            getComments(postId, lastId).onEach {
                val newList = _comments.value?.toMutableList() ?: mutableListOf()
                newList.addAll(it.data)
                _comments.postValue(newList)
                if(it.data.isNotEmpty()){
                    lastId = it.data.last().comment.id
                }

            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    /**
     * Use when new comment has been posted
     */
    fun putCommentAtTheBeginning(commentDto: CommentDto) {
        val newList: MutableList<CommentDto> = mutableListOf(commentDto)
        newList.addAll(_comments.value ?: mutableListOf())
        _comments.postValue(newList)
    }

}