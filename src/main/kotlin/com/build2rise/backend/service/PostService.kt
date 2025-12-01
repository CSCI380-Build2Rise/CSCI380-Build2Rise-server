package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.Post
import com.build2rise.backend.repository.PostRepository
import com.build2rise.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val supabaseStorageService: SupabaseStorageService
) {

    /**
     * Create a new post
     */
    @Transactional
    fun createPost(userId: String, request: CreatePostRequest): PostResponse {
        val userUuid = UUID.fromString(userId)

        // Verify user exists
        val user = userRepository.findById(userUuid).orElseThrow {
            IllegalArgumentException("User not found")
        }

        // Create post
        val post = Post(
            userId = userUuid,
            postDescription = request.postDescription,
            postType = request.postType,
            mediaUrl = request.mediaUrl
        )

        val savedPost = postRepository.save(post)

        return PostResponse(
            id = savedPost.id.toString(),
            userId = savedPost.userId.toString(),
            firstName = user.firstName ?: "",
            lastName = user.lastName,
            userType = user.userType,
            postDescription = savedPost.postDescription,
            postType = savedPost.postType,
            mediaUrl = savedPost.mediaUrl,
            postingDate = savedPost.postingDate.toString(),
            createdAt = savedPost.createdAt.toString(),
            likeCount = savedPost.likeCount,
            commentCount = savedPost.commentCount,
            shareCount = savedPost.shareCount

        )
    }


    /**
     * Create a new post with media file
     */
    @Transactional
    fun createPostWithMedia(
        userId: String,
        description: String?,
        file: MultipartFile?
    ): PostResponse {
        val userUuid = UUID.fromString(userId)

        // Verify user exists
        val user = userRepository.findById(userUuid).orElseThrow {
            IllegalArgumentException("User not found")
        }

        var mediaUrl: String? = null
        var postType = "text"

        // Upload file if provided
        if (file != null && !file.isEmpty) {
            mediaUrl = supabaseStorageService.uploadFile(file, userUuid)

            // Determine post type from file content type
            postType = when {
                file.contentType?.startsWith("image/") == true -> "image"
                file.contentType?.startsWith("video/") == true -> "video"
                else -> "text"
            }
        }

        // Create post
        val post = Post(
            userId = userUuid,
            postDescription = description,
            postType = postType,
            mediaUrl = mediaUrl
        )

        val savedPost = postRepository.save(post)

        return PostResponse(
            id = savedPost.id.toString(),
            userId = savedPost.userId.toString(),
            firstName = user.firstName ?: "",
            lastName = user.lastName,
            userType = user.userType,
            postDescription = savedPost.postDescription,
            postType = savedPost.postType,
            mediaUrl = savedPost.mediaUrl,
            postingDate = savedPost.postingDate.toString(),
            createdAt = savedPost.createdAt.toString(),
            likeCount = savedPost.likeCount,
            commentCount = savedPost.commentCount,
            shareCount = savedPost.shareCount
        )
    }

    /**
     * Get all posts (feed)
     */
    fun getAllPosts(): FeedResponse {
        val posts = postRepository.findAllByOrderByPostingDateDesc()

        val postResponses = posts.map { post ->
            val user = userRepository.findById(post.userId).orElse(null)

            PostResponse(
                id = post.id.toString(),
                userId = post.userId.toString(),
                firstName = user?.firstName ?: "",
                lastName = user?.lastName,
                userType = user?.userType ?: "unknown",
                postDescription = post.postDescription,
                postType = post.postType,
                mediaUrl = post.mediaUrl,
                postingDate = post.postingDate.toString(),
                createdAt = post.createdAt.toString(),
                likeCount = post.likeCount,
                commentCount = post.commentCount,
                shareCount = post.shareCount
            )
        }

        return FeedResponse(
            posts = postResponses,
            totalCount = postResponses.size
        )
    }

    /**
     * Get posts by user ID
     */
    fun getPostsByUserId(userId: String): FeedResponse {
        val userUuid = UUID.fromString(userId)

        val posts = postRepository.findByUserIdOrderByPostingDateDesc(userUuid)
        val user = userRepository.findById(userUuid).orElse(null)

        val postResponses = posts.map { post ->
            PostResponse(
                id = post.id.toString(),
                userId = post.userId.toString(),
                firstName = user?.firstName ?: "",
                lastName = user?.lastName,
                userType = user?.userType ?: "unknown",
                postDescription = post.postDescription,
                postType = post.postType,
                mediaUrl = post.mediaUrl,
                postingDate = post.postingDate.toString(),
                createdAt = post.createdAt.toString(),
                likeCount = post.likeCount,
                commentCount = post.commentCount,
                shareCount = post.shareCount
            )
        }

        return FeedResponse(
            posts = postResponses,
            totalCount = postResponses.size
        )
    }

    /**
     * Get post by ID
     */
    fun getPostById(postId: String): PostResponse {
        val postUuid = UUID.fromString(postId)

        val post = postRepository.findById(postUuid).orElseThrow {
            IllegalArgumentException("Post not found")
        }

        val user = userRepository.findById(post.userId).orElse(null)

        return PostResponse(
            id = post.id.toString(),
            userId = post.userId.toString(),
            firstName = user?.firstName ?: "",
            lastName = user?.lastName,
            userType = user?.userType ?: "unknown",
            postDescription = post.postDescription,
            postType = post.postType,
            mediaUrl = post.mediaUrl,
            postingDate = post.postingDate.toString(),
            createdAt = post.createdAt.toString(),
            likeCount = post.likeCount,
            commentCount = post.commentCount,
            shareCount = post.shareCount
        )
    }
}