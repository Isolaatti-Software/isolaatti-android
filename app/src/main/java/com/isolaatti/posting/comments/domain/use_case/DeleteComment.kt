package com.isolaatti.posting.comments.domain.use_case

import com.isolaatti.posting.comments.domain.CommentsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteComment @Inject constructor(private val commentsRepository: CommentsRepository) {
    operator fun invoke(commentId: Long): Flow<Resource<Boolean>> {
        return commentsRepository.deleteComment(commentId)
    }
}