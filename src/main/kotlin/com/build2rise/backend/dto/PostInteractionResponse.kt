package com.build2rise.backend.dto

import java.util.UUID

data class PostInteractionResponse(
    val postId: UUID,
    val likeCount: Int,
    val commentCount: Int,
    val shareCount: Int,
    val likedByCurrentUser: Boolean
)

