package com.isolaatti.posting.comments.domain.use_case

import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.data.remote.FeedCommentsDto
import com.isolaatti.posting.comments.domain.CommentsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetComments @Inject constructor(private val commentsRepository: CommentsRepository) {
    operator fun invoke(postId: Long, lastId: Long? = null): Flow<FeedCommentsDto> =
        commentsRepository.getComments(postId, lastId ?: 0)
}