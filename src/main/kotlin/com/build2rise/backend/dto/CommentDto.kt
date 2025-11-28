package com.build2rise.backend.dto

import java.time.Instant
import java.util.UUID

data class CommentDto(
    val id: UUID,
    val userId: UUID,
    val authorName: String,
    val content: String,
    val createdAt: Instant
)