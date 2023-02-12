package com.isolaatti.comments.data.repository

import com.isolaatti.comments.data.remote.CommentsApi
import com.isolaatti.comments.domain.CommentsRepository
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(private val commentsApi: CommentsApi) : CommentsRepository {
}