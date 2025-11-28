package com.build2rise.backend.repository

import com.build2rise.backend.entity.PostComment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PostCommentRepository : JpaRepository<PostComment, UUID> {
    fun findByPostIdOrderByCreatedAtAsc(postId: UUID): List<PostComment>
    fun countByPostId(postId: UUID): Int
}