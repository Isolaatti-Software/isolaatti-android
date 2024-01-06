package com.isolaatti.posting.comments.domain.use_case

import com.isolaatti.posting.comments.domain.CommentsRepository
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetComments @Inject constructor(private val commentsRepository: CommentsRepository) {
    operator fun invoke(postId: Long, lastId: Long? = null): Flow<Resource<MutableList<Comment>>> =
        commentsRepository.getComments(postId, lastId)
}