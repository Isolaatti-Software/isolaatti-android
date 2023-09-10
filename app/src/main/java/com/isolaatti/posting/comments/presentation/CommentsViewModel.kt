package com.isolaatti.posting.comments.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.posting.comments.domain.use_case.DeleteComment
import com.isolaatti.posting.comments.domain.use_case.EditComment
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
class CommentsViewModel @Inject constructor(
    private val getComments: GetComments,
    private val postComment: PostComment,
    private val editCommentUseCase: EditComment,
    private val deleteCommentUseCase: DeleteComment
) : ViewModel() {
    private val commentsList: MutableList<Comment> = mutableListOf()

    private val _comments: MutableLiveData<Pair<List<Comment>, UpdateEvent>> = MutableLiveData()

    val comments: LiveData<Pair<List<Comment>, UpdateEvent>> get() = _comments
    val commentPosted: MutableLiveData<Boolean?> = MutableLiveData()
    val noMoreContent: MutableLiveData<Boolean?> = MutableLiveData()
    val commentToEdit: MutableLiveData<Comment?> = MutableLiveData()
    val finishedEditingComment: MutableLiveData<Boolean?> = MutableLiveData()
    val error: MutableLiveData<Resource.Error.ErrorType?> = MutableLiveData()

    private val toRetry: MutableList<Runnable> = mutableListOf()


    // runs the lists of "Runnable" one by one and clears list. After this is executed,
    // caller should report as handled
    fun retry() {
        toRetry.forEach {
            it.run()
        }

        toRetry.clear()
    }

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
            if (refresh) {
                commentsList.clear()
            }
            getComments(postId, lastId).onEach {
                when(it) {
                    is Resource.Error -> {
                        error.postValue(it.errorType)
                        toRetry.add {
                            getContent(refresh)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val eventType =
                            if ((commentsList.isNotEmpty())) UpdateEvent.UpdateType.COMMENT_PAGE_ADDED_BOTTOM else UpdateEvent.UpdateType.REFRESH
                        if(it.data == null) {
                            return@onEach
                        }
                        commentsList.addAll(it.data)
                        _comments.postValue(Pair(commentsList, UpdateEvent(eventType, null)))
                        if (it.data.isEmpty()) {
                            noMoreContent.postValue(true)
                        }
                        if (it.data.isNotEmpty()) {
                            lastId = it.data.last().id
                        }
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun postComment(content: String) {
        viewModelScope.launch {
            postComment(content, postId).onEach {
                when (it) {
                    is Resource.Success -> {
                        commentPosted.postValue(true)
                        putCommentAtTheBeginning(it.data!!)
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {
                        commentPosted.postValue(false)
                        error.postValue(it.errorType)


                        // this is the original call, put to retry
                        toRetry.add {
                            postComment(content)
                        }
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun editComment(newContent: String) {
        // TODO handle audios
        val comment = commentToEdit.value ?: return
        viewModelScope.launch {
            commentToEdit.postValue(null)
            editCommentUseCase(comment.id, newContent, null).onEach { commentResource ->
                when (commentResource) {
                    is Resource.Success -> {
                        val newComment = commentResource.data ?: return@onEach
                        val oldComment =
                            commentsList.find { it.id == newComment.id } ?: return@onEach

                        val index = commentsList.indexOf(oldComment)
                        commentsList[index] = newComment
                        val updateEvent = UpdateEvent(UpdateEvent.UpdateType.COMMENT_UPDATED, index)
                        _comments.postValue(Pair(commentsList, updateEvent))
                        finishedEditingComment.postValue(true)
                    }

                    is Resource.Error -> {
                        error.postValue(commentResource.errorType)

                        toRetry.add {
                            editComment(newContent)
                        }
                    }

                    is Resource.Loading -> {}
                }

            }.flowOn(Dispatchers.IO).launchIn(this)
        }

    }

    fun deleteComment(commentId: Long) {
        val index =
            commentsList.find { it.id == commentId }?.let { commentsList.indexOf(it) } ?: return
        viewModelScope.launch {
            deleteCommentUseCase(commentId).onEach {
                when(it) {
                    is Resource.Error -> {
                        error.postValue(it.errorType)

                        toRetry.add {
                            deleteComment(commentId)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        commentsList.removeAt(index)
                        _comments.postValue(Pair(commentsList, UpdateEvent(UpdateEvent.UpdateType.COMMENT_REMOVED, index)))
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
        _comments.postValue(
            Pair(
                commentsList,
                UpdateEvent(UpdateEvent.UpdateType.COMMENT_ADDED_TOP, null)
            )
        )
    }

    fun switchToEditMode(comment: Comment) {
        commentToEdit.postValue(comment)
    }
}