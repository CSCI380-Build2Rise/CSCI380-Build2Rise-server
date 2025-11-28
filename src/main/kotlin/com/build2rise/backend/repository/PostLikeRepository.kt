package com.build2rise.backend.repository

import com.build2rise.backend.entity.PostLike
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PostLikeRepository : JpaRepository<PostLike, UUID> {
    fun findByPostIdAndUserId(postId: UUID, userId: UUID): PostLike?
    fun countByPostId(postId: UUID): Int
}