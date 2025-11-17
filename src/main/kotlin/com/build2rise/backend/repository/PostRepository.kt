package com.build2rise.backend.repository

import com.build2rise.backend.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PostRepository : JpaRepository<Post, UUID>{
    fun findByUserIdOrderByPostingDateDesc(userId: UUID): List<Post>

    @Query("SELECT p FROM Post p ORDER BY p.postingDate DESC")
    fun findAllByOrderByPostingDateDesc(): List<Post>
}