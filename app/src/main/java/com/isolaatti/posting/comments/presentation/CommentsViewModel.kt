package com.isolaatti.posting.comments.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.posting.comments.domain.use_case.GetComments
import com.isolaatti.posting.comments.domain.use_case.PostComment
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(private val getComments: GetComments, private val postComment: PostComment) : ViewModel() {
    private val commentsList: MutableList<Comment> = mutableListOf()

    private val _comments: MutableLiveData<Pair<List<Comment>, UpdateEvent>> = MutableLiveData()

    val comments: LiveData<Pair<List<Comment>, UpdateEvent>> get() = _comments
    val commentPosted: MutableLiveData<Boolean?> = MutableLiveData()
    val noMoreContent: MutableLiveData<Boolean?> = MutableLiveData()
    val commentToEdit: MutableLiveData<Comment> = MutableLiveData()
    val finishedEditingComment: MutableLiveData<Boolean?> = MutableLiveData()

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

    fun getContent(refresh: Boolean = false) {
        viewModelScope.launch {
            if(refresh) {
                commentsList.clear()
            }
            getComments(postId, lastId).onEach {
                val eventType = if((commentsList.isNotEmpty())) UpdateEvent.UpdateType.COMMENT_PAGE_ADDED_BOTTOM else UpdateEvent.UpdateType.REFRESH
                commentsList.addAll(it)
                _comments.postValue(Pair(commentsList, UpdateEvent(eventType, null)))
                if(it.isEmpty()) {
                    noMoreContent.postValue(true)
                }
                if(it.isNotEmpty()){
                    lastId = it.last().id
                }

            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun postComment(content: String) {
        viewModelScope.launch {
            postComment(content, postId).onEach {
                when(it) {
                    is Resource.Success -> {
                        commentPosted.postValue(true)
                        putCommentAtTheBeginning(it.data!!)
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Error -> {
                        commentPosted.postValue(false)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun handledCommentPosted() {
        commentPosted.postValue(null)
    }

    /**
     * Use when new comment has been posted
     */
    private fun putCommentAtTheBeginning(comment: Comment) {
        val newList: MutableList<Comment> = mutableListOf(comment)
        newList.addAll(commentsList)
        commentsList.clear()
        commentsList.addAll(newList)
        _comments.postValue(Pair(commentsList, UpdateEvent(UpdateEvent.UpdateType.COMMENT_ADDED_TOP, null)))
    }

    fun switchToEditMode(comment: Comment) {
        commentToEdit.postValue(comment)
    }
}