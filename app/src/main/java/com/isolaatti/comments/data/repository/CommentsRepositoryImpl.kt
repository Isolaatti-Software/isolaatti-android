package com.isolaatti.comments.data.repository

import com.isolaatti.comments.data.remote.CommentsApi
import com.isolaatti.comments.domain.CommentRepository
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(commentsApi: CommentsApi) : CommentRepository {
}