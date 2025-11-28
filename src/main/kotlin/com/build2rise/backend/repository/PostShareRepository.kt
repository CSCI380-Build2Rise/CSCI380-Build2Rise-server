package com.build2rise.backend.repository

import com.build2rise.backend.entity.PostShare
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PostShareRepository : JpaRepository<PostShare, UUID> {
    fun countByPostId(postId: UUID): Int
}