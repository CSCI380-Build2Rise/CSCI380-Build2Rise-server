package com.build2rise.backend.dto

// Create Post Request
data class CreatePostRequest(
    val postDescription: String?,
    val postType: String = "text", // text, photo, video
    val mediaUrl: String? = null
)

// Update Post Request
data class UpdatePostRequest(
    val postDescription: String?
)

// Post Response
data class PostResponse(
    val id: String,
    val userId: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val postDescription: String?,
    val postType: String,
    val mediaUrl: String?,
    val postingDate: String,
    val createdAt: String
)

// Feed Response (list of posts)
data class FeedResponse(
    val posts: List<PostResponse>,
    val totalCount: Int
)